[TOC]





## 0. 예제 image 생성

- http web server 생성 

~~~
# main.go
package main

import (
	"fmt"
	"github.com/julienschmidt/httprouter"
	"net/http"
	"log"
	"os"
)

func Index(w http.ResponseWriter,  r *http.Request, _ httprouter.Params) {
	hostname, err := os.Hostname()
	if err == nil {
		fmt.Fprint(w, "Welcome! " + hostname +  "\n")
	} else {
		fmt.Fprint(w, "Welcome! Error\n")
	}
}

func main() {
	router := httprouter.New()
	router.GET("/", Index)

	log.Fatal(http.ListenAndServe(":8080", router))
}
~~~

- 컴파일 / 실행
- ./main

~~~
$ apt install golang
$ go get github.com/julienschmidt/httprouter
$ go build main.go
$ ./main
$ curl 127.0.0.1:8080
~~~



- make docker file

~~~
FROM golang:1.11
WORKDIR /usr/src/app
COPY main /usr/src/app
CMD ["/usr/src/app/main"]
~~~



- docker build &  push

~~~
$ docker build -t http-go .
$ docker tag http-go seongtaekkim/http-go:1.0
$ docker push seongtaekkim/http-go:1.0
$ docker rmi `docker images -a -q`
$ docker pull seongtaekkim/http-go:1.0
~~~









## 1. 포드 소개



팟은 하나의 노드에서만 실행된다

<img align=left src="./img/sec04-1.png" style="zoom:50%;" />

<img align=left src="./img/sec04-2.png" style="zoom:50%;" />

<img align=left src="./img/sec04-3.png" style="zoom:50%;" />



<img align=left src="./img/sec04-4.png" style="zoom:50%;" />

- 두가지 컨테이너가 밀접한 경우에만 하나의 팟에 넣고 그렇지않으면 팟 하나에는 컨테이너 하나가 좋다.
  - 사이드카, 로그 등






<img align=left src="./img/sec04-5.png" style="zoom:50%;" />





<img align=left src="./img/sec04-6.png" style="zoom:50%;" />









## 2. 포드 디스크립터 작성

~~~
$ kubectl explain pods
~~~



[reference](https://kubernetes.io/docs/concepts/workloads/pods/)

포드하나에 여러 컨테이너를 사용하는건 일반적으로 권장되지 않지만 사용하는 경우가 있다.

- app & log

~~~
kubectl log http-go
kubectl annotate pod http-go test=test
kubectl get pod -o yaml 
~~~



- go-http-pod.yaml

~~~
apiVersion: v1
kind: Pod
metadata:
	name: go-go
spec:
	containers:
	- name: http-go
		image: gasbugs/http-go
		ports:
		- containerPort: 8080
~~~



~~~
   99  vim go-http-pod.yaml 
  100  kubectl create -f go-http-pod.yaml
  101  kubectl get pod http-go
  103  kubectl get pod http-go -o wide
  104  kubectl get pod http-go -o yaml
  105  kubectl port-forward http-go

$ kubectl port-forward http-go 8080:8080
  
$  kubectl describe pod http-go

~~~







chxortnl@master-1:~$ kubectl port-forward http-go 8080:8080
Forwarding from 127.0.0.1:8080 -> 8080
Forwarding from [::1]:8080 -> 8080



- gcp 에서 하고 있다면, 콘솔을 하나 더 열어서 curl 로 확인해보자.

~~~
curl 127.0.0.1:8080
~~~

- 결과가 이렇게 뜨는데 왜이러는지 모르겟음.................

~~~
chxortnl@mater-1:~/yaml$ kubectl port-forward http-go 8888:8888
Forwarding from 127.0.0.1:8888 -> 8888
Forwarding from [::1]:8888 -> 8888
Handling connection for 8888
E0611 00:01:55.340647   14208 portforward.go:409] an error occurred forwarding 8888 -> 8888: error forwarding port 8888 to pod bb88fca13fb1375bef860763d0fc5cb8755e12199ad32d8cf26359fbc3d7c8c5, uid : failed to execute portforward in network namespace "/var/run/netns/cni-9849d087-9a2f-3788-7f3c-a87fa6fd8c56": failed to connect to localhost:8888 inside namespace "bb88fca13fb1375bef860763d0fc5cb8755e12199ad32d8cf26359fbc3d7c8c5", IPv4: dial tcp4 127.0.0.1:8888: connect: connection refused IPv6 dial tcp6: address localhost: no suitable address found 
error: lost connection to pod
~~~





~~~~
kubectl explain pods
~~~~



~~~
kubectl delete -f go-http-pod.yaml
~~~



~~~
kubectl logs http-go
kubectl annotate pod http-go test1234=test1234 # 필요한 레이블을 작성할 수 있다.
kubectl get pod -o yaml # annotaions가 추가 되어 있음을 알수 있다.
kubectl delete pod http-go # 원하는 팟을 삭제할 수 있다.
kubectl delete pod --all # 모든 팟을 삭제할 수 있다.
~~~









## 3. 연습문제



![스크린샷 2023-04-11 오후 10.51.09](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-11 오후 10.51.09.png)





- jenkins-manual.yaml 생성

~~~
apiVersion: v1
kind: Pod
metadata:
	name: jenkins-manual
spec:
	containers:
	- name: jenkins
		image: jenkins/jenkins
		ports:
		- containerPort: 8080
~~~







~~~
 97  vim go-http-pod.yaml 
   98  kubectl create -f go-http-pod.yaml
   99  vim go-http-pod.yaml 
  100  kubectl create -f go-http-pod.yaml
  101  kubectl get pod http-go
  102  kubectl et pod http-go -o wide
  103  kubectl get pod http-go -o wide
  104  kubectl get pod http-go -o yaml
  105  kubectl port-forward http-go
  106  kubectl port-forward http-go 8080:8080
  107  history
  108  kubectl delete pod -all
  109  kubectl delete pod --all
  110  kubectl get nodes
  111  kubectl logs http-go
  112  docker search jenkins
  113  ls
  114  touch jenkins.yaml
  115  vi jenkins.yaml 
  116  curl 127.0.0.1:8080
  117  kubectl explain pods
  118  kubectl et pod jenkins
  119  kubectl get pod jenkins
  120  kubectl get pod
  121  kubectl get pod -w
  122  kubectl exec jenkins -- curl 127.0.0.1:8080
  123  kubectl exec jenkins -- curl 127.0.0.1:8080 -s
  124  kubectl create -f jenkins
  125  kubectl create -f jenkins.yaml 
  126  kubectl remove -f jenkins.yaml 
  127  kubectl remove jenkins
  128  kubectl delete jenkins
  129  kubectl describe pod jenkin
  130  kubectl describe pod jenkins
  131  vi jenkins.yaml 
  132  mv jenkins.yaml jenkins-manual.yaml 
  133  kubectl create -f jenkins-manual.yaml 
  134  kubectl describe pod jenkins-manual.yaml 
  135  kubectl et pod
  136  kubectl get pod
  137  kubectl describe pod jenkins-manual.yaml 
  138  kubectl describe pod jenkins-manual
  139  kubectl get pod jenkins-manual -o jyml
  140  kubectl get pod jenkins-manual -o yaml
  141  kubectl logs jenkins-manual
  142  kubectl exec jenkins-manual -- curl 127.0.0.1 -s
  143  kubectl exec jenkins-manual -- curl 127.0.0.1:8080 -s
  144  kubectl port-forward jenkins-manual 8888:8080
  145  history
~~~









## 4. 라이브네스, 레디네스, 스타트업 프로브 소개

[라이브네스](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/)



![스크린샷 2023-04-12 오전 7.01.16](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-12 오전 7.01.16.png)

- pod의 보조역할을 한다.





![스크린샷 2023-04-12 오전 7.02.07](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-12 오전 7.02.07.png)

- livenessProbe 가 5초마다 command 를 실행해서 검사한다.







![스크린샷 2023-04-12 오전 7.03.34](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-12 오전 7.03.34.png)





![스크린샷 2023-04-12 오전 7.05.07](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-12 오전 7.05.07.png)





 ![스크린샷 2023-04-12 오전 7.05.57](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-12 오전 7.05.57.png)









## 5. 라이브네스와 레디네스 프로브 실습

### Define a liveness command

- liveness-exec.yaml

~~~
apiVersion: v1
kind: Pod
metadata:
  labels:
    test: liveness
  name: liveness-exec
spec:
  containers:
  - name: liveness
    image: registry.k8s.io/busybox
    args:
    - /bin/sh
    - -c
    - touch /tmp/healthy; sleep 30; rm -f /tmp/healthy; sleep 600
    livenessProbe:
      exec:
        command:
        - cat
        - /tmp/healthy
      initialDelaySeconds: 5
      periodSeconds: 5
~~~

- 30초 이후 파일을 지운다.
- livenessProbe에서 파일을 5초간격으로 cat하는데, 없으면 프로세스를 죽인다. livene는 잠시 후 재실행된다.



- describe
  - 30초 후 liveness probe 실패 가 되고, 컨테이너를 재시작 하였다.


~~~
$ kubectl describe pod liveness-exec

Events:
  Type     Reason     Age                 From               Message
  ----     ------     ----                ----               -------
  Normal   Scheduled  104s                default-scheduler  Successfully assigned default/liveness-exec to worker-2
  Normal   Pulled     102s                kubelet            Successfully pulled image "registry.k8s.io/busybox" in 738.049163ms (738.070982ms including waiting)
  Warning  Unhealthy  58s (x3 over 68s)   kubelet            Liveness probe failed: cat: can't open '/tmp/healthy': No such file or directory
  Normal   Killing    58s                 kubelet            Container liveness failed liveness probe, will be restarted
  Normal   Pulling    28s (x2 over 102s)  kubelet            Pulling image "registry.k8s.io/busybox"
  Normal   Created    28s (x2 over 102s)  kubelet            Created container liveness
  Normal   Pulled     28s                 kubelet            Successfully pulled image "registry.k8s.io/busybox" in 208.668909ms (208.703789ms including waiting)
  Normal   Started    27s (x2 over 101s)  kubelet            Started container liveness
~~~

- 재시작 1회를 확인할 수 있다.

~~~
chxortnl@mater-1:~/yaml$ kubectl get pod
NAME                  READY   STATUS    RESTARTS     AGE
jenkins-manual        1/1     Running   0            16m
liveness-exec         1/1     Running   1 (3s ago)   79s
tc-857c8578b4-69fqg   1/1     Running   0            20m
tc-857c8578b4-jwvjm   1/1     Running   0            22m
tc-857c8578b4-lfrzn   1/1     Running   0            22m
tc-857c8578b4-qhbq9   1/1     Running   0            22m
tc-857c8578b4-zmm6t   1/1     Running   0            22m
~~~







### Define a liveness HTTP request

- http-liveness.yaml 

~~~
apiVersion: v1
kind: Pod
metadata:
  labels:
    test: liveness
  name: liveness-http
spec:
  containers:
  - name: liveness
    image: k8s.gcr.io/liveness
    args:
    - /server
    livenessProbe:
      httpGet:
        path: /healthz
        port: 8080
        httpHeaders:
        - name: Custom-Header
          value: Awesome
      initialDelaySeconds: 3
      periodSeconds: 3
~~~





~~~
kubectl create -f http-liveness.yaml # pod을 실행한다.
kubectl get pod # pod 상태를 조회한다/
kubectl get pod -w # pod의 상태를 계속 조회한다.

~~~



- `kubectl describe liveness-http`
  - 10초 이후 500에러가 발생하면, unhealthy 가발생하고, 다시 실행하고 를 반복한다.
  - start-up probe 를 사용하면 해결될 문제이다.

~~~
Events:
  Type     Reason     Age                From               Message
  ----     ------     ----               ----               -------
  Normal   Scheduled  54s                default-scheduler  Successfully assigned default/liveness-http to worker-1
  Normal   Pulled     53s                kubelet            Successfully pulled image "registry.k8s.io/liveness" in 470.274281ms (470.308131ms including waiting)
  Normal   Pulled     36s                kubelet            Successfully pulled image "registry.k8s.io/liveness" in 131.511768ms (131.559613ms including waiting)
  Normal   Pulling    18s (x3 over 54s)  kubelet            Pulling image "registry.k8s.io/liveness"
  Normal   Created    18s (x3 over 53s)  kubelet            Created container liveness
  Normal   Started    18s (x3 over 53s)  kubelet            Started container liveness
  Normal   Killing    18s (x2 over 36s)  kubelet            Container liveness failed liveness probe, will be restarted
  Normal   Pulled     18s                kubelet            Successfully pulled image "registry.k8s.io/liveness" in 107.864781ms (107.873491ms including waiting)
  Warning  Unhealthy  3s (x8 over 42s)   kubelet            Liveness probe failed: HTTP probe failed with statuscode: 500
~~~





### Define a TCP liveness probe



- tcp-liveness-readiness.yaml
  - `kubectl apply -f https://k8s.io/examples/pods/probe/tcp-liveness-readiness.yaml`
    - apply 명령어로 yaml을 실행할 수 있다.

~~~
apiVersion: v1
kind: Pod
metadata
  name: goproxy
  labels:
    app: goproxy
spec:
  containers:
  - name: goproxy
    image: registry.k8s.io/goproxy:0.1
    ports:
    - containerPort: 8080
    readinessProbe:
      tcpSocket:
        port: 8080
      initialDelaySeconds: 5
      periodSeconds: 10
    livenessProbe:
      tcpSocket:
        port: 8080
      initialDelaySeconds: 15
      periodSeconds: 20
~~~





- `kubectl describe pod goproxy`
  - goproxy pod의 실행 이력을 조회한다.

~~~
Events:
  Type    Reason     Age   From               Message
  ----    ------     ----  ----               -------
  Normal  Scheduled  38s   default-scheduler  Successfully assigned default/goproxy to worker-1
  Normal  Pulling    37s   kubelet            Pulling image "registry.k8s.io/goproxy:0.1"
  Normal  Pulled     36s   kubelet            Successfully pulled image "registry.k8s.io/goproxy:0.1" in 746.286323ms (746.297583ms including waiting)
  Normal  Created    36s   kubelet            Created container goproxy
  Normal  Started    36s   kubelet            Started container goproxy
~~~







## 6. **레이블과 셀렉터 소개**



![스크린샷 2023-04-13 오후 10.48.54](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-13 오후 10.48.54.png)





![스크린샷 2023-04-13 오후 10.49.15](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-13 오후 10.49.15.png)





![스크린샷 2023-04-13 오후 10.49.41](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-13 오후 10.49.41.png)

![스크린샷 2023-04-13 오후 10.49.49](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-13 오후 10.49.49.png)



![스크린샷 2023-04-13 오후 10.50.09](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-13 오후 10.50.09.png)



![스크린샷 2023-04-13 오후 10.50.26](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-13 오후 10.50.26.png)

![스크린샷 2023-04-13 오후 10.50.41](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-13 오후 10.50.41.png)



![스크린샷 2023-04-13 오후 10.50.52](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-13 오후 10.50.52.png)





## 7. 레이블 추가, 생성, 삭제, 필터링 실습



~~~
kubectl delete all --all # 모든 리소스 삭제
~~~



- http-go-pod-v2.yaml

~~~
apiVersion: v1
kind: Pod
metadata:
  name: http-go
  labels:
    creation_method: manual
    env: prod
spec:
  containers:
  - name: http-go
    image: gasbugs/http-go
    ports:
    - containerPort: 8080
      protocol: TCP
~~~





~~~
chxortnl@master-1:~$ kubectl get pod
NAME      READY   STATUS    RESTARTS   AGE
http-go   1/1     Running   0          4s
chxortnl@master-1:~$ kubectl get pod --show-labels # 모든 레이블을 보여준다.
NAME      READY   STATUS    RESTARTS   AGE   LABELS
http-go   1/1     Running   0          13s   creation_method=manual,env=prod
chxortnl@master-1:~$ cp http-go-pod-v2.yaml http-go-pod-v3.yaml 
chxortnl@master-1:~$ vi http-go-pod-v3.yaml 
chxortnl@master-1:~$ kubectl create -f http-go-pod-v3.yaml 
pod/http-go-v3 created
chxortnl@master-1:~$ kubectl get pod --show-labels
NAME         READY   STATUS    RESTARTS   AGE   LABELS
http-go      1/1     Running   0          96s   creation_method=manual,env=prod
http-go-v3   1/1     Running   0          10s   creation_method=manual-v3
chxortnl@master-1:~$ kubectl get pod -L env # env 열이 추가되어 조회된다.
NAME         READY   STATUS    RESTARTS   AGE    ENV
http-go      1/1     Running   0          2m1s   prod
http-go-v3   1/1     Running   0          35s    
chxortnl@master-1:~$ kubectl get pod -L creation_method
NAME         READY   STATUS    RESTARTS   AGE     CREATION_METHOD
http-go      1/1     Running   0          2m13s   manual
http-go-v3   1/1     Running   0          47s     manual-v3
chxortnl@master-1:~$ kubectl label pod http-go test=foo # test 레이블을 추가한다.
pod/http-go labeled
chxortnl@master-1:~$ kubectl get pod --show-labels
NAME         READY   STATUS    RESTARTS   AGE     LABELS
http-go      1/1     Running   0          2m55s   creation_method=manual,env=prod,test=foo
http-go-v3   1/1     Running   0          89s     creation_method=manual-v3
chxortnl@master-1:~$ kubectl label pod http-go test=foo
pod/http-go not labeled
chxortnl@master-1:~$ kubectl label pod http-go test=foo1
error: 'test' already has a value (foo), and --overwrite is false
chxortnl@master-1:~$ kubectl label pod http-go test=foo1 --overwrite # 이미 있는 레이블을 수정할 수 있다.
pod/http-go labeled
chxortnl@master-1:~$ kubectl get pod --show-labels
NAME         READY   STATUS    RESTARTS   AGE     LABELS
http-go      1/1     Running   0          3m32s   creation_method=manual,env=prod,test=foo1
http-go-v3   1/1     Running   0          2m6s    creation_method=manual-v3
chxortnl@master-1:~$ kubectl label pod http-go test- # 레이블을 삭제한다.
pod/http-go unlabeled
chxortnl@master-1:~$ kubectl get pod --show-labels
NAME         READY   STATUS    RESTARTS   AGE     LABELS
http-go      1/1     Running   0          3m57s   creation_method=manual,env=prod
http-go-v3   1/1     Running   0          2m31s   creation_method=manual-v3
~~~





~~~
$ kubectl get pod -l env # -l : env 레이블이 존재하는 pod 만 조회
NAME      READY   STATUS    RESTARTS   AGE
http-go   1/1     Running   0          4m26s

$ kubectl get pod -l '!env' # env 이외 만 조회
NAME         READY   STATUS    RESTARTS   AGE
http-go-v3   1/1     Running   0          3m9s

$ kubectl get pod -l 'env=test' # env 값지정 조회 
No resources found in default namespace.

$ kubectl get pod -l 'env=prod' # env 값지정 조회 
NAME      READY   STATUS    RESTARTS   AGE
http-go   1/1     Running   0          4m57s

$ kubectl get pod -l 'env=prod,creation_method=manual' # 여러조건 지정 후 조회
NAME      READY   STATUS    RESTARTS   AGE
http-go   1/1     Running   0          5m13s
~~~









## 8.**레이블과 셀렉터 연습문제**



![스크린샷 2023-04-13 오후 11.02.46](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-13 오후 11.02.46.png)



- http-go-pod-v4.yaml 

~~~
apiVersion: v1
kind: Pod
metadata:
  name: nginx
  labels:
    app: nginx
spec:
  containers:
  - name: nginx
    image: nginx
    ports:
    - containerPort: 80
      protocol: TCP
~~~



~~~
kubectl get pod -w # 진행상태를 계속 확인
~~~



~~~
sudo kubectl port-forward nginx 80:80
~~~

chxortnl@master-1:~$ sudo kubectl port-forward nginx 9991:80
E0413 14:16:39.358400   70952 memcache.go:265] couldn't get current server API group list: Get "http://localhost:8080/api?timeout=32s": dial tcp 127.0.0.1:8080: connect: connection refused
E0413 14:16:39.358695   70952 memcache.go:265] couldn't get current server API group list: Get "http://localhost:8080/api?timeout=32s": dial tcp 127.0.0.1:8080: connect: connection refused
E0413 14:16:39.360168   70952 memcache.go:265] couldn't get current server API group list: Get "http://localhost:8080/api?timeout=32s": dial tcp 127.0.0.1:8080: connect: connection refused
E0413 14:16:39.361532   70952 memcache.go:265] couldn't get current server API group list: Get "http://localhost:8080/api?timeout=32s": dial tcp 127.0.0.1:8080: connect: connection refused
The connection to the server localhost:8080 was refused - did you specify the right host or port?

# 왜 이런게 뜨는걸까????????? 미해결



- 레이블 조회,추가, 셀렉터

~~~
$ kubectl create -f http-go-pod-v4.yaml 
pod/http-go-v4 created

$ kubectl get pod --show-labels
NAME         READY   STATUS    RESTARTS   AGE   LABELS
http-go-v4   1/1     Running   0          13s   app=nginx

$ kubectl get pod -L app
NAME         READY   STATUS    RESTARTS   AGE   APP
http-go-v4   1/1     Running   0          81s   nginx

$ kubectl label pod http-go-v4 team=dev1
pod/http-go-v4 labeled

$ kubectl get pod --show-labels
NAME         READY   STATUS    RESTARTS   AGE    LABELS
http-go-v4   1/1     Running   0          118s   app=nginx,team=dev1
~~~





## 9. **레플리케이션 컨트롤러와 레플리카셋**



![스크린샷 2023-04-13 오후 11.23.36](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-13 오후 11.23.36.png)

- 레플리케이션컨트롤러 : 레플리카셋의 구버전, 거의 다른게 없다.



'![스크린샷 2023-04-13 오후 11.24.03](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-13 오후 11.24.03.png)





![스크린샷 2023-04-13 오후 11.24.20](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-13 오후 11.24.20.png)





![스크린샷 2023-04-13 오후 11.24.42](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-13 오후 11.24.42.png)





![스크린샷 2023-04-13 오후 11.24.56](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-13 오후 11.24.56.png)



![스크린샷 2023-04-13 오후 11.25.20](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-13 오후 11.25.20.png)



![스크린샷 2023-04-13 오후 11.25.43](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-13 오후 11.25.43.png)



![스크린샷 2023-04-13 오후 11.26.19](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-13 오후 11.26.19.png)



![스크린샷 2023-04-13 오후 11.26.50](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-13 오후 11.26.50.png)







###  [reference](https://kubernetes.io/docs/concepts/workloads/controllers/replicationcontroller/)



- http-go-rc.yaml

~~~
apiVersion: v1
kind: ReplicationController
metadata:
  name: http-go
spec:
  replicas: 3
  selector:
    app: http-go
  template:
    metadata:
      name: http-go
      labels:
        app: http-go
    spec:
      containers:
      - name: http-go
        image: gasbugs/http-go
        ports:
        - containerPort: 8080
~~~







~~~
Last login: Thu Apr 13 13:29:10 2023 from 35.235.240.144
chxortnl@master-1:~$ kubectl get nodes;
NAME       STATUS   ROLES           AGE     VERSION
master-1   Ready    control-plane   3d15h   v1.26.3
worker-1   Ready    <none>          3d14h   v1.26.3
worker-2   Ready    <none>          3d14h   v1.26.3
chxortnl@master-1:~$ vi http-go-rc.yaml
chxortnl@master-1:~$ kubectl create -f http-go-rc.yaml 
replicationcontroller/http-go created
chxortnl@master-1:~$ kubectl get pod
NAME            READY   STATUS         RESTARTS   AGE
http-go-4d2bw   0/1     ErrImagePull   0          6s
http-go-jlvsq   0/1     ErrImagePull   0          6s
http-go-k2xqr   0/1     ErrImagePull   0          6s
nginx           1/1     Running        0          11h
chxortnl@master-1:~$ kubectl get rc
NAME      DESIRED   CURRENT   READY   AGE
http-go   3         3         0       19s
chxortnl@master-1:~$ kubectl delete pod http-go-4d2bw
pod "http-go-4d2bw" deleted
chxortnl@master-1:~$ kubectl get pod
NAME            READY   STATUS             RESTARTS   AGE
http-go-jlvsq   0/1     ImagePullBackOff   0          45s
http-go-k2xqr   0/1     ImagePullBackOff   0          45s
http-go-zsjgs   0/1     ErrImagePull       0          11s
nginx           1/1     Running            0          11h
chxortnl@master-1:~$ kubectl get pod --show-labels
NAME            READY   STATUS             RESTARTS   AGE   LABELS
http-go-jlvsq   0/1     ImagePullBackOff   0          83s   app=http-go
http-go-k2xqr   0/1     ImagePullBackOff   0          83s   app=http-go
http-go-zsjgs   0/1     ImagePullBackOff   0          49s   app=http-go
nginx           1/1     Running            0          11h   app=nginx
chxortnl@master-1:~$ kubectl label pod http-go-zsjgs app-
pod/http-go-zsjgs unlabeled
chxortnl@master-1:~$ kubectl get pod --show-labels
NAME            READY   STATUS             RESTARTS   AGE    LABELS
http-go-jlvsq   0/1     ErrImagePull       0          107s   app=http-go
http-go-k2xqr   0/1     ErrImagePull       0          107s   app=http-go
http-go-nzmk2   0/1     ErrImagePull       0          4s     app=http-go
http-go-zsjgs   0/1     ImagePullBackOff   0          73s    <none>
nginx           1/1     Running            0          11h    app=nginx
chxortnl@master-1:~$ kubectl get pod
NAME            READY   STATUS             RESTARTS   AGE
http-go-jlvsq   0/1     ImagePullBackOff   0          2m6s
http-go-k2xqr   0/1     ImagePullBackOff   0          2m6s
http-go-nzmk2   0/1     ImagePullBackOff   0          23s
http-go-zsjgs   0/1     ImagePullBackOff   0          92s
nginx           1/1     Running            0          11h
chxortnl@master-1:~$ kubectl get pod -o wide
NAME            READY   STATUS             RESTARTS   AGE     IP           NODE       NOMINATED NODE   READINESS GATES
http-go-jlvsq   0/1     ImagePullBackOff   0          2m33s   10.0.1.89    worker-1   <none>           <none>
http-go-k2xqr   0/1     ImagePullBackOff   0          2m33s   10.0.1.155   worker-1   <none>           <none>
http-go-nzmk2   0/1     ImagePullBackOff   0          50s     10.0.2.100   worker-2   <none>           <none>
http-go-zsjgs   0/1     ImagePullBackOff   0          119s    10.0.2.158   worker-2   <none>           <none>
nginx           1/1     Running            0          11h     10.0.1.17    worker-1   <none>           <none>
chxortnl@master-1:~$ kubectl get pod -w
NAME            READY   STATUS             RESTARTS   AGE
http-go-jlvsq   0/1     ImagePullBackOff   0          2m47s
http-go-k2xqr   0/1     ImagePullBackOff   0          2m47s
http-go-nzmk2   0/1     ErrImagePull       0          64s
http-go-zsjgs   0/1     ImagePullBackOff   0          2m13s
nginx           1/1     Running            0          11h
http-go-nzmk2   0/1     ImagePullBackOff   0          68s
http-go-k2xqr   0/1     ErrImagePull       0          3m2s
~~~





- **노드가 끊겼을 경우 rc 기능**

work0-2 네트워크를 끊고 다시 확인한다

끊겼을 때 러닝상태로 있는데 5분간 유예기간이 주어진다. (네트워크 이슈가 있을 수 있으므로)

5분이후 Terminating 되는데, 통신이 불가능하기 때문에 준비단계만 진행하고 ,work1에서 running 된다.

이후 네트워크를 복구하면 실제로 해당 rc의 터미네이팅이 진행된다.

work2네트워크를 다시 연결하고확인한다





- 기타 명령어

~~~
kubectl get rc http-go
kubectl get rc http-go -o wide
kubectl get pod -o wide # pod이 어떤 node에서 실행되는지 확인 가능
kubectl delete rc http-go
~~~





### replication controller scale edit

- scale 명령어로 레플리카 개수 수정

~~~
$ kubectl scale rc http-go --replicas=5
$ kubectl get pod
~~~

- 파일에 직접 접근해서 수정

~~~
$ kubectl edit rc http-go 
$ kubectl get pod
~~~

- 파일을 만들어서 apply 명령어로 레플리카 개수 변경

~~~
$ cp http-go-rc.yaml http-go-rc-v2.yaml 
$ vi http-go-rc-v2.yaml # 레플리카 개수 변경
$ kubectl apply -f http-go-rc-v2.yaml 
$ kubectl get pod
~~~







## 10. 레플리카셋 개요와 연습문제



![스크린샷 2023-04-14 오후 11.04.46](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-14 오후 11.04.46.png)



![스크린샷 2023-04-14 오후 11.05.12](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-14 오후 11.05.12.png)



![스크린샷 2023-04-14 오후 11.05.34](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-14 오후 11.05.34.png)

- 레플리케이션은 rel 값이 다른 팟을 한번에 선택할 수 없다.



![스크린샷 2023-04-14 오후 11.05.57](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-14 오후 11.05.57.png)



![스크린샷 2023-04-14 오후 11.06.17](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-14 오후 11.06.17.png)

![스크린샷 2023-04-14 오후 11.06.25](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-14 오후 11.06.25.png)





### 연습문제

- nginx3개 생성하는 rs-nginx 레플리카셋 생성
- rs-nginx 포드의 개수를 10개로 스케일링해라

### [replicaSet reference](https://kubernetes.io/docs/concepts/workloads/controllers/replicaset/)

- nginx-rs.yaml

~~~
apiVersion: apps/v1
kind: ReplicaSet
metadata:
  name: rs-nginx
spec:
  # modify replicas according to your case
  replicas: 3
  selector:
    matchLabels:
      app: frontend
  template:
    metadata:
      labels:
        app: frontend
    spec:
      containers:
      - name: nginx
        image: nginx
        ports:
          - containerPort: 80 

~~~

- matchLabels app 과 tempate > metadata > labels > app 값은 같아야 동작한다.





- replicaset 실행
- pod, rs 조회
- edit 명령어로 replica 개수 변경 후 적용내용 재 조회

~~~
$ kubectl create -f nginx-rs.yaml 
replicaset.apps/rs-nginx created

$ kubectl get pod
NAME             READY   STATUS              RESTARTS   AGE
rs-nginx-72qgc   0/1     ContainerCreating   0          4s
rs-nginx-gzwxl   1/1     Running             0          4s
rs-nginx-sjpcc   1/1     Running             0          4s

$ kubectl get rs
NAME       DESIRED   CURRENT   READY   AGE
rs-nginx   3         3         3       10s

$ kubectl edit rs rs-nginx # object를 직접수정하면, 즉시 설정변경이 적용된다. 
											  # spec > replica를 10으로 수정한 후 결과를 확인해보자.
replicaset.apps/rs-nginx edited

$ kubectl get rs
NAME       DESIRED   CURRENT   READY   AGE
rs-nginx   10        10        10      75s

$ kubectl get pod
NAME             READY   STATUS    RESTARTS   AGE
rs-nginx-5mzm6   1/1     Running   0          8s
rs-nginx-72qgc   1/1     Running   0          78s
rs-nginx-gzwxl   1/1     Running   0          78s
rs-nginx-h5tks   1/1     Running   0          8s
rs-nginx-kt5mh   1/1     Running   0          8s
rs-nginx-l9zvf   1/1     Running   0          8s
rs-nginx-pmr4x   1/1     Running   0          8s
rs-nginx-qb7r9   1/1     Running   0          8s
rs-nginx-sjpcc   1/1     Running   0          78s
rs-nginx-vhpc5   1/1     Running   0          8s

$ kubectl get pod -o wide
NAME             READY   STATUS    RESTARTS   AGE   IP           NODE       NOMINATED NODE   READINESS GATES
rs-nginx-5mzm6   1/1     Running   0          20s   10.0.2.47    worker-2   <none>           <none>
rs-nginx-72qgc   1/1     Running   0          90s   10.0.2.39    worker-2   <none>           <none>
rs-nginx-gzwxl   1/1     Running   0          90s   10.0.1.221   worker-1   <none>           <none>
rs-nginx-h5tks   1/1     Running   0          20s   10.0.1.131   worker-1   <none>           <none>
rs-nginx-kt5mh   1/1     Running   0          20s   10.0.2.110   worker-2   <none>           <none>
rs-nginx-l9zvf   1/1     Running   0          20s   10.0.1.130   worker-1   <none>           <none>
rs-nginx-pmr4x   1/1     Running   0          20s   10.0.1.215   worker-1   <none>           <none>
rs-nginx-qb7r9   1/1     Running   0          20s   10.0.2.225   worker-2   <none>           <none>
rs-nginx-sjpcc   1/1     Running   0          90s   10.0.1.60    worker-1   <none>           <none>
rs-nginx-vhpc5   1/1     Running   0          20s   10.0.2.53    worker-2   <none>           <none>
~~~





## 11. 디플로이먼트 소개와 연습문제

- 레플리카셋을 다룰수 있는 기능

![스크린샷 2023-04-14 오후 11.45.28](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-14 오후 11.45.28.png)





![스크린샷 2023-04-14 오후 11.45.36](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-14 오후 11.45.36.png)



![스크린샷 2023-04-14 오후 11.45.48](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-14 오후 11.45.48.png)



### 연습문제

- jenkins deployment 생성
- jenkins deployment 로 배포되는 앱을 app: jenkins-test 로 레이블링 해라
- 포드 하나를 삭제하고 이후 생성되는 포드를 관찰해라
- 새로 생성된 포드 레이블을 바꾸어 deployment 관리영역에서 벗어나게 해라
- scale 명령을 사용해 레플리카 수를 5개로 정의
- edit 기능을 사용해서 10으로 스케일링 해라



### [deployment refer](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/)

- deploy-jenkins.yaml

~~~
apiVersion: apps/v1
kind: Deployment
metadata:
  name: deploy-jenkins
  labels:
    app: jenkins-test
spec:
  replicas: 3
  selector:
    matchLabels:
      app: jenkins-test
  template:
    metadata:
      labels:
        app: jenkins-test
    spec:
      containers:
      - name: jenkins
        image: jenkins/jenkins
        ports:
        - containerPort: 8080
~~~



> - deploy 생성
> - delete 명령어로 pod 1개 하고, 다시 생성되는 지 확인
> - label 하나 삭제하고, 다시 생성되는 지 확인 (레이블삭제 된 팟은 그대로 살아 있음.)
> - scale 명령어로 replicas 개수 5개로 변경
> - edit 명령어로 replicas 개수 10개로 변경

~~~
$ kubectl create -f deploy-jenkins.yaml 
$ kubectl get all
$ kubectl delete pod deploy-jenkins-68d885ccbb-5x6b2
$ kubectl get pod
$ kubectl describe rs
$ kubectl get pod
$ kubectl label pod deploy-jenkins-68d885ccbb-dm8pk app-
$ kubectl get pod
$ kubectl scale deploy deploy-jenkins --replicas=5
$ kubectl get pod
$ kubectl edit deploy deploy-jenkins
$ kubectl get pod
$ kubectl get pod -l app
~~~



- 결과

~~~
$ kubectl create -f deploy-jenkins.yaml 
deployment.apps/deploy-jenkins created

$ kubectl get all
NAME                                  READY   STATUS              RESTARTS   AGE
pod/deploy-jenkins-68d885ccbb-5x6b2   0/1     ContainerCreating   0          8s
pod/deploy-jenkins-68d885ccbb-kfjxb   0/1     ContainerCreating   0          8s
pod/deploy-jenkins-68d885ccbb-t2sf7   0/1     ContainerCreating   0          8s

NAME                 TYPE        CLUSTER-IP   EXTERNAL-IP   PORT(S)   AGE
service/kubernetes   ClusterIP   10.96.0.1    <none>        443/TCP   32s

NAME                             READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/deploy-jenkins   0/3     3            0           8s

NAME                                        DESIRED   CURRENT   READY   AGE
replicaset.apps/deploy-jenkins-68d885ccbb   3         3         0       8s


$ kubectl delete pod deploy-jenkins-68d885ccbb-5x6b2
pod "deploy-jenkins-68d885ccbb-5x6b2" deleted

$ kubectl get pod
NAME                              READY   STATUS    RESTARTS   AGE
deploy-jenkins-68d885ccbb-dm8pk   1/1     Running   0          14s
deploy-jenkins-68d885ccbb-kfjxb   1/1     Running   0          100s
deploy-jenkins-68d885ccbb-t2sf7   1/1     Running   0          100s

$ kubectl describe rs
Name:           deploy-jenkins-68d885ccbb
Namespace:      default
Selector:       app=jenkins-test,pod-template-hash=68d885ccbb
Labels:         app=jenkins-test
                pod-template-hash=68d885ccbb
Annotations:    deployment.kubernetes.io/desired-replicas: 3
                deployment.kubernetes.io/max-replicas: 4
                deployment.kubernetes.io/revision: 1
Controlled By:  Deployment/deploy-jenkins
Replicas:       3 current / 3 desired
Pods Status:    3 Running / 0 Waiting / 0 Succeeded / 0 Failed
Pod Template:
  Labels:  app=jenkins-test
           pod-template-hash=68d885ccbb
  Containers:
   jenkins:
    Image:        jenkins/jenkins
    Port:         8080/TCP
    Host Port:    0/TCP
    Environment:  <none>
    Mounts:       <none>
  Volumes:        <none>
Events:
  Type    Reason            Age   From                   Message
  ----    ------            ----  ----                   -------
  Normal  SuccessfulCreate  112s  replicaset-controller  Created pod: deploy-jenkins-68d885ccbb-kfjxb
  Normal  SuccessfulCreate  112s  replicaset-controller  Created pod: deploy-jenkins-68d885ccbb-t2sf7
  Normal  SuccessfulCreate  112s  replicaset-controller  Created pod: deploy-jenkins-68d885ccbb-5x6b2
  Normal  SuccessfulCreate  26s   replicaset-controller  Created pod: deploy-jenkins-68d885ccbb-dm8pk

$ kubectl get pod
NAME                              READY   STATUS    RESTARTS   AGE
deploy-jenkins-68d885ccbb-dm8pk   1/1     Running   0          38s
deploy-jenkins-68d885ccbb-kfjxb   1/1     Running   0          2m4s
deploy-jenkins-68d885ccbb-t2sf7   1/1     Running   0          2m4s

$ kubectl label pod deploy-jenkins-68d885ccbb-dm8pk app-
pod/deploy-jenkins-68d885ccbb-dm8pk unlabeled

$ kubectl get pod
NAME                              READY   STATUS    RESTARTS   AGE
deploy-jenkins-68d885ccbb-dm8pk   1/1     Running   0          93s
deploy-jenkins-68d885ccbb-kfjxb   1/1     Running   0          2m59s
deploy-jenkins-68d885ccbb-t2sf7   1/1     Running   0          2m59s
deploy-jenkins-68d885ccbb-w2q2x   1/1     Running   0          6s


$ kubectl scale deploy deploy-jenkins --replicas=5
deployment.apps/deploy-jenkins scaled

$ kubectl get pod
NAME                              READY   STATUS    RESTARTS   AGE
deploy-jenkins-68d885ccbb-2qplm   1/1     Running   0          4s
deploy-jenkins-68d885ccbb-dm8pk   1/1     Running   0          2m16s
deploy-jenkins-68d885ccbb-kfjxb   1/1     Running   0          3m42s
deploy-jenkins-68d885ccbb-t2sf7   1/1     Running   0          3m42s
deploy-jenkins-68d885ccbb-w2q2x   1/1     Running   0          49s
deploy-jenkins-68d885ccbb-xp8fn   1/1     Running   0          4s

$ kubectl edit deploy deploy-jenkins
deployment.apps/deploy-jenkins edited

$ kubectl get pod
NAME                              READY   STATUS    RESTARTS   AGE
deploy-jenkins-68d885ccbb-2qplm   1/1     Running   0          54s
deploy-jenkins-68d885ccbb-6jssb   0/1     Pending   0          9s
deploy-jenkins-68d885ccbb-9mtc5   0/1     Pending   0          9s
deploy-jenkins-68d885ccbb-c9787   0/1     Pending   0          9s
deploy-jenkins-68d885ccbb-dm8pk   1/1     Running   0          3m6s
deploy-jenkins-68d885ccbb-jkzgl   0/1     Pending   0          9s
deploy-jenkins-68d885ccbb-kfjxb   1/1     Running   0          4m32s
deploy-jenkins-68d885ccbb-t2sf7   1/1     Running   0          4m32s
deploy-jenkins-68d885ccbb-w2q2x   1/1     Running   0          99s
deploy-jenkins-68d885ccbb-wjwbh   0/1     Pending   0          9s
deploy-jenkins-68d885ccbb-xp8fn   1/1     Running   0          54s

$ kubectl get pod -l app
NAME                              READY   STATUS    RESTARTS   AGE
deploy-jenkins-68d885ccbb-2qplm   1/1     Running   0          63s
deploy-jenkins-68d885ccbb-6jssb   0/1     Pending   0          18s
deploy-jenkins-68d885ccbb-9mtc5   0/1     Pending   0          18s
deploy-jenkins-68d885ccbb-c9787   0/1     Pending   0          18s
deploy-jenkins-68d885ccbb-jkzgl   0/1     Pending   0          18s
deploy-jenkins-68d885ccbb-kfjxb   1/1     Running   0          4m41s
deploy-jenkins-68d885ccbb-t2sf7   1/1     Running   0          4m41s
deploy-jenkins-68d885ccbb-w2q2x   1/1     Running   0          108s
deploy-jenkins-68d885ccbb-wjwbh   0/1     Pending   0          18s
deploy-jenkins-68d885ccbb-xp8fn   1/1     Running   0          63s
~~~







## 12.**롤링 업데이트와 롤백**





## 13. 롤링 업데이트와 롤백 실습





http-go-deploy-v1.yaml



~~~
$ kubectl get all
$ kubectl describe deploy "name"


~~~



~~~
$ kubectl create -f http-go-deploy-v1.yaml --record=true # history record
$ kubectl rollout status deploy http-go
$ kubectl rollout history deploy http-go
$ kubectl patch deploy http-go -p '{"spec":{"minReadySecond":10}}' # 10초의 준비시간 후 진행한다.
$ kubectl expose deploy http-go
$ kubectl get svc
$ kubectl run -it --rm --image busybox -- bash
# wget -O- -q ip:8080
# while true; to wget -O- -q ip:8080; sleep 1; done
~~~



~~~
$ kubectl set image deploy http-go http-go=gasbugs/http-go:v2
$ kubectl get pod -w
~~~



~~~
$ kubectl edit deploy http-go --record-=true # -> image를 v3로 변경
$ kubectl get all # history replicaset 확인
$ kubectl rollout history deploy http-go
~~~





- v2 로 롤백되고 있는지 확인

~~~
$ kubetl rollout undo deploy http-go
$ kubectl rollout history deploy http-go 
~~~



- 원하는 버전으로 롤백할 수도 있다.

~~~
$ kubectl rollout undo deploy http-go --to-revision=1 
~~~









## 14. 롤링 업데이트와 롤백 연습문제

- 문제
  - 다음 alpine이미지를 사용하여 업데이트와 롤백을 실행해라. 모든 revision 내용은 기록돼야 한다.
    - apline:3.4이미지를 사용해서 deployment를 생성해라.
      - Replicas: 10
      - maxSurge: 50%
      - maxUnavailable" 50%
    - alpine:3.5 롤링 업데이트를 숳애해라
    - aplien3.4로 록백을 수행해라.



~~~
$ kubectl create deploy --image alpine:3.4 alpine-deploy --dry-run=client -o yaml > alpine-deploy.yaml
~~~



- strategy 항목에 type과 rollingUpdate를 정의한다.

~~~
apiVersion: apps/v1
kind: Deployment
metadata:
  creationTimestamp: null
  labels:
    app: alpine-deploy
  name: alpine-deploy
spec:
  replicas: 10
  selector:
    matchLabels:
      app: alpine-deploy
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 50%
      maxUnavailable: 50%
  template:
    metadata:
      creationTimestamp: null
      labels:
        app: alpine-deploy
    spec:
      containers:
      - image: alpine:3.4
        name: alpine
        resources: {}
status: {}
~~~



~~~
$ kubectl create -f alpine-deploy.yaml --record=true # recode 를 작성해야 버전 history를 볼 수 있다.
$ kubectl rollout history deploy alpine-deploy # history 조회
$ kubectl edit deploy alpine-deploy --record=true # alpine 버전을 올린다. 
$ kugectl get pod
$ kubectl get rs
$ kubectl rollout history deploy alpine-deploy
$ kubectl rollout undo deploy alpine-deploy --to-revision=1 # 버전을 1로 revision한다.
$ kubectl rollout history deploy alpine-deploy
$ kubectl get rs
~~~





## 15. 네임스페이스 소개와 실습



~~~
$ kubectl create ns office
$ kubectl create ns office --dry-run # 문법 검증
$ kubectl create ns office --dry-run -o yaml > office-ns.yaml
$ kubectl create deploy nginx --image nginx --port 80 -n office
$ kubectl get pod # 안보임
$ kubectl get all -n office # 일ㅓㅎ게 조회해야 보인다.
$ kubectl get all --all-namespaces # 모든 네임스페이스 조회
~~~



~~~
$ vi ~./kube/config # contests > namespace: office 로 변경해주면
$ kubectl get pod # 이제 office namespace 내부의 pod이 조회된다.
~~~



~~~
$ kubectl delete ns office
$ kubectl get all -n office  # 모두 삭제되었음을 확인.
~~~







## 16.  네임스페이스 연습문제



- 문제
  - 현재 시스템에는 몇개의 namespace가 존재하는가?
  - kube-system 에는 몇개의 포드가 존재하는가?
  - ns-jenkins 네임스페이스를 생성하고 jenkins포드를 배치해라
    - pod image: jenkins
    - pod name: jenkins
  - coredns는 어느 네임스페이스에 속해있는가?



~~~
$ kubectl get ns # 네임스페이스 리스트
$ kubectl get pod -n kube-system | wc -l
$ kubectl get pod --all-namespaces | wc -l
~~~



~~~
$ kubectl create ns ns-jenkins --dry-run=client -o yaml > jenkins-ns.yaml
~~~



~~~
apiVersion: v1
kind: Namespace
metadata:
  name: ns-jenkins
---
apiVersion: v1
kind: Pod
metadata:
  name: jenkins
  namespace: ns-jenkins
spec:
	containers:
  - name: jenkins
    image: jenkins
    ports:
    - containerPort: 8080
~~~



~~~
$ kubectl create -f jenkins-ns.yaml # 두 가지 정보가 모두 생성된다/
$ kubectl get pod -n ns-jenkins # 생성한 jenkins 조회
~~~



~~~
$ kubectl get pod --all-namespaces | grep coredns
~~~



































