

## 1. 유저 관리를 위한 다양한 리소스 소개







## 2. 스태틱토큰과 서비스어카운트 & 연습문제



### Account

- 유저: 일반 사용자를 위한 계정 
- 서비스어카운트: 애플리케이션(파드 등)을 위한 계정



![스크린샷 2023-07-01 오후 12.51.54](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-01 오후 12.51.54.png)

- somefile.csv 파일 추가.

~~~
password1,user1,uid001,"group1"
password2,user2,uid002
password3,user3,uid003
password4,user4,uid004
~~~



~~~
sudo vi /etc/kubernetes/manifests/kube-apiserver.yaml
# 아래와 같이 추가.
- --token-auth-file=/etc/kubernetes/pki/somefile.csv
~~~



~~~
kubectl get pod # 잠시후 조회 됨.
~~~



- kubectl 유저등록, 스위치
- 결과는 forbidden (인증은 되었으나 권한이 없음)

~~~
$ kubectl config set-credentials user1 --token=password1
$ kubectl config set-context user1-context --cluster=kubernetes \
--namespace=frontend --user=user1
$ kubectl get pod --user user1
~~~



~~~
$ kubectl config use-context kubernetes-admin@kubernetes # 권한 되돌림
$ kubectl get pod
~~~





## 3. 서비스 어카운트를 활용한 Pod API 통신 이해, 실습



- default account 는 가능한 하지 말것.

~~~
$ k create serviceaccount sa1
$ k get sa,secret
~~~



- pod
- 자동으로 mount가 연결되어 있다.

~~~
# pod
spec:
  serviceAccountName: sa1
  containers:
~~~





~~~
k exec -it nx --bash
ls /var/run/secrets/kubernetes.io/serviceaccount
~~~



![스크린샷 2023-07-02 오전 9.32.56](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-02 오전 9.32.56.png)

- pod list 를 조회할 수 있다고 한다 권한이 필요하다던데 필습은 잘모름







docs > sa pod > Configure Service Accounts for Pods



~~~
apiVersion: v1
kind: Pod
metadata:
  name: nx
spec:
  serviceAccountName: sa1
  containers:
  - image: nginx
    name: nginx
~~~





[pod v1 list](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.27/#list-pod-v1-core)















## 4. TLS 인증서를 활용한 통신 이해



















## 5. TLS 인증서 정보 확인과 자동 갱신 방법



## 6. TLS 인증서를 활용한 유저 생성



## 7. Kube Config 설정 관리



## 8. TLS 유저 생성 및 유저 등록 연습문제



## 9. RBAC를 활용한 권한 부여



## 10. RBAC를 활용한 권한 부여 실습



## 11. RBAC를 활용한 권한 부여 연습문제와 풀이

















































































