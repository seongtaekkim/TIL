

### 1 round

~~~
Falco
ImagePolicyWebhook
networkpolicy(1)
networkpolicy(2)
kubernetes upgrade
Encryption Data
Trivy
Apparmor
Audit
role rolebinding serviceaccount
RuntimeClass 생성
Docker 취약점해결
secret create
readonlyRootFileSystem
kubebench --targets=master | grep 
~~~



### 2 round

~~~
Falco fileoutput 설정, workernode에 rule추가, 테스트가 안됨.
								     %evt.time,%user.uid or %user.name,%proc.name가 요구인데
                     마지막에 %evt.time,%user.name,%proc.name 로 바꿔버리고 수정못함
Docker 취약점해결 deploy에 capabilities >drop > all 못찾음 .. 4개중 3개 수정.
                        dockerfile : From 버전명시, USER root를 nobody 로 변경
                        deploy 파일 : Privileged: False, capabilities > drop > all 
apiserver: nodeRestricton 추가 clusterrolebinding system:anomous 삭제 ,,, 
           하라고 해서 하긴 했는데, 개념을 모름.
networkpolicy(1) - ingress deny all 인데, podSelector 에 namespace label 작성함
tls1.3 업그레이드 문제 - 못 풀음
ImagePolicyWebhook
networkpolicy(2) - ingress 두개설정. or조건으로 pod하나 namespace하나 설정
Trivy
Apparmor
Audit - audit 설정에서 실수 있을 수 있음
role rolebinding serviceaccount
RuntimeClass 생성
secret create generic
readonlyRootFileSystem
kubebench --targets=master | grep ~
serviceaccount 생성하고 적용한 후, 사용안하는 sa 삭제.
                - autoamountServiceAcountToken: false
~~~

























































