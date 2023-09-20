### 01

~~~
 ETCDCTL_API=3 etcdctl --endpoints=https://[127.0.0.1]:2379 \
--cacert=/etc/kubernetes/pki/etcd/ca.crt \
--cert=/etc/kubernetes/pki/etcd/server.crt \
--key=/etc/kubernetes/pki/etcd/server.key \
snapshot save /opt/etcd-backup.db
~~~





### 02

Create a Pod called `redis-storage` with image: `redis:alpine` with a Volume of type `emptyDir` that lasts for the life of the Pod.

~~~
apiVersion: v1
kind: Pod
metadata:
  name: redis-storage
spec:
  containers:
    - image: redis:alpine
      name: redis-storage
      volumeMounts:
      - mountPath: /data/redis
        name: cache-volume
  volumes:
  - name: cache-volume
    emptyDir:
      sizeLimit: 500Mi
~~~





### 03

~~~
apiVersion: v1
kind: Pod
metadata:
  name: super-user-pod
spec:
  containers:
  - name: super-user-pod
    image: busybox:1.28
    command: ['sleep','4800']
    securityContext:
      capabilities:
        add: ["SYS_TIME"]
~~~





### 4

~~~
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: use-pv
  name: use-pv
spec:
  containers:
  - image: nginx
    name: use-pv
    resources: {}
    volumeMounts:
    - mountPath: /data
      name: test
  volumes:
  - name: test
    persistentVolumeClaim:
      claimName: my-pvc
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}
~~~



~~~
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: my-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 10Mi 
~~~

~~~
apiVersion: v1
items:
- apiVersion: v1
  kind: PersistentVolume
  metadata:
    annotations:
      pv.kubernetes.io/bound-by-controller: "yes"
    creationTimestamp: "2023-07-09T00:21:47Z"
    finalizers:
    - kubernetes.io/pv-protection
    name: pv-1
    resourceVersion: "7132"
    uid: 7e25e836-0ab9-4c58-88e8-bacd923be45f
  spec:
    accessModes:
    - ReadWriteOnce
    capacity:
      storage: 10Mi
    claimRef:
      apiVersion: v1
      kind: PersistentVolumeClaim
      name: my-pvc
      namespace: default
      resourceVersion: "7130"
      uid: fee4c58d-b969-470b-a948-e059db8c70cf
    hostPath:
      path: /opt/data
      type: ""
    persistentVolumeReclaimPolicy: Retain
    volumeMode: Filesystem
  status:
    phase: Bound
kind: List
metadata:
  resourceVersion: ""
~~~





### 5



~~~
 k create deploy --image=nginx:1.16 nginx-deploy
~~~





### 6

~~~
Create a new user called john. Grant him access to the cluster. John should have permission to create, list, get, update and delete pods in the development namespace . The private key exists in the location: /root/CKA/john.key and csr at /root/CKA/john.csr.


Important Note: As of kubernetes 1.19, the CertificateSigningRequest object expects a signerName.

Please refer the documentation to see an example. The documentation tab is available at the top right of terminal.

CSR: john-developer Status:Approved

Role Name: developer, namespace: development, Resource: Pods

Access: User 'john' has appropriate permissions
~~~



~~~
cat john.csr | base64 -w 0
~~~

~~~
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

~~~
k create -f csr "cert.yaml"
~~~



- role

~~~
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

~~~
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



~~~
k auth can-i create deployment --as=john -n development
 k certificate approve john
 k get csr
~~~



### 7



~~~
Create a nginx pod called nginx-resolver using image nginx, expose it internally with a service called nginx-resolver-service. Test that you are able to look up the service and pod names from within the cluster. Use the image: busybox:1.28 for dns lookup. Record results in /root/CKA/nginx.svc and /root/CKA/nginx.pod
~~~



~~~
apiVersion: v1
kind: Service
metadata:
  name: nginx-resolver-service
spec:
  selector:
    app: test
  ports:
  - protocol: TCP
    port: 8080
    targetPort: 8080
~~~

~~~
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    app: test
  name: nginx-resolver
spec:
  containers:
  - image: nginx
    name: nginx-resolver
    resources: {}
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}

~~~



~~~
k run --image=busybox busybox -- sleep 4000
k exec -it busy -- nslookup 10-244-192-6.default.pod.cluster.local > nginx.pod
~~~



~~~
k exec -it busy -- nslookup 10-244-192-6.default.pod.cluster.local > nginx.pod
~~~



~~~
k exec -it busy -- nslookup nginx-resolver-service.default.svc.cluster.local
~~~





### 8

~~~
Create a static pod on node01 called nginx-critical with image nginx and make sure that it is recreated/restarted automatically in case of a failure.
Use /etc/kubernetes/manifests as the Static Pod path for example.

static pod configured under /etc/kubernetes/manifests ?
Pod nginx-critical-node01 is up and running
~~~



~~~
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: nginx-critical
  name: nginx-critical
spec:
  containers:
  - image: nginx
    name: nginx-critical
    resources: {}
  dnsPolicy: ClusterFirst
  restartPolicy: Always
  nodeName: node01
status: {}
~~~

































