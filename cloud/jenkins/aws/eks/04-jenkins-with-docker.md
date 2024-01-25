# jenkins with docker



- jenkins 를 pod으로 실행할 것이다.
- jenkins 의 CI 진행 시, ECR 로 push하기 위해서는 docker build 작업이 있어야 한다.
- jenkins에서 docker build를 수행하기 위해 dind를 적용해야 한다.





### dind docker image 생성



### 1. helm jenkins-values.yaml

- jenkins 설정파일에 agent를 추가한다.
- pipeline agent가 "dind-agent"인 경우 아래 agent가 pod 형태로 실행된다.
- container 두 개 (하나는 jnlp, 하나는 docker dind) 가 실행된다.
- dind-daemon 의 privileged 를 true로 하면 host의 권한을 대부분 사용할 수 있다 (필수설정)
- jnlp는 DOCKER_HOST(port 2375) 를 이용해서 docker server에 docker build 등의 명령어를 사용한다.
  - 이때 docker server는 DOCKER_TLS_VERIFY, DOCKER_TLS_CERTDIR 값을 빈문자열로 주어서 인증없이 사용할 수 있도록 허가해 준다 (필수설정)
- 위의 필수설정 두개는 insecure 하지만, cluster내부에서 빌드목적으로 사용되는 agent이기에 설정하였다.

~~~yaml
additionalAgents:
	dind:
    podName: dind-agent
    customJenkinsLabels: dind-agent
    image: seongtaekkim/dind-client-jenkins-agent
    tag: latest
    envVars:
     - name: DOCKER_HOST
       value: "tcp://localhost:2375"
    alwaysPullImage: true
    yamlTemplate:  |-  
     spec: 
         containers:
           - name: dind-daemon
             image: docker:24.0.0-rc.1-dind
             securityContext: 
               privileged: true
             env: 
               - name: DOCKER_TLS_VERIFY
                 value: ""
               - name: DOCKER_TLS_CERTDIR
                 value: ""
~~~





### Dockerfile

- 위 agent 첫번째 이미지를 만든다.

- jenkins agent는 jnlp 이미지를 사용해야 한다.

~~~dockerfile
FROM jenkins/jnlp-agent-docker
USER root

COPY entrypoint.sh /entrypoint.sh
RUN chown jenkins:jenkins /entrypoint.sh
RUN chmod +x /entrypoint.sh

USER jenkins
ENTRYPOINT "/entrypoint.sh"
~~~

### entrypoint.sh

- agent 실행 이후, dind 컨테이너가 실행될때까지 한시적 대기 (docker version 확인) 후 pipeline 작업을 시작한다.

~~~sh
#!/usr/bin/env bash

RETRIES=6

sleep_exp_backoff=1

for((i=0;i<RETRIES;i++)); do
    docker version
    dockerd_available=$?
    if [ $dockerd_available == 0 ]; then
        break
    fi
    sleep ${sleep_exp_backoff}
    sleep_exp_backoff="$((sleep_exp_backoff * 2))"
done

exec /usr/local/bin/jenkins-agent "$@"
~~~

### docker push

- 주의) 예제는 aws linux2 로 실행하기에 am64 환경에서 빌드해야함.

~~~sh
# build the image
$ docker build -t dind-client-jenkins-agent .
# tag the image
$ docker tag dind-client-jenkins-agent [docker_hub_id]/dind-client-jenkins-agent
# push the image
$ docker push [docker_hub_id]/dind-client-jenkins-agent
~~~







## ref

https://rokpoto.com/jenkins-docker-in-docker-agent/

http://cloudrain21.com/remove-docker-forcely-and-reinstall

https://docs.gitlab.com/ee/ci/docker/using_docker_build.html

https://senticoding.tistory.com/94

https://devwithpug.github.io/devops/docker-remote-ssh-and-tls/

















