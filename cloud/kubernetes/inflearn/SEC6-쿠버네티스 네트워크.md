# SEC6. 쿠버네티스 네트워크



[TOC]



## 1. 서비스와 ClusterIP 소개

![스크린샷 2023-04-15 오전 11.11.01](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-15 오전 11.11.01.png)





![스크린샷 2023-04-15 오전 11.13.18](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-15 오전 11.13.18.png)

![스크린샷 2023-06-13 오후 12.46.58](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-13 오후 12.46.58.png)8080 : pod의 포트

80 : 서비스의 포트

![스크린샷 2023-04-15 오전 11.15.47](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-15 오전 11.15.47.png)



![스크린샷 2023-04-15 오전 11.15.52](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-15 오전 11.15.52.png)





![스크린샷 2023-04-15 오전 11.16.58](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-15 오전 11.16.58.png)



![스크린샷 2023-04-15 오전 11.17.07](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-15 오전 11.17.07.png)

- 처음 로그인한 포드에 계속 연결되어 있도록 설정.
  - 로그인 세션이 하나의 포드에만 있기 때문.



![스크린샷 2023-04-15 오전 11.19.43](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-15 오전 11.19.43.png)

- yaml 문법

​	s 뒤쪽에는 - 가 나올 수 있는데, 배열형태의 리스트라고 보면 된다.

​	-가 한 덩어리 라고 보면 된다.



![스크린샷 2023-04-15 오전 11.22.06](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-15 오전 11.22.06.png)

- 세개의 포드로 로드밸런싱하도록 설정되어 있다.



![스크린샷 2023-04-15 오전 11.22.53](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-15 오전 11.22.53.png)

로드밸런싱에서 웹으로 나가고자 할 때 필요

실습은 안한다고 함

![스크린샷 2023-04-15 오전 11.24.03](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-15 오전 11.24.03.png)







## 2. ClusterIP와 SessionAffinity 실습



~~~
kubectl create deploy --image=gasbugs/http-go http-go --dry-run=client -o yaml > http-go-deploy.yaml
vi http-go-deploy.yaml # service 추가, selector, label 키,값을 맞춘다.
kubectl create -f http-go-deploy-yaml
kubectl get all
kubectl get pod -o wide # ip 할당 확인
kubectl describe svc # endpoint 확인


kubectl scele deploy http-go --replicas=5
kubectl describe svc
kubectl edit svc http-go-svc # spec > sessionAffinity : ClientIP # 하나의 포드에 접속하면 계속 유지됨.
                                                                # type default는 ClusterIP 이다.

~~~



- http-go-deploy.yaml 

~~~
apiVersion: v1
kind: Service
metadata:
  name: http-go-svc
spec:
  selector:
    app: http-go
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
---
apiVersion: apps/v1
kind: Deployment
metadata:
  creationTimestamp: null
  labels:
    app: http-go
  name: http-go
spec:
  replicas: 1
  selector:
    matchLabels:
      app: http-go
  strategy: {}
  template:
    metadata:
      creationTimestamp: null
      labels:
        app: http-go
    spec:
      containers:
      - image: gasbugs/http-go
        name: http-go
        resources: {}
status: {}
~~~





$ kubectl get svc
$ kubectl run -it --rm --image=busybox bash
$ wget -O- -q "ip" # 같은 pod으로 요청되는지 확인

~~~
/ # wget -O- -q 10.8.4.214
Welcome! http-go-565d5486c9-fqh7l
/ # wget -O- -q 10.8.4.214
Welcome! http-go-565d5486c9-fqh7l
/ # wget -O- -q 10.8.4.214
Welcome! http-go-565d5486c9-fqh7l
/ # wget -O- -q 10.8.4.214
Welcome! http-go-565d5486c9-fqh7l
~~~





### [session affinity](https://kubernetes.io/docs/reference/networking/virtual-ips/#session-affinity)

![스크린샷 2023-04-15 오후 12.00.57](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-15 오후 12.00.57.png)











## 3. ClusterIP를 활용한 외부 통신 엔드포인트 실습

- 강의자료 p266 ~p271



[docs](https://kubernetes.io/docs/concepts/services-networking/service/)

### GKE Test

- my-service-endpoints.yaml 생성
  - nslookup naver.com => ip 추출
  - nslookup https://www.malware-traffic-analysis.net/ => ip 추출

~~~
apiVersion: v1
kind: Service
metadata:
  name: my-service
spec:  
  ports:
    - protocol: TCP
      port: 80
      targetPort: 80
---
apiVersion: v1
kind: Endpoints
metadata:
  name: my-service
subsets:
  - addresses:
      - ip: 223.130.195.200 # naver ip
      - ip: 199.201.110.204 # mulware ip
    ports:
      - port: 80
~~~





- 만든 yaml 실행

~~~
kubectl apply -f my-service-endpoints.yaml
kubectl get svc
~~~

- 예제 pod 생성

~~~
kubectl run http-go --image=gasbugs/http-go
~~~



- `kubectl exec -it http-go --  bash`명령어로 포드 접속 후 실행

~~~
root@http-go:/usr/src/app# curl my-service
<html>
<head><title>302 Found</title></head>
<body>
<center><h1>302 Found</h1></center>
<hr><center> NWS </center>
</body>
</html>

root@http-go:/usr/src/app# curl my-service
<!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML 2.0//EN">
<html><head>
<title>301 Moved Permanently</title>
</head><body>
<h1>Moved Permanently</h1>
<p>The document has moved <a href="https://www.malware-traffic-analysis.net/">here</a>.</p>
<hr>
<address>Apache/2.4.52 (Ubuntu) Server at my-service Port 80</address>
</body></html>

root@http-go:/usr/src/app# curl my-service
<!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML 2.0//EN">
<html><head>
<title>301 Moved Permanently</title>
</head><body>
<h1>Moved Permanently</h1>
<p>The document has moved <a href="https://www.malware-traffic-analysis.net/">here</a>.</p>
<hr>
<address>Apache/2.4.52 (Ubuntu) Server at my-service Port 80</address>
</body></html>
~~~







## 4. 외부로 서비스하는 방법들과 노드포트



273 ~ 



## 5. 노드포트로 서비스하기 실습



~~~
vi http-go-np.yaml 
kubectl create -f http-go-np.yaml 
kubectl get svc
kubectl get svc -o wide
kubectl get nodes -o wide
gcloud compute firewall-rules create http-go-svc-rule --allow=tcp:30001
kubectl get nodes -o wide
~~~



- http-go-np.yaml
  - node port 를 지정할 수 있다.

~~~
apiVersion: v1
kind: Service
metadata:
  name: http-go-np
spec:
  type: NodePort
  selector:
    app: http-go
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
      nodePort: 30001
~~~



- node 의 ip, port로 직접 접근하면 해당서비스에 접근할 수 있다.

~~~
$ kubectl get nodes -o wide
NAME                                       STATUS   ROLES    AGE   VERSION            INTERNAL-IP   EXTERNAL-IP    OS-IMAGE                             KERNEL-VERSION   CONTAINER-RUNTIME
gke-cluster-1-default-pool-7b501396-d9vr   Ready    <none>   59m   v1.25.8-gke.1000   10.128.0.6    34.123.77.25   Container-Optimized OS from Google   5.15.89+         containerd://1.6.18
gke-cluster-1-default-pool-7b501396-r0b1   Ready    <none>   59m   v1.25.8-gke.1000   10.128.0.7    35.225.16.85   Container-Optimized OS from Google   5.15.89+         containerd://1.6.18
gke-cluster-1-default-pool-7b501396-rxsq   Ready    <none>   59m   v1.25.8-gke.1000   10.128.0.8    34.171.93.16   Container-Optimized OS from Google   5.15.89+         containerd://1.6.18

$ curl 34.123.77.25:30001
Welcome! http-go-565d5486c9-nvx7d
 
$ curl 35.225.16.85:30001
Welcome! http-go-565d5486c9-wcrjh
~~~









## 6. 로드밸러서로 서비스하기 실습

- 클라우드환경에서만 가능하다.
  - GCP는 클라우드 서비스에서 프로바이더가 제공해주기 때문에 가능하다/.
- 다른 환경이라면 외부 dns 서비스로 해서 연결해야한다. (external dns)



- http-go-lb.yaml

~~~
apiVersion: v1
kind: Service
metadata:
  name: http-go-lb
spec:
  type: LoadBalancer
  selector:
    app: http-go
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
~~~



~~~
kubectl create -f http-go-lb.yaml
~~~



- 2분정도 후에 외부 ip를 받아온다.
- 80포트로 접속해보면 동작을 확인할 수 있다.



- 로드밸런서 아이디가만들어지면서 부하분산기가 생성된다.
- 서비스를 삭제하면 로드밸런서도 삭제된다.

![스크린샷 2023-06-14 오후 1.23.23](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-14 오후 1.23.23.png)



![스크린샷 2023-06-14 오후 1.23.48](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-14 오후 1.23.48.png)





## **7. 노드포트, 로드밸랜서 연습문제**

문제

- tomcat을 노드포트로 서비스하기(30002 포트)
- tomcat을 로드밸런서로 서비스하기(80포트)



~~~
$ kubectl create deploy --image=consol/tomcat:7.0 tomcat --dry-run=client -o yaml > tomcat-deploy-np-lb.yaml 
~~~



- 위 명령어로 생성된 deployment 밑에 Service 로 NodePort, LoadBalancer 를 각각 설정한다.

~~~
apiVersion: apps/v1
kind: Deployment
metadata:
  creationTimestamp: null
  labels:
    app: tomcat
  name: tomcat
spec:
  replicas: 1
  selector:
    matchLabels:
      app: tomcat
  strategy: {}
  template:
    metadata:
      creationTimestamp: null
      labels:
        app: tomcat
    spec:
      containers:
      - image: tomcat
        name: tomcat
        resources: {}
status: {}
---
apiVersion: v1
kind: Service
metadata:
  name: tomcat-np
spec:
  type: NodePort
  selector:
    app: tomcat
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
      nodePort: 30002
---
apiVersion: v1
kind: Service
metadata:
  name: tomcat-lb
spec:
  type: LoadBalancer
  selector:
    app: tomcat
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
~~~



- 아래 명령어로 세가지 설정에 대한 deployment, service 를 생성하고 접근할 수 있다.

~~~
$ kubectl create -f "name.yaml"
$ kubectl get pod -w
$ kubectl get svc
$ gcloud compute firewall-rules create tomcat-svc-rule --allow=tcp:30002
$ kubectl  get nodes  -o wide # node ip 로 포트와 함께 웹에서 접속

$ kubectl edit deploy tomcat # consol/tomcat-7.0 
~~~





- 조금 기다리면 external-ip 가 설정되어 로드밸런스를 통해 접근이 가능하다.

~~~
chxortnl@cloudshell:~ (staek-2023-04-08)$ kubectl get svc
NAME         TYPE           CLUSTER-IP    EXTERNAL-IP      PORT(S)        AGE
kubernetes   ClusterIP      10.8.0.1      <none>           443/TCP        4m35s
tomcat-lb    LoadBalancer   10.8.14.155   34.135.214.183   80:31134/TCP   59s
tomcat-np    NodePort       10.8.11.161   <none>           80:30002/TCP   96s
~~~

![스크린샷 2023-06-14 오후 3.01.47](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-14 오후 3.01.47.png)







- fireall allow
  -  30002 포트를 외부에 연다.

~~~
chxortnl@cloudshell:~ (staek-2023-04-08)$ gcloud compute firewall-rules create tomcat-svc-rule --allow=tcp:30002
Creating firewall...working..Created [https://www.googleapis.com/compute/v1/projects/staek-2023-04-08/global/firewalls/tomcat-svc-rule].                                                                          
Creating firewall...done.                                                                                                                                                                                         
NAME: tomcat-svc-rule
NETWORK: default
DIRECTION: INGRESS
PRIORITY: 1000
ALLOW: tcp:30002
DENY: 
DISABLED: False
~~~



- node 의 ip와, 방화벽이 열린 30002 포트로 접근이 가능하다.

~~~
chxortnl@cloudshell:~ (staek-2023-04-08)$ kubectl get nodes -o wide
NAME                                       STATUS   ROLES    AGE     VERSION           INTERNAL-IP   EXTERNAL-IP      OS-IMAGE                             KERNEL-VERSION   CONTAINER-RUNTIME
gke-cluster-1-default-pool-cb7d7c70-00jc   Ready    <none>   3d21h   v1.25.8-gke.500   10.128.0.14   34.121.200.160   Container-Optimized OS from Google   5.15.89+         containerd://1.6.18
gke-cluster-1-default-pool-cb7d7c70-4gwf   Ready    <none>   3d21h   v1.25.8-gke.500   10.128.0.15   34.30.221.160    Container-Optimized OS from Google   5.15.89+         containerd://1.6.18
gke-cluster-1-default-pool-cb7d7c70-xhdv   Ready    <none>   3d21h   v1.25.8-gke.500   10.128.0.16   34.122.115.222   Container-Optimized OS from Google   5.15.89+         containerd://1.6.18
~~~



![스크린샷 2023-06-14 오후 3.03.36](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-14 오후 3.03.36.png)







## 8. 인그레스(ingress) 소개

ALB

![스크린샷 2023-06-26 오전 11.42.09](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-26 오전 11.42.09.png)















## **인그레스(ingress) 실습**



~~~
git clone https://github.com/kubernetes/ingress-nginx/
kubectl apply -k `pwd`/ingress-nginx/deploy/static/provider/baremetal/
kubectl delete validatingwebhookconfigurations.admissionregistration.k8s.io ingress-nginx-admission
~~~



- ingress 자원을 사용해 룰 생성

~~~
kubectl get all -n ingress-nginx
~~~



~~~
cat <<EOF | kubectl apply -f -
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: http-go-ingress
  annotations:
    kubernetes.io/ingress.class: nginx
    nginx.ingress.kubernetes.io/rewrite-target: /welcome/test
spec:
  rules:
    - http:
        paths:
          - pathType: Exact
            path: /welcome/test
            backend:
              service:
                name: http-go
                port: 
                  number: 80
EOF
~~~



- http-go 서비스 구성

~~~
kubectl create deployment http-go --image=gasbugs/http-go:ingress # 인그레스 테스트용 http-go
kubectl expose deployment http-go --port=80 --target-port=8080
~~~



- 생성한 서비스로 진입해서 localhost 로 테스트.

~~~
chxortnl@cloudshell:~ (staek-2023-04-08)$ kubectl exec -it http-go-647985c674-r8lvs -- bash
root@http-go-647985c674-r8lvs:/usr/src/app# curl 127.0.0.1:8080
404 page not found
root@http-go-647985c674-r8lvs:/usr/src/app# curl 127.0.0.1:8080/welcome/test
Welcome! http-go-647985c674-r8lvs
root@http-go-647985c674-r8lvs:/usr/src/app# kubectl
~~~





### gcp 에서 접근 테스트

- master에서 하지 않고 gcp cloud shell 에서 했기 때문에 node 에서 열린 포트 방화벽을 열어주고 테스트 했다.

~~~
gcloud compute firewall-rules create nginx-ingress-svc --allow=tcp:32393
~~~



~~~
kubectl get ing -o yaml # ingress ip
kubectl get all -n ingress-nginx # 아래에서 열린 32393 포트를 참조.
NAME                                         TYPE        CLUSTER-IP   EXTERNAL-IP   PORT(S)                      AGE
service/ingress-nginx-controller             NodePort    10.8.7.133   <none>        80:32393/TCP,443:31542/TCP   17m
service/ingress-nginx-controller-admission   ClusterIP   10.8.11.32   <none>        443/TCP                      17m

~~~



- 정확한 path를 주지 않으면 nginx 에서  404 를 리턴하고 정확하면 요청한 서비스를 진행한다.

~~~
chxortnl@cloudshell:~ (staek-2023-04-08)$ curl 34.122.115.222:32393/welcome/test
Welcome! http-go-647985c674-r8lvs
chxortnl@cloudshell:~ (staek-2023-04-08)$ curl 34.122.115.222:32393
<html>
<head><title>404 Not Found</title></head>
<body>
<center><h1>404 Not Found</h1></center>
<hr><center>nginx</center>
</body>
</html>
~~~





## 9. 인그레스 TLS 실습



~~~
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
    -out ingress-tls.crt \
    -keyout ingress-tls.key \
    -subj "/CN=ingress-tls"

kubectl create secret tls ingress-tls \
    --namespace default \
    --key ingress-tls.key \
    --cert ingress-tls.crt
~~~



~~~
$ kubectl get secret
NAME          TYPE                DATA   AGE
ingress-tls   kubernetes.io/tls   2      9s
~~~





~~~
cat <<EOF | kubectl apply -f -
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: http-go-ingress
  annotations:
    kubernetes.io/ingress.class: nginx
    nginx.ingress.kubernetes.io/rewrite-target: /welcome/test
    nginx.ingress.kubernetes.io/ssl-redirect: "true"
spec:
  tls:
  - hosts:
    - gasbugs.com
    secretName: ingress-tls
  rules:
    - host: gasbugs.com
      http:
        paths:
          - pathType: Exact
            path: /welcome/test
            backend:
              service:
                name: http-go
                port: 
                  number: 80
EOF
~~~



~~~
$  kubectl describe ingress http-go-ingress
$ kubectl get ingress http-go-ingress # 80, 443 확인
$ kubectl get svc -n ingress-nginx
$ gcloud compute firewall-rules create nginx-ingress-svc-tls --allow=tcp:31542 # 443포트에 대한 포트도 방화벽설정해준다.
~~~





- 아래 주소로 차례대로 테스트 해보자

~~~
curl http://gasbugs.com:30636/welcome/test -kv --resolve gasbugs.com:30636:127.0.0.1

curl https://gasbugs.com:31407/welcome/test -kv --resolve gasbugs.com:31407:127.0.0.1

curl https://gasbugs1.com:31407/welcome/test -kv --resolve gasbugs1.com:31407:127.0.0.1
~~~



- 1번 테스트

~~~
chxortnl@cloudshell:~ (staek-2023-04-08)$ curl http://gasbugs.com:32393/welcome/test -kv --resolve gasbugs.com:32393:34.122.115.222

* Added gasbugs.com:32393:34.122.115.222 to DNS cache
* Hostname gasbugs.com was found in DNS cache
*   Trying 34.122.115.222:32393...
* Connected to gasbugs.com (34.122.115.222) port 32393 (#0)
> GET /welcome/test HTTP/1.1
> Host: gasbugs.com:32393
> User-Agent: curl/7.74.0
> Accept: */*
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 308 Permanent Redirect
< Date: Wed, 14 Jun 2023 07:19:29 GMT
< Content-Type: text/html
< Content-Length: 164
< Connection: keep-alive
< Location: https://gasbugs.com/welcome/test
< 
<html>
<head><title>308 Permanent Redirect</title></head>
<body>
<center><h1>308 Permanent Redirect</h1></center>
<hr><center>nginx</center>
</body>
</html>
* Connection #0 to host gasbugs.com left intact
~~~





- 2번 테스트
  -   subject: O=Acme Co; CN=Kubernetes Ingress Controller Fake Certificate 현재 이부분이 제대로 적용이 안되는 문제가 있다...

~~~
chxortnl@cloudshell:~ (staek-2023-04-08)$ curl https://gasbugs.com:31542/welcome/test -kv --resolve gasbugs.com:31542:34.122.115.222
* Added gasbugs.com:31542:34.122.115.222 to DNS cache
* Hostname gasbugs.com was found in DNS cache
*   Trying 34.122.115.222:31542...
* Connected to gasbugs.com (34.122.115.222) port 31542 (#0)
* ALPN, offering h2
* ALPN, offering http/1.1
* successfully set certificate verify locations:
*  CAfile: /etc/ssl/certs/ca-certificates.crt
*  CApath: /etc/ssl/certs
* TLSv1.3 (OUT), TLS handshake, Client hello (1):
* TLSv1.3 (IN), TLS handshake, Server hello (2):
* TLSv1.3 (IN), TLS handshake, Encrypted Extensions (8):
* TLSv1.3 (IN), TLS handshake, Certificate (11):
* TLSv1.3 (IN), TLS handshake, CERT verify (15):
* TLSv1.3 (IN), TLS handshake, Finished (20):
* TLSv1.3 (OUT), TLS change cipher, Change cipher spec (1):
* TLSv1.3 (OUT), TLS handshake, Finished (20):
* SSL connection using TLSv1.3 / TLS_AES_256_GCM_SHA384
* ALPN, server accepted to use h2
* Server certificate:
*  subject: O=Acme Co; CN=Kubernetes Ingress Controller Fake Certificate
*  start date: Jun 14 06:42:39 2023 GMT
*  expire date: Jun 13 06:42:39 2024 GMT
*  issuer: O=Acme Co; CN=Kubernetes Ingress Controller Fake Certificate
*  SSL certificate verify result: self signed certificate (18), continuing anyway.
* Using HTTP2, server supports multi-use
* Connection state changed (HTTP/2 confirmed)
* Copying HTTP/2 data in stream buffer to connection buffer after upgrade: len=0
* Using Stream ID: 1 (easy handle 0x559af40222c0)
> GET /welcome/test HTTP/2
> Host: gasbugs.com:31542
> user-agent: curl/7.74.0
> accept: */*
> 
* TLSv1.3 (IN), TLS handshake, Newsession Ticket (4):
* TLSv1.3 (IN), TLS handshake, Newsession Ticket (4):
* old SSL session ID is stale, removing
* Connection state changed (MAX_CONCURRENT_STREAMS == 128)!
< HTTP/2 200 
< date: Wed, 14 Jun 2023 07:20:30 GMT
< content-type: text/plain; charset=utf-8
< content-length: 34
< strict-transport-security: max-age=15724800; includeSubDomains
< 
Welcome! http-go-647985c674-r8lvs
* Connection #0 to host gasbugs.com left intact
~~~



- 3번 테스트
  - 등록하지 않은 도메인으로 접속하게 되면 404를 리턴한다.

~~~
chxortnl@cloudshell:~ (staek-2023-04-08)$ curl https://gasbugs1.com:31542/welcome/test -kv --resolve gasbugs1.com:31542:34.122.115.222
...
<html>
<head><title>404 Not Found</title></head>
<body>
<center><h1>404 Not Found</h1></center>
<hr><center>nginx</center>
</body>
</html>
* Connection #0 to host gasbugs1.com left intact
~~~





## 10. 인그레스(ingress) TLS 연습문제

- 다음 요구사항을 모두 만족하는 인그레스와 서비스, 디플로이먼트를 생성하라.
  - tomcat.gasbugs.com 도메인으로 consol/tomcat-7.0 서비스를 지원
  - http-go.gasbugs.com 도메인으로 gasbugs/http-go 서비스를 지원
  - 모든 서비스는 SSL 통신을 지원
  - HTTP 포트로 접근 시 HTTPS로 리다이렉션하도록 구성
  
  

~~~
cat <<EOF | kubectl apply -f -
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: tomcat-ingress
  annotations:
    kubernetes.io/ingress.class: nginx
    nginx.ingress.kubernetes.io/ssl-redirect: "true"
spec:
  tls:
  - hosts:
    - tomcat.gasbugs.com
    - http-go.gasbugs.com
    secretName: ingress-tls
  rules:
    - host: tomcat.gasbugs.com
      http:
        paths:
        - path: /
          pathType: Prefix
          backend:
            service:
              name: tomcat
              port: 
                number: 80
    - host: http-go.gasbugs.com
      http:
        paths:
        - path: /
          pathType: Prefix
          backend:
            service:
              name: http-go
              port: 
                number: 80
EOF
~~~



~~~
kubectl create deployment http-go --image=gasbugs/http-go
kubectl expose deployment http-go --port=80 --target-port=8080

kubectl create deployment tomcat --image=consol/tomcat-7.0
kubectl expose deployment tomcat --port=80 --target-port=8080
~~~



~~~
kubectl get all -n ingress-nginx
~~~



~~~
curl http://http-go.gasbugs.com:30636/welcome/test -kv --resolve http-go.gasbugs.com:30636:127.0.0.1

curl https://http-go.gasbugs.com:31407 -kv --resolve http-go.gasbugs.com:31407:127.0.0.1

curl https://gasbugs1.com:31407 -kv --resolve gasbugs1.com:31407:127.0.0.1
~~~



~~~
curl https://tomcat.gasbugs.com:31407 -kv --resolve tomcat.gasbugs.com:31407:127.0.0.1

curl https://tomcat.gasbugs.com:31407 -kv --resolve tomcat.gasbugs.com:31407:127.0.0.1

curl https://tomcat1.gasbugs.com:31407 -kv --resolve tomcat1.gasbugs.com:31407:127.0.0.1
~~~









## 11. 쿠버네티스 네트워크 - 컨테이너 간의 인터페이스 공유 방법-1

### 

~~~
sudo apt install net-tools
~~~



pause

apiserver 시작시 실행하고, 아무 역할을 하지 않는다.

네트워크인터페이스 공유목적으로 만들어진 pod

컨테이너가 켜졋다 꺼지는 경우도 있는데, pause를 통해 유지하는거다.



~~~
docker ps -a 
docker ps = a| gerp apiserver

sudo docker images
~~~





ifconfig





















## 12. 쿠버네티스 네트워크 - 포드 간의 통신

### 

~~~
sudo docker ps | grep weave 
sudo netstat -antp | grep weave
ps -eaf | grep 19979
ifconfig weave
kubectl get ds weave-net -n kube-system -o yaml # 데몬셋 : 노드마다 하나씩 포드를배치하는 방식. 포드를 지정하지 않아도 노드별 하나씩 설치함.
~~~













## 13. 쿠버네티스 네트워크 - 포드와 서비스, 외부 클라이언트 사이의 통신

![스크린샷 2023-06-27 오후 12.35.43](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-27 오후 12.35.43.png)











## 14. CoreDNS 서비스 소개











## 15. **서비스 DNS 연습문제**

- 연습문제
  - 네임스페이스 blue에 jenkins 이미지를 사용하는 pod-jenkins 디플로이먼트를 생성하고 이를위한 서비스 srv-jenkins를 생성해라
  - default 네임스페이스의 http-go 이미지의 curl을 사용하여 pod-jenkins:8080을 요청하라
  - kubectl exec http-go-id -- curl srv-jenkins.blue:8080

![스크린샷 2023-06-14 오후 6.10.33](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-14 오후 6.10.33.png)







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





























