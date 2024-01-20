



## jenkins item 추가



### 플러그인 추가

~~~
Pipeline Maven Integration Plugin
~~~



### item 추가

- type: pipeline
- [Pipeline Syntax](http://192.168.64.29:31077/job/Pipeline-springboot/pipeline-syntax)
  - checkout: Check out from version control
    - git, branch 작성
  - withMaven: Provide Maven environmemnt
    - 빌드할 mvn, jdk 선택

~~~groovy
node {
	stage('Build') {
    	checkout scmGit(branches: [[name: '*/jenkins']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/seongtaekkim/TIL.git']])
            withMaven(globalMavenSettingsConfig: '', jdk: 'jdk17', maven: 'Maven3.9.6', mavenSettingsConfig: '', traceability: true) {
             sh 'mvn -f cloud/jenkins/src/demo clean package'
            }
	}
	stage('Test') {
	
	}
	stage('Deploy') {
	
	}
}
~~~



