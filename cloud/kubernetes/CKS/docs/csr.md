# CSR



## CSR with TLS

- mTLS: Is secure communication between pods
- With service mesh Istio & Linkerd mTLS is easier, managable
  - mTLS can be Enforced or Strict
- Istio: https://istio.io/latest/docs/tasks/security/authentication/mtls-migration/
- Linkerd: https://linkerd.io/2.11/features/automatic-mtls/
- Article: https://itnext.io/how-to-secure-kubernetes-in-cluster-communication-5a9927be415b

**Steps for TLS certificate for a Kubernetes service accessed through DNS**

- Download and install CFSSL

- Generete private key using `cfssl genkey`

- Create CertificateSigningRequest

  ```sh
  cat <<EOF | kubectl apply -f -
  apiVersion: certificates.k8s.io/v1
  kind: CertificateSigningRequest
  metadata:
    name: my-svc.my-namespace
  spec:
    request: $(cat server.csr | base64 | tr -d '\n')
    signerName: kubernetes.io/kubelet-serving
    usages:
    - digital signature
    - key encipherment
    - server auth
  EOF
  ```

- Get CSR approved (by k8s Admin)

  - `kubectl certificate approve my-svc.my-namespace`

- Once approved then retrive from status.certificate

  - `kubectl get csr my-svc.my-namespace -o jsonpath='{.status.certificate}' | base64 --decode > server.crt`

- Download and use it

  ```sh
  kubectl get csr
  ```

  

-  https://kubernetes.io/docs/tasks/tls/managing-tls-in-a-cluster/



## CSR with role

- 유저가 존재할 때(key, cert) CSR을 생성하고, rolebinding 하는 방법

~~~sh
Create a new user called john. Grant him access to the cluster. John should have permission to create, list, get, update and delete pods in the development namespace . The private key exists in the location: /root/CKA/john.key and csr at /root/CKA/john.csr.


Important Note: As of kubernetes 1.19, the CertificateSigningRequest object expects a signerName.

Please refer the documentation to see an example. The documentation tab is available at the top right of terminal.

CSR: john-developer Status:Approved

Role Name: developer, namespace: development, Resource: Pods

Access: User 'john' has appropriate permissions
~~~



~~~sh
cat john.csr | base64 -w 0
~~~

~~~yaml
apiVersion: certificates.k8s.io/v1
kind: CertificateSigningRequest
metadata:
  name: myuser
spec:
  request: "bas64 paste"
  signerName: kubernetes.io/kube-apiserver-client
  expirationSeconds: 86400  # one day
  usages:
  - client auth
~~~

~~~sh
k create -f csr "cert.yaml"
~~~



- role

~~~yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  namespace: development
  name: developer
rules:
- apiGroups: [""] # "" indicates the core API group
  resources: ["pods"]
  verbs: ["create", "delete", "list", "get", "update"]
~~~



- rolebinding

~~~yaml
apiVersion: rbac.authorization.k8s.io/v1
# This role binding allows "jane" to read pods in the "default" namespace.
# You need to already have a Role named "pod-reader" in that namespace.
kind: RoleBinding
metadata:
  name: dev-user-binding
  namespace: development
subjects:
# You can specify more than one "subject"
- kind: User
  name: john # "name" is case sensitive
roleRef:
  # "roleRef" specifies the binding to a Role / ClusterRole
  kind: Role #this must be Role or ClusterRole
  name: developer # this must match the name of the Role or ClusterRole you wish to bind to
~~~



~~~sh
k auth can-i create deployment --as=john -n development
k certificate approve john
k get csr
~~~

