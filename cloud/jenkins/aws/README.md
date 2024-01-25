# aws에서 jenkins 테스트



### 1. ec2

- ec2 인스턴스 4개를 생성한다.
- 각 인스턴스에 jenkins, docker, tomcat, ansible 을 설치한다.
- jenkins 빌드 후 tomcat으로 배포 테스트
- jenkins 빌드 후 ansible을 통해 docker에 배포 테스트

### 2. eks

- eks csi 드라이버를 설정하고 sc, pvc를 생성해서 EBS를 추가한다/
- jenkins 컨트롤을 위한 sa 등 rbac을 생성한다.
- dind 등을 설정한 helm jenkins values file을 작성한다.
- helm 으로 jenkins를 구동하고 github과 webhook 연결을 한다.
- pipeline CICD 설정 후 테스트한다.
