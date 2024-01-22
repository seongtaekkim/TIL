# jenkinsfile 사용(1)



### 개요

- k8s 로컬 vm 구성
- jenkins를 pod으로 실행했을 때, docker build 등 docker 명령어 사용 방법
- jenkins springboot ci 구성.



### jenkins 플러그인 설치

~~~sh
Maven Integration
Maven Pipeline Integration
Docker Pipeline
~~~



### jenkins pod 구성

- jenkins를 root로 실행한다.
- sidecar를 docker로 구성하고, docker.sock를 volume 으로 연결해준다. (jenkins에서 사용하기 위해)
- nfs server 디렉토리를 volume으로 설정한다.

~~~yaml
---
    apiVersion: apps/v1
    kind: Deployment
    metadata:
      name: jenkins
      namespace: jenkins
    spec:
      replicas: 1
      selector:
        matchLabels:
          app: jenkins
      template:
        metadata:
          labels:
            app: jenkins
        spec:
          containers:
          - name: jenkins
            securityContext:
              runAsUser: 0
            image: jenkins/jenkins:lts
            ports:
            - containerPort: 8080
            volumeMounts:
            - name: jenkins
              mountPath: /var/jenkins_home
            - name: shared
              mountPath: /var/run
          - image: docker:dind
            imagePullPolicy: IfNotPresent
            name: docker
            securityContext:
              privileged: true
            volumeMounts:
            - mountPath: /var/run
              name: shared
          volumes:
            - name: jenkins
              persistentVolumeClaim:
                claimName: jenkins-pvc
            - name: shared
              emptyDir: {}
~~~

- jenkins에 bash로 로그인 후 아래 명령어로 도커를 설치한다

~~~sh
$ k exec -it -n jenkins "pod-id" -c "container name" -- bash
~~~

~~~sh
# Docker 설치
## - Setup Repo
apt-get update
apt-get install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release
mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/debian/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/debian \
  $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null
## - Install Docker Engine
apt-get update
apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin -y
~~~

- 그 후 jenkins에 접속해서
  - jdk17 설정
  - maven 설정
  - plugin 설치 진행.
- jenkinsfile 배포를 한다 (git에 있는 springboot)

~~~groovy
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                withMaven(globalMavenSettingsConfig: '', jdk: 'jdk17', maven: 'Maven3.9.6', mavenSettingsConfig: '', traceability: true) {
                    sh 'mvn -f cloud/jenkins/src/demo clean package -Dmaven.test.skip=true'
                }
            }
        }
        stage('Docker Build And Push') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com/', 'seongtaekkim') {
                        image = docker.build("seongtaekkim/demo-springboot:v1", "./cloud/jenkins/src/demo");
                        image.push();
                    }
                }
            }
        }
    }
}
~~~

- docker.withRegistry 두번째 인자는, jenkins에서 credential 을 username/password 으로 생성해준 후 id값으로 사용한 것이다.
- stage 순서대로 maven빌드 후, docker hub 로그인, 빌드, push를 진행한다.















