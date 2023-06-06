

## hostpath 컨테이너와 노드 간 데이터 공유

- 컨테이너와 노드의 데이터 공유
- 노드상태를 모니터링 하는 용도. (프로메테우스 등)
- 보통 노드데이터를 포드에 전달



- 이미 모니터링 툴이 설치되어있는 gcp 에서 실습할 에정이다.

~~~
$ kubectl get pods -n kube-system
NAME                                                  READY   STATUS    RESTARTS   AGE
event-exporter-gke-755c4b4d97-t7826                   2/2     Running   0          31h
fluentbit-gke-27vx2                                   2/2     Running   0          31h
fluentbit-gke-7smnw                                   2/2     Running   0          31h
fluentbit-gke-9w7g2                                   2/2     Running   0          31h
~~~





## hostpath 컨테이너와 노드 간 데이터 공유 실습



### virtual box 에서 테스트

- work1, work2 에 아래와 같이 작성한다.

~~~
sudo mkdir /var/htdocs
sudo -i
sudo echo "work1" > /var/htdocs/index.html

~~~





- hostpath-httpd.yaml

~~~
apiVersion: v1
kind: Pod
metadata:
  name: hostpath-http
spec:
  containers:
  - image: httpd
    name: web-server
    volumeMounts:
    - name: html
      mountPath: /usr/local/apache2/htdocs
      readOnly: true
    ports:
    - containerPort: 80
      protocol: TCP
  volumes:
  - name: html
    hostpath:
      path: /var/htdocs
      type: Directory
~~~



~~~
$ kubectl create -f hostpath-httpd.yaml
$ kubectl get pod -w
$ kubectl port-forward hostpath-httpd.yaml 8888:80

# 접속
$ kubectl get pod -o wide # ip 확인
$ wget -O- -q "ip"
$ kubectl get pod -o wide # pod 실행 된 node 확인
~~~





### gcp에서 확인

~~~
$ kubectl get pod -n kube-system # fluented : 데이터를 모니터링할때 로깅되는 리소스를 가져오고 있다.
$ kubectl get pod fluentd-gcp-v3.1.1.-468d9 -n kubesystem -o yaml # volumes > hostpath 확인
																															    # volumeMounts 
~~~



