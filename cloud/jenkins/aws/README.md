# aws에서 jenkins 테스트



### 1. ec2

- ec2 인스턴스 4개를 생성한다.
- 각 인스턴스에 jenkins, docker, tomcat, ansible 을 설치한다.
- jenkins 빌드 후 tomcat으로 배포 테스트
- jenkins 빌드 후 ansible을 통해 docker에 배포 테스트

### 2. eks
