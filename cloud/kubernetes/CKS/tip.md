1. 시험 준비
    1. 허용된 사이트
        1. https://docs.linuxfoundation.org/tc-docs/certification/certification-resources-allowed#certified-kubernetes-security-specialist-cks
    2. 숙지하고 봐야 할 것 (별 다섯개)
        1. Kube apiserver trouble shooting
        2. [Audit](https://kubernetes.io/docs/tasks/debug/debug-cluster/audit/)
        3. [ImageWebhookPolicy](https://kubernetes.io/docs/reference/access-authn-authz/admission-controllers/#imagepolicywebhook)
        4. [Falco](https://falco.org/docs/)
        5. [Trivy](https://aquasecurity.github.io/trivy/v0.38/tutorials/additional-resources/cks/)
        6. [AppArmor](https://gitlab.com/apparmor/apparmor/-/wikis/Documentation)
        7. [Encryption Data](https://kubernetes.io/docs/tasks/administer-cluster/encrypt-data/)
        8. [NetworkPolicy](https://kubernetes.io/docs/concepts/services-networking/network-policies/)
2. 팁팁!!
    1. kube-apiserver 를 edit 할 경우 반드시 백업을 해놓을 것! 그래야 문제 생겼을 시 쉽게 롤백이 가능
    2. grep 명령어, jq 명령어 등 리눅스 명령어를 잘 활용하기
    3. 내가 쓰고 있는 클러스터가 무엇인지 확인하기
        1. `k config current-context`
    4. JSON Path 사용법을 잘 익혀두기
       
        ```bash
        # 시크릿의 데이터 확인하기
        $ k get secret my-secret -o jsonpath='{.data}'
        
        # 네임스페이스 내 파드가 사용하고 있는 이미지 목록 확인하기
        $ k get pods -n my-namespace -o jsonpath='{range .items[*]}{"\n"}{.metadata.name}{":\t"}{range .spec.containers[*]}{.image}{", "}{end}{end}' |\
        sort
        ```
        
    
    e. kube-apiserver가 죽는다고 당황하지 말기
    
    f. 실습 위주로 공부. 4~5번 정도 반복하니 나중에는 K8s Reference 없이도 해결 가능할만큼 익숙해졌으며, 이는 실제 시험에서도 큰 도움이 되었다. 그리고 시간 단축을 위해 YAML 파일을 직접 작성하는 대신 'kubectl run' 또는 'kubectl create' 명령어와 더불어 '-oyaml', '--dry-run=client' 옵션을 최대한 활용
    
    g. 시험에는 gviser 설정, trivy 돌려보기, docker 및 pod 취약점 있을 만한 것 찾기, pod security policy 구성, RBAC role / clusterrole binding, OPA, CKA 에 자주 등장한 network policy, ETCD를 이용한 secret 찾기 base64 decode 하기 등
    
    공부했던 거의 모든 부분이 출제 되었습니다. 특히 kube-apiserver 취약 설정 찾아내서 고치는 부분의 경우 manifest 의 pod이 재시작 되고 항상 잘 기동되길 바라는 마음을 가져야 했습니다.
    
    docker 가 아닌 crictl 로 구동 되기에 `watch -n 0.5 crictl ps` 명령을 자주 사용 하여 kube-apiserver 가 재기동 되는 순간을 알아낼 수 있었습니다. 지금에서야 느끼는데 kube-apiserver 가 죽는데는 거의 10~15 초가 걸리고, 되살아나는대는 2~3초 밖에 안 걸린다는 점입니다. udemy 강사는 4~5 초만에 재기동 되는 것 같은데.. 시험보는 VM 서버는 사양이 좋지 않은 모양입니다. 그래서 manifest 를 수정 후, `watch -n 0.5 crictl ps` 를 치면서 그야말로 그냥 기다리는 시간은 좀 아깝다고 느껴졌습니다. (그래서 다음 문제를 먼저 살펴보기도 했습니다.)
    
    h. 이번에는 파이어폭스 ctrl + f 써봐!! -, + 로 화면 크기 조절 가능
    
    i. `Copy  = Ctrl+SHIFT+C (inside the terminal)`
    
    `Paste = Ctrl+SHIFT+V (inside the terminal)`
    OR `Use the Right Click Context Menu and select Copy or Paste`
    
    j. `translation tool (Firefox Simple Translate Extension)` is also installed in the **Firefox Browser of our Remote Desktop**. (번역 프로그램 설치되어있다)
    
    k. 시험을 치르게 되면 각 문제 하단에 k8s resource 를 생성할 수 있는 템플릿이 제공됩니다.
    
    Remote deskop 의 firefox 에서 검색하는것보다 해당 템플릿을 빠르게 복사하여 리소스를 생성하는것이 시간을 아낄 수 있으므로 이 방법을 적극 활용하시면 좋습니다. (복붙을 잘하면 시간을 많이 아낄 수 있다)
    
3. 예제 문제 스타일 파악
    1. https://www.study4exam.com/linux-foundation/free-cks-questions
    2. https://cwal.tistory.com/category/Kubernetes/Certificates
    3. https://github.com/abdennour/certified-kubernetes-security-specialist

1. 추천 강의
    1. https://www.udemy.com/course/certified-kubernetes-security-specialist/
2. 기타
    1. **시험전 확인**
        - kubectl bash autocompletion 동작 확인
            - https://kubernetes.io/docs/reference/kubectl/cheatsheet/
        - tmux 동작 확인
            - https://linuxize.com/post/getting-started-with-tmux/
    2. **시험중 알아야할 명렁어**
    - kubectl
        - Resoruce API Version 확인 : `kubectl api-resources`
        - Resource Spec/Status 확인 : `kubectl explain –recursive {resource}`
    - AppArmor
        - Profile 적용 : `apparmor_parser {profile_path}`
        - Profile 확인 : `aa-status | grep {profile_name}`
    - kubesec
        - Resource 검사 : `kubesec scan {resource}`
    - Trivy
        - Image 검사 : `trivy image –severity {UNKNOWN,LOW,MEDIUM,HIGH,CRITICAL} {image_name}`
        - Tar Image 검사 : `trivy image –severity {UNKNOWN,LOW,MEDIUM,HIGH,CRITICAL} –input {image_tar}`
    - Falco
        - Falco 시작 : `systemctl start falco`
        - Falco Config 설정 변경 : `vim /etc/falco/falco.yaml`
        - Falco Config 설정 변경 적용 : `systemctl restart falco`
        - Falco Rule 추가/변경 : `vim /etc/falco/falco_rules.local.yaml`

~~~
curl https://kubernetes.default/api/v1/secrets/namespaces/restricted -H
"Authrization: Bearer $(cat /run/secrets/kubernetes.io/serviceaccount/token)" -k
~~~



~~~
curl https://kubernetes.default/api/v1/namespaces/reawe/secets -h
~~~



~~~
https://killer.sh/attendee/c9a6d42d-194e-4b79-8062-9f643ceee6b0/content
~~~









