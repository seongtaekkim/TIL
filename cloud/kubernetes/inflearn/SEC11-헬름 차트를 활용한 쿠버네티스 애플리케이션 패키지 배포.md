## helm 소개와 설치

- 패키지 등을 만들어서 쉽게 배포할 수 있게 도와주는 프로그램

- kubectl 이 설치된 곳에 설치해야 함.



- 온프라미스로 진행하려면 look-ceph 스토리지를 구성해야 가능하다.
- 강의에서는 gcp-cloud shell 에서 진행할 예정



## **공개 레파지토리를 활용한 애플리케이션 배포**



저장소 추가

```bash
$ helm repo add bitnami https://charts.bitnami.com/bitnami 
```

목록 업데이트

```bash
$ helm repo update
Hang tight while we grab the latest from your chart repositories...
...Successfully got an update from the "bitnami" chart repository
Update Complete. ⎈Happy Helming!⎈
```

추가된 레파지토리에 헬름 차트 리스트 확인

```bash
helm search repo bitnami
```

mysql 배포

~~~
 helm install mysqlname bitnami/mysql -n mysql # 배포
~~~

~~~shell
NAME: mysqlname
LAST DEPLOYED: Thu Jun 15 09:50:15 2023
NAMESPACE: mysql
STATUS: deployed
REVISION: 1
TEST SUITE: None
NOTES:
CHART NAME: mysql
CHART VERSION: 9.10.4
APP VERSION: 8.0.33

** Please be patient while the chart is being deployed **

Tip:

  Watch the deployment status using the command: kubectl get pods -w --namespace mysql

Services:

  echo Primary: mysqlname.mysql.svc.cluster.local:3306

Execute the following to get the administrator credentials:

  echo Username: root
  MYSQL_ROOT_PASSWORD=$(kubectl get secret --namespace mysql mysqlname -o jsonpath="{.data.mysql-root-password}" | base64 -d)

To connect to your database:

  1. Run a pod that you can use as a client:

      kubectl run mysqlname-client --rm --tty -i --restart='Never' --image  docker.io/bitnami/mysql:8.0.33-debian-11-r17 --namespace mysql --env MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASSWORD --command -- bash

  2. To connect to primary service (read/write):

      mysql -h mysqlname.mysql.svc.cluster.local -uroot -p"$MYSQL_ROOT_PASSWORD"
~~~



- decode된 패스워드  출력

~~~
kubectl get secret --namespace mysql mysqlname -o jsonpath="{.data.mysql-root-password}" | base64 -d
~~~



~~~
$ kubectl get pod -n mysql
NAME          READY   STATUS    RESTARTS   AGE
mysqlname-0   1/1     Running   0          117s

$ kubectl -n mysql exec -it mysqlname-0 -- mysql -u root -p
Enter password: 

mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| my_database        |
| mysql              |
| performance_schema |
| sys                |
+--------------------+
5 rows in set (0.01 sec)

mysql> exit
~~~



- 아래와 같이 많은 설정을 한번에 설치하고 사용할 수 있는게 helm 의 장점이다.

~~~
$ kubectl -n mysql get all
NAME              READY   STATUS    RESTARTS   AGE
pod/mysqlname-0   1/1     Running   0          3m3s

NAME                         TYPE        CLUSTER-IP   EXTERNAL-IP   PORT(S)    AGE
service/mysqlname            ClusterIP   10.8.6.197   <none>        3306/TCP   3m5s
service/mysqlname-headless   ClusterIP   None         <none>        3306/TCP   3m5s

NAME                         READY   AGE
statefulset.apps/mysqlname   1/1     3m6s

$ kubectl -n mysql get pvc
NAME               STATUS   VOLUME                                     CAPACITY   ACCESS MODES   STORAGECLASS   AGE
data-mysqlname-0   Bound    pvc-052644f9-84e5-4669-88bc-84c5e0ae9e21   8Gi        RWO            standard-rwo   3m26s

$ kubectl -n mysql get secret
NAME                              TYPE                 DATA   AGE
mysqlname                         Opaque               2      3m42s
sh.helm.release.v1.mysqlname.v1   helm.sh/release.v1   1      3m43s

$ kubectl -n mysql get configmap
NAME               DATA   AGE
kube-root-ca.crt   1      4m30s
mysqlname          1      3m49s

$ kubectl -n mysql get sa
NAME        SECRETS   AGE
default     0         4m40s
mysqlname   1         4m
~~~





- helm mysqlname uninstall

~~~
$ helm uninstall mysqlname -n mysql
release "mysqlname" uninstalled

$ helm list -n mysql
NAME    NAMESPACE       REVISION        UPDATED STATUS  CHART   APP VERSION
~~~



- helm repo remove

~~~
$ helm list -n mysql
NAME    NAMESPACE       REVISION        UPDATED STATUS  CHART   APP VERSION

$ helm repo list
NAME    URL                               
bitnami https://charts.bitnami.com/bitnami

$ helm repo remove bitnami
"bitnami" has been removed from your repositories

$ helm repo update
Error: no repositories found. You must add one before updating

$ helm repo list
Error: no repositories to show
~~~





## 새로운 차트 생성과 실행



~~~
$ helm create mychart
$ sudo apt update & & sudo apt install tree -y
~~~



~~~
 vi mychart/values.yaml 
~~~





~~~
 helm create mychart
  297  tree mychart
  298  vim mychart/Chart.yaml 
  299  vim mychart/templates/deployment.yaml 
  300  vim mychart/values.yaml 
  301  helm lint mychar
  302  helm lint mychart
  303  helm install  mychart ./mychart/
  304  helm list -n default 
  305  kubectl get pod
  306  kubectl get sa
  307  kubectl get pod -o yaml
  308  kubectl get deploy -o yaml
  309  kubectl get svc -o yaml
  310  kubectl edit svc mychart
  311  kubect get svc
  312  kubectl get svc
  313  kubectl get deploy -o yaml
  314  history
  315  kubectl get pod
  316  kubectl edit svc mychart-7b9989bd45-ktg8g
  317  kubectl edit pod  mychart-7b9989bd45-ktg8g
  318  kubectl get svc
  319  kubectl get svc -n default
  320  kubectl edit svc mychart-7b9989bd45-ktg8g
  321  kubectl edit pod  mychart-7b9989bd45-ktg8g
  322  ls
  323  kubectl edit svc mychart
  324  helm uninstall mychart
  325  history
~~~



~~~
chxortnl@cloudshell:~ (staek-2023-04-08)$ kubectl edit svc mychart
kubEdit cancelled, no changes made.
chxortnl@cloudshell:~ (staek-2023-04-08)$ kubectl get svc
NAME         TYPE           CLUSTER-IP    EXTERNAL-IP      PORT(S)        AGE
kubernetes   ClusterIP      10.8.0.1      <none>           443/TCP        22h
mychart      LoadBalancer   10.8.14.196   35.202.215.211   80:30022/TCP   2m24s
~~~





helm list -n default



![스크린샷 2023-06-16 오후 1.19.25](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-16 오후 1.19.25.png)







## 차트 패키징 및 github 레파지토리를 활용한 배포



- github repository 생성
  - repository name : helm-charts

- gcp cloud shell 에 pull 하고, helm package 진행.

~~~
helm create mychart1
helm create mychart2
helm package mychart1
helm package mychart2
~~~

- 패키지 정보를 index.yaml 파일에 업데이트 한다.
  - 패키지 정보가 변경되면 아래 명령어 실행해야함

~~~
helm repo index ./
~~~



- 생성한 정보를 모두 push 하고, helm repo 를 add 한다.

~~~
helm repo add git-charts https://raw.githubusercontent.com/seongtaekkim/helm-charts/main
~~~



- 생성된 정보를 확인하고, helm install 진행.

~~~

helm repo list
helm repo update # 변경된 repo를 update 할 수 있다.
helm search repo git
helm install git-mychart1 git-charts/mychart1
kubectl get pod # 구성된 nginx pod 확인.
kubectl edit svc git-mychart1 # spec > type 을 LoadBalancer 로 변경
kubectl get svc # 잠시 후 external ip가 할당 된다.
~~~



- 접근확인.

![스크린샷 2023-06-16 오후 1.45.13](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-16 오후 1.45.13.png)





























