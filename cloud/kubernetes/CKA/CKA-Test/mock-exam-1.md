## 



### 1

~~~
 k run --image=nginx:alpine nginx-pod
~~~



### 2

~~~
 k run --image=redis:alpine messaging --labels tier=msg
~~~





### 3

~~~
k create ns apx-x9984574
~~~





### 4

~~~
 k get node -o json > /opt/outputs/nodes-z3444kd9.json
~~~



### 5

~~~
apiVersion: v1
kind: Service
metadata:
  name: messaging-service
spec:
  selector:
    tier: msg
  ports:
  - name: name-of-service-port
    protocol: TCP
    port: 6379
    targetPort: 6379
~~~



### 6

~~~
k create deploy --image=kodekloud/webapp-color hr-web-app --replicas=2
~~~



### 7

~~~
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: static-busybox
  name: static-busybox
spec:
  containers:
  - image: busybox
    name: static-busybox
    command: ['sleep', '1000']
    resources: {}
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}

~~~



### 8

~~~
  k run --image=redis:alpine temp-bus -n finance
~~~



### 9

~~~
apiVersion: v1
kind: Service
metadata:
  name: init-myservice
spec:
  ports:
  - protocol: TCP
    port: 80
    targetPort: 80
~~~





### 10

~~~
apiVersion: v1
kind: Service
metadata:
  name: hr-web-app-service
spec:
  type: NodePortd
  selector:
    app: hr-web-app
  ports:
    - port: 8080
      nodePort: 30082
~~~



### 11

~~~
k get nodes -o jsonpath='{.items[*].status.nodeInfo.osImage}' > /opt/outputs/nodes_os_x43kj56.txt
~~~



### 12

~~~
apiVersion: v1
kind: PersistentVolume
metadata:
  name: pv-analytics
spec:
  capacity:
    storage: 100Mi
  volumeMode: Filesystem
  accessModes:
    - ReadWriteMany
  hostPath:
    path: /pv/data-analytics
~~~









