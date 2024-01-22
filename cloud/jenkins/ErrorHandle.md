# Jenkins 에러 관련 정리





### git

1. 아래와 같이 에러발생
   - git에서 보안관련 패치로 인해 발생한게 됨.

```
hudson.plugins.git.GitException: Command "git config remote.origin.url https://github.com/seongtaekkim/TIL.git" returned status code 128:
```

해결방법

1. jenkins에서 git 설정 시 secret key 를 부여하면 된다.
2. 1번을 할 수 없는경우 jenkins에서 아래와 같은 명령어를 수행한다. 

~~~sh
cd /var/jenkins_home
git config --global --add safe.directory '*'
~~~





### maven build

- 일반 maven build에서는 발생하지 않았는데, pipeline maven 에서 빌드 시 발생했다.

~~~
[ERROR] Tests run: 1, Failures: 0, Errors: 1, Skipped: 0, Time elapsed: 23.82 s <<< FAILURE! -- in me.staek.demo.DemoApplicationTests
~~~

- 해결방법

1. pipeline에 테스트 skip코드를 추가해준다.

~~~groovy
withMaven(globalMavenSettingsConfig: '', jdk: 'jdk17', maven: 'Maven3.9.6', mavenSettingsConfig: '', traceability: true) {
    sh 'mvn -f cloud/jenkins/src/demo clean package -Dmaven.test.skip=true'
}
~~~















