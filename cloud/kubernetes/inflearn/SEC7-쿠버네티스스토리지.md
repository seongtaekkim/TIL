## 1. 쿠버네티스 스토리지 - Volume 개요와 종류



임시볼륨

- 컨테이너와 공유하기 위해서 사용

로컬볼륨

- 노드와 공유하기 위해 사용한다.

- 1번 노드에 pod가 만들어지고 볼륨을 저장했다가, 1번노드에서 2번노드로 pod가 새로 만들어지게 되면 기존내용을 볼 수 없다.
  - 목적은 노드 관리에 있다고 한다.

![스크린샷 2023-06-27 오후 5.03.34](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-27 오후 5.03.34.png)

네트워크 볼륨

- 클러스터 외부에 있는 자원과 데이터를 공유하기 위해 쓰인다.

- 강의에서는 비용문제로 gcp 에서 진행한다.





## 2. EmptyDir을 활용한 컨테이너 간 데이터 공유

- pod 가 한번 삭제되면 저장되었던 데이터는 다시 볼 수 가 없었다.





## **3. EmptyDir 컨테이너 간 데이터 공유 실습**



- count-httpd.yaml

~~~
apiVersion: v1
kind: Pod
metadata:
  name: count
spec:
  containers:
  - image: gasbugs/count
    name: html-generator
    volumeMounts:
    - name: html
      mountPath: /var/htdocs
  - image: httpd
    name: web-server
    volumeMounts:
    - name: html
      mountPath: /usr/local/apache2/htdocs
      readOnly: true
    ports:
    - containerPort: 80
      protocol: TCP
  volumes:
  - name: html
    emptyDir: {}
~~~





~~~
$ kubectl create -f count-httpd.yaml 
pod/count created
$ kubectl get pod -w
NAME                       READY   STATUS    RESTARTS   AGE
count                      2/2     Running   0          18s
http-go-647985c674-r8lvs   1/1     Running   0          178m

$ kubectl get pod -o wide
NAME                       READY   STATUS    RESTARTS   AGE    IP          NODE                                       NOMINATED NODE   READINESS GATES
count                      2/2     Running   0          37s    10.4.1.14   gke-cluster-1-default-pool-cb7d7c70-xhdv   <none>           <none>
http-go-647985c674-r8lvs   1/1     Running   0          178m   10.4.1.13   gke-cluster-1-default-pool-cb7d7c70-xhdv   <none>           <none>

$ kubectl exec -it http-go-647985c674-r8lvs -- curl 10.4.1.14
Running loop seq 8

~~~



### 임시 디렉토리는 직접 접근을 어떻게 해야 하나???









## 4. hostpath 컨테이너와 노드 간 데이터 공유

- 컨테이너와 노드의 데이터 공유
- 노드상태를 모니터링 하는 용도. (프로메테우스 등)
- 보통 노드데이터를 포드에 전달



- 이미 모니터링 툴이 설치되어있는 gcp 에서 실습할 에정이다.

~~~
$ kubectl get pods -n kube-system
NAME                                                  READY   STATUS    RESTARTS   AGE
event-exporter-gke-755c4b4d97-t7826                   2/2     Running   0          31h
fluentbit-gke-27vx2                                   2/2     Running   0          31h
fluentbit-gke-7smnw                                   2/2     Running   0          31h
fluentbit-gke-9w7g2                                   2/2     Running   0          31h
~~~







## 5. hostpath 컨테이너와 노드 간 데이터 공유 실습



### virtual box 에서 테스트

- work1, work2 에 아래와 같이 작성한다.

~~~
sudo mkdir /var/htdocs
sudo -i
sudo echo "work1" > /var/htdocs/index.html

~~~





- hostpath-httpd.yaml

~~~
apiVersion: v1
kind: Pod
metadata:
  name: hostpath-http
spec:
  containers:
  - image: httpd
    name: web-server
    volumeMounts:
    - name: html
      mountPath: /usr/local/apache2/htdocs
      readOnly: true
    ports:
    - containerPort: 80
      protocol: TCP
  volumes:
  - name: html
    hostpath:
      path: /var/htdocs
      type: Directory
~~~



~~~
$ kubectl create -f hostpath-httpd.yaml
$ kubectl get pod -w
$ kubectl port-forward hostpath-httpd.yaml 8888:80

# 접속
$ kubectl get pod -o wide # ip 확인
$ wget -O- -q "ip"
$ kubectl get pod -o wide # pod 실행 된 node 확인
~~~





### gcp에서 확인

~~~
$ kubectl get pod -n kube-system # fluented : 데이터를 모니터링할때 로깅되는 리소스를 가져오고 있다.
$ kubectl get pod fluentd-gcp-v3.1.1.-468d9 -n kubesystem -o yaml # volumes > hostpath 확인
																															    # volumeMounts 
~~~







## 6. GCE 디스크를 활용한 네트워크 볼륨 연결

pdstandard : hdd, pdssd : ssd

~~~
$ gcloud compute disks create --size=10Gib --zone=us-central1-c mongodb
WARNING: You have selected a disk size of under [200GB]. This may result in poor I/O performance. For more information, see: https://developers.google.com/compute/docs/disks#performance.
Created [https://www.googleapis.com/compute/v1/projects/staek-2023-06-24/zones/us-central1-c/disks/mongodb].
NAME: mongodb
ZONE: us-central1-c
SIZE_GB: 10
TYPE: pd-standard
STATUS: READY

New disks are unformatted. You must format and mount a disk before it
can be used. You can find instructions on how to do this at:

https://cloud.google.com/compute/docs/disks/add-persistent-disk#formatting
~~~



![스크린샷 2023-06-15 오후 3.06.43](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-15 오후 3.06.43.png)



- gce-mongodb-pod.yaml

~~~
apiVersion: v1
kind: Pod
metadata:
  name: mongodb
spec:
  containers:
  - image: mongo
    name: mongodb
    volumeMounts:
    - mountPath: /data/db
      name: mongodb
  volumes:
  - name: mongodb
    # This GCE PD must already exist.
    gcePersistentDisk:
      pdName: mongodb # 네트워크 스토리지 연결
      fsType: ext4
~~~



- 생성이 안되면 describe 를 적극 활용해서 확인해라.

~~~
$ kubectl create -f gce-mongodb-pod.yaml 
pod/mongo created
chxortnl@cloudshell:~ (staek-2023-04-08)$ kubectl get pod -w
kubectl describe pod mongo # 에러가 발생할 수 있다. 이 명령어로 찾아보자.


kubectl exec -it mongodb -- mongosh # 접속
~~~



- 데이터 생성

~~~
mystore> use mydb
switched to db mydb
mydb> db.foo.insert({name:'test',value:'1234'})
DeprecationWarning: Collection.insert() is deprecated. Use insertOne, insertMany, or bulkWrite.
{
  acknowledged: true,
  insertedIds: { '0': ObjectId("649aa9f21e5f5bd6ff441b73") }
}

mydb> db.foo.find({name:'test',value:'1234'})
~~~



- pod 삭제 후 다시생성 후 데이터 조회

~~~
kubectl delete pod mongodb # 삭제 후 다시 시작해서 이전 데이터가 존재하는 지 조회한다.
kubectl create -f gce-mongodb-pod.yaml 
kubectl exec -it mongodb -- mongosh # 접속

test> use mydb
switched to db mydb
mydb> db.foo.find()
[
  {
    _id: ObjectId("649aa9f21e5f5bd6ff441b73"),
    name: 'test',
    value: '1234'
  }
]
~~~



[mongodb cli accept](https://help.ovhcloud.com/csm/asia-public-cloud-databases-mongodb-connect?id=kb_article_view&sysparm_article=KB0049073)









## 7. nfs 네트워크 볼륨 설치



![스크린샷 2023-06-15 오후 3.51.13](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-15 오후 3.51.13.png)



- 클러스터 안에 있는 노드에 설치하기 보다는 별도 서버를 두고 세팅함.  쿱 시스템에 영향이 갈 수 있기 때문.
- 여기서는 테스트할 거라서 worker-2 에 설치한다.



~~~
$ apt update
$ mkdir /home/nfc
$ chmod 777 /home/nfc
~~~



- nfs 에 필요한 프로그램 설치

~~~
$ sudo apt install nfs-common nfs-kernel-server portmap
~~~



- sudo vi /etc/exports
  - nfs 에 접근할 ip를 작성한다.

~~~
/home/nfs       10.138.0.2(rw,sync,no_subtree_check) 10.138.0.3(ro,sync,no_subtree_check) 10.138.0.4(ro,sync,no_subtree_check)
~~~



- nfs-server restart

~~~
$ systemctl status nfs-server
● nfs-server.service - NFS server and services
     Loaded: loaded (/lib/systemd/system/nfs-server.service; enabled; vendor preset: enabled)
     Active: active (exited) since Tue 2023-06-27 09:34:36 UTC; 29min ago
   Main PID: 45586 (code=exited, status=0/SUCCESS)
      Tasks: 0 (limit: 4680)
     Memory: 0B
     CGroup: /system.slice/nfs-server.service

$ sudo systemctl restart nfs-server
~~~



- 생성한 nfs 디렉토리를 mount 특정 디렉토리에 마운트 한다.
- nfs 디렉토리에 파일을 생성하면, /mnt에서 보여진다.

~~~
showmount -e 127.0.0.1 #  마운트 가능한 서버가 뭐가 있는지 조회
sudo mount -t nfs 10.138.0.4:/home/nfs/ /mnt
echo 'test' > /home/nfs/test.txt
cat /mnt/test.txt
~~~





## 8. k8s와 nfs 네트워크 볼륨 연결 실습



https://github.com/kubernetes/examples/blob/master/staging/volumes/nfs/nfs-pv.yaml



- emptyDir + nfs 예제
- docs > nfs > example 에서 nfs 옵션을 따로 가져올 수 있다.
- nfs-httpd.yaml

~~~
apiVersion: v1
kind: Pod
metadata:
  name: nfs-httpd
spec:
  containers:
  - image: httpd
    name: web
    volumeMounts:
    - mountPath: /usr/local/apache2/htdocs
      name: nfs-volume
      readOnly: true
  volumes:
  - name: nfs-volume
    nfs:
    server: 10.138.0.4
    path: /home/nfs
~~~



- worker-2 에서 httpd에서 접근할 기본 파일을 만들어둔다.

~~~
cd /home/nfs
echo nfs > index.html
~~~



- 실행
  - nfs가 실행이 안되는데, describe로 확인을 해보면 /sbin/mount 가 필요하다고 나온다.
  - 해결을 위해 nfs 관련 소프트웨어를 설치해야 한다.

~~~
kubectl create -f nfs-httpd.yaml
kubectl get pod -w
kubectl describe pod nfs-httpd 
~~~

~~~
Mounting command: mount
Mounting arguments: -t nfs 10.138.0.4:/home/nfs /var/lib/kubelet/pods/83698ccd-ff7c-4c16-b5e7-cb6a0bf2f7cd/volumes/kubernetes.io~nfs/nfs-volume
Output: mount: /var/lib/kubelet/pods/83698ccd-ff7c-4c16-b5e7-cb6a0bf2f7cd/volumes/kubernetes.io~nfs/nfs-volume: bad option; for several filesystems (e.g. nfs, cifs) you might need a /sbin/mount.<type> helper program.
~~~



- master 뿐 아니라 worker-1,2 에 모두 설치해야 한다.
  - 노드에 있는 nfs 기능을 통해 마운트 시키는 거라서 네트워크파일시스템 관련 기능이 필요햇던 것이다.

~~~
$ sudo apt install nfs-common nfs-kernel-server portmap
~~~



- 재실행

~~~
kubectl delete pod nfs-httpd.yaml
kubectl create -f nfs-httpd.yaml
kubectl port-forward nfs-httpd 8080:80

wget -O- -q localhost:8080 # nfs 라는 문구가 나오면 성공이다.
~~~



- 남아있을지 모르는 port-forward 옵션 삭제

~~~
ps -eaf | grep port-forward # 혹시 남아있는 pid를 찾아서 삭제한다
kill -9 "pid"
~~~





## 9. pv와 pvc에 대한 개념 이해





## 10. pv와 pvc 작성 실습



- gcePersistentDisk 를 이전 실습의 nfs로 대체할 수도 있다.

~~~
apiVersion: v1
kind: Pod
metadata:
  name: mongodb
spec:
  containers:
  - image: mongo
    name: mongodb
    volumeMounts:
    - mountPath: /data/db
      name: mongodb
  volumes:
  - name: mongodb
    persistentVolumeClaim:
      claimName: mongo-pvc
---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mongo-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 10Gi
  storageClassName: ""
---
apiVersion: v1
kind: PersistentVolume
metadata:
  name: mongo-pv
spec:
  capacity:
    storage: 10Gi
  volumeMode: Filesystem
  accessModes:
    - ReadWriteOnce
    - ReadOnlyMany
  persistentVolumeReclaimPolicy: Retain
  gcePersistentDisk:
    pdName: mongodb
    fsType: ext4
~~~



- 실행 후 pv, pvc, pod을 확인한다.
- pv, pvc는 Bound 되었는지 확인해야 한다.
  - pvc 는 namespace 있고 pv 는 없다.
- 두개 모두 bound면 pod이 생성될 수 있다.

~~~
$ kubectl create -f gce-mongo-pv-pvc-pod.yaml 
$ kubectl get pv
NAME       CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS   CLAIM               STORAGECLASS   REASON   AGE
mongo-pv   10Gi       RWO,ROX        Retain           Bound    default/mongo-pvc                           35s

$ kubectl get pvc
NAME        STATUS   VOLUME     CAPACITY   ACCESS MODES   STORAGECLASS   AGE
mongo-pvc   Bound    mongo-pv   10Gi       RWO,ROX                       56s

$ kubectl get pod
NAME                       READY   STATUS    RESTARTS   AGE
mongodb                    1/1     Running   0          64s
~~~



- mongo 에 이전에 생성했던 데이터를 확인할 수 있다.

~~~
kubectl exec -it mongodb -- mongosh
test> use mydb
switched to db mydb
mydb> db.foo.find()
[
  {
    _id: ObjectId("649aa9f21e5f5bd6ff441b73"),
    name: 'test',
    value: '1234'
  }
]
~~~



## 스토리지클래스를 활용한 동적 프로비저닝





## GCE 동적 프로비저닝 실습





nfs 는 정적디스크 제공임.









- mongo-storage.yaml

~~~
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: storage
provisioner: kubernetes.io/gce-pd
parameters:
  type: pd-ssd  
---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mongo-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 10Gi
  storageClassName: storage
---
apiVersion: v1
kind: Pod
metadata:
  name: mongodb
spec:
  containers:
  - image: mongo
    name: mongodb
    volumeMounts:
    - mountPath: /data/db
      name: mongodb
  volumes:
  - name: mongodb
    persistentVolumeClaim:
      claimName: mongo-pvc

~~~







~~~
kubectl create -f mongo-storage.yaml
kubectl get sc
kubectl get pvc
kubectl get pv (동적으로 생성됨.)
kubectl edit pv "id" # Delete, Retain 등으로 정책을 변경할 수 있다.
# Compute Engine에 생성되어있음을 확인 가능.
~~~









## rook-ceph를 활용한 프라이빗 클라우드 스토리지클래스 개요







## rook-ceph를 위한 워커 노드 준비





- 이거로 ip 고정 할 수 있는듯?

~~~
sudo -i
vi /etc/netplan/00-installer-config.yaml
netplan apply
kubeadm reest

kubeadm token create --print-join-command 
~~~



![스크린샷 2023-06-28 오후 12.39.17](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-28 오후 12.39.17.png)







## rook-ceph 설치와 스토리지클래스 활용



~~~
lsblk
swapoff -a

rook-ceph 설치 # masert 에 설치 
kubectl get ns 
kubectl get pod -n look-ceph

csi 설치

bash 접근
ceph status 
~~~



![스크린샷 2023-06-28 오후 12.58.00](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-28 오후 12.58.00.png)









## 스테이트풀셋 개요







## 스테이트풀셋 실습



[블로그 정리](https://blog.naver.com/isc0304/221885403537)





~~~
~~~





















































