

## Cluster Setup (10%)

### 1. Use Network security policies to restrict cluster level access

- NetworkPolicy 설정. [link](./networkpolicy.md)

### 2. Use CIS benchmark to review the security configuration of Kubernetes components (etcd, kubelet, kubedns, kubeapi)

- kube-bench 를 이용해서 위반사항 추출 및 개선. [link](./kube-bench.md)


### 3. Properly set up Ingress objects with security control

- ingress에 tls 설정하는 문제 [link](./ingress-and-TLS.md)

  - Create TLS Certificate & key
  - Create a Secret
  - Apply to ingress yaml

  - [reference](https://kubernetes.io/ko/docs/concepts/services-networking/ingress/#tls)

### 4. Protect node metadata and endpoints

- NetworkPolicy 설정해서  서버 접근 못하게 하는 문제. 네임스페이스 만 허용하도록 하는 변형 가능해보임. [link](./networkpolicy.md)
- 클라우드 프로바이더 metadata 서버 쪽으로 접근 못하게 설정. `podSelector`를 통해 특정 파드는 접근 열 수 있음.

### 5. Minimize use of, and access to, GUI elements

- Restrict Access to GUI like Kubernetes Dashboard
- kubernetes-dashboard RBAC 설정. [link](dashboard-by-RBAC.md)
  - Creating a Service Account User
  - Create ClusterRole
  - Create ClusterRoleBinding
  - Retrieve Bearer Token & Use

### 6. Verify platform binaries before deploying

- `sha512sum` 으로 바이너리 해시값 비교하는 문제 [link](./binaries.md)

​	

## Cluster Hardening (15%)

### 7. Restrict access to Kubernetes API

- 유저 생성,CERT 발급, kubeconfig 설정, role생성. [link](./clusterrole.md)



### 8. Use Role Based Access Controls to minimize exposure

- sa, role, rolebinding 만들고 Pod에 설정. [link](role.md)




### 9. Exercise caution in using service accounts e.g. disable defaults, minimize permissions on newly created ones

- 권한 & SA 생성하고 바인딩. 그리고 특정 파드에서 sa 사용하는 문제 [link](./role.md)

  

### 10. Update Kubernetes frequently

- [link](./upgrade.md)






## System Hardening (15%)

### 11. Minimize host OS footprint (reduce attack surface)

- privileged true로 되어있는 파드 검출하고 삭제하는 문제

  ```yaml
  spec:
    securityContext:
      runAsUser: RunAsAny
      runAsGroup: RunAsAny
      fsGroup: RunAsAny
    # container will use host IPC namespace (Default is false)
    hostIPC: true
    # containers will use host network namespace (Default is false)
    hostNetwork: true
    # containers will use host pid namespace (Default is false)
    hostPID: true
    containers:
      - image: nginx:latsts
        name: web
        resources: {}
        securityContext:
          # container will ran as root (Default is false)
          privileged: true
  ```



- Containers will use host namespace(not k8s ns)

1. **Create Pod to use host namespace only if necessary.**

```yaml
spec:
  # container will use host IPC namespace (Default is false)
  hostIPC: true
  # containers will use host network namespace (Default is false)
  hostNetwork: true
  # containers will use host pid namespace (Default is false)
  hostPID: true
  containers:
  - name: nginx
    image: nginx:latest
```



1. **Don't run containers in privileged mode (privileged = false)**

```yaml
spec:
  securityContext:
    runAsUser: RunAsAny
    runAsGroup: RunAsAny
    fsGroup: RunAsAny
  # container will use host IPC namespace (Default is false)
  hostIPC: true
  # containers will use host network namespace (Default is false)
  hostNetwork: true
  # containers will use host pid namespace (Default is false)
  hostPID: true
  containers:
    - image: nginx:latsts
      name: web
      resources: {}
      securityContext:
        # container will ran as root (Default is false)
        privileged: true
```



- **Limit Node Access**

```sh
# delete user
userdel user1
# delete group
groupdel group1
#suspend user
usermod -s /usr/sbin/nologin user2
#create user sam, home dir is /opt/sam, uid 2328 & login shell bash
useradd -d /opt/sam -s /bin/bash -G admin -u 2328 sam
```



- **Remove Obsolete/unnecessary Software**

```sh
# list all services
systemctl list-units --type service
# stop services
systemctl stop squid
# disable services
systemctl disable squid
# uninstall
apt remove squid
```



- **SSH hardening**

```sh
#generate keys
ssh-keygen –t rsa

#view auth key
cat /home/mark/.ssh/authorized_keys

# harden ssh config /etc/ssh/sshd_config
PermitRootLogin no
PasswordAuthentication no

systemctl restart sshd
```



- **Restrict Obsolete Kernel Modules**

```sh
# list all kernel modules
lsmod
# blocklist module sctp, dccp
vi /etc/modprobe.d/blacklist.conf
blacklist sctp
blacklist dccp
#reboot
shutdown –r now
```



- **UFW**

```sh
Install ufw   apt-get intall ufw
    systemctl enable ufw
    systemctl start ufw

check ufw firewall status  ufw status/ufw status numbered
    ufw default allow outgoing
    ufw default deny incoming

Allow specific (80)   ufw allow from 172.1.2.5 to any port 22 proto tcp
    ufw allow 1000:2000/tcp
    ufw allow from 172.1.3.0/25 to any port 80 proto tcp
    ufw allow 22
default deny 8080  ufw deny 80
activate ufw firewall  ufw enable
    ufw delete deny 80
    ufw delete 5
reset ufw   ufw reset
activate ufw firewall  ufw disable
```



- **Restrict allowed hostpaths with PodSecurityPolicy**
  - using PodSecurityPolicy can restrict AllowedHostPaths (used by hostPath volumes)
  - Ref: https://kubernetes.io/docs/concepts/policy/pod-security-policy/#volumes-and-file-systems
- **Identify and Fix Open Ports, Remove Packages**

```sh
#Identify Open Ports, Remove Packages
list all installed packages         apt list --installed
list active services                systemctl list-units --type service
list the kernel modules             lsmod
search for service                  systemctl list-units --all | grep -i nginx
stop remove nginx services          systemctl stop nginx
remove nginx service packages       rm /lib/systemd/system/nginx.service
remove packages from controlplane   apt remove nginx -y
check service listing on 9090       netstat -atnlp | grep -i 9090 | grep -w -i listen
check port to service mapping       cat /etc/services | grep -i ssh
check port listing on 22            netstat -an | grep 22  | grep -w -i listen
check lighthttpd service port       netstat -natulp | grep -i light
```





### 12. Minimize IAM roles

- aws, gcp 등 상용 클라우드 권한 잘 설정하라는 내용 (시험하고 상관 없을듯)

- **Least Privilege:** make sure IAM roles of EC2 permissions are limited,
- **Block Access:** If not IAM; block CIDR, Firewall or NetPol. Example block EC2 169.254.169.254
- Use AWS Trusted Advisor/GCP Security Command Center/Adviser Ref: https://kubernetes.io/docs/reference/access-authn-authz/authentication/



### 13. Minimize external access to the network

- network policy [link](./networkpolicy.md)



### 14. Appropriately use kernel hardening tools such as AppArmor, seccomp

- AppArmor & seccomp 프로파일 생성은 시험에 안나올 거고 생성된 프로파일 사용하는 정도만 나올듯

- 🥸 AppArmor

  - ```
    aa-status
    ```

     

    명령어로 프로파일 확인하고 포드 어노테이션 설정

     

    ```
    container.apparmor.security.beta.kubernetes.io/<container_name>
    ```

    ```bash
    # controlplane
    apparmor_parser /root/profile
    aa-status | grep docker-nginx-custom
    
    # node01
    scp /root/profile node01:/root
    ssh node01
        apparmor_parser /root/profile
        aa-status | grep docker-nginx-custom
    ```

    ```yaml
    apiVersion: v1
    kind: Pod
    metadata:
      annotations:
        container.apparmor.security.beta.kubernetes.io/secure: localhost/docker-nginx-custom
      name: secure
    spec:
      containers:
      - image: nginx
        name: secure
    ```

- seccomp

  - kubelet seccomp 경로 확인 및 설정하고 해당 경로에 프로파일 저장

    - `/var/lib/kubelet/seccomp/profiles`
    - 모든 워커 노드에서 동일하게 작업

  - pod >

     

    `securityContext` > `seccompProfile`

     

    설정

    ```yaml
    apiVersion: v1
    kind: Pod
    metadata:
      name: audit-pod
      labels:
        app: audit-pod
    spec:
      securityContext:
        seccompProfile:
          type: Localhost
          # specfy violation.json or fine-grained.json
          localhostProfile: profiles/audit.json
      containers:
      - name: test-container
        image: hashicorp/http-echo:0.2.3
        args:
        - "-text=just made some syscalls!"
        securityContext:
          allowPrivilegeEscalation: false
    ```



#### 3.4.1 **SECCOMP PROFILES**

- restricting the system calls it is able to make from userspace into the kernel

- SECCOMP can operate with 3 modes

  - Mode **0 - disabled**
  - Mode **1 - strict mode**
  - Mode **2 - filtered mode**

- SECCOMP profile default directory `/var/lib/kubelet/seccomp/profiles`

- SECCOMP Profiles are 3 types

  - **Default Profile**
  - **Audit** - audit.json
  - **Violation**- violation.json
  - **Custom** - fine-grained.json

- SECCOMP profile action can be

  - `"action": "SCMP_ACT_ALLOW"`
  - `"action": "SCMP_ACT_ERRNO"`
  - `"defaultAction": "SCMP_ACT_LOG"`
  - Create POD with Default Profile

  ```
  apiVersion: v1
  kind: Pod
  metadata:
    name: audit-pod
    labels:
      app: audit-pod
  spec:
    securityContext:
      seccompProfile:
        # default seccomp
        type: RuntimeDefault
    containers:
    - name: test-container
      image: hashicorp/http-echo:0.2.3
      args:
      - "-text=just made some syscalls!"
      securityContext:
        allowPrivilegeEscalation: false
  ```

  

- Create POD with Specific Profile

  ```
  apiVersion: v1
  kind: Pod
  metadata:
    name: audit-pod
    labels:
      app: audit-pod
  spec:
    securityContext:
      seccompProfile:
        type: Localhost
        # specfy violation.json or fine-grained.json
        localhostProfile: profiles/audit.json
    containers:
    - name: test-container
      image: hashicorp/http-echo:0.2.3
      args:
      - "-text=just made some syscalls!"
      securityContext:
        allowPrivilegeEscalation: false
  ```

  

  ```
  #trace system calls
  strace -c touch /tmp/test.log
  
  # check SECCOMP is supported by kernel
  grep -i seccomp /boot/config-$(uname -r)
  
  #Run Kubbernetes POD (amicontained - open source inspection tool)
  kubectl run amicontained --image r.j3ss.co/amicontained amicontained -- amicontained
  kubectl logs amicontained
  
  #create dir in default location /var/lib/kubelet/seccomp
  mkdir -p /var/lib/kubelet/seccomp/profiles
  
  #use in pod
  localhostProfile: profiles/audit.json
  ```

  

  - Ref: https://kubernetes.io/docs/tutorials/clusters/seccomp
  - Ref: https://man7.org/linux/man-pages/man2/syscalls.2.html

#### 3.4.2 **APPARMOR**

- Kernel Security Module to granular access control for programs on Host OS

- **AppArmor Profile** - Set of Rules, to be enabled in nodes

- AppArmor Profile loaded in 2 modes

  - **Complain Mode** - Discover the program
  - **Enforce Mode** - prevent the program

- **create AppArmor Profile**

  ```
  sudo vi /etc/apparmor.d/deny-write
  
  #include <tunables/global>
  profile k8s-apparmor-example-deny-write flags=(attach_disconnected) {
    #include <abstractions/base>
    file,
    # Deny all file writes.
    deny /** w,
  }
  ```

  

- copy deny-write profile to worker node01 `scp deny-write node01:/tmp`

- load the profile on all our nodes default directory /etc/apparmor.d `sudo apparmor_parser /etc/apparmor.d/deny-write`

- apply to pod

  ```
  apiVersion: v1
  kind: Pod
  metadata:
    name: hello-apparmor
    annotations:
      container.apparmor.security.beta.kubernetes.io/hello: localhost/k8s-apparmor-example-deny-write
  spec:
    containers:
    - name: hello
      image: busybox
      command: [ "sh", "-c", "echo 'Hello AppArmor!' && sleep 1h" ]
  ```

  

- useful commands

  ```
  #check status            
  systemctl status apparmor
  
  #check enabled in nodes  
  cat /sys/module/apparmor/parameters/enabled
  
  #check profiles          
  cat /sys/kernel/security/apparmor/profiles
  
  #install                 
  apt-get install apparmor-utils
  
  #default Profile file directory  is /etc/apparmor.d/
  
  #create apparmor profile   
  aa-genprof /root/add_data.sh
  
  #apparmor module status    
  aa-status
  
  #load profile file           
  apparmor_parser -q /etc/apparmor.d/usr.sbin.nginx
  
  #load profile                
  apparmor_parser /etc/apparmor.d/root.add_data.sh
  aa-complain /etc/apparmor.d/profile1
  aa-enforce  /etc/apparmor.d/profile2
  
  #disable profile             
  apparmor_parser -R /etc/apparmor.d/root.add_data.sh
  ```

  

- Ref: https://kubernetes.io/docs/tutorials/clusters/apparmor

- Ref: https://gitlab.com/apparmor/apparmor/-/wikis/Documentation

- Ref: https://man7.org/linux/man-pages/man2/syscalls.2.html





## Minimize Microservice Vulnerabilities (20%)



### 15. Setup appropriate OS level security domains

- Setup appropriate OS level security domains e.g. using PSP, OPA, security contexts

  - pod 또는 container 레벨 securityContext, runAsNonRoot, privileged 설정하는 내용

  - PSP 설정 문제 반드시 나올 것으로 예상함

    - 파드시큐리티폴리시 리소스가 생성되면 아무 것도 수행하지 않는다.

    - 이를 사용하려면 요청 사용자 또는 대상 파드의 [서비스 어카운트](https://kubernetes.io/docs/tasks/configure-pod-container/configure-service-account/)는 정책에서 `use` 동사를 허용하여 정책을 사용할 권한이 있어야 한다.

      ```bash
      kubectl create role psp-access --verb=use --resource=podsecuritypolicies
      kubectl create rolebinding psp-access --role=psp-access --serviceaccount=default:default
      ```

  - securityContext

    - 루트 권한, 읽기쓰기 설정 같이 보안에 위협이 되는 설정을 가진 오브젝트 찾아서 삭제하는 문제.



#### **Admission Controller**

- Implement security measures to enforce. Triggers before creating a pod

- Enable a Controller in kubeadm cluster `/etc/kubernetes/manifests/kube-apiserver.yaml`

  ```
  spec:
  containers:
  - command:
  - kube-apiserver
  ...
  - --enable-admission-plugins=NameSpaceAutoProvision,PodSecurityPolicy
  image: k8s.gcr.io/kube-apiserver-amd64:v1.11.3
  name: kube-apiserver
  ```

  

- Ref: https://kubernetes.io/blog/2019/03/21/a-guide-to-kubernetes-admission-controllers/

#### 4.1.1 **Pod Security Policies (PSP)**

- Defines policies to controls security sensitive aspects of the pod specification

- PodSecurityPolicy is one of the admission controller

- enable at api-server using `--enable-admission-plugins=NameSpaceAutoProvision,PodSecurityPolicy`

- Create Pod using PSP

  ```
  apiVersion: policy/v1beta1
  kind: PodSecurityPolicy
  metadata:
    name: psp-test
    annotations:
      seccomp.security.alpha.kubernetes.io/allowedProfileNames: '*'
  spec:
    privileged: true
    allowPrivilegeEscalation: true
    allowedCapabilities:
    - '*'
    volumes:
    - '*'
    hostNetwork: true
    hostPorts:
    - min: 0
      max: 65535
    hostIPC: true
    hostPID: true
    runAsUser:
      rule: 'RunAsAny'
    seLinux:
      rule: 'RunAsAny'
    supplementalGroups:
      rule: 'RunAsAny'
    fsGroup:
      rule: 'RunAsAny'
  ```

  

  - Create Service Account `kubectl create sa psp-test-sa -n dev`

  - Create ClusterRole

     

    ```
    kubectl -n psp-test create clusterrole psp-pod --verb=use --resource=podsecuritypolicies --resource-name=psp-test
    ```

     

    or

    ```
    apiVersion: rbac.authorization.k8s.io/v1
    kind: ClusterRole
    metadata:
      name: psp-pod
    rules:
    - apiGroups: ['policy']
      resources: ['podsecuritypolicies']
      verbs:     ['use']
      resourceNames:
      - psp-test
    ```

    

  - Create ClusterRoleBinding

     

    ```
    kubectl -n psp-test create rolebinding -n dev psp-mount --clusterrole=psp-pod --serviceaccount=dev:psp-test-sa
    ```

     

    or

    ```
    apiVersion: rbac.authorization.k8s.io/v1
    kind: ClusterRoleBinding
    metadata:
      name: psp-mount
    roleRef:
      kind: ClusterRole
      name: psp-pod
      apiGroup: rbac.authorization.k8s.io
    subjects:
    - kind: ServiceAccount
      name: psp-test-sa
      namespace: dev
    ```

    

  - POD Access to PSP for authorization

    - Create Service Account or use Default Service account
    - Create Role with podsecuritypolicies, verbs as use
    - Create RoleBinding to Service Account and Role

- Ref: https://kubernetes.io/docs/concepts/policy/pod-security-policy/

#### 4.1.2 **Open Policy Agent (OPA)**

- OPA for enforcing authorization policies for kubernetes

  - All images must be from approved repositories
  - All ingress hostnames must be globally unique
  - All pods must have resource limits
  - All namespaces must have a label that lists a point-of-contact

- Deploy OPA Gatekeeper in cluster

  ```
  kubectl apply -f https://raw.githubusercontent.com/open-policy-agent/gatekeeper/release-3.7/deploy/gatekeeper.yaml
  
  helm repo add gatekeeper https://open-policy-agent.github.io/gatekeeper/charts --force-update
  helm install gatekeeper/gatekeeper --name-template=gatekeeper --namespace gatekeeper-system --create-namespace
  ```

  

- Example constraint template to enforce to require all lables

  ```
  #create template
  kubectl apply -f https://raw.githubusercontent.com/open-policy-agent/gatekeeper/master/demo/basic/templates/k8srequiredlabels_template.yaml
  ```

  

  ```
  apiVersion: constraints.gatekeeper.sh/v1beta1
  kind: K8sRequiredLabels
  metadata:
    name: ns-must-have-hr
  spec:
    match:
      kinds:
        - apiGroups: [""]
          kinds: ["Namespace"]
    parameters:
      labels: ["hr"]
  ```

  

  `kubectl get constraints`

- Ref: https://kubernetes.io/blog/2019/08/06/opa-gatekeeper-policy-and-governance-for-kubernetes/

- Ref: https://open-policy-agent.github.io/gatekeeper/website/docs/install/

#### 4.1.3 **Security Contexts**

- Defines privilege, access control, Linux capabilities settings for a Pod or Container

- Set the security context for a Pod (applies to all containers)

  ```
  apiVersion: v1
  kind: Pod
  metadata:
    name: security-context-demo
  spec:
    securityContext:
      runAsUser: 1000
      runAsGroup: 3000
      fsGroup: 2000
    volumes:
    - name: sec-ctx-vol
      emptyDir: {}
    containers:
    - name: sec-ctx-demo
      image: busybox
      command: [ "sh", "-c", "sleep 1h" ]
      volumeMounts:
      - name: sec-ctx-vol
        mountPath: /data/demo
      securityContext:
        allowPrivilegeEscalation: false
  ```

  

  ```
  kubectl exec -it security-context-demo -- sh
  ps
  cd demo
  echo hello > testfile
  ls -l
  id
  ```

  

- Set the security context for a Container (container level user is different)

  ```
  apiVersion: v1
  kind: Pod
  metadata:
    name: security-context-demo-2
  spec:
    securityContext:
      runAsUser: 1000
    containers:
    - name: sec-ctx-demo-2
      image: gcr.io/google-samples/node-hello:1.0
      securityContext:
        runAsUser: 2000
        allowPrivilegeEscalation: false
  ```

  

  ```
  kubectl exec -it security-context-demo-2 -- sh
  ps aux
  id
  ```

  

- Set additional(CAP_NET_ADMIN & CAP_SYS_TIME) capabilities for a Container

  ```
  apiVersion: v1
  kind: Pod
  metadata:
    name: security-context-demo-4
  spec:
    containers:
    - name: sec-ctx-4
      image: gcr.io/google-samples/node-hello:1.0
      securityContext:
        capabilities:
          add: ["NET_ADMIN", "SYS_TIME"]
  ```

  

- Assign SELinux labels to a Container

  ```
  ...
  securityContext:
    seLinuxOptions:
      level: "s0:c123,c456"
  ```

  

- Set Seccomp profile to Container [Refer 3.4.1](https://github.com/ramanagali/Interview_Guide/blob/main/CKS_Preparation_Guide.md#341-seccomp-profiles)

- Ref: https://kubernetes.io/docs/tasks/configure-pod-container/security-context/





### 16. Manage Kubernetes secrets

- EncryptionConfiguration 설정해서 시크릿 암호화해서 저장하고 복호화 하는 문제 [link](./secret.md), [link2](./etcd.md)
- 컨테이너를 통한 시크릿 노출 확인하는 문제 `볼륨`, `env`, `api-server`



### 17. Use container runtime sandboxes in multi-tenant environments (e.g. gvisor, kata containers)

- runtimeclass 생성, pod에 지정 [link](./runtime-class.md)

  


### 18. Implement pod to pod encryption by use of mTLS

- istio, pod encryption [link](./csr.md)



## Supply Chain Security (20%)



### 19. Minimize base image footprint

- Use **Slim/Minimal** Images than base images
- Use Docker **multi stage builds** for lean
- Use Distroless:
  - Distroless Images will have only your app & runtime dependencies
    - No package managers, shell, n/w tools, text editors etc
  - Distroless images are very small
- use `trivy` image scanner for container vulnerabilities, filesys, git etc
- Ref: https://github.com/GoogleContainerTools/distroless
- Ref: https://github.com/aquasecurity/trivy



### 20. Secure your supply chain: whitelist allowed registries, sign and validate images

-  ImagePolicyWebhook Admission 설정하는 문제 [link](./image-policy-webhook.md)
  - kube-apiserver 매니패스트에서 폴리시 위치 확인하고 문제에 맞게 수정 `--admission-control-config-file`
  - 폴리시에 맞는 플러그인 설정 활성화 `--enable-admission-plugins`
  - config 경로에 있는 kubeconfig 도 확인



### 21. Use static analysis of user workloads (e.g.Kubernetes resources, Docker files)

- kubesec, dockerfile 수정 [link](./static-analysis.md)

### 22. Scan images for known vulnerabilities

trivy 사용해서 취약점 파악된 이미지 삭제 [link](./trivy.md)





## Monitoring, Logging and Runtime Security (20%)



### 23. Perform behavioral analytics of syscall process and file activities at the host and container level to detect malicious activities

- Falco rule 설정으로 특정 pod, image 이벤트에 대한 원하는 메세지 logging [link](./falco.md)

### 24. Detect threats within physical infrastructure, apps, networks, data, users and workloads



### 25. Detect all phases of attack regardless where it occurs and how it spreads

- Kubernetes attack matrix (9 Tactics, 40 techniques)
  - Initial access
  - Execution
  - Persistence
  - Privilege escalation
  - Defense evasion
  - Credential access
  - Discovery
  - Lateral movement
  - Impact
- Use MITRE & ATT&CK framework of tacticques and techniques
- Best Practices protection: apply native Kubernetes controls
  1. Configure & Monitor RBAC - Least privilege, avoid overlaping role
  2. Configure Network Policies - Use default-deny-all & explicitly allow
  3. Harden pod configurations - security context for pod/container, use pod serviceaccount
  4. Detect Inscure Pods
- Ref: https://www.cncf.io/online-programs/mitigating-kubernetes-attacks/
- Ref: https://www.microsoft.com/security/blog/2020/04/02/attack-matrix-kubernetes/
- Ref: https://sysdig.com/blog/mitre-attck-framework-for-container-runtime-security-with-sysdig-falco/



### 26. Perform deep analytical investigation and identification of bad actors within environment

- Monitor using sysdig k8s cluster, ns, svc, rc and labels
- sysdig capture system calls and other OS events
- Exploring a Kubernetes Cluster with csysdig `csysdig -k http://127.0.0.1:8080`
- Monitoring/Visualize Kubernetes with Sysdig Cloud
- Ref: https://kubernetes.io/blog/2015/11/monitoring-kubernetes-with-sysdig/
- Ref: https://docs.sysdig.com/



### 27. Ensure immutability of containers at runtime

- container에 root 접근, 파일쓰기 등을 제한하는 방법. [link](./pod-security-policy.md)

### 28. Use Audit Logs to monitor access

- Audit log [link](./audit.md)

  

  





### reference

https://velog.io/@comeonyo/CKS-%EC%8B%9C%ED%97%98-%EB%B2%94%EC%9C%84-%EC%A0%95%EB%A6%AC





