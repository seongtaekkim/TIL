내용정리 블로그

https://kdeon.tistory.com/62



~~~
set tabstop=2
set expandtab
set shiftwidth=2

echo 1 | tr -d '\n'
~~~



~~~
k config get-contexts -o name
kubectl config current-context
cat ~/.kube/config | grep current | sed -e "s/current-context: //"
~~~

​	

~~~
kubectl get events -A --sort-by=.metadata.creationTimestamp
kubectl get event --field-selector involvedObject.name=web-dp-cka06-trb-xxx
~~~



~~~
ssh cluster2-node1
crictl ps | grep kube-proxy
crictl stop 1e020b43c4423
crictl ps | grep kube-proxy
crictl inspect "container id" | grep runtimeType
~~~

~~~
k api-resources --namespaced
~~~



~~~
k run curl --image=alpine/curl --rm -it -- sh
curl np-test-service # hang 걸린다.

wget -O- -q 10.8.4.214

k run --image=busybox busybox -- sleep 4000
k exec -it busy -- nslookup 10-244-192-6.default.pod.cluster.local > nginx.pod
~~~



~~~
k get nodes -o json | jq | grep -i intera -B 100
k get nodes -o json | jq -c 'paths' | grep type # 모든 경로
k get nodes -o jsonpath='{.items[*].status.addresses[?(@.type=="InternalIP")].address}'

kubectl get pods -n spectra-1267 -o=custom-columns='POD_NAME:metadata.name,IP_ADDR:status.podIP' --sort-by=.status.podIP > /root/pod_ips_cka05_svcn
~~~





~~~
 journalctl -u kubelet --since "30 min ago" | grep 'Error:'
~~~

~~~
vi /etc/kubernetes/kubelet.conf # 6443
~~~





## ETCD 설치

~~~
ssh root@cluster2-controlplane

wget https://github.com/etcd-io/etcd/releases/download/v3.5.9/etcd-v3.5.9-linux-amd64.tar.gz

tar -xf etcd-v3.5.9-linux-amd64.tar.gz

cd etcd-v3.5.9-linux-amd64
~~~

~~~
mv etcd etcdctl  /usr/local/bin/
~~~



### restore

~~~
 ssh root@cluster1-controlplane
~~~

~~~
# 설치
# restore
etcdctl snapshot restore --data-dir /root/default.etcd /opt/cluster1_backup_to_restore.db 
~~~



## kubelet

~~~
whereis kubelet
실행위치
10-kubeadm.conf	
~~~





## kubeadm update



~~~
k get node

ssh cluster3-node2
kubeadm version
kubectl version --short
kubelet --version

kubeadm upgrade node # fail

apt-get update
apt show kubectl -a | grep 1.27
apt-get install kubectl=1.27.1-00 kubelet=1.27.1-00
kubelet --version
systemctl restart kubelet # fail

ssh cluster3-controlplane1
kubeadm token create --print-join-command #결과복사

ssh cluster3-node2
붙여넣기
sustemctl daemon-reload
systemctl restart kubelet
~~~





## certificate

~~~
ssh controlplane
find /etc/kubernetes/pki | grep apiserver
openssl x509 -noout -text -in /etc/kubernetes/pik/apiserver.crt | grep Validity -A2


kubeadm certs check-expiration | grep apiserver
~~~



~~~
ssh controlplane
openssl x509 -noout -text -in /var/lib/kubelet/pki/kubelet-client-current.pem | grep Iss -A1 -B1

openssl x509 -noout -text -in /var/lib/kubelet/pki/kubelet-client-current.pem | grep Exten -A1 -B1
~~~





~~~
k get nodes --kubeconfig /root/CKA/super.kubeconfig
~~~



## taint

~~~
kubectl taint nodes node01 env_type=production:NoSchedule
~~~





~~~
    env:                                                                          # add
    - name: MY_NODE_NAME                                                          # add
      valueFrom:                                                                  # add
        fieldRef:                                                                 # add
          fieldPath: spec.nodeName  
~~~

~~~
spec:
  containers:
  - args:
    - sh
    - -c
    - sleep 1d
    image: busybox:1.31.1
    name: secret-pod
    resources: {}
    env:                                  # add
    - name: APP_USER                      # add
      valueFrom:                          # add
        secretKeyRef:                     # add
          name: secret2                   # add
          key: user                       # add
    - name: APP_PASS                      # add
      valueFrom:                          # add
        secretKeyRef:                     # add
          name: secret2                   # add
          key: pass                       # add
    volumeMounts:                         # add
    - name: secret1                       # add
      mountPath: /tmp/secret1             # add
      readOnly: true                      # add
  dnsPolicy: ClusterFirst
  restartPolicy: Always
  volumes:                                # add
  - name: secret1                         # add
    secret:                               # add
      secretName: secret1                 # add
~~~





affility

~~~
  affinity:
    nodeAffinity:
      requiredDuringSchedulingIgnoredDuringExecution:
        nodeSelectorTerms:
        - matchExpressions:
          - key: node
            operator: In
            values:
            - cluster2-node01
~~~

- 해당 키벨류가 있는 노드에 세팅되겟다는 의미 그랫 ㅓ밑에처럼 레이블 생성 해주면 된다.

~~~
kubectl label node cluster2-node01 node=cluster2-node01
~~~



~~~
containers:
      - image: kodekloud/webapp-color
        name: webapp-color-wl10
        envFrom:
        - configMapRef: 
            name: webapp-wl10-config-map
~~~



~~~
 k get ingress
ssh cluster3-controlplane
curl -I  172.25.0.68 
~~~



~~~
  tolerations:                                 # add
  - effect: NoSchedule                         # add
    key: node-role.kubernetes.io/control-plane # add
  nodeSelector:                                # add
    node-role.kubernetes.io/control-plane: ""  # add
~~~



~~~
  containers:
  - image: nginx:1.16.1-alpine
    name: ready-if-service-ready
    resources: {}
    livenessProbe:                                      # add from here
      exec:
        command:
        - 'true'
    readinessProbe:
      exec:
        command:
        - sh
        - -c
        - 'wget -T2 -O- http://service-am-i-ready:80'   # to here
~~~





## 4  번 테스트



~~~
find /etc/systemd/system/ | grep kube 
~~~



~~~
CKA 팁

- 시험용 VM의 터미널에서 복사/붙여넣기 단축키는 Ctrl+SHIFT+C, Ctrl+SHIFT+V 를 이용해야 한다. 이 단축키는 오직 터미널 안에서만 동작한다. 터미널 바깥에서는 Ctrl+C, Ctrl+V 를 이용한다. 단축키가 잘 듣지 않을 때엔 마우스 오른클릭을 이용하는 것이 좋다.
- 시험 감독관은 이슈 사항에 대한 대처가 부족하다… 시험 감독관에게 문의할 게 아니라 상단에 있는 LIVE CHAT을 통해 Support 팀에 요청하자!
- Applications 메뉴 > Accessories > vim 편집기 사용하면 메모장 대용으로 쓸만한데 etcd 백업 및 복구 문제나 Control Plane Upgrade 문제 말고는 쓸 일이 거의 없다. 차라리 yaml 파일로 바로 생성해 버리자!
- VM에 포함되어 있는 텍스트 편집기(Mousepad)를 이용하자. 공식 문서의 샘플 YAML을 여기로 복사해서 편집한 뒤, 터미널의 vi 또는 vim 화면에 붙여넣는 식으로 활용할 수 있다.
- VM 화면의 우측 상단에 보면 여러 개의 작업공간(Workspace)을 만들어 전환할 수 있는 버튼이 있다. 이걸 잘 활용하면 문서 참고용 Firefox 화면, YAML 편집 화면, 터미널 화면을 각각 넓게 구성해서 쓸 수 있다.

- kubectl Cheat Sheet를 반드시 참고하자
- JSONPath 익히기 (JSONPath Support, 쿠버네티스에서 JSON 데이터 처리를 위한 JSONPath 사용법)

- 시험 환경 리눅스에서 쿠버네티스 공식 문서에서 검색해서 나오는 블로그 글들(쿠버네티스 포럼) 또한 접속이 가능하다
- 가능하다면 선언형으로 생성합니다(선언형을 모르겠다면 k create deploy —help ) 
-  yaml 파일을 직접 만드는것보단 run / create 명령에서 —dry-run=client -o yaml 로 떨궈서 수정하는게 좋다
- svc 만드는건 expose 명령 후 yaml로 떨궈서 수정하자
- yaml 떨구는거 example
# image와 이름이 nginx인 pod yaml 파일 생성
k run nginx —image nginx $do > nginx.yaml

# image가 nginx고 replica가 3인 deployment yaml 생성
k create deploy —image nginx —replicas 3 $do > nginx-deploy.yaml

# nginx deploy를 expose 하는 svc yaml 생성
k expose deploy/nginx —port 80 $do > nginx-svc.yaml
- 


숙지 사항
* 각 문제마다 kubectl config user-context 명령을 통해 매번 특정 컨텍스트로 전환해야 하므로, 컨텍스트 전환이 제대로 이뤄졌는지 꼭 확인해야 합니다.
* 문제 하단의 flag 기능을 통해 잘 풀리지 않거나 오래 걸릴 것으로 예상되는 문제들은 체크해놓고 다른 문제를 푼 후에 다시 확인합니다.
* 특정 노드에 ssh 명령을 통해 접속하였다면, 반드시 logout하여 기존의 노드로 돌아온 것을 확인 후 문제를 풀어나가야 합니다.





빈출 유형


- Kubernetes 클러스터 업그레이드
Kubernetes 클러스터의 구성 요소인 kubeadm, kubelet, kubectl를 특정 버전으로 업그레이드하는 문제 유형입니다. 업그레이드 전 drain, uncordon을 통한 노드 스케줄링을 적절히 활용해야 하며, 많은 시간이 소요되는 문제(약 10분)이므로 가장 마지막에 푸는 것을 추천드립니다.

클러스터 업그레이드를 할 때 반드시 업그레이드 순서를 지켜야 합니다. 아래 URL을 참고하여 연습해보시기 바랍니다.
kubernetes Cluster Maintenance 정리

참고 자료: https://kubernetes.io/docs/tasks/administer-cluster/kubeadm/kubeadm-upgrade/


- RBAC(Role Based Access Control)
RBAC를 기반으로 하는 ServiceAccount를 생성하는 문제 유형입니다.
일반적으로 ServiceAccount 생성 → Role or Clusterrole 생성 → Rolebinding or Clusterrolebinding 적용 순으로 문제를 풀어나가시면 됩니다.
kubectl auth can-i 명령을 통해 권한이 제대로 설정되었는지 꼭 확인하고 넘어가시는 것을 추천드립니다.
참고 자료: https://velog.io/@khyup0629/K8S-RBAC-%EC%9D%98-%EA%B0%9C%EB%85%90%EA%B3%BC-Role-RoleBinding-ClusterRole-ClusterRoleBinding


- ETCD Backup & Restore
ETCD를 통해 이전 클러스터 상태를 backup하고 복원하는 문제 유형입니다. /etc/kubernetes/manifests/etcd.yaml에 정의된 인증서 및 key 파일의 경로를 참조하여 etcdctl 명령을 통해 backup 및 복원을 진행해주시면 됩니다.
자세한 내용은 해당 블로그에 아주 잘 정리되어 있으므로, 해당 내용만 숙지하셔도 무리없이 푸실 수 있습니다.


- Troubleshooting
Kubernetes 클러스터에서 발생할 수 있는 여러 가지 오류 상황을 적절히 해결해야 하는 문제 유형입니다. 일반적으로 애플리케이션 내부의 오류보다는 노드의 오류를 수정하는 유형이 더 많이 출제되므로, Mumshad 강의 섹션 13의 Control Plane Failure, Worker Node Failure 문제를 반복적으로 풀어보시는 것을 추천드립니다.


- Sidecar 컨테이너 구축
기존에 존재하는 파드에 사이드카 컨테이너를 추가하여 로그 수집과 같은 보조 역할을 수행하는 멀티 컨테이너 파드를 생성하는 문제 유형입니다. 사이드카 컨테이너에서 실행할 command, 로그를 저장할 volume 등의 옵션을 적절히 설정하고, kubectl exec 명령을 통해 command가 컨테이너 내에서 정상적으로 실행되는지 확인하는 방식으로 문제를 푸시면 됩니다. 주의해야 할 점은 직접 기존 파드의 yaml 파일을 수정하는 과정에서 에러가 발생할 경우 복구하는 데에 시간이 많이 소요될 수 있으므로, 기존의 파드를 미리 백업용으로 yaml로 저장한 이후에 컨테이너를 추가하는 작업을 진행하는 것을 권장드립니다.


- Ingress
네트워크 규칙을 정의하고 해당 규칙을 기반으로 한 Ingress를 생성하는 문제 또한 자주 출제됩니다. 문제에 명시된 namespace, 포트 번호, prefix, label과 같은 조건들을 꼼꼼히 살펴보신 후에 해당 조건을 기반으로 하는 yaml 파일을 통해 Ingress를 생성하시면 됩니다. Ingress 오브젝트의 yaml file은 다른 오브젝트에 비해 depth가 깊기 때문에 yaml file 작성시 특히 들여쓰기에 신경을 써주셔야 합니다.


- jsonpath
jsonpath는 kubernetes에서 지원하는 템플릿의 일종입니다. yaml 파일 내부의 특정 필드를 필터링하는 데에 유용하게 사용됩니다. jsonpath를 이용하여 특정 조건에 부합하는 필드를 파일을 통해 추출하는 방식의 문제 또한 빈번하게 출제되므로, 실습을 통해 숙달하시는 것을 추천드립니다.
참고 자료: https://kubernetes.io/ko/docs/reference/kubectl/jsonpath/

~~~









