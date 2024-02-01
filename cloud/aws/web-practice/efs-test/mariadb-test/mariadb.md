

### 코드 설명



~~~
db-secret.yaml 
- 접속할 때 패스워드를 base64 인코딩한 결과를 secret 리소스에 작성.
db-pv.yaml     
- EFS Fileid 로 csi에 참조를 한다. (claim 발생 시 bound처리)
db-claim.yaml  
- storageClassName를 작성하면, 드라이버가 pv를 참조해서 bound해준다.
db-deploy.yaml 
- claim 을 참조한다. 실행 시 pvc가 bound되어 잇으면 pv를 사용할 수 있다.
- 이 예제에서는 EFS 사용가능.
db-svc.yaml
- service 열어줌
~~~

- 확인

~~~
 kubectl exec -it "pod id" -- bash
 mysql -u root -p 
 # password는 secret에 작성한 패스워드 입력
~~~

