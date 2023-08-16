# image policy webhook



## Approach 1 - using ImagePolicyWebhook Admission Controller



### Prerequisites - ImagePolicyWebhook Admission webhook server (deployment & service)

```sh
#cfssl
sudo apt update
sudo apt install golang-cfssl
```



```sh
#create CSR to send to KubeAPI
cat <<EOF | cfssl genkey - | cfssljson -bare server
{
  "hosts": [
    "image-bouncer-webhook",
    "image-bouncer-webhook.default.svc",
    "image-bouncer-webhook.default.svc.cluster.local",
    "192.168.56.10",
    "10.96.0.0"
  ],
  "CN": "system:node:image-bouncer-webhook.default.pod.cluster.local",
  "key": {
    "algo": "ecdsa",
    "size": 256
  },
  "names": [
    {
      "O": "system:nodes"
    }
  ]
}
EOF

#create csr request
cat <<EOF | kubectl apply -f -
apiVersion: certificates.k8s.io/v1
kind: CertificateSigningRequest
metadata:
  name: image-bouncer-webhook.default
spec:
  request: $(cat server.csr | base64 | tr -d '\n')
  signerName: kubernetes.io/kubelet-serving
  usages:
  - digital signature
  - key encipherment
  - server auth
EOF

# approver cert
kubectl certificate approve image-bouncer-webhook.default

# download signed server.crt
kubectl get csr image-bouncer-webhook.default -o jsonpath='{.status.certificate}' | base64 --decode > server.crt

mkdir -p /etc/kubernetes/pki/webhook/

#copy to /etc/kubernetes/pki/webhook
cp server.crt /etc/kubernetes/pki/webhook/server.crt

# create secret with signed server.crt
kubectl create secret tls tls-image-bouncer-webhook --key server-key.pem --cert server.crt
```



### Using ImagePolicyWebhook Admission webhook server (deployment & service)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: image-bouncer-webhook
spec:
  selector:
    matchLabels:
      app: image-bouncer-webhook
  template:
    metadata:
      labels:
        app: image-bouncer-webhook
    spec:
      containers:
        - name: image-bouncer-webhook
          imagePullPolicy: Always
          image: "kainlite/kube-image-bouncer:latest"
          args:
            - "--cert=/etc/admission-controller/tls/tls.crt"
            - "--key=/etc/admission-controller/tls/tls.key"
            - "--debug"
            - "--registry-whitelist=docker.io,gcr.io"
          volumeMounts:
            - name: tls
              mountPath: /etc/admission-controller/tls
      volumes:
        - name: tls
          secret:
            secretName: tls-image-bouncer-webhook
---
apiVersion: v1
kind: Service
metadata:
  labels:
    app: image-bouncer-webhook
  name: image-bouncer-webhook
spec:
  type: NodePort
  ports:
    - name: https
      port: 443
      targetPort: 1323
      protocol: "TCP"
      nodePort: 30020
  selector:
    app: image-bouncer-webhook
```



Add this to resolve service `echo "127.0.0.1 image-bouncer-webhook" >> /etc/hosts`

check service using`telnet image-bouncer-webhook 30020` or `netstat -na | grep 30020`

Create custom kubeconfig with above service, its client certificate

`/etc/kubernetes/pki/webhook/admission_kube_config.yaml`

```yaml
apiVersion: v1
kind: Config
clusters:
- cluster:
    certificate-authority: /etc/kubernetes/pki/webhook/server.crt
    server: https://image-bouncer-webhook:30020/image_policy
  name: bouncer_webhook
contexts:
- context:
    cluster: bouncer_webhook
    user: api-server
  name: bouncer_validator
current-context: bouncer_validator
preferences: {}
users:
- name: api-server
  user:
    client-certificate: /etc/kubernetes/pki/apiserver.crt
    client-key:  /etc/kubernetes/pki/apiserver.key
```



### Create ImagePolicyWebhook AdmissionConfiguration file(json/yaml), update custom kubeconfig file at

- `/etc/kubernetes/pki/admission_config.json`

```yaml
apiVersion: apiserver.config.k8s.io/v1
kind: AdmissionConfiguration
plugins:
- name: ImagePolicyWebhook
  configuration:
    imagePolicy:
      kubeConfigFile: /etc/kubernetes/pki/webhook/admission_kube_config.yaml
      allowTTL: 50
      denyTTL: 50
      retryBackoff: 500
      defaultAllow: false
```



### Enable ImagePolicyWebhook in enable-admission-plugins in kubeapi server config at

- Update admin-config file in kube api server admission-control-config-file

  - `/etc/kubernetes/manifests/kube-apiserver.yaml`

  ```sh
  - --enable-admission-plugins=NodeRestriction,ImagePolicyWebhook
  - --admission-control-config-file=/etc/kubernetes/pki/admission_config.json
  ```

  

- Test

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: dh-busybox
spec:
  restartPolicy: Never
  containers:
  - name: busybox
    image: docker.io/library/busybox
    #image: gcr.io/google-containers/busybox:1.27
    command: ['sh', '-c', 'sleep 3600']
```



- Ref: https://stackoverflow.com/questions/54463125/how-to-reject-docker-registries-in-kubernetes
- Ref: https://github.com/containerd/cri/blob/master/docs/registry.md
- Ref: https://kubernetes.io/docs/reference/access-authn-authz/admission-controllers/#imagepolicywebhook
- Ref: https://kubernetes.io/blog/2019/03/21/a-guide-to-kubernetes-admission-controllers/
- Ref: https://computingforgeeks.com/how-to-install-cloudflare-cfssl-on-linux-macos/





## Approach 2 - ConstraintTemplate

### Create ConstraintTemplate CRD to whitelist docker registries

```yaml
apiVersion: templates.gatekeeper.sh/v1beta1
kind: ConstraintTemplate
metadata:
  name: k8sallowedrepos
spec:
  crd:
    spec:
      names:
        kind: K8sAllowedRepos
      validation:
        # Schema for the `parameters` field
        openAPIV3Schema:
          properties:
            repos:
              type: array
              items:
                type: string
targets:
  - target: admission.k8s.gatekeeper.sh
    rego: |
      package k8sallowedrepos
      violation[{"msg": msg}] {
        container := input.review.object.spec.containers[_]
        satisfied := [good | repo = input.parameters.repos[_] ; good = startswith(container.image,repo)]
        not any(satisfied)
        msg := sprintf("container <%v> has an invalid image repo <%v>, allowed repos are %v",[container.name, container.image, input.parameters.repos])
      }
      violation[{"msg": msg}] {
        container := input.review.object.spec.initContainers[_]
        satisfied := [good | repo = input.parameters.repos[_] ; good = startswith(container.image,repo)]
        not any(satisfied)
        msg := sprintf("container <%v> has an invalid image repo <%v>, allowed repos are %v",
        [container.name, container.image, input.parameters.repos])
      }
```



### Create a Resource Constraint with allowed docker registries

```yaml
apiVersion: constraints.gatekeeper.sh/v1beta1
kind: K8sAllowedRepos
metadata:
  name: whitelist-dockerhub
spec:
  match:
  kinds:
  - apiGroups: [""]
    kinds: ["Pod"]
parameters:
  repos:
  - "docker.io"
```



### Create a pod with valid registry and test

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: dh-busybox
spec:
  restartPolicy: Never
  containers:
  - name: busybox
    image: docker.io/library/busybox
    command: ['sh', '-c', 'sleep 3600']
```