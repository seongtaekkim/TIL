



## Authentication

인증주체는 user(Admin, Developers) 와 Service Accounts (Bots) 로 나뉘어짐

계정을 따로 새성할 수는 없는데 ServiceAccount 는 생성할 수 있다.

User는 kubectl, curl https://kube-server-ip:6443/ 으로 apiserver로 요청하는데, 요청 처리전에 인증절차를 거친다

인증을 위한 쉬운 절차 중 하나는, static 인데, apiserver yaml에 아래와 같이 csv 를작성해서 인증할 수 있다.

 \- --basic-auth-file=/tmp/users/user-details.csv

안전하지 않기에 권장하지 않는다.









## Article on Setting up Basic Authentication

기본 인증 설정에 대한 문서

#### Setup basic authentication on Kubernetes (Deprecated in 1.19)

> Note: This is not recommended in a production environment. This is only for learning purposes. Also note that this approach is deprecated in Kubernetes version 1.19 and is no longer available in later releases

Follow the below instructions to configure basic authentication in a kubeadm setup.

Create a file with user details locally at `/tmp/users/user-details.csv`

```
# User File Contentspassword123,user1,u0001password123,user2,u0002password123,user3,u0003password123,user4,u0004password123,user5,u0005
```



Edit the kube-apiserver static pod configured by kubeadm to pass in the user details. The file is located at `/etc/kubernetes/manifests/kube-apiserver.yaml`



```
apiVersion: v1kind: Podmetadata:  name: kube-apiserver  namespace: kube-systemspec:  containers:  - command:    - kube-apiserver      <content-hidden>    image: k8s.gcr.io/kube-apiserver-amd64:v1.11.3    name: kube-apiserver    volumeMounts:    - mountPath: /tmp/users      name: usr-details      readOnly: true  volumes:  - hostPath:      path: /tmp/users      type: DirectoryOrCreate    name: usr-details
```



Modify the kube-apiserver startup options to include the basic-auth file



```
apiVersion: v1kind: Podmetadata:  creationTimestamp: null  name: kube-apiserver  namespace: kube-systemspec:  containers:  - command:    - kube-apiserver    - --authorization-mode=Node,RBAC      <content-hidden>    - --basic-auth-file=/tmp/users/user-details.csv
```

Create the necessary roles and role bindings for these users:



```
---kind: RoleapiVersion: rbac.authorization.k8s.io/v1metadata:  namespace: default  name: pod-readerrules:- apiGroups: [""] # "" indicates the core API group  resources: ["pods"]  verbs: ["get", "watch", "list"] ---# This role binding allows "jane" to read pods in the "default" namespace.kind: RoleBindingapiVersion: rbac.authorization.k8s.io/v1metadata:  name: read-pods  namespace: defaultsubjects:- kind: User  name: user1 # Name is case sensitive  apiGroup: rbac.authorization.k8s.ioroleRef:  kind: Role #this must be Role or ClusterRole  name: pod-reader # this must match the name of the Role or ClusterRole you wish to bind to  apiGroup: rbac.authorization.k8s.io
```

Once created, you may authenticate into the kube-api server using the users credentials

```
curl -v -k https://localhost:6443/api/v1/pods -u "user1:password123"
```











## TLS Basics









##  View Certificate Details

로그 등으로 인증서 상세정보 등을 살펴볼 수 있다.



###  리소스: Kubernetes 인증서 상태 점검 스프레드시트 다운로드

I have uploaded the Kubernetes Certificate Health Check Spreadsheet here:

https://github.com/mmumshad/kubernetes-the-hard-way/tree/master/tools

Feel free to send in a pull request if you improve it.





## Test



~~~
 openssl x509 -in /etc/kubernetes/pki/apiserver.crt -text -noout
~~~



~~~
 openssl x509 -in /etc/kubernetes/pki/etcd/server.crt -text -noout 
~~~





### 12

The certificate file used here is incorrect. It is set to `/etc/kubernetes/pki/etcd/server-certificate.crt` which does not exist. As we saw in the previous questions the correct path should be `/etc/kubernetes/pki/etcd/server.crt`.

```
root@controlplane:~# ls -l /etc/kubernetes/pki/etcd/server* | grep .crt
-rw-r--r-- 1 root root 1188 May 20 00:41 /etc/kubernetes/pki/etcd/server.crt
root@controlplane:~# 
```

Update the YAML file with the correct certificate path and wait for the ETCD pod to be recreated. wait for the `kube-apiserver` to get to a `Ready` state.


**NOTE**: It may take a few minutes for the kubectl commands to work again so please be patient.



### 13

If we inspect the `kube-apiserver` container on the `controlplane`, we can see that it is frequently exiting.

```sh
root@controlplane:~# crictl ps -a | grep kube-apiserver
1fb242055cff8       529072250ccc6       About a minute ago   Exited              kube-apiserver            3                   ed2174865a416       kube-apiserver-controlplane
```

If we now inspect the logs of this exited container, we would see the following errors:

```sh
root@controlplane:~# crictl logs --tail=2 1fb242055cff8  
W0916 14:19:44.771920       1 clientconn.go:1331] [core] grpc: addrConn.createTransport failed to connect to {127.0.0.1:2379 127.0.0.1 <nil> 0 <nil>}. Err: connection error: desc = "transport: authentication handshake failed: x509: certificate signed by unknown authority". Reconnecting...
E0916 14:19:48.689303       1 run.go:74] "command failed" err="context deadline exceeded"
```

This indicates an issue with the ETCD CA certificate used by the `kube-apiserver`. Correct it to use the file `/etc/kubernetes/pki/etcd/ca.crt`.

Once the YAML file has been saved, wait for the `kube-apiserver` pod to be `Ready`. This can take a couple of minutes.



## Certificates API

관리자 권한키를 생성하는 방법.





## Test



~~~
cat akshay.csr  | base64 -w 0
~~~



- document 에서 검색해서 생성할 수 있다.

~~~
---
apiVersion: certificates.k8s.io/v1
kind: CertificateSigningRequest
metadata:
  name: akshay
spec:
  groups:
  - system:authenticated
  request:
    LS0tLS1CRUdJTiBDRVJUSUZJQ0FURSBSRVFVRVNULS0tLS0KTUlJQ1ZqQ0NBVDRDQVFBd0VURVBNQTBHQTFVRUF3d0dZV3R6YUdGNU1JSUJJakFOQmdrcWhraUc5dzBCQVFFRgpBQU9DQVE4QU1JSUJDZ0tDQVFFQTE0cGJpWUxnbjhhQVY5T1dJZWt1UHpBTXhiWkFOTDhGWUhmcGRmNE1xZmd5CktUbHdEcU50SzBrKzFtcU10YS9qckIzZ09Qck1DL2d0bW1UaWd3QVdsTi9TZGZaRGQ2RmR1cU91bHl6UWx0MkUKRG5RRG9LZU8xYnlvRk0xNVh6Y1BDdEtIZDV1Wi8rNDIvdUhlUHhXZ3prMlY3aXBlT0d0cHFmZllyTUR0UDFCYwpNVzVkMTZDYVpVUm9ZU01GaEVFbExDSHdFaFVkc1ZKYVVXWldCemVqZ3h5MEd0eGx0UXVuWG5Nc1g1SlZaUytQCnpRMGljSFhXL0ZHbzZtZy9wai9BL1AvbkQ0L1Jnc0gzLzVIQ3hpd3BpaGtmY3dMeWZrdFVCdVl3c2FUaE5Zb3QKYU5Zb0FkWFV5KzlLZitXcVhPZUgycDExZ3FVb0x2UUN2NS9vQWk1SVhRSURBUUFCb0FBd0RRWUpLb1pJaHZjTgpBUUVMQlFBRGdnRUJBQlU4RkR6SWdmV0VPRFdpRE5JMytmUlJrQ2ZFNkJ3L0J5RWdvRG1FZXAwVjRzK3JXV1Y2ClRUbzZrRWhjUTFQMGRtZzBEcC92SjkrczR1YktnSEladElTQmthWVdXZ0J5ejQzcnZZSFdQbTl1R1VwbVREVlEKNFVGT1FlcHdHYlR6QUtsdzd3LzN1UEp6cWJZanlvcjhocFQxOTZtem8wUkhQdDllWmwxeTFJNVdZbkNvRXBaZgp4TWhxd2s5QVIxMUxiNDlTMHp1V2JidmVoZk0xSmIvNmFNcmVRYVUyZEJvbkhyOENubzdodTBkcVliZ0JFNFFqCjNLcVV0dERydlE1V1Q5T3lPWG9uc1A3SW1LekFRblVpZVplQmcrWGFpdnFkYWlRdlFPTjY1Y0t5TWJpbm0vOVMKYXpWQWlBS1FvOTBFYzBWVjNWQmN4aUJ4RlJ0Y2ZqSXkvVGc9Ci0tLS0tRU5EIENFUlRJRklDQVRFIFJFUVVFU1QtLS0tLQo=
  signerName: kubernetes.io/kube-apiserver-client
  usages:
  - client auth

~~~



~~~
 k get CertificateSigningRequest -o wide # pending
~~~



~~~
 k certificate approve akshay
~~~



~~~
 k get csr agent-smith -o yaml # group 확인
~~~



~~~
kubectl certificate deny agent-smith # deny
~~~



## KubeConfig

cluseter / contest / user 를 연결하는 config파일을 만든다

특정유저가 명령을 내릴 때마다 필요한 인증키를 생략하기 위해서이다.

namespace도 명시할 수 있다.

파일적용을 변경할 수 도 있다.



## Test



~~~
cat .kube/config
~~~



~~~
kubectl config --kubeconfig=/root/my-kube-config use-context research

kubectl config --kubeconfig=/root/my-kube-config current-context
~~~

### 영구 키/값 저장소

We have already seen how to secure the ETCD key/value store using TLS certificates in the previous videos. Towards the end of this course, when we setup an actual cluster from scratch we will see this in action.



## API Groups



## Authorization



계정간 인가를 다르게 책정하는 방법

NODE ABAC RBAC

apiserver 에서 설정할 수 있다.



##  Role Based Access Controls



~~~
k get roles
k get rolbinding

k auth can-i delete nodes
k auth can-i create pods --as dev-user
~~~





## Test



~~~
ps -aux | grep authorization
~~~





~~~
k auth can-i list  pods --as dev-user
~~~



~~~
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  namespace: default
  name: developer
rules:
- apiGroups: [""] # "" indicates the core API group
  resources: ["pods"]
  verbs: ["create", "delete", "list"]
~~~





~~~apiVersion: rbac.authorization.k8s.io/v1
apiVersion: rbac.authorization.k8s.io/v1
# This role binding allows "jane" to read pods in the "default" namespace.
# You need to already have a Role named "pod-reader" in that namespace.
kind: RoleBinding
metadata:
  name: dev-user-binding
  namespace: default
subjects:
# You can specify more than one "subject"
- kind: User
  name: dev-user # "name" is case sensitive
roleRef:
  # "roleRef" specifies the binding to a Role / ClusterRole
  kind: Role #this must be Role or ClusterRole
  name: developer # this must match the name of the Role or ClusterRole you wish to bind to
~~~





~~~
 kubectl edit role developer -n blue #  resourceNames 변경 (pod 이름)
~~~



~~~
k --as dev-user get pod dark-blue-app -n -blue # forbidden 확인
~~~





### 11

~~~
k --as dev-user create deployment nginx --image=nginx -n blue # forbidden
~~~



Edit the `developer` role in the `blue` namespace to add a new rule under the `rules` section.

Append the below rule to the end of the file

```
kubectl edit role developer -n blue
- apiGroups:
  - apps # deployment 는 apps/v1 이라서 그럼.
  resources:
  - deployments
  verbs:
  - create
```

So it looks like this:

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: Role
metadata:
  name: developer
  namespace: blue
rules:
- apiGroups:
  - apps
  resourceNames:
  - dark-blue-app
  resources:
  - pods
  verbs:
  - get
  - watch
  - create
  - delete
- apiGroups:
  - apps
  resources:
  - deployments
  verbs:
  - create
```





##  Cluster Roles and Role Bindings

- cluster role 생성



## Test





ClusterRole is a non-namespaced resource. You can check via the `kubectl api-resources --namespaced=false` command. So the correct answer would be `Cluster Roles are cluster wide and not part of any namespace`.

~~~
kubectl api-resources --namespaced=false
~~~







~~~
k get nodes --as michelle  # forbidden
k create clusterrole --help
~~~



~~~
k create clusterrole michelle-role --verb=get,list,watch --resource=nodes
~~~



~~~
k create clusterrolebinding michelle-role-binding --clusterrole=michelle-role --user=michelle
~~~





~~~
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  name: role
rules:
- apiGroups: [""]
  resources: ["nodes"]
  verbs: ["list", "get", "create", "delete"]
~~~





~~~
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: cluster-admin-role-binding
subjects:
- kind: User
  name: michelle
  apiGroup: rbac.authorization.k8s.io
roleRef:
  kind: ClusterRole
  name: role
  apiGroup: rbac.authorization.k8s.io
~~~





~~~
k auth can-i list nodes --as michelle
~~~







### 8

`michelle`'s responsibilities are growing and now she will be responsible for storage as well. Create the required `ClusterRoles` and `ClusterRoleBindings` to allow her access to Storage.

Get the API groups and resource names from command `kubectl api-resources`. Use the given spec:

~~~
 k api-resources # short name, apiversion
~~~



~~~
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  name: storage-admin
rules:
- apiGroups: [""]
  resources: ["persistentvolumes"]
  verbs: ["list", "get", "create", "delete"]
- apiGroups: ["storage.k8s.io"]
  resources: ["storageclasses"]
  verbs: ["list", "get", "create", "delete"]
~~~



~~~
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: michelle-storage-admin
subjects:
- kind: User
  name: michelle
  apiGroup: rbac.authorization.k8s.io
roleRef:
  kind: ClusterRole
  name: storage-admin
  apiGroup: rbac.authorization.k8s.io
~~~





~~~
kubectl auth can-i list storageclasses --as michelle.
~~~



~~~
k --as michelle get storageclass
~~~





## Service Accounts



1.22 이전에서 sa 생성시 시크릿이 생성되는데, 내부에 token에 기한만료가 없다. 

1.24 이후 sa 생성 시 secret 생성이 안된다.





## Test



~~~
  kubectl create token dashboard-sa
~~~



- deploy edit 에서 template spec 에 아래와 같이 추가.
-  이렇게 하면 dashboard-sa 에 대한 토큰을 입력하지 않아도 목록을 볼 수 있게 된다.

~~~
serviceAccountName: dashboard-sa
~~~



~~~
k describe sa dashboard-sa # 여기서 토큰값을 복사

k describe secret "토큰값 paste" # 결과를 복사하여 웹 ui 에 붙여넣으면 된다.
~~~





​	

## Image Security

image를 private registry 에서 가져올 경우 문서작성 시

imagesecret 을 통해 자격증명작성된 secret 을 입력하여 가져울 수 있도록 한다.





## Test



- secret type을 볼 수 있다.
- image 관련해서는 docker-registry 로 생성한다.

~~~kubectl create secret --help
kubectl create secret --help
~~~



- private server 의 이미지

~~~
myprivateregistry.com:5000/nginx:alpine
~~~



- private image pull 할 때 인증을 위한 secret 객체 생성

~~~
k create secret docker-registry -h # 참고
~~~



~~~
kubectl create secret docker-registry private-reg-cred --docker-username=dock_user --docker-password=dock_password --docker-server=myprivateregistry.com:5000 --docker-email=dock_user@myprivateregistry.com
~~~



- deploy > template spec 에 아래와같이 작성한다.

~~~
        imagePullSecrets:
        - name: private-reg-cred
~~~



##  Pre-requisite - Security in Docker

도커 기본 보안을 설정.

특정유저 명으로 컨테이너프로세스를 실행하거나

컨테이너의 root가 실행하는 명령어 (reboot 등) 을 add 하거나 remove 할 수 있다.	 



## Security Contexts

도커에서의 설정을 pod에도 할 수 있다.





## Test



- 컨테이너수준, pod 수준 둘다 선택이 가능하다.
- 컨테이너 수준은 image 내부에 설정해야 한다.

~~~
securityContext:
  runAsUser: 
~~~



~~~
k exec ubuntu-sleeper -- whoami # 유저가 없다고 나옴.
# pod이 컨테이너를 실행하고 있기에 문제가 되지는 않는다.
~~~





~~~
    securityContext:
      capabilities:
        add: ["SYS_TIME"]
~~~





## Solution Security Contexts



## Network Policy

- ingress, egress 등의 network policy 로 제약을 둘 수 있다.
- 이는 selector로 파악할 수 있다.



## Developing network policies

ingress 

from

- "-"  를기준으로 하면, podSelector, namespaceSelector, ipBlock 이 and조건으로 붙는다.

- 또는 아래처럼 하면 podSelector와 namespaceSelector 는 or 이 된다.

  ~~~
  - from:
    - podSelector:
      namespaceSelector
    - ipBlock
  ~~~

  





## Test



![스크린샷 2023-07-08 오전 2.35.02](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-08 오전 2.35.02.png)



~~~
k get pod --show-labels
~~~





![스크린샷 2023-07-08 오전 2.39.37](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-08 오전 2.39.37.png)





![스크린샷 2023-07-08 오전 2.41.58](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-08 오전 2.41.58.png)



![스크린샷 2023-07-08 오전 2.42.50](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-08 오전 2.42.50.png)



~~~
k get service
~~~



~~~
apiVersion: v1
items:
- apiVersion: networking.k8s.io/v1
  kind: NetworkPolicy
  metadata:
    name: internal-policy
  spec:
    egress:
    - to:
      - podSelector:
          matchLabels:
            name: mysql
      ports:
      - port: 3306
        protocol: TCP
    - to:
      - podSelector:
          matchLabels:
            name: payroll
      ports:
      - port: 8080
        protocol: TCP

    podSelector:
      matchLabels:
        name: internal
    policyTypes:
    - Egress
  status: {}
kind: List
metadata:
  resourceVersion: ""

~~~





## Kubectx and Kubens – Command line Utilities

### Kubectx 및 Kubens – 명령줄 유틸리티

Through out the course, you have had to work on several different namespaces in the practice lab environments. In some labs, you also had to switch between several contexts.



While this is excellent for hands-on practice, in a real “live” kubernetes cluster implemented for production, there could be a possibility of often switching between a large number of namespaces and clusters.



This can quickly become and confusing and overwhelming task if you had to rely on kubectl alone.



This is where command line tools such as kubectx and kubens come in to picture.



Reference: https://github.com/ahmetb/kubectx



**Kubectx:**

With this tool, you don't have to make use of lengthy “kubectl config” commands to switch between contexts. This tool is particularly useful to switch context between clusters in a multi-cluster environment.



**Installation:**

```
sudo git clone https://github.com/ahmetb/kubectx /opt/kubectxsudo ln -s /opt/kubectx/kubectx /usr/local/bin/kubectx
```



**Syntax:**

To list all contexts:

> kubectx



To switch to a new context:

> kubectx <context_name>



To switch back to previous context:

> kubectx -



To see current context:

> kubectx -c





**Kubens:**

This tool allows users to switch between namespaces quickly with a simple command.

**Installation:**

```
sudo git clone https://github.com/ahmetb/kubectx /opt/kubectxsudo ln -s /opt/kubectx/kubens /usr/local/bin/kubens
```



**Syntax:**

To switch to a new namespace:

> kubens <new_namespace>



To switch back to previous namespace:

> kubens -





























































