## 미니큐브 설치와 소개, 접속하고 활용하기



# 가상환경 설치가 잘 안되어서 추후에 하자





[미니큐브 정리링크](https://blog.naver.com/isc0304/221879359568)



**minicube start error**

~~~
minikube start --driver=docker
~~~

- less than the required 1800MiB for Kubernetes error

- 최소사양 : Creating docker container (CPUs=2, Memory=2200MB) .

[don't start](https://github.com/kubernetes/minikube/issues/10538)





**snap install**

~~~
sudo apt-get install snapd
~~~



**kubectl install**

- snap 환경설정이 안되어있어서 실행위치 찾아서 실행

~~~
/snap/bin/kubectl get all
/snap/bin/kubectl get pod
/snap/bin/kubectl  create deployment hello-minikube --image=k8s.gcr.io/echoserver:1.10
/snap/bin/kubectl expose deployment hello-minikube --type=NodePort --port=8080
/snap/bin/kubectl get all
/snap/bin/kubectl get svc
~~~



<img src="/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-04-11 오전 8.11.16.png" alt="스크린샷 2023-04-11 오전 8.11.16" style="zoom:50%;" />





