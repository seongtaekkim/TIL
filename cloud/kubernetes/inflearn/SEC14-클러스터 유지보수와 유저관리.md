## 노드 OS 업그레이드 절차



p510 ~

drain node -> cordon이 실행됨(팟 못움직임바리게이트)

- drain 을 하면 팟이 삭제되고 다른 노드에 실행됨.
- 이동하려는 노드가 꽉차서 실행이 안되면 pending 상태가 되었다가 uncordon되었을 때 실행된다.

나중에 반드시 uncordon을 해야 함.



## 노드 업데이트를 위한 노드 드레인과 언커든 실습



~~~
http-go replica 10개 실행
~~~



~~~
kuectl drain work1  # work1 pod 을 모두 내린다. work2에서 실행한다.
kubectl get pod --all-namespaces -o wide
kubectl get node # schedulingDisabled 확인
kubectl scale deploy http-go --replicas=30
kubectl get pod # work2 에만 생성된다. 너무 많으면 pending 상태도 보일것이다.
kubectl uncordon work1 # 이 명령어 이후에 work1에 pod 실행이 가능하다.
kubectl get pod -o wide # 옮겨가지 않고 그대로 있다.
kubectl get nodes # ready 확인.

~~~





## 쿠버네티스 버전 업데이트











## 쿠버네티스 업데이트 실습



https://www.katacoda.com/courses/kubernetes/playground 에 접속이 안되어서 나중에 하자..

~~~
~~~



down grade 는 upgrade의 역순으로 진행하면 된다.





## 백업과 복원



etcd data는 yaml 로 조회 할 수 없다.		



## 백업과 복원 실습



~~~
kubectl get all --all-namespaces -o -yaml > all-deploy-services.yaml


sudo ETCDTL_API=3 ./etcdctl -- endpoint 127.0.0.1:2379 --cacert /etc/kubernetes/pki/etcd/ca.crt --cert /etc/kubernetes/pki/etcd/server.crt --key /etc/kubernetes/pki/etcd/server.key snapshot save snapshotdb

cd /etc/kubernetes/pki/etcd # 위치

sudo ETCDTL_API=3 ./etcdctl -- endpoint 127.0.0.1:2379 --cacert /etc/kubernetes/pki/etcd/ca.crt --cert /etc/kubernetes/pki/etcd/server.crt --key /etc/kubernetes/pki/etcd/server.key snapshot status snapshotdb --write-out=table
~~~





- 백업 이후에 스냅샷 등으로 예전으로 복원



~~~
sudo chown seongtki. snapshotdb # 바탕화면 아이콘 드래그앤 드롭 권환 획득
cp '백업파일명' ./
cp '백업파일명' ./
kubectl delete all --all

~~~



![스크린샷 2023-06-17 오후 4.34.37](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-17 오후 4.34.37.png)



~~~
sudo -i
cd /var/lib/etcd-restore/ # member 폴더 있으면 됨.
vim /etc/kubernetes/manifests/etc.yaml
# --data-dir=/var/lib/etcd =>  --data-dir=/var/lib/etcd-restore 
# volumeMounts: > mountPath: /var/lib/etcd => volumeMounts: > mountPath: /var/lib/etcd-restore
# hostPath: > path: /var/lib/etcd => hostPath: > path: /var/lib/etcd-restore
# command를 추가한다
# --initial-cluster-token=this-is-token

sudo docker ps -a | grep etcd
kubectl get all -n kubernetes-dashboard # 복구확인

kubectl create -f ~/yaml/all-deploy-services.yaml # 모든파일 복구

kubectl get secret -n kubernetes-dashboard # 토큰 확인

~~~

































































































