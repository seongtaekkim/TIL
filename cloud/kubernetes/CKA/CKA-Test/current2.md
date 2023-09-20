### 1

~~~
create sa eploy-cka20-arch
~~~

~~~
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  # "namespace" omitted since ClusterRoles are not namespaced
  name: deploy-role-cka20-arch
rules:
- apiGroups: [""]
  #
  # at the HTTP level, the name of the resource for accessing Secret
  # objects is "secrets"
  resources: ["deployments"]
  verbs: ["get"]

~~~



~~~
kubectl create clusterrolebinding deploy-role-binding-cka20-arch \
  --clusterrole=deploy-role-cka20-arch \
  --serviceaccount=deploy-cka20-arch:default
~~~



~~~
student-node ~ ➜  kubectl --context cluster1 create serviceaccount deploy-cka20-arch
student-node ~ ➜  kubectl --context cluster1 create clusterrole deploy-role-cka20-arch --resource=deployments --verb=get
student-node ~ ➜  kubectl --context cluster1 create clusterrolebinding deploy-role-binding-cka20-arch --clusterrole=deploy-role-cka20-arch --serviceaccount=default:deploy-cka20-arch
~~~



~~~
student-node ~ ➜  kubectl --context cluster1 auth can-i get deployments --as=system:serviceaccount:default:deploy-cka20-arch
yes
~~~







### 2

pod 만들기

~~~
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: alpine-sleeper-cka15-arch
  name: alpine-sleeper-cka15-arch
spec:
  containers:
  - image: alpine
    name: alpine-sleeper-cka15-arch
    command: ['sleep', '7200']
    resources: {}
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}
~~~



### 3

secret decode 

~~~
 echo VGhpcyBpcyB0aGUgc2VjcmV0IQo= | base64 --decode > /opt/beta-sec-cka14-arch
~~~



### 4.

- sa, clusterrole, clusterrolebinding 확인 후 permission write	

~~~
echo "resource:deployments.apps|verbs:get,list,watch" > /opt/red-sa-cka23-arch
~~~

~~~
 k auth can-i list deployments.apps --as red-sa-cka23-arch
 근데 이게 안됨 왜지?
~~~





### 5

메모리 사용량

~~~
echo cluster1,kube-apiserver-cluster1-controlplane > /opt/high_memory_node
~~~





### 6

pod crash 수정

/bin/bash -> /bin/sh 수정





### 7

networkpolicy 모르겟음



### 8

![스크린샷 2023-07-10 오후 6.16.01](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-10 오후 6.16.01.png)

cronjob 에서 실행할 팝의 명령어를 수정함.

~~~
~~~



### 9

![스크린샷 2023-07-10 오후 6.19.22](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-10 오후 6.19.22.png)

oom 발생하는데, 리소스 정한거 어떻게 보지???????

~~~
kubectl logs -f 
~~~

**** 리소스 메모리를 더 크게 잡아보면 된다.





### 10

![스크린샷 2023-07-10 오후 6.26.49](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-10 오후 6.26.49.png)

node notready 문제

- requst resource 문제.

모르겟음

##### SSH into the `cluster4-node01` and check if `kubelet` service is running

```
ssh cluster4-node01
systemctl status kubelet
```

You will see its inactive, so try to start it.

```sh
systemctl start kubelet
```

Check again the status

```sh
systemctl status kubelet
```

Its still failing, so let's look into some latest error logs:

```sh
journalctl -u kubelet --since "30 min ago" | grep 'Error:'
```

You will see some errors as below:

```
cluster4-node01 kubelet[6301]: Error: failed to construct kubelet dependencies: unable to load client CA file /etc/kubernetes/pki/CA.crt: open /etc/kubernetes/pki/CA.crt: no such file or directory
```

Check if `/etc/kubernetes/pki/CA.crt` file exists:

```sh
ls /etc/kubernetes/pki/
```

You will notice that the file name is `ca.crt` instead of `CA.crt` so possibly `kubelet` is looking for a wrong file. Let's fix the config:

```sh
vi /var/lib/kubelet/config.yaml
  
```

- Change clientCAFile from `/etc/kubernetes/pki/CA.crt` to `/etc/kubernetes/pki/ca.crt`

Try to start it again

```sh
systemctl start kubelet
```

Service should start now but there might be an error as below

```
ReportingIn
stance:""}': 'Post "https://cluster4-controlplane:6334/api/v1/namespaces/default/events": dial tcp 10.9.63.18:633
4: connect: connection refused'(may retry after sleeping)
Sep 18 09:21:47 cluster4-node01 kubelet[6803]: E0918 09:21:47.641184    6803 kubelet.go:2419] "Error getting node
" err="node \"cluster4-node01\" not found"
```

You must have noticed that its trying to connect to the `api` server on port `6334` but the default port for `kube-apiserver` is `6443`. Let's fix this:

##### Edit the kubelet config

```sh
vi /etc/kubernetes/kubelet.conf
    
```

- Change server

```
server: https://cluster4-controlplane:6334
```

to

```
server: https://cluster4-controlplane:6443
```

##### Finally restart kublet service

```sh
systemctl restart kubelet
```

Check from the `student-node` now and `cluster4-node01` should be ready now.

```sh
kubectl get node --context=cluster4
```



### 11

 affinity 문제

모름

##### Let's check the POD status

```sh
kubectl get pod
```

You will see that `cat-cka22-trb` pod is stuck in `Pending` state. So let's try to look into the events

```sh
kubectl --context cluster2 get event --field-selector involvedObject.name=cat-cka22-trb
```

You will see some logs as below

```
Warning   FailedScheduling   pod/cat-cka22-trb   0/3 nodes are available: 1 node(s) had untolerated taint {node-role.kubernetes.io/master: }, 2 node(s) didn't match Pod's node affinity/selector. preemption: 0/2 nodes are available: 3 Preemption is not helpful for scheduling.
```

So seems like this POD is using the node affinity, let's look into the POD to understand the node affinity its using.

```sh
kubectl --context cluster2 get pod cat-cka22-trb -o yaml
```

Under `affinity:` you will see its looking for `key: node` and `values: cluster2-node02` so let's verify if node01 has these labels applied.

```sh
kubectl --context cluster2 get node cluster2-node01 -o yaml
```

Look under `labels:` and you will not find any such label, so let's add this label to this node.

```sh
kubectl label node cluster1-node01 node=cluster2-node01
```

Check again the node details

```sh
kubectl get node cluster2-node01 -o yaml
```

The new label should be there, let's see if POD is scheduled now on this node

```sh
kubectl --context cluster2 get pod
```

Its is but it must be crashing or restarting, so let's look into the pod logs

```sh
kubectl --context cluster2 logs -f cat-cka22-trb
```

You will see logs as below:

```sh
The HOST variable seems incorrect, it must be set to kodekloud
```

Let's look into the POD env variables to see if there is any HOST env variable

```sh
kubectl --context cluster2 get pod -o yaml
```

Under `env:` you will see this

```
env:
- name: HOST
  valueFrom:
    secretKeyRef:
      key: hostname
      name: cat-cka22-trb
```

So we can see that HOST variable is defined and its value is being retrieved from a secret called "cat-cka22-trb". Let's look into this secret.

```sh
kubectl --context cluster2 get secret
kubectl --context cluster2 get secret cat-cka22-trb -o yaml
```

You will find a `key/value` pair under `data:`, let's try to decode it to see its value:

```sh
echo "<the decoded value you see for hostname" | base64 -d
```

ok so the value is set to `kodekloude` which is incorrect as it should be set to `kodekloud`. So let's update the secret:

```sh
echo "kodekloud" | base64
kubectl edit secret cat-cka22-trb
```

- Change requests storage `hostname: a29kZWtsb3Vkdg==` to `hostname: a29kZWtsb3VkCg==` (values may vary)

POD should be good now.





### 12

![스크린샷 2023-07-10 오후 6.31.53](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-10 오후 6.31.53.png)



~~~
apiVersion: apps/v1
kind: Deployment
metadata:
  creationTimestamp: null
  labels:
    app: ocean-tv-wl09
  name: ocean-tv-wl09
spec:
  replicas: 3
  selector:
    matchLabels:
      app: ocean-tv-wl09
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 55%
      maxUnavailable: 40%
  template:
    metadata:
      creationTimestamp: null
      labels:
        app: ocean-tv-wl09
    spec:
      containers:
      - image: kodekloud/webapp-color:v1
        name: webapp-color
        resources: {}
status: {}

~~~





~~~

k create -f ~ --record=true
k edit deploy ocean-tv-wl09 --record=true
student-node ~ ➜  k rollout history  deploy ocean-tv-wl09
deployment.apps/ocean-tv-wl09 
REVISION  CHANGE-CAUSE
1         kubectl create --filename=12.yaml --record=true
2         kubectl edit deploy ocean-tv-wl09 --record=true



student-node ~ ✖ k rollout undo deploy ocean-tv-wl09 --to-revision=1 
deployment.apps/ocean-tv-wl09 rolled back

student-node ~ ➜  k rollout history deploy ocean-tv-wl09
deployment.apps/ocean-tv-wl09 
REVISION  CHANGE-CAUSE
2         kubectl edit deploy ocean-tv-wl09 --record=true
3         kubectl create --filename=12.yaml --record=true
~~~









### 13

주어진 deploy image 버전 올려서 

 curl http://cluster1-node01:30080 체크



### 14



![스크린샷 2023-07-10 오후 6.46.38](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-10 오후 6.46.38.png)

recource > requests 를 임의대로 내렸는데 기준을 어떻게 잡아야 하는건가?





### 15

![스크린샷 2023-07-10 오후 6.56.22](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-10 오후 6.56.22.png)

pvc 잘 한거같은데 안됨 뭘까



~~~
  volumeName: olive-pv-cka10-str
빠져잇엇음
~~~





### 16

![스크린샷 2023-07-10 오후 6.59.06](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-10 오후 6.59.06.png)

- storage class 생성해보기



~~~
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: banana-sc-cka08-str
provisioner: kubernetes.io/no-provisioner
parameters:
  type: gp2
reclaimPolicy: Retain
allowVolumeExpansion: true
mountOptions:
  - debug
volumeBindingMode: WaitForFirstConsumer

~~~



### 17

deploy , service 생성문제



### 18

pod, svc 생성해서 nslookup 해보는거.



~~~
 k exec -it test -- nslookup nginx-resolver-service-cka06-svcn
~~~



~~~
 k exec -it test -- nslookup 10-99-47-215.default.pod.cluster.local > /root/CKA/nginx.pod.cka06.svcn
~~~

































