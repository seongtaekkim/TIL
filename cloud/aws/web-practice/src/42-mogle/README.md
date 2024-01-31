## outline
- 해당 eks예제는 42seoul 교육생 대상으로 제공되는 서비스를 기반으로 작성되었다. (42Cluster 개발 중)

### mogle project setting

코드: [mogle-fe](https://github.com/42mogle/42-mogle-frontend.git)
- branch (develop)
- FE하위 내용을 clone 하위로 이동 (cp -r FE/* clone/)


코드: [mogle-be](https://github.com/42mogle/42-mogle-backend.git)
- branch (develop)
- BE하위 내용을 clone 하위로 이동 (cp -r BE/* clone/)


### deploy to eks

1. db pod 설정

~~~
db/init-sql.sql
- database 생성 쿼리.
db/pg-config.sh
- 쿼리를 실행할 configmap 리소스생성
db/pg-hostpath-pv.yaml, db/pg-hostpath-pvc.yaml
- postgre데이터를 저장할 hostpath를 설정하기 위해 pv,pvc 생성
db/pg-secret.yaml
- password 저장할 secret 생성
db/pg-svc.yaml
- service 생성 (clusterIp)
db/pg-deploy.yaml
- db/init-sql.sql를 db최초 생성1회시에만 실행하도록 세팅
- port: 5432
~~~




2. ECR Image 준비
- frontend, backend 각각 준비한다.

~~~
npm install
docker build -t name .
docker tag ~
docker push ~
~~~




3. deploy to eks

~~~
deploy/fe-svc.yaml
deploy/fe-deploy.yaml

deploy/be-svc.yaml
deploy/be-deploy.yaml
~~~




4. db 확인

~~~
k exec -it "pod id" --  psql -U postgres
\l 
\c "database name"
\dt
~~~




5. log

~~~
kubectl logs "pod id"
~~~

