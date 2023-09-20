



## 1. Storage in Docker





## Volume Driver Plugins in Docker



##  Container Storage Interface (CSI)



## Volumes

쿠버네티스의 볼륨에대해 간략히 설명함

팟이 올라올 때, volume을 설정하여 node안에 hostpath로 저장하는 방식을 설명함.

하지만 여러개 노드일 경우 데이터를 통합하지 못해서,

이를 해결할 여러 서비스 google PV, NFS 등을 소개함.



##  Persistent Volumes



pvc 를 생성한다. 요청한 accessmode, resource가 있는	pv 에 속하면서 bind 된다.

pv 남는 용량 없거나 존재하지 않으면 pending 상태가 도니다.

presistentVolumeReclaimPolicy 가 retain 이면 남겨지고, delete면 삭제할때 같이 삭제되고

Recycle이면 

In this case, the data in the data volume will be scrubbed

before making it available to other claims.




## Teest

~~~
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: webapp
  name: webapp
spec:
  containers:
  - image: kodekloud/event-simulator
    name: webapp
    volumeMounts:
    - mountPath: /log
      name: test-volume
  volumes:
  - name: test-volume
    hostPath:
      # directory location on host
      path: /var/log/webapp
      # this field is optional
      type: Directory
status: {}

~~~





~~~
apiVersion: v1
kind: PersistentVolume
metadata:
  name: pv-log
spec:
  storageClassName: ""
  accessModes:
  - ReadWriteMany
  persistentVolumeReclaimPolicy: Retain
  hostPath:
    path: /pv/log
  capacity:
    storage: 100Mi

~~~



~~~
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: claim-log-1
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 50Mi 
~~~





hostpath에서 pv로 변경

~~~
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: webapp
  name: webapp
spec:
  containers:
  - image: kodekloud/event-simulator
    name: webapp
    volumeMounts:
    - mountPath: /log
      name: test-volume
  volumes:
  - name: test-volume
    persistentVolumeClaim:
      claimName: claim-log-1
status: {}

~~~



### Application Configuration

We discussed how to configure an application to use a volume in the "Volumes" lecture using volumeMounts. This along with the practice test should be sufficient for the exam.

### Additional Topics

Additional topics such as StatefulSets are out of scope for the exam. However, if you wish to learn them, they are covered in the Certified Kubernetes Application Developer (CKAD) course.



## Storage Class



static Provisioning



dynamic provisioning





## Test

~~~
controlplane ~ ✖ k get pv
NAME       CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS   CLAIM               STORAGECLASS    REASON   AGE
local-pv   500Mi      RWO            Retain           Bound    default/local-pvc   local-storage            17m

controlplane ~ ➜  k get sc
NAME                        PROVISIONER                     RECLAIMPOLICY   VOLUMEBINDINGMODE      ALLOWVOLUMEEXPANSION   AGE
local-path (default)        rancher.io/local-path           Delete          WaitForFirstConsumer   false                  24m
local-storage               kubernetes.io/no-provisioner    Delete          WaitForFirstConsumer   false                  18m
portworx-io-priority-high   kubernetes.io/portworx-volume   Delete          Immediate              false                  18m
~~~





~~~apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: local-pvc
spec:
  storageClassName: local-storage
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 500Mi 
~~~



~~~
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: nginx
  name: nginx
spec:
  containers:
  - image: nginx:alpine
    name: nginx
    volumeMounts:
    - mountPath: /var/www/html
      name: test
  volumes:
  - name: test
    persistentVolumeClaim:
      claimName: local-pvc
status: {}
~~~



~~~
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: delayed-volume-sc 
provisioner: kubernetes.io/no-provisioner
volumeBindingMode: WaitForFirstConsumer

~~~













