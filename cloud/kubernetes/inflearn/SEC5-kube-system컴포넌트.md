



## 1. 큐브시스템 컴포넌트 살펴보기

![스크린샷 2023-06-13 오후 1.08.28](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-13 오후 1.08.28.png)

![스크린샷 2023-06-13 오후 1.08.50](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-13 오후 1.08.50.png)





## 스택티 포드 이해와 실습



/etc/kubernetes/manifests # 저장 즉시 반영이 된다.

~~~
ls /etc/kubernetes/manifests/
service kubelet status # kubelet service status
vi /etc/systemd/system/kubelet.service.d/10-kubeadm.conf # 데몬 실행위치의 config 파일을 연다.
sudo vi /var/lib/kubelet/config.yaml # staticpod 위치가 지정되어 있다. (staticPodPath: /etc/kubernetes/manifests)
cd /etc/kubernetes/manifests/
sudo vi static-pod.yaml # 현 위치 디렉토리는 kubelet 에서 무조건 실행한다. 새로 생성한 yaml 도 마찬가지.
kubectl get pod -w
kubectl delete pod static-web-master-1 # 삭제를 해도 kubelet에서 즉시 재 실행 한다.
~~~





## etcd 데이터베이스 살펴보기



- apiserver 내부에 client가 있어서 직접 컨트롤하기 어려우므로
- client 프로그램만 따로 받아서 제어해 보자. [etcd site](https://github.com/etcd-io/etcd/releases/tag/v3.5.9)
- etcd : 서버프로그램, etcdctl : 클라이언트 프로그램

~~~
wget https://github.com/etcd-io/etcd/releases/download/v3.5.9/etcd-v3.5.9-linux-amd64.tar.gz
tar -xf etcd-v3.5.9-linux-amd64.tar.gz
cd etcd-v3.5.9-linux-amd64
~~~



- 클라이언트 옵션을 선택하고, 
- ca 인증서, etcd서버 인증서, 개인 인증서 옵션을 주고
- get, put 등의 명령어로 etcd db 내용을 제어할 수있다.

~~~
sudo ETCDCTL_API=3 ./etcdctl --endpoints 127.0.0.1:2379 --cacert /etc/kubernetes/pki/etcd/ca.crt --cert /etc/kubernetes/pki/etcd/server.crt --key /etc/kubernetes/pki/etcd/server.key get / --prefix --keys-only
~~~



- 모든 키 조회
- 키가 디렉토리형태로 되어 있다.
- 시크릿 데이터, 역할관련, 등 기타 민감데이터들을 보관하고 있다. 

~~~
sudo ETCDCTL_API=3 ./etcdctl --endpoints 127.0.0.1:2379 --cacert /etc/kubernetes/pki/etcd/ca.crt --cert /etc/kubernetes/pki/etcd/server.crt --key /etc/kubernetes/pki/etcd/server.key get / --prefix --keys-only
~~~



- 데이터 세팅

~~~
sudo ETCDCTL_API=3 ./etcdctl --endpoints 127.0.0.1:2379 --cacert /etc/kubernetes/pki/etcd/ca.crt --cert /etc/kubernetes/pki/etcd/server.crt --key /etc/kubernetes/pki/etcd/server.key put key1 value1
~~~



- 키값 조회

~~~
sudo ETCDCTL_API=3 ./etcdctl --endpoints 127.0.0.1:2379 --cacert /etc/kubernetes/pki/etcd/ca.crt --cert /etc/kubernetes/pki/etcd/server.crt --key /etc/kubernetes/pki/etcd/server.key get key1
~~~

























































































































## 2. 스택티 포드 이해와 실습





## 3. etcd 데이터베이스 살펴보기

