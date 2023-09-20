



- gcePersistentDisk는  nfs, homePath로 대체할 수도 있다.

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



















