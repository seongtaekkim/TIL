### 1

~~~
Create a new service account with the name pvviewer. Grant this Service account access to list all PersistentVolumes in the cluster by creating an appropriate cluster role called pvviewer-role and ClusterRoleBinding called pvviewer-role-binding.
Next, create a pod called pvviewer with the image: redis and serviceAccount: pvviewer in the default namespace.
~~~



~~~
controlplane ~ ➜  cat pod.yaml 
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: pvviewer
  name: pvviewer
spec:
  containers:
  - image: redis
    name: pvviewer
    resources: {}
  dnsPolicy: ClusterFirst
  restartPolicy: Always
  serviceAccountName: pvviewer
status: {}

controlplane ~ ➜  cat sa.yaml 
apiVersion: v1
kind: ServiceAccount
metadata:
  name: pvviewer

controlplane ~ ➜  cat cr.yaml 
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  name: pvviewer-role
rules:
- apiGroups: [""]
  resources: ["persistentvolumes"]
  verbs: ["list"]

controlplane ~ ➜  cat crb.yaml 
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: pvviewer-role-binding
subjects:
- kind: User
  name: pvviewer
  apiGroup: rbac.authorization.k8s.io
roleRef:
  kind: ClusterRole
  name: pvviewer-role
  apiGroup: rbac.authorization.k8s.io
~~~



~~~
k describe pod pvviewer | grep -i service
~~~



### 2

~~~
List the InternalIP of all nodes of the cluster. Save the result to a file /root/CKA/node_ips.

Answer should be in the format: InternalIP of controlplane<space>InternalIP of node01 (in a single line)
~~~

~~~
 k get nodes -o jsonpath='{.items[*].status.addresses[0].address}' > /root/CKA/node_ips
~~~



~~~
k get nodes -o json | jq | grep -i interalip -B 100

k get nodes -o json | jq -c 'paths' | grep type # 모든 경로
k get nodes -o jsonpath='{.items[*].status.addresses[?(@.type="InternalIP"].address}'
~~~





### 3

~~~
Create a pod called multi-pod with two containers.
Container 1, name: alpha, image: nginx
Container 2: name: beta, image: busybox, command: sleep 4800

Environment Variables:
container 1:
name: alpha

Container 2:
name: beta
~~~



~~~
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: alpha
  name: multi-pod
spec:
  containers:
  - image: nginx
    name: alpha
    env:
    - name: name
      value: alpha
  - image: busybox
    name: beta
    command: ['sleep', '4800']
    env:
    - name: name
      value: beta
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}
~~~









### 4

~~~
Create a Pod called non-root-pod , image: redis:alpine
runAsUser: 1000
fsGroup: 2000
~~~

~~~
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: non-root-pod
  name: non-root-pod
spec:
  securityContext:
    runAsUser: 1000
    fsGroup: 2000
  containers:
  - image: redis:alpine
    name: non-root-pod
    resources: {}
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}

~~~



### 5

~~~
We have deployed a new pod called np-test-1 and a service called np-test-service. Incoming connections to this service are not working. Troubleshoot and fix it.

Create NetworkPolicy, by the name ingress-to-nptest that allows incoming connections to the service over port 80.

Important: Don't delete any current objects deployed.
~~~



~~~
k run curl --image=alpine/curl --rm -it --sh
curl np-test-service # hang 걸린다.
~~~



~~~
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: ingress-to-nptest
spec:
  podSelector:
    matchLabels:
      run: np-test-1
  ingress:
  - ports:
    - port: 80
      protocol: TCP
  policyTypes:
  - Ingress
  

~~~

~~~
 k exec -it multi-pod beta -- curl 10.99.219.1
~~~



### 6

~~~
Taint the worker node node01 to be Unschedulable. Once done, create a pod called dev-redis, image redis:alpine, to ensure workloads are not scheduled to this worker node. Finally, create a new pod called prod-redis and image: redis:alpine with toleration to be scheduled on node01.


key: env_type, value: production, operator: Equal and effect: NoSchedule
~~~



~~~
kubectl taint nodes node01 env_type=production:NoSchedule

~~~



~~~
# dev.yaml
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: dev-redis
  name: dev-redis
spec:
  containers:
  - image: redis:alpine
    name: dev-redis
    resources: {}
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}
~~~



~~~
# prod.yaml
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: prod-redis
  name: prod-redis
spec:
  containers:
  - image: redis:alpine
    name: prod-redis
    resources: {}
  dnsPolicy: ClusterFirst
  restartPolicy: Always
  tolerations:
  - key: "env_type"
    value: "production"
    operator: "Equal"
    effect: "NoSchedule"
  nodeName: node01
status: {}
~~~





### 7

~~~
Create a pod called hr-pod in hr namespace belonging to the production environment and frontend tier .
image: redis:alpine


Use appropriate labels and create all the required objects if it does not exist in the system already.
~~~





~~~
k create namespace hr
  k run --image=redis:alpine hr-pod -n hr --labels environment=production,tier=frontend
~~~



### 8

~~~
A kubeconfig file called super.kubeconfig has been created under /root/CKA. There is something wrong with the configuration. Troubleshoot and fix it.
~~~



~~~
k get nodes --kubeconfig /root/CKA/super.kubeconfig # 처음엔 안되고 수정후 동작함.
~~~



### 9

~~~
We have created a new deployment called nginx-deploy. scale the deployment to 3 replicas. Has the replica's increased? Troubleshoot the issue and fix it.
~~~



~~~
# replica 3개로 올리고, describe 에서 오류내용이 없다면 kube-system을 확인한다.

k describe -n kube-system pod kube-contro1ler-manager-controlplane
 # log에서 iamge 잘 못불러와서 가져옴 
 # 이미지 올바른지 어케 체크하지?
~~~



