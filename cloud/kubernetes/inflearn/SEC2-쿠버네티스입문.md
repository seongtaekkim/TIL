

[강의 내용](https://gasbugs.notion.site/3-bb29b4f852cf41d2aac8582447cf7309)

[TOC]





## 1. 쿠버네티스 소개





## 2. **클러스터를 두 개나 사용하는 이유**





## 4. 구글 클라우드에서 GKE 클러스터 만들기







- autopilot구글이 직접 다 관리하고 우리는 컨테이너만 관리한다. -> 컨테이너만 비용 부담

- standard - 가상머신 -> 가상머신과 컨테이너 비용 부담





- 구글클라우드에서 쿠버네티스엔진 시작 및 클러스터 만들기





사용이쉽지만 공부하기에는 별로

서버리스 - 오토바일럿 - 컨테이너에 비용







가상머신에 비용을





이름

위치유형

- 영역(zone)
  - 데이터센터 하나하나를 zone이라고 부른다
- 리전(지역)
  - 서울리전, 도코리전 - 여러개의 데이터센터 전체를 포함하는 말( 각각이 이중화되어있어 재해방지시스템 등이 구축되어있다.)
  - 비용이 영역보다 크다.



정적버전

- 버전을 수동으로관리 (사용자가)

출시채널

- 구글이 알아서 버전관리를 해준다.





default-pool 

- 노드 수 (가상머신 개수)





## 5. **GKE 클러스터 사용과 애플리케이션 배포 실습**

- google kubernetes engine





쉘은 별도의 가상머신이고, 클러스터에 별도로 원격접속을 하는 것이다.	

- 프로젝트 접근

~~~
   11  gcloud container clusters get-credentials cluster-1 --zone us-central1-c --project staek-2023-04-08
   12  history
   13  kubectl get nodes
   14  kubectl get pods, svc
~~~



- 톰캣 이미지 생성(레플리카 5개),배포

~~~
chxortnl@cloudshell:~ (staek-2023-04-08)$ kubectl create deploy tc --image=consol/tomcat-7.0 --replicas=5
deployment.apps/tc created
chxortnl@cloudshell:~ (staek-2023-04-08)$ kubectl expose deploy tc --type=LoadBalancer --port=80 --target-port=8080
service/tc exposed
~~~



- 클라우드의 로드벨런서에 의해 external ip 할당 받음

~~~
chxortnl@cloudshell:~ (staek-2023-04-08)$ kubectl get pods,svc
NAME                      READY   STATUS    RESTARTS   AGE
pod/tc-85f75d5c76-5hggg   1/1     Running   0          80s
pod/tc-85f75d5c76-7ktvx   1/1     Running   0          81s
pod/tc-85f75d5c76-cgd29   1/1     Running   0          81s
pod/tc-85f75d5c76-khtwf   1/1     Running   0          81s
pod/tc-85f75d5c76-wkj66   1/1     Running   0          80s

NAME                 TYPE           CLUSTER-IP   EXTERNAL-IP   PORT(S)        AGE
service/kubernetes   ClusterIP      10.8.0.1     <none>        443/TCP        11m
service/tc           LoadBalancer   10.8.2.225   <pending>     80:30483/TCP   36s
~~~



~~~
chxortnl@cloudshell:~ (staek-2023-04-08)$ kubectl get pods,svc
NAME                      READY   STATUS    RESTARTS   AGE
pod/tc-85f75d5c76-5hggg   1/1     Running   0          109s
pod/tc-85f75d5c76-7ktvx   1/1     Running   0          110s
pod/tc-85f75d5c76-cgd29   1/1     Running   0          110s
pod/tc-85f75d5c76-khtwf   1/1     Running   0          110s
pod/tc-85f75d5c76-wkj66   1/1     Running   0          109s

NAME                 TYPE           CLUSTER-IP   EXTERNAL-IP     PORT(S)        AGE
service/kubernetes   ClusterIP      10.8.0.1     <none>          443/TCP        11m
service/tc           LoadBalancer   10.8.2.225   34.72.149.105   80:30483/TCP   66s
~~~

- 로드벨런서 : 클라우드에서 밖에 안된다.



~~~
chxortnl@cloudshell:~ (staek-2023-04-08)$ kubectl get pods,svc
NAME                      READY   STATUS    RESTARTS   AGE
pod/tc-7796b549ff-4fzcl   1/1     Running   0          18h
pod/tc-7796b549ff-9g52n   1/1     Running   0          18h
pod/tc-7796b549ff-9s228   1/1     Running   0          18h
pod/tc-7796b549ff-9vf8t   1/1     Running   0          18h
pod/tc-7796b549ff-sthsg   1/1     Running   0          18h

NAME                 TYPE           CLUSTER-IP    EXTERNAL-IP     PORT(S)        AGE
service/kubernetes   ClusterIP      10.8.0.1      <none>          443/TCP        18h
service/tc           LoadBalancer   10.8.15.205   35.239.113.92   80:30583/TCP   18h
~~~

- 35.239.113.92 : 공인아이피



- 35.239.113.92 접속

- server status 클릭 -> admin/admin

![스크린샷 2023-04-09 오전 11.56.07](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-09 오전 11.56.07.png)

- 새로고침으로 로드밸런스 확인



- owide

~~~
hxortnl@cloudshell:~ (staek-2023-04-08)$ kubectl get pods -owide
NAME                  READY   STATUS    RESTARTS   AGE   IP          NODE                                       NOMINATED NODE   READINESS GATES
tc-7796b549ff-4fzcl   1/1     Running   0          18h   10.4.1.9    gke-cluster-1-default-pool-cdad677b-phlw   <none>           <none>
tc-7796b549ff-9g52n   1/1     Running   0          18h   10.4.2.4    gke-cluster-1-default-pool-cdad677b-bn0r   <none>           <none>
tc-7796b549ff-9s228   1/1     Running   0          18h   10.4.0.4    gke-cluster-1-default-pool-cdad677b-gqhq   <none>           <none>
tc-7796b549ff-9vf8t   1/1     Running   0          18h   10.4.1.10   gke-cluster-1-default-pool-cdad677b-phlw   <none>           <none>
tc-7796b549ff-sthsg   1/1     Running   0          18h   10.4.0.5    gke-cluster-1-default-pool-cdad677b-gqhq   <none>           <none>
~~~

- 컨테이너 5대가 배포가 어느곳에 되었는지 NODE항목을 통해 알 수 있다.





## 6. 우분투 환경으로 클러스터 구성 설명 1 - 노드 설정

- 클러스터 구성을 위한 개략적인 설명
- 교재 p108~

스왑 비활성화

컨테이너 런타임 

kubernetes 를 설치하면 kubelet데몬이 설치됨 -> containerd 를 컨트롤함 (예전에는 도커가 컨트롤 했었음. 설치도 따로 해야 함.)

![스크린샷 2023-06-09 오후 12.19.04](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-09 오후 12.19.04.png)

![스크린샷 2023-06-09 오후 12.19.25](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-09 오후 12.19.25.png)

![스크린샷 2023-06-09 오후 12.20.11](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-09 오후 12.20.11.png)



## 7. 우분투 환경으로 클러스터 구성 설명 2 - 클러스터 설정

- 클러스터 구성을 위한 개략적인 설명



init을 한다.

토큰을 통해서 조인을한다.

토큰 종류에 따라 마스터노드/워커노드 가 된다;.





쿠버네티스 정상구동을 위해서는 네트워크를 구성해주는 플러그인이 필요하다.

파드네트워크를 설치하고 나면 not ready 가 ready 로 세팅된다. (여기서는 cilinum - CNCF프로젝트	 으로 구성.) 



~~~
sudo kubeadm reset # 초기설정으로 돌아갈 수 있다. (init 이전)
~~~

~~~
# token 재발급 방법
sudo kubeadm token list # 리스트 확인
sudo kubeadm token create --print-join-command # 재발급
~~~



## 8. GCP 환경에서 VM 준비하기

- GCP에서 가상머신3기 설치(master(1), worker(2))



체험판이라 특정 리전에 만들수있는 리소스 양이 제한되어 있다.

- us-west1 선택



<img src="/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-09 오후 12.31.59.png" alt="스크린샷 2023-04-09 오후 12.31.59" style="zoom:50%;" />



<img src="/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-09 오후 12.37.40.png" alt="스크린샷 2023-04-09 오후 12.37.40" style="zoom:50%;" />

- AMD 를 선택하면 안됨
- ssd도 제한이 되어있어서 표준영구디스크를 선택한다.

![스크린샷 2023-04-09 오후 1.41.24](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-09 오후 1.41.24.png)

- SSH 접속하고, ping으로 각 인스턴스 접속확인



## 9. VMware 환경에서 VM 준비하기



[UTM - ubuntu install](https://king-ja.tistory.com/93)

[ubuntu - 20.04](https://releases.ubuntu.com/focal/)

- ubunt 22는 kubeadm init 에서 에러 발생..

[ubuntu root first password](https://positivemh.tistory.com/583)

- 

- 

- vmware에서 준비 (pc사양,용량이 작다면 클라우드권장)

- 



ubuntu로 vmware 3개 설치

hostname 변경

- master-1
- worker-1
- worker-2



~~~
sudo passwd root
sudo apt update
sudo apt install ssh vim net-tools
sudo hostnamectl set-hostname "name"
~~~







## 10. 노드 준비하기



- 리부팅시마다 무조건 해야 함.

~~~
sudo swapoff -a # 현재 시스템에 적용(리부팅하면 재설정 필요)
sudo sed -i '/ swap / s/^\(.*\)$/#\1/g' /etc/fstab # 리부팅 필수
~~~



~~~
# Using Docker Repository
sudo apt update
sudo apt install -y ca-certificates curl gnupg lsb-release
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list

# containerd 설치
sudo apt update
sudo apt install -y containerd.io
# sudo systemctl status containerd # Ctrl + C를 눌러서 나간다.

# Containerd configuration for Kubernetes
cat <<EOF | sudo tee -a /etc/containerd/config.toml
[plugins."io.containerd.grpc.v1.cri".containerd.runtimes.runc]
[plugins."io.containerd.grpc.v1.cri".containerd.runtimes.runc.options]
SystemdCgroup = true
EOF

sudo sed -i 's/^disabled_plugins \=/\#disabled_plugins \=/g' /etc/containerd/config.toml
sudo systemctl restart containerd

# 소켓이 있는지 확인한다.
ls /var/run/containerd/containerd.sock
~~~







[kubeadm 설치](https://kubernetes.io/ko/docs/setup/production-environment/tools/kubeadm/install-kubeadm/)

- 변경 전 (23.05.27 이전)

~~~
cat <<EOF > kube_install.sh
# 1. apt 패키지 색인을 업데이트하고, 쿠버네티스 apt 리포지터리를 사용하는 데 필요한 패키지를 설치한다.
sudo apt-get update
sudo apt-get install -y apt-transport-https ca-certificates curl

# 2. 구글 클라우드의 공개 사이닝 키를 다운로드 한다.
sudo curl -fsSLo /usr/share/keyrings/kubernetes-archive-keyring.gpg https://packages.cloud.google.com/apt/doc/apt-key.gpg

# 3. 쿠버네티스 apt 리포지터리를 추가한다.
echo "deb [signed-by=/usr/share/keyrings/kubernetes-archive-keyring.gpg] https://apt.kubernetes.io/ kubernetes-xenial main" | sudo tee /etc/apt/sources.list.d/kubernetes.list

# 4. apt 패키지 색인을 업데이트하고, kubelet, kubeadm, kubectl을 설치하고 해당 버전을 고정한다.
sudo apt-get update
sudo apt-get install -y kubelet kubeadm kubectl
sudo apt-mark hold kubelet kubeadm kubectl
EOF

sudo bash kube_install.sh
~~~

- 번경 후

~~~
sudo mkdir /etc/apt/keyrings

cat <<EOF > kube_install.sh
# 1. apt 패키지 색인을 업데이트하고, 쿠버네티스 apt 리포지터리를 사용하는 데 필요한 패키지를 설치한다.
sudo apt-get update
sudo apt-get install -y apt-transport-https ca-certificates curl

# 2. 구글 클라우드의 공개 사이닝 키를 다운로드 한다.
curl -fsSL https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo gpg --dearmor -o /etc/apt/keyrings/kubernetes-archive-keyring.gpg

# 3. 쿠버네티스 apt 리포지터리를 추가한다.
echo "deb [signed-by=/etc/apt/keyrings/kubernetes-archive-keyring.gpg] https://apt.kubernetes.io/ kubernetes-xenial main" | sudo tee /etc/apt/sources.list.d/kubernetes.list

# 4. apt 패키지 색인을 업데이트하고, kubelet, kubeadm, kubectl을 설치하고 해당 버전을 고정한다.
sudo apt-get update
sudo apt-get install -y kubelet kubeadm kubectl
sudo apt-mark hold kubelet kubeadm kubectl
EOF

sudo bash kube_install.sh
~~~





swap 비활성 

containerd 설치 및 설정

- containerd.sock 확인

~~~
ls /var/run/containerd/containerd.sock

~~~

```bash
sudo -i
modprobe br_netfilter
echo 1 > /proc/sys/net/ipv4/ip_forward
echo 1 > /proc/sys/net/bridge/bridge-nf-call-iptables
exit
```



넷필터 브릿지 설정 - 이것도 docker.io 설치하면 자동으로 되는데 직접해주어야함,





## 11. 클러스터 구성하기



~~~
sudo kubeadm init # 옵션을 주면 HA구성 파트(마스터 여러 개 구성)를 따로 진행할 수 있다 (추후) 현재는 마스터노드 하나

kubeadm config images pull # 폐쇄망에서 진행할 경우 해당 명령어로 미리 이미지를 다운로드 받고 진행한다.
~~~



- init 성공 후 메세지

~~~
Your Kubernetes control-plane has initialized successfully!

# 1) 유저 설정
To start using your cluster, you need to run the following as a regular user:

  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config

Alternatively, if you are the root user, you can run:

  export KUBECONFIG=/etc/kubernetes/admin.conf

# 2) 파드 네트워크 설정
You should now deploy a pod network to the cluster.
Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
  https://kubernetes.io/docs/concepts/cluster-administration/addons/

# 워커노드 조인방법
Then you can join any number of worker nodes by running the following on each as root:

kubeadm join 10.138.0.4:6443 --token wqetus.py3ixqhj6duy2sx6 \
        --discovery-token-ca-cert-hash sha256:b95a9eb71804095be091b3c440cfc27780b38150a7d1e62a1c1472d7981798b0
~~~



- 내 결과

~~~
Your Kubernetes control-plane has initialized successfully!

To start using your cluster, you need to run the following as a regular user:

  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config

Alternatively, if you are the root user, you can run:

  export KUBECONFIG=/etc/kubernetes/admin.conf

You should now deploy a pod network to the cluster.
Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
  https://kubernetes.io/docs/concepts/cluster-administration/addons/

Then you can join any number of worker nodes by running the following on each as root:

sudo kubeadm join 10.138.0.2:6443 --token 91vimh.mu1y4ukxx8csbzff \
	--discovery-token-ca-cert-hash sha256:913d5f78e38de216c5726313c64187149e480dd9e4de12f1be382639c3cd3403
~~~



- 

~~~
$ mkdir -p $HOME/.kube
$ sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
$ sudo chown $(id -u):$(id -g) $HOME/.kube/config

$ kubectl get nodes
NAME       STATUS     ROLES           AGE   VERSION
master-1   NotReady   control-plane   13m   v1.26.3
~~~

- 아직 조인을 안해서 하나만 보인다.

~~~
kubeadm token create --print-join-command # token 생성
~~~







### `파드 네트워크 배포

`마스터 노드`에서 다음 명령을 실행하면 앞서 구성한 유저 설정을 통해 클러스터에 cilium을 설치한다.

- m1 의 리눅스는 arm64로 설치해야 한다.
- linux 는 amd64로 설치한다.

https://github.com/cilium/cilium-cli/releases

https://kubernetes.io/docs/tasks/administer-cluster/network-policy-provider/cilium-network-policy/

```bash
curl -LO https://github.com/cilium/cilium-cli/releases/latest/download/cilium-linux-arm64.tar.gz
sudo tar xzvfC cilium-linux-arm64.tar.gz /usr/local/bin
rm cilium-linux-arm64.tar.gz
cilium install
```

​	

- `$ cilium status`

~~~
DaemonSet         cilium             Desired: 3, Ready: 3/3, Available: 3/3
Deployment        cilium-operator    Desired: 1, Ready: 1/1, Available: 1/1
Containers:       cilium             Running: 3
                  cilium-operator    Running: 1
Cluster Pods:     2/2 managed by Cilium
Image versions    cilium             quay.io/cilium/cilium:v1.13.3@sha256:77176464a1e11ea7e89e984ac7db365e7af39851507e94f137dcf56c87746314: 3
                  cilium-operator    quay.io/cilium/operator-generic:v1.13.3@sha256:fa7003cbfdf8358cb71786afebc711b26e5e44a2ed99bd4944930bba915
~~~











## 12. 애플리케이션 배포와 쿠버네티스 아키텍처 이해





### 컨테이너 배포해보기

톰캣 컨테이너를 배포하고 외부로 노출해보자.

```bash
kubectl create deploy tc --image=consol/tomcat-7.0 --replicas=5
kubectl expose deploy tc --type=NodePort --port=80 --target-port=8080 # 로드벨런서를 활용할 수 없어서 NodePort로 진행한다.
```

약 40초 정도 지나서 확인하면 모든 컨테이너가 Running 상태인 것을 확인할 수 있다.

```bash
$ kubectl get pod,svc
NAME                      READY   STATUS    RESTARTS   AGE
pod/tc-685d6bd5d5-hdfx2   1/1     Running   0          35s
pod/tc-685d6bd5d5-n5pwq   1/1     Running   0          35s
pod/tc-685d6bd5d5-ng864   1/1     Running   0          35s
pod/tc-685d6bd5d5-nt66b   1/1     Running   0          35s
pod/tc-685d6bd5d5-nvcqj   1/1     Running   0          35s

NAME                 TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)        AGE
service/kubernetes   ClusterIP   10.96.0.1        <none>        443/TCP        54m
service/tc           NodePort    10.100.121.107   <none>        80:32023/TCP   34s
```

모든 노드에 위 결과에 표시된 3xxxx번대 포트로 서비스가 오픈된다. 이 포트로 접속을 시도한다.

IP는 GCP의 내부아이피로 접근하면 된다.

```bash
curl 10.142.0.3:32023 | grep title
curl 10.142.0.4:32023 | grep title
curl 10.142.0.5:32023 | grep title
```

다음은 클러스터에 노드포트를 통해서 tc 서비스와 통신하는 그림이다.

![스크린샷 2023-06-10 오후 6.34.13](/Users/staek/Downloads/스크린샷 2023-06-10 오후 6.34.13.png)

![스크린샷 2023-06-10 오후 6.31.22](/Users/staek/Downloads/스크린샷 2023-06-10 오후 6.31.22.png)



![img](https://gasbugs.notion.site/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Fba4ddff7-a1d3-4c7c-a721-1acead9f036b%2FUntitled.png?id=0a6645db-2226-4b75-abe4-54c09a39dd35&table=block&spaceId=6c6bc534-f820-444c-9de9-0ff0fe485f99&width=2000&userId=&cache=v2)

### 클러스터를 두 개나 사용하는 이유

- GKE는 구글 관리하는 관리형 클러스터다. 따라서 클러스터 세부 설정을 직접 할 수 없으므로 공부를 하는데 걸림돌이 된다. 장점은 클라우드 네이티브를 어느 정도 바로 사용할 수 있도록 세팅되어 있다는 것이다.
- 온프레미스(우분투)은 단점은 클라우드 네이티브에 필요한 모든 구성 요소를 직접 세팅해야 한다. 장점은 쿠버네티스 클러스터의 모든 설정을 직접 조작할 수 있으므로 서비스를 구성하는 다양한 기능들을 만져볼 수 있다.
- 어느 쪽에서 실행해도 무방한 경우에는 GKE를 사용해서 실습을 진행하겠다.







