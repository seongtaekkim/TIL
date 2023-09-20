

## 01. Manual Scheduling



~~~
# pod-definition.yaml

~~~



~~~
# pod-bind-definition.yaml
apiVersion: v1
kind: Binding
metadata:
  name: nginx
target:
  apiVersion: v1
  kind: Node
  name: node02
~~~



~~~
curl --header "Content-Type:application/json" --request POST --data '{"apiVersion":"v1","kind":"Binding"...}' http://$SERVER/api/v1/namespaces/default/pods/$PODNAME/binding/
~~~





## 02. Practice Test



~~~
apiVersion: v1
kind: Pod
metadata:
  name: nginx
spec:
  containers:
  - image: nginx
    name: nginx
~~~

- pod를 실행하면, 스케쥴러가 없어서 pending상태에 머문다.

~~~
kubectl get nodes
kubectl create -f nginx.yaml
kubectl get pods # pending 
                 # 스케쥴러가 노드데 있는 포드의 스케쥴링을 제대로 수행 못했다는 의미
kubectl get pods -n kube-system # scheduler pod이 존재하지 않음

~~~



- binding 을 만들어서 적용을 할 수 도 있고, 그게 아니라면

- nodeName을 설정하고 pod을 삭제 후 다시 만들어야 한다.
- 혹은 kubectl replace --force -f nginx.yaml 로 진행.

~~~
apiVersion: v1
kind: Pod
metadata:
  name: nginx
spec:
  nodeName: node01 # 수동스케쥴러
  containers:
  - image: nginx
    name: nginx
~~~



- nodeName이 변경되는 경우에 실행중인 포드를 다른 노드로 옮길 수 없다.

- 따라서 nodeName을 변경하고, 실행중인 포드 삭제 후 재실행 해야 한다.



## 03. Labels and Selectors



- label, selector filter

~~~
kubectl get pods --selector app=App1, ...
~~~





## 04. Practice Test - Labels and Selectors



~~~
kubectl get pods --selector env=dev --no-headers
~~~



~~~
kubectl get pod --show-labels
kubectl get pod -L env
~~~



- 모든 object label flter 조회

~~~
$ kubectl get all --selector env=prod
NAME              READY   STATUS    RESTARTS   AGE
pod/auth          1/1     Running   0          7m19s
pod/app-1-zzxdf   1/1     Running   0          7m19s
pod/db-2-4d4jg    1/1     Running   0          7m19s
pod/app-2-sxxt5   1/1     Running   0          7m20s

NAME            TYPE        CLUSTER-IP     EXTERNAL-IP   PORT(S)    AGE
service/app-1   ClusterIP   10.43.136.23   <none>        3306/TCP   7m19s

NAME                    DESIRED   CURRENT   READY   AGE
replicaset.apps/db-2    1         1         1       7m20s
replicaset.apps/app-2   1         1         1       7m20s
~~~



- label filter 여러개
  - --selector 옵션으로도 가능하다.

~~~
$ kubectl get pod -l 'env=prod,bu=finance,tier=frontend'
~~~



## 05. Taints and Tolerations

- taint, toleration 개념
- taint 가 있는 노드에는 팟이 생성 안됨





~~~
kubectl taint nodes node-name key=value:taint-effect

~~~

Noschedule

PreferNoSchedule

NoExecute

~~~
kubectl taint nodes node1 app=blue:NoSchedule
~~~



~~~
apiVersion: v1
kind: Pod
metadata:
  name: nginx
spec:
  containers:
  - image: nginx
    name: nginx
  tolerations:
  - key: app
    operator: "Equal"
    value: blue
    effect: NoSchedule
~~~

- taint, tolearation은  특포드를 못받아들이게 하는데 목적이 있다.



- 쿠버네티스 클러스터가 실행될 때 master노드에는 이미 taint가 설정되어 있어 스케쥴러가 pod를 할당할 수 없다.

~~~
kubectl describe node master | grep Taint # taint 정보확인.
~~~





## 06. Test



~~~
kubectl taint nodes node01 spray=mortein:NoSchedule
~~~





- master, worker 두개 노드가 있는 상황에서
- worker에 taint가 있으면 
- 일반 노드 실행 시 어디에도 배치되지 않는다.
- 이 때 master의 taint를 삭제하면
- 스케쥴러는 배치되지 못했던 pod을 master 노드에 배치한다.



## 07. Node Selectors



어떤포드를 생성할 때, 여러 노드의 자원상태가 고려되지 않고 배치되어 문제가발생할  수있다.

이를 해결하기 위해 특정노드에만 팟이 작동하도록 해야하는데, 아래와 같이 두가지 방법이 있다.

1. 노드선택기
   - 복잡한 명령은 수행할 수 없다.

~~~
spec:
  nodeSelector:
    size: Large
~~~



- Label Nodes

~~~
kubectl label nodes <nodename> <label key=<label value>
kubectl label nodes node-1 size=Large
~~~





## 08. Node Affinity

- 포드가 특정 노드에 호스트될 수 있도록 해준다.
- 특정노드에 포드배치를 제한하는 고급기능을 제공한다.



~~~
apiVersion:
kind: Pod
metadata:
  name:
spec:
  containers:
  - name:
    image:
  affinity:
    nodeAffinity:
      requiredDuringSchedulingIgnoredDuringExecution:
        nodeSelectorTerms:
        - matchExpressions:
          - key: size
            operator: In
            values:
            - Large
~~~



~~~
        nodeSelectorTerms:
        - matchExpressions:
          - key: size
            operator: In
            values:
            - Large
            - Medium
~~~



~~~
        nodeSelectorTerms:
        - matchExpressions:
          - key: size
            operator: NotIn
            values:
            - Small
~~~



~~~
        nodeSelectorTerms:
        - matchExpressions:
          - key: size
            operator: Exists
~~~



node affinity type 은 스케줄러의 기능을 정의합니다.  Affinity 와 해당 포드의
라이프사이클 단계를 정의합니다. 현재 두 가지 유형의 노드 친화성이 있습니다



스케줄링 중에 필요, 실행 중에는 무시됨

스케줄링 중에는 선호되고 실행 중에는 무시됩니다.

그리고 다음과 같은 추가 유형의 노드 선호도가 계획되어 있습니다.

추가 유형이 계획되어 있습니다. 스케줄링 중에 필요하며 실행 중에 필요합니다.



There are currently two types of node affinity available,

required during scheduling, ignored during execution

and preferred during scheduling, ignored during execution.

And there are additional types of node affinity planned

as of this recording.

Required during scheduling, required during execution.



Available:

- requiredDuringSchedulingIgnoreDuringExecution

- prererredDuringSchedulingIgnoreDuringExecution

During : 포드가 존재하지 않다가 처음으로 만들어지는 상태.

Planned 

- requiredDuringSchedulingRequiredDuringExecution

|        | DuringScheduling | DuringExecution |
| ------ | ---------------- | --------------- |
| Type 1 | Required         | Ignored         |
| Type 2 | Preffered        | Ignored         |
| Type 3 | Required         | Required        |



## 09. Practice Test - Node Affinity





~~~
 kubectl label node node01 color=blue
~~~





~~~
kubectl create deploy --image=nginx blue --replicas=3
~~~



~~~
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
            - matchExpressions:
              - key: color
                operator: In
                values:
                - blue
~~~





~~~
 kubectl create deploy --image=nginx red --dry-run=client -o yaml > nginx.yaml
kubectl get nodes controlplane -L node-role.kubernetes.io/control-plane
~~~





~~~
apiVersion: apps/v1
kind: Deployment
metadata:
  creationTimestamp: null
  labels:
    app: red
  name: red
spec:
  replicas: 2
  selector:
    matchLabels:
      app: red
  strategy: {}
  template:
    metadata:
      creationTimestamp: null
      labels:
        app: red
    spec:
      containers:
      - image: nginx
        name: nginx
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
            - matchExpressions:
              - key: node-role.kubernetes.io/control-plane
                operator: Exists 
status: {}
~~~







## 10. Taints and Tolerations vs Node Affinity



taintand and tolerations 

- pod이 꼭 원하는 node에 설정되는 건 아니다.

Node Affinity

- 다른 pod가 원하지 않는 node에 올 수 도 있다.



trains 로 노드를 막아놓고

pod의 node afflinity 설정에 접근하지 말아야 할 node를 설정하면

원하는 노드에만 배치할 수 있게 된다.





## 11. Resource Requirements and Limits



- pod가 만들어질 때 룰이 적용되는거다. Pod생성 이후 룰이 만들어지면 적용 안됨.

~~~
apiVersion: v1
kind: LimitRange
metadata:
  name: cpu-resource-constraint
spec:
  limits:
  - default:
      cpu: 500m
    defaultRequest:
      cpu: 500m
    max:
      cpu: 1
    min:
      cpu: 100m
    type: Container
~~~



- namespace 리소스 할당

~~~
apiVersion: v1
kind: LimitRange
metadata:
  name: my-resource-quota
spec:
  hard:
    requests.cpu: 4
    requests.memory: 4Gi
    limits.cpu: 10
    limits.memory: 10Gi
~~~





## 12. Tip



A quick note on editing Pods and Deployments

#### Edit a POD

Remember, you CANNOT edit specifications of an existing POD other than the below.

- spec.containers[*].image
- spec.initContainers[*].image
- spec.activeDeadlineSeconds
- spec.tolerations

For example you cannot edit the environment variables, service accounts, resource limits (all of which we will discuss later) of a running pod. But if you really want to, you have 2 options:

1. Run the `kubectl edit pod <pod name>` command.  This will open the pod specification in an editor (vi editor). Then edit the required properties. When you try to save it, you will be denied. This is because you are attempting to edit a field on the pod that is not editable.

![img](https://img-c.udemycdn.com/redactor/raw/2019-05-30_14-46-21-89ea56fea6b993ee0ccff1625b13341e.PNG)

![img](https://img-c.udemycdn.com/redactor/raw/2019-05-30_14-47-14-07b2638d1a72cb2d5b000c00971f6436.PNG)

A copy of the file with your changes is saved in a temporary location as shown above.

You can then delete the existing pod by running the command:

```
kubectl delete pod webapp
```



Then create a new pod with your changes using the temporary file

```
kubectl create -f /tmp/kubectl-edit-ccvrq.yaml
```



2. The second option is to extract the pod definition in YAML format to a file using the command

```
kubectl get pod webapp -o yaml > my-new-pod.yaml
```

Then make the changes to the exported file using an editor (vi editor). Save the changes

```
vi my-new-pod.yaml
```

Then delete the existing pod

```
kubectl delete pod webapp
```

Then create a new pod with the edited file

```
kubectl create -f my-new-pod.yaml
```



#### Edit Deployments

With Deployments you can easily edit any field/property of the POD template. Since the pod template is a child of the deployment specification, with every change the deployment will automatically delete and create a new pod with the new changes. So if you are asked to edit a property of a POD part of a deployment you may do that simply by running the command

```
kubectl edit deployment my-deployment
```





## 13. Test



- 문제 : OOM으로 종료된 pod의 limit을 늘리라는 문제.

~~~
kubectl edit pod elephant # 여기서 limit을 수정하고 나오면, invalid 메시지와 함께, 우리가 변경한내용을 임시파일에 저장하고 적용이 안된다. 이 임시파일로 replace 하면 적용할 수 있다.
kubectl replace --force -f /tmp/임시파일.yaml
~~~



- 나는 그냥 아래처럼 다시만들고 진행했는데, 위 방법이 깔끔한 거 같다.

~~~
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: elephant
  name: elephant
spec:
  containers:
  - image: polinux/stress
    name: elephant
    resources:
      limits:
        memory: 20Mi
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}
~~~



## 14. DaemonSets



- Monitoring Solution, Logs Viewr 같은 pod에 사용하기 좋음.
- kube-proxy
- networking : weave-net



yaml replicaSet 하고 같음 

~~~
kubectl get daemonsets
~~~





## 15. DaemonSets Test



~~~
kubectl get all --all-namespaces
~~~





## 16.  Static Pods

kubelet은 독립적으로 노드를 관리하 수 있다.

노드마다 kubelet 이 설지되어잇고 docket도 잇다.

kubelet 도 pod을 만들수  있다.

kubelet 은 주기적으로 /etc/kubernetes/manifest 폴더를 확인하고

호스트에 포드를 만든다

~~~
위치는 kubelet 실행시 아래와 같은 옵션으로 전달됨
--pod-manifest-path=/etc/kuber .... \\
~~~



정적포드는 왜 사용하나

- kubelet이 controlplane 의 구성요소 (apiserver, scheduler) 등을 설정하는데 유리하다.
- 충돌이 일어나지 않는다.
- 스케쥴러는 정적포드을 무시한다.



## 17. Test





- 이름을 조회하면 마지막에 노드 이름이 나옴
- 그 노드로 접속하고
- kubelet config 파일을 열어서 staticpod 위치조회 후 삭제하면 된다.

~~~
kubectl get pod --all-namespaces
ssh node01
ps -ef | grep /usr/bin/kubelet
cat var/lib/kubelet/config.yaml
cd /etc/just-to-mess-with-you
rm ~.yaml
~~~



## 18. Multiple Schedulers

?? 어려움 뭔말이야



~~~
kubectl get pods --namespace=kube-system
~~~



~~~
pod 생성

spec:
  schedulerName: my-custom-scheduler
~~~





~~~
kubectl get events -o wide

kubectl logs my-custom-scheduler --name-space=kube-system
~~~





## 19 Practice Test - Multiple Schedulers

- 개념이 이해가 잘 안감.
- 담에 다시 해보자



## 20. Configuring Scheduler Profiles

- scheduling queue
  - PrioritySort
    - 우선순위 정렬
-  filter (preFilter, postFilter 확장)
  - NodeResourcesFit
    - 배치 불가능한 노드를 제외함
  - NodeName
  - Nodeunscheduleable : 나중에 논의한다 함
- scoring (preScore, reserve permit 확장)
  - NodeResourcesFit
    - 적절한 노드에 배치
  - ImageLocality
- binding(preBind, postBind 확장)
  - DefaultBinder



~~~
# pod
apiVersino
kind
metadata:
  name:
spec:
  priorityClassName: high-priority
  containers:
  - name:
    image:
    resources:
      requests:
        memory:
        cpu:
~~~



~~~
# PriorityClass
apiVersion: scheduling.k8s.io/v1
kind: PriorityClass
metadata:
  name: high-priority
value: 1000000
globalDefault: false
description: ""
~~~



- scheduler profiles

~~~
# KubeSchedulerConfiguration
apiVersion: kubescheduler.config.k8s.io/v1
kind: KubeSchedulerConfiguration
profiles:
- schedulerName: my-scheduler-2
- schedulerName: my-scheduler-3
- schedulerName: my-scheduler-4
~~~



### References

https://github.com/kubernetes/community/blob/master/contributors/devel/sig-scheduling/scheduling_code_hierarchy_overview.md

https://kubernetes.io/blog/2017/03/advanced-scheduling-in-kubernetes/

https://jvns.ca/blog/2017/07/27/how-does-the-kubernetes-scheduler-work/

https://stackoverflow.com/questions/28857993/how-does-kubernetes-scheduler-work





























































