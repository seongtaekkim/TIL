# kube-bench

- **kube-bench:** Tool to check Kubernetes cluster CIS Kubernetes Benchmarks

- Can Deploy as a Docker Container
  - `docker run --rm -v `pwd`:/host aquasec/kube-bench:latest install`



- kube-bench 결과를 바탕으로 위반사항 검색

  ```sh
  # 마스터 노드 검사
  ./kube-bench --config-dir `pwd`/cfg --config `pwd`/cfg/config.yaml master
  
  # 워커 노드 검사
  ./kube-bench --config-dir `pwd`/cfg --config `pwd`/cfg/config.yaml node
  ```



개선 방법

- kubelet :  /var/lib/kubelet/config.yaml
  - `sudo systemctl restart kubelet`
- control plane :  `/etc/kubernetes/manifests/`

[reference](https://github.com/aquasecurity/kube-bench/blob/main/docs/installation.md)



### example

---



Fix all issues via configuration and restart the affected components to ensure the new setting takes effect.

1. Fix all of the following violations that were found against the **API server**:

- a. Ensure that the RotateKubeletServerCertificate argument is set to true.
- b. Ensure that the admission control plugin PodSecurityPolicy is set.
- c. Ensure that the --kubelet-certificate-authority argument is set as appropriate.

2.  Fix all of the following violations that were found against the **Kubelet**:

- a. Ensure the --anonymous-auth argumentissettofalse.
-  b. Ensure that the --authorization-mode argumentissetto Webhook.

3. Fix all of the following violations that were found against the **ETCD**:

- a. Ensure that the --auto-tls argumentisnotsettotrue
- b. Ensure that the --peer-auto-tls argumentisnotsettotrue



~~~sh
Fix all of thefollowing violations that were found against the API server:-

apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
labels:
  component: kubelet
  tier: control-plane
  name: kubelet
namespace: kube-system
spec:
containers:
- command:
- kube-controller-manager
+ - --feature-gates=RotateKubeletServerCertificate=true
  image: gcr.io/google_containers/kubelet-amd64:v1.6.0
  livenessProbe: failureThreshold: 8 httpGet:
host: 127.0.0.1
path: /healthz
port: 6443
scheme: HTTPS
initialDelaySeconds: 15
timeoutSeconds: 15
name:kubelet
resources:
requests:
cpu: 250m
volumeMounts:
- mountPath: /etc/kubernetes/ name: k8s
readOnly: true
- mountPath: /etc/ssl/certs name: certs
- mountPath: /etc/pki name:pki hostNetwork: true volumes:
- hostPath:
path: /etc/kubernetes
name: k8s
- hostPath:
path: /etc/ssl/certs
name: certs
- hostPath: path: /etc/pki name: pki


✑ b. Ensure that theadmission control plugin PodSecurityPolicy is set.
audit: "/bin/ps -ef | grep $apiserverbin | grep -v grep" tests:
test_items:
- flag: "--enable-admission-plugins"
compare:
op: has
value:"PodSecurityPolicy"
set: true
remediation: |
Follow the documentation and create Pod Security Policy objects as per your environment. Then, edit the API server pod specification file $apiserverconf
on themaster node and set the --enable-admission-plugins parameter to a
value that includes PodSecurityPolicy :
--enable-admission-plugins=...,PodSecurityPolicy,...
Then restart the API Server.
scored: true


✑ c. Ensure thatthe --kubelet-certificate-authority argument is set as appropriate.
audit: "/bin/ps -ef | grep $apiserverbin | grep -v grep"
tests:
test_items:
- flag: "--kubelet-certificate-authority" set: true
remediation: |
Follow the Kubernetes documentation and setup the TLS connection between the apiserver and kubelets. Then, edit the API server pod specification file $apiserverconf on the master node and set the --kubelet-certificate-authority parameter to the path to the cert file for the certificate authority. --kubelet-certificate-authority=<ca-string>
scored: true



Fix all of the following violations that were found against the ETCD:-
✑ a. Ensurethat the --auto-tls argument is not set to true

Edit the etcd pod specification file $ etcd conf on the masternode and either remove the -- auto-tls parameter or set it to false.--auto-tls=false

✑ b. Ensure that the --peer-auto-tls argumentisnotsettotrue
Edit the etcd pod specification file $etcdconf on the masternode and either remove the -- peer-auto-tls parameter or set it to false.--peer-auto-tls=false
~~~





