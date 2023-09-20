## 1. 컨테이너 환경 변수 전달 방법



- 환경변수를 yaml 에 저장할수 있지만 관리가 안됨. (여러개의 pod을 모두 일일이 변경해야 할 수 있다.)

- mysql 환경변수 등을 이미지를 파괴하지 않으면서 환경변수를 전달해 줄 수 있다.



[docs](https://kubernetes.io/docs/tasks/inject-data-application/define-environment-variable-container/)

- env 검색



- envars.yaml

~~~
apiVersion: v1
kind: Pod
metadata:
  name: envar-demo
  labels:
    purpose: demonstrate-envars
spec:
  containers:
  - name: envar-demo-container
    image: gcr.io/google-samples/node-hello:1.0
    env:
    - name: DEMO_GREETING
      value: "Hello from the environment"
    - name: DEMO_FAREWELL
      value: "Such a sweet sorrow"
~~~



~~~
kubectl create -f envars.yaml
kubectl exec -it envar-demo -- bash
printenv # 설정한 환경변수를 확인한다.
echo $DEMO_GREETING
~~~





## **2. ConfigMap을 활용한 환경 변수 설정**



p368 ~

### 

- configmap을 만들 때 파일로 만드는게 가장 일반적이다.

~~~
$ echo -n 1234 > test
$ kubectl create configmap map-name --from-file=test
# $ kubectl create configmap map-name --from-file=test --from-file=test2 ... # 여러개 생성
$ kubectl get configmap map-name -o yaml # key 인 test 는 여러개를 설정할 수 있다.
~~~



- envars-configmap.yaml

~~~
apiVersion: v1
kind: Pod
metadata:
  name: envar-configmap
  labels:
    purpose: demonstrate-envars
spec:
  containers:
  - name: envar-demo-container
    image: gcr.io/google-samples/node-hello:1.0
    env:
    - name: DEMO_GREETING
      valueFrom:
        configMapKeyRef:
          name: map-name
          key: test
~~~

- 실행

~~~
$ kubectl create -f envars-configmap.yaml
$ kubectl exec -it envar-configmap -- bash
$ printenv DEMO_GREETING
~~~





## **3. ConfigMap을 활용한 디렉토리 마운트**





[docs](https://kubernetes.io/docs/tasks/configure-pod-container/configure-pod-configmap/)

- [Configure a Pod to Use a ConfigMap](https://kubernetes.io/docs/tasks/configure-pod-container/configure-pod-configmap/) > Add ConfigMap data to a Volume



```shell
kubectl create -f https://kubernetes.io/examples/configmap/configmap-multikeys.yaml
kubectl get configmaps special-config -o yaml
```



- volumes-configmap.yaml
- special-config는 앞에서 생성한 configmap-multikyes.yaml 의 Configmap name이다.

~~~
apiVersion: v1
kind: Pod
metadata:
  name: volumes-configmap
  labels:
    purpose: demonstrate-envars
spec:
  containers:
  - name: envar-demo-container
    image: gcr.io/google-samples/node-hello:1.0
    volumeMounts:
      - name: config-volume
        mountPath: /etc/config
  volumes:
    - name: config-volume
      configMap:
        # Provide the name of the ConfigMap containing the files you want
        # to add to the container
        name: special-config
~~~





~~~
$ kubectl create -f volumes-configmap.yaml
$ kubectl get pod
$ kubectl exec -it volumes-configmap -- bash
$ cd /etc/config/
$ cat SPACIAL_LEVEL
~~~



- 이전 예제까지는 컨테이너를 재시작해야 변수가 들어간다 

- 이번 예제처럼마운트 되는 형태는 1분마다 리프레시 되기 때문에 데이터를 무중단하면서 넣고 참고할 수 있다.





## **4. Secret을 활용한 환경 변수 설정**



- secret 은 etcd 에 저장되고, 이 데이터는 허가된 pod에서만 볼 수 있다.

~~~
echo -n admin > username
echo -n 12341234 > password
# echo -n 'admin' | base64
# echo -n '1234' | base64
kubectl create secret generic db-user-pass --from-file=username --from-file=password
kubectl get secret -o yaml db-user-pass # base64가 기본인듯?
echo "암호화된값" | base64 --decode # decode 값 출력

~~~



- envars-secret.yaml

~~~
apiVersion: v1
kind: Pod
metadata:
  name: envar-secret
  labels:
    purpose: demonstrate-envars
spec:
  containers:
  - name: envar-demo-container
    image: gcr.io/google-samples/node-hello:1.0
    env:
    - name: user
      valueFrom:
        secretKeyRef:
          name: db-user-pass
          key: username
    - name: pass
      valueFrom:
        secretKeyRef:
          name: db-user-pass
          key: password
~~~



~~~
kubectl create -f envars-secret.yaml
kubectl exec -it envars-secret -- bash
printenv # user, pass 가 decode 되어 환경변수에 저장되어 있다.
~~~







## **5. 환경 변수 설정 연습문제**

![스크린샷 2023-06-15 오후 5.22.40](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-15 오후 5.22.40.png)

~~~
kubectl get secret -n kube-system | wc -l
kubectl get secret -n kubec-system | grep default
kubectl get secret -n kubec-system | grep "pod id" -o yaml # type 확인 
~~~



~~~ 
kubectl create secret generic db-secret --from-literal='DB_Password=!@#123qwe' --dry-run=client -o yaml > secret-mysql.yaml
kubectl create -f secret-mysql.yaml
~~~



~~~
kubectl exec -it mysql -- mysql -u root -p
~~~



### 파일 만들 때 주의사항

- 파일에 (DB_Password) 개행을 넣으면 mysql 실행이 안됨. 반드시 개행 빼고 넣을것.





## 6. 초기 명령어 및 아규먼트 전달과 실행

![스크린샷 2023-06-15 오후 5.36.50](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-15 오후 5.36.50.png)



![스크린샷 2023-06-30 오전 10.02.13](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-30 오전 10.02.13.png)



- busybox-sleep.yaml

~~~
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: busybox
  name: busybox
spec:
  containers:
  - image: busybox
    name: busybox
    resources: {}
    command: ["sleep"]
    args: ["1000"]
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}
~~~

- arg 대신 아래처럼 작성할 수 있다.

~~~
command: ['sh', '-c', 'echo hello && sleep 3000']
~~~

- sh로 접속하거나 로그 등을 확인할 수 있다.

~~~
kubectl logs busybox
kubectl exec -it busybox -- sh
~~~





 

## 7. 한 포드에 멀티 컨테이너



모니터링, 프로바이더



~~~
로그가 발생하면, 모니터링 컨테이너가 읽어서
stdout 으로 출력하여 다른 노드에 저장이 된다.
docker logs
~~~



### 하나의 포드에서 nginx와 redis 이미지를 모두 실행하는 yaml을 만들고 실행해라.

- 포트가 겹치면 안된다.
- 네트워크 인터페이스가 하나이기 때문.





### 생성된 노드에서 아래와 같이 같은 네트워크인터페이스 에서 컨테이너가 돌아가는 지 확인 할 수 있다.

- 네트워크인터페이스를 띄우고
- 유지하기위해 pause를 띄우고 (sleep 컨테이너)
- nginx, redis 가 네트워크 인터페이스를 공유한 상태로 돌아가고 있다.

~~~
docker ps -a | grep "nginx"
~~~



## 8. init 컨테이너의 개념과 실습



![스크린샷 2023-06-30 오전 10.18.11](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-30 오전 10.18.11.png)





- until nslookup myservice; do echo wait; sleep 2; done;
  - name server 를 찾을 때까지 반복한다.

~~~
apiVersion: v1
kind: Pod
metadata:
  name: myapp-pod
  labels:
    app: myapp
spec:
  containers:
  - name: myapp-container
    image: busybox:1.28
    command: ['sh', '-c', 'echo The app is running! && sleep 3000']
  initContainers:
  - name: init-myservice
    image: busybox:1.28
    command: ['sh', '-c', 'until nslookup myservice; do echo wating for myservice; sleep 2; done;']
  - name: init-mydb
    image: busybox:1.28
    command: ['sh', '-c', 'until nslookup mydb; do echo waiting for mydb; sleep 2; done;']
~~~



~~~
$ kubectl get pod -w
NAME                  READY   STATUS     RESTARTS   AGE
myapp-pod             0/1     Init:0/2   0          4s
~~~



- init service 생성

~~~
apiVersion: v1
kind: Service
metadata:
  name: myservice
spec:
  ports:
  - protocal: TCP
    port: 80
    targetPort: 9376
---
apiVersion: v1
kind: Service
metadata:
  name: mydb
spec:
  ports:
  - protocal: TCP
    port: 80
    targetPort: 9377
~~~



- service 를 찾아서 init이 해소되는 과정.

~~~
chchxortnldl@master-1:~$ kubectl get pod -w
NAME                  READY   STATUS     RESTARTS   AGE
myapp-pod             0/1     Init:0/2   0          110s
static-web-master-1   1/1     Running    0          29m
myapp-pod             0/1     Init:1/2   0          3m32s
myapp-pod             0/1     PodInitializing   0          3m33s
myapp-pod             1/1     Running           0          3m34s
~~~





## 9. 컨테이너 리소스 제한 및 요구사항 설정



컨테이너에서 리소스 요구사항 

CPU와 메모리는 집합적으로 컴퓨팅 리소스 또는 리소스로 부름 

 CPU 및 메모리 는 각각 자원 유형을 지니며 자원 유형에는 기본 단위를 사용 

 리소스 요청 설정 방법 

- spec.containers[].resources.requests.cpu 
- spec.containers[].resources.requests.memory

 리소스 제한 설정 방법 

- spec.containers[].resources.limits.cpu 
- spec.containers[].resources.limits.memory



~~~
apiVersion: v1
kind: Pod
metadata:
  name: front-end
spec:
  containers:
  - name: db
    image: mysql
    env:
    - name: MYSQL_ROOT_PASSWORD
      value: "password"
    resources:
      requests:
        memory: "64Mi"
        cpu: "250m"
      limits:
        memory: "128Mi"
        cpu: "500m"
  - name: wp
    image: wordpress
    resources:
      requests:
        memory: "64Mi"
        cpu: "250m"
      limits:
        memory: "128Mi"
        cpu: "500m"
   
~~~



- strategy 비어있으면 기본 : 롤링업데이트
- deploy-resource.yaml

~~~
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: nginx
  name: nginx
spec:
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx
        ports:
        - containerPort: 80
        resources:
          requests:
            memory: "200Mi"
            cpu: "1m"
          limits:
            memory: "400Mi"
            cpu: "2m
~~~



## 10. 컨테이너 리소스 정책 설정 리미트레인지



- namespaace 마다 설정

![스크린샷 2023-06-30 오전 11.26.57](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-30 오전 11.26.57.png)

~~~
kubectl describe limitrange -n 'namespace'
~~~

![스크린샷 2023-06-30 오전 11.27.32](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-30 오전 11.27.32.png)



![스크린샷 2023-06-30 오전 11.27.48](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-30 오전 11.27.48.png)



## 11. 리미트레인지 컨테이너 실습



~~~
sudo vi /etc/kubernetes/manifests/kube-apiserver.yaml
~~~



  ![스크린샷 2023-06-30 오전 11.34.08](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-30 오전 11.34.08.png)

- 저장하고 시간이 좀 지나야 다시 kubectl을 사용할 수 있다.





- limitrange-demo namespace 를 생성하고
- container별 resource 범위를 지정해보자.

~~~
kubectl create namespcae limitrange-demo
kubectl create -f https://k8s.io/examples/admin/resource/limit-mem-cpu-container.yaml -n limitrange-demo

~~~





~~~
$ kubectl get limitranges -n limitrange-demo
NAME                          CREATED AT
limit-mem-cpu-per-container   2023-06-30T02:47:18Z

$ kubectl describe limitranges -n limitrange-demo
Name:       limit-mem-cpu-per-container
Namespace:  limitrange-demo
Type        Resource  Min   Max   Default Request  Default Limit  Max Limit/Request Ratio
----        --------  ---   ---   ---------------  -------------  -----------------------
Container   cpu       100m  800m  110m             700m           -
Container   memory    99Mi  1Gi   111Mi            900Mi          -
~~~



- limit 범위를 앞에서 만들었기 때문에, pod에 설정이 안되어 있더라도 설정이 강제된다/

~~~
kubectl create -f https://k8s.io/examples/admin/resource/limit-range-pod-1.yaml -n limitrange-demo


~~~





- cpu를 namespace에서 정한 범위를 초과하여 작성하고 생성해보자.

~~~
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: nginx
  name: nginx
  namespace: limitrange-demo
spec:
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx
        ports:
        - containerPort: 80
        resources:
          requests:
            memory: "200Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1"
~~~



- pod을 조회해보면 생성이 안되었다.
- deploy를 조회해 보면 ready 가 안되고 있다.

~~~
$ kubectl get pod -n limitrange-demo
NAME       READY   STATUS    RESTARTS   AGE


$ kubectl get deploy -n limitrange-demo
NAME    READY   UP-TO-DATE   AVAILABLE   AGE
nginx   0/1     0            0           27s
~~~

- deploy 를 살펴보면, replica 에 문제가 있따고 나온다.
- replica를 살펴보면, cpu usage 범위에러에 대한 내용을 명시하고 있다.

~~~
$ kubectl describe -n limitrange-demo
  Normal  ScalingReplicaSet  93s   deployment-controller  Scaled up replica set nginx-5c6b64dd85 to 1
  
$ kubectl describe rs -n limitrange-demo

Warning  FailedCreate  24s (x5 over 62s)  replicaset-controller  (combined from similar events): Error creating: pods "nginx-5c6b64dd85-wgstf" is forbidden: maximum cpu usage per Container is 800m, but limit is 1
~~~





## 12. 네임스페이스별 리소스 총량 제한 방법 리소스쿼타

- 리소스 전체 합을 제한할 수 있다.

- Document: Configure Memory and CPU Quotas for a Namespace





- ResourceQuota를 적용할 namespace 생성

~~~
kubectl create namespace quota-mem-cpu-example
kubectl describe resourcequota -n quota-mem-cpu-example
~~~





- ResourceQuota 내용

```shell
kubectl apply -f https://k8s.io/examples/admin/resource/quota-mem-cpu.yaml --namespace=quota-mem-cpu-example
```

~~~
apiVersion: v1
kind: ResourceQuota
metadata:
  name: mem-cpu-demo
spec:
  hard:
    requests.cpu: "1"
    requests.memory: 1Gi
    limits.cpu: "2"
    limits.memory: 2Gi
~~~



- resourcequota를 생성하면 해당내용을 볼 수 있다.

~~~
$ kubectl describe resourcequotas -n quota-mem-cpu-example
Name:            mem-cpu-demo
Namespace:       quota-mem-cpu-example
Resource         Used  Hard
--------         ----  ----
limits.cpu       0     2
limits.memory    0     2Gi
requests.cpu     0     1
requests.memory  0     1Gi
~~~



- 첫번 째 pod을 실행하고 resourcequotas를 조회하면, 사용한 리소스가 조회된다.
- 두번 째 pod을 실행하면, 남은 resource가 사용하려는 리소스를 초과하여 생성할 수 없다는 메세지를 출력한다.

~~~
$  kubectl apply -f https://k8s.io/examples/admin/resource/quota-mem-cpu-pod.yaml --namespace=quota-mem-cpu-example

pod/quota-mem-cpu-demo created
chchxortnldl@master-1:~$
chchxortnldl@master-1:~$ kubectl describe resourcequotas -n quota-mem-cpu-example
Name:            mem-cpu-demo
Namespace:       quota-mem-cpu-example
Resource         Used   Hard
--------         ----   ----
limits.cpu       800m   2
limits.memory    800Mi  2Gi
requests.cpu     400m   1
requests.memory  600Mi  1Gi

$ kubectl apply -f https://k8s.io/examples/admin/resource/quota-mem-cpu-pod-2.yaml --namespace=quota-mem-cpu-example

Error from server (Forbidden): error when creating "https://k8s.io/examples/admin/resource/quota-mem-cpu-pod-2.yaml": pods "quota-mem-cpu-demo-2" is forbidden: exceeded quota: mem-cpu-demo, requested: requests.memory=700Mi, used: requests.memory=600Mi, limited: requests.memory=1Gi
~~~







## 13. 데몬셋, 노드마다 포드를 하나씩 배치하는 방법



hostpath : 노드에 파일을 공유하는 것

hostpath 와 데몬셋을 사용하는 ㄱ케이스가 많다. 노드 리소스를 관리하기 위해

![스크린샷 2023-06-30 오후 12.18.27](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-30 오후 12.18.27.png)



- replicaset처럼 개수를 지정하지 않아도 노드당 포드 하나씩 자동세팅 된다.

- tolerations : master에도 pod을 배치할 수 있다.

- host관리목적을 위해 master서버에도 설치하는 데몬셋이다.





- $ kubectl get nodes -o json | jq '.items[].spec.taints' 에서 조회된 key 로 작성되어야 한다.

~~~
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: http-go
spec:
  selector:
    matchLabels:
      app: http-go
  template:
    metadata:
      labels:
        app: http-go
    spec:
      tolerations:
      - key: node-role.kubernetes.io/control-plane
        effect: NoSchedule
      containers:
      - name: http-go
        image: gasbugs/http-go
~~~



~~~
chchxortnldl@master-1:~$ kubectl get ds
NAME      DESIRED   CURRENT   READY   UP-TO-DATE   AVAILABLE   NODE SELECTOR   AGE
http-go   3         3         3       3            3           <none>          53s
~~~







## 14. 테인트를 활용한 스케줄링 이해



![스크린샷 2023-06-30 오후 12.43.45](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-30 오후 12.43.45.png)



- Taint 가 있으면 Toleration 옵션이 있어야 노드에 배치가 될 수 있다.



- jq : json query

~~~
kubectl get nodes
kubectl get nodes -o json | jq '.items[].spec.taints'
~~~



~~~
kubectl taint nodes node-1 key1=value1:NoExecute
~~~





## 15. 테인트를 활용한 스케줄링 실습



- sudo apt install jq
- taints 를 모두 조회한다.

~~~
$ kubectl get nodes -o json | jq '.items[].spec.taints'
[
  {
    "effect": "NoSchedule",
    "key": "node-role.kubernetes.io/control-plane"
  }
]
null
~~~

~~~
kubectl taint nodes node-1 key1=value1:NoExecute # 생성
kubectl taint nodes node-1 key1=value1:NoExecute- # 삭제
~~~



- NoExecute : 존재하는 pod는 해당 노드에서 모두 추출되고 스케쥴링되지 않는다.

~~~
$ kubectl taint nodes worker-1 key1=value1:NoExecute
node/worker-1 tainted

$ kubectl get nodes -o json | jq '.items[].spec.taints'
[
  {
    "effect": "NoSchedule",
    "key": "node-role.kubernetes.io/control-plane"
  }
]
[
  {
    "effect": "NoExecute",
    "key": "key1",
    "value": "value1"
  }
]
~~~





- kubectl apply -f no-ds,yaml
  - 노드 하나만 배치됨.

~~~
# no-ds.yaml
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: ns-ds
spec:
  selector:
    matchLabels:
      app: no-ds
  template:
    metadata:
      labels:
        app: no-ds
    spec:
      containers:
      - name: nx
        image: nginx
~~~



~~~
kubectl apply -f no-ds.yaml
kubectl get pod -o wide # worker-2 # 조회됨
kubectl taint nodes worker-2 key1=value1:NoExecute
kubectl get pod -o wide # worker-2 # 추출되어 모두 조회 안됨.
~~~





~~~
# toleration-ds.yaml
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: toleration-ds
spec:
  selector:
    matchLabels:
      app: toleration-ds
  template:
    metadata:
      labels:
        app: toleration-ds
    spec:
      containers:
      - name: nx
        image: nginx
      tolerations:
      - key: "node-role.kubernetes.io/control-plane"
        operator: "Exists"
        effect: "NoSchedule"
      - key: "key1"
        operator: "Equal"
        value: "value1"
        effect: "NoExecute"
~~~



~~~
kubectl apply -f toleration-ds.yaml
kubectl get ds
~~~



- 모두 조회됨.

~~~
$ kubectl get pod
dNAME                  READY   STATUS              RESTARTS   AGE
toleration-ds-cxn74   0/1     ContainerCreating   0          2s
toleration-ds-n969w   1/1     Running             0          41s
toleration-ds-txfz7   1/1     Running             0          7s
~~~







## 16. 수동 스케줄링, 원하는 포드를 원하는 노드에

- 임의의 노드에만 배치될 수 있다면, 그래픽 카드 없는 노드에 gpu 동작을 요하는 프로그램이 배치될 수 있다.

![스크린샷 2023-07-01 오전 11.29.01](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-01 오전 11.29.01.png)



- 원하는 node에 pod을 배치할 수있다.

- pod.yaml

~~~

spec:

  nodeName: work-1
~~~



- node에 label을 설정하고, pod에 nodeSelector 를 설정해서 배치할 수 있다.

~~~
kubectl label node <nodename> gpu=true
kubectl get node --show-labels
~~~



- pod.yaml

~~~
spec:

  nodeSelector:
    gpu: "true"
~~~





~~~
 kubectl label worker-2 gpu="true"
~~~

![스크린샷 2023-07-01 오전 11.41.04](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-01 오전 11.41.04.png)



## 17. 멀티플 스케줄러

- 실습은 하지 않고 개념만

![스크린샷 2023-07-01 오전 11.44.06](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-01 오전 11.44.06.png)



## 18. 오토스케일링 HPA, VPA, CA



- replicaset, deployment 만 설정 가능
-  



## 19. 스케일링 자동화 HPA 실습



- docs > HPA > workthrough > [site](https://kubernetes.io/docs/tasks/run-application/horizontal-pod-autoscale-walkthrough/)



- cpu부하를 주는 연산하는 phpserver pod 생성

```shell
kubectl apply -f https://k8s.io/examples/application/php-apache.yaml
```



- cpu 50%이상일 경우 replica를 늘려가는 autoscale 설정

```shell
kubectl autoscale deployment php-apache --cpu-percent=50 --min=1 --max=10
```



- busybox 이미지가 phpserver를 계속 호출하여 cpu에 부하를 주도록 유도한다.

```shell
kubectl run -i --tty load-generator --rm --image=busybox:1.28 --restart=Never -- /bin/sh -c "while sleep 0.01; do wget -q -O- http://php-apache; done"
```



- cpu 부하가 50보다 높을땐 replica가 늘어간다. 15초마다 리소스모니터링에 의해 측정되고 늘려간다.
- busybox를 중단했을때, cpu사용량이줄고 replica를 줄여간다.
  - 너무 자주 pod 생성/삭제를 하면 그 자체로 부하가 될 수 있기 떼문에 일정부분 유예를 준다.

~~~
chchxortnldl@cloudshell:~ (staek-2023-06-24)$ kubectl get hpa -w
NAME         REFERENCE               TARGETS   MINPODS   MAXPODS   REPLICAS   AGE
php-apache   Deployment/php-apache   1%/50%    1         10        1          2m9s
php-apache   Deployment/php-apache   193%/50%   1         10        1          2m30s
php-apache   Deployment/php-apache   193%/50%   1         10        4          2m45s
php-apache   Deployment/php-apache   229%/50%   1         10        4          3m
php-apache   Deployment/php-apache   229%/50%   1         10        5          3m15s
php-apache   Deployment/php-apache   62%/50%    1         10        5          4m
php-apache   Deployment/php-apache   62%/50%    1         10        7          4m15s
php-apache   Deployment/php-apache   44%/50%    1         10        7          5m
php-apache   Deployment/php-apache   20%/50%    1         10        7          6m
php-apache   Deployment/php-apache   1%/50%     1         10        7          6m30s


^Cchchxortnldl@cloudshell:~ (staek-2023-06-24)$ kubectl get hpa -w
NAME         REFERENCE               TARGETS   MINPODS   MAXPODS   REPLICAS   AGE
php-apache   Deployment/php-apache   1%/50%    1         10        7          8m26s
php-apache   Deployment/php-apache   1%/50%    1         10        7          11m
php-apache   Deployment/php-apache   1%/50%    1         10        3          11m
php-apache   Deployment/php-apache   1%/50%    1         10        3          11m
php-apache   Deployment/php-apache   1%/50%    1         10        1          11m
~~~









































































































