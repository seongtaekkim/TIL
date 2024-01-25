# helm jenkins values



- helm 에서 jenkins 실행에 대한 옵션을 설정한다.

- [jenkins install](https://www.jenkins.io/doc/book/installing/kubernetes/) 에서 제공하는 [jenkins vaules](https://raw.githubusercontent.com/jenkinsci/helm-charts/main/charts/jenkins/values.yaml) 파일을 수정하였다.





~~~yaml
serviceType: LoadBalancer
~~~

- nlb, subnet을 작성한다.

~~~yaml
  serviceAnnotations:
    service.beta.kubernetes.io/aws-load-balancer-type: "nlb"
    service.beta.kubernetes.io/aws-load-balancer-subnets: subnet-03448e2baf0a8cd9c,subnet-0e3e88be22c01fb5a
~~~

- 외부 접속 포트 작성

~~~sh
servicePort: 80
~~~



- 미리 설치할 플러그인 작성

~~~yaml
  installPlugins:
    - kubernetes:4174.v4230d0ccd951
    - workflow-aggregator:596.v8c21c963d92d
    - git:5.1.0
    - configuration-as-code:1670.v564dc8b_982d0
~~~



- dind를 위한 agent 작성

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



- 요구 리소스 작성

~~~yaml
  resources:
    requests:
      memory: "512Mi"
      cpu: "512m"
    limits:
      memory: "2048Mi"
      cpu: "1024m"
~~~



- 앞에서 만든 pvc, sc 등을 작성.

~~~yaml
persistence:
  enabled: true
  existingClaim: jenkins-pvc
  storageClass: jenkins-sc
  accessMode: "ReadWriteOnce"
  size: "10Gi"
~~~



- 이미 sa를 만들었기 때문에 false로 설정.

~~~yaml
serviceAccount:
  create: false
~~~









## ref

https://ekwkqk12.tistory.com/36

https://velog.io/@aylee5/EKS-Helm%EC%9C%BC%EB%A1%9C-Jenkins-%EB%B0%B0%ED%8F%AC-MasterSlave-%EA%B5%AC%EC%A1%B0-with-Persistent-VolumeEBS

















