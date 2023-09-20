##  리소스 로깅과 모니터링 과정 소개

- 여기까지오면 기초는 한거임
- 서드파트라 좀 어려움
- 대충이지만 잘하자



## 쿠버네티스 모니터링 시스템과 아키텍처





## 메트릭스 서버 설치 및 kubectl top 명령



~~~
git clone https://github.com/kubernetes-sigs/metrics-server
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/download/v0.3.7/components.yaml
~~~



- 아래 명령어 수행

~~~
kubectl get pod -n kube-system
kubectl edit deploy -n kube-system metrics-server
~~~

![스크린샷 2023-06-16 오후 3.15.31](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-16 오후 3.15.31.png)

- 옵션 수정 후 아래와 같이 metrics 가 출력된다.

~~~
$ kubectl top pod
NAME    CPU(cores)   MEMORY(bytes)   
count   15m          9Mi 

$ kubectl top nodes
NAME                                       CPU(cores)   CPU%   MEMORY(bytes)   MEMORY%   
gke-cluster-1-default-pool-cb7d7c70-00jc   103m         10%    923Mi           32%       
gke-cluster-1-default-pool-cb7d7c70-4gwf   118m         12%    905Mi           32%       
gke-cluster-1-default-pool-cb7d7c70-xhdv   92m          9%     902Mi           32%     

$ kubectl top pod --all-namespaces
NAMESPACE       NAME                                                  CPU(cores)  MEMORY(bytes)   
default         count                                                 1m           9Mi             
ingress-nginx   ingress-nginx-controller-6dbd5f6448-jdfzc             3m           69Mi            
kube-system     event-exporter-gke-755c4b4d97-w9dmp                   1m           20Mi            
kube-system     fluentbit-gke-drcfw                                   7m           25Mi 
...
~~~



## 쿠버네티스 애플리케이션 로그 관리



kubectl -> apiserver 에 질의하는 구조인데 apiserver가 응답하지 않는 경우 (인증만료, static pod argugument 잘못 설정된 경우)

docker logs 를 통해서 확인이 가능하다. pod 가 재시작되면 comtainer가 부숴졌다가 재시작되므로 재빨리 조회해야한다.





docker container 의 로그위치

- master-1

~~~
journalctl -u kubelet -f # kubelet 의 로그를 볼 수 있다. 
/var/log/containers # 상세한 로그를 볼 수 있다.
~~~





- pstree # process tree

![스크린샷 2023-06-16 오후 3.30.19](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-16 오후 3.30.19.png)





- 나중에 해보자

![스크린샷 2023-06-16 오후 3.28.44](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-16 오후 3.28.44.png)



## 큐브 대시보드 설치와 사용









- 아래 명령어에서 token이 있어야 되는데 없다... 

~~~
seongtki@master-0:/$ kubectl get secret -n kubernetes-dashboard
NAME                              TYPE     DATA   AGE
kubernetes-dashboard-certs        Opaque   0      4m18s
kubernetes-dashboard-csrf         Opaque   1      4m18s
kubernetes-dashboard-key-holder   Opaque   2      4m18s
~~~

![스크린샷 2023-06-16 오후 3.54.40](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-16 오후 3.54.40.png)





~~~
  150  kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.7.0/aio/deploy/recommended.yaml
  151  kubectl get all -n kubernetes-dashboard
  152  kubectl edit service/kubernetes-dashboard -n kubernetes-dashboard
  153  kubectl get all -n kubernetes-dashboard
  154  curl 127.0.0.1:31747
  155  curl https://127.0.0.1:31747
  156  kubectl get sa -n kubernetes-dashboard
  157  kubectl describe sa -n kubernetes-dashboard
  158  kubectl get sa -n kubernetes-dashboard
  159  kubectl get sa -n kubernetes-dashboard kubernetes-dashboard
  160  kubectl get sa -n kubernetes-dashboard kubernetes-dashboard -o yaml
  161  kubectl get secret -n kubernetes-dashboard
  162  kubectl get sa -n kubernetes-dashboard
  163  kubectl get sa -n kubernetes-dashboard kubernetes-dashboard
  164  kubectl get secret -n kubernetes-dashboard kubernetes-dashboard
  165  kubectl  secret -n kubernetes-dashboard kubernetes-dashboard
  166  history
  
~~~









## 프로메테우스 그라파나를 활용한 리소스 모니터링



[참고 사이트](https://blog.naver.com/isc0304/222515904650)



~~~
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add grafana https://grafana.github.io/helm-charts
helm repo update
helm repo list
mkdir grafana_prometheus
cd grafana_prometheus
~~~



- pv를 구성하여 스토리지를 생성.

~~~
cat <<EOF > values-prometheus.yaml
server:
  enabled: true

  persistentVolume:
    enabled: true
    accessModes:
      - ReadWriteOnce
    mountPath: /data
    size: 100Gi
  replicaCount: 1

  ## Prometheus data retention period (default if not specified is 15 days)
  ##
  retention: "15d"
EOF

~~~





- . pvc를 구성하여 스토리지를 구성하여 설정정보를 유지할 수 있도록 구성.
-  아이디 패스워드는 admin//test1234!234로 구성한다.
- 서비스가 생성될 때 접속하기 쉽도록 로드 밸런서로 구성.
- 실무에서는 모니터링 서비스가 외부로 노출되지 않도록 해야한다.

~~~
cat << EOF > values-grafana.yaml
replicas: 1

service:
  type: LoadBalancer

persistence:
  type: pvc
  enabled: true
  # storageClassName: default
  accessModes:
    - ReadWriteOnce
  size: 10Gi
  # annotations: {}
  finalizers:
    - kubernetes.io/pvc-protection

# Administrator credentials when not using an existing secret (see below)
adminUser: admin
adminPassword: test1234!234
EOF

~~~





- helm 으로 생성한 yaml 을 배포

~~~
kubectl create ns prometheus
helm install prometheus prometheus-community/prometheus -f values-prometheus.yaml -n prometheus
helm install grafana grafana/grafana -f values-grafana.yaml -n prometheus
~~~



~~~
$ kubectl get pod -n prometheus
NAME                                                 READY   STATUS    RESTARTS   AGE
grafana-7b648f487d-g98hm                             0/1     Running   0          23s
prometheus-alertmanager-0                            1/1     Running   0          42s
prometheus-kube-state-metrics-5fb6fbbf78-kp885       1/1     Running   0          43s
prometheus-prometheus-node-exporter-222sn            1/1     Running   0          44s
prometheus-prometheus-node-exporter-gxskx            1/1     Running   0          44s
prometheus-prometheus-node-exporter-pg4xx            1/1     Running   0          44s
prometheus-prometheus-pushgateway-7d55869d46-g2hk8   1/1     Running   0          43s
prometheus-server-74cb675c7d-fh7t2                   1/2     Running   0          43s

$ kubectl get svc -n prometheus                                          
NAME                                  TYPE           CLUSTER-IP    EXTERNAL-IP   PORT(S)        AGE
grafana                               LoadBalancer   10.8.6.245    <pending>     80:31163/TCP   37s
prometheus-alertmanager               ClusterIP      10.8.2.42     <none>        9093/TCP       58s
prometheus-alertmanager-headless      ClusterIP      None          <none>        9093/TCP       58s
prometheus-kube-state-metrics         ClusterIP      10.8.14.24    <none>        8080/TCP       58s
prometheus-prometheus-node-exporter   ClusterIP      10.8.13.36    <none>        9100/TCP       58s
prometheus-prometheus-pushgateway     ClusterIP      10.8.3.220    <none>        9091/TCP       58s
prometheus-server                     ClusterIP      10.8.15.224   <none>        80/TCP         58s

$ kubectl get svc -n prometheus
NAME                                  TYPE           CLUSTER-IP    EXTERNAL-IP       PORT(S)        AGE
grafana                               LoadBalancer   10.8.6.245    173.255.115.213   80:31163/TCP   86s
prometheus-alertmanager               ClusterIP      10.8.2.42     <none>            9093/TCP       107s
prometheus-alertmanager-headless      ClusterIP      None          <none>            9093/TCP       107s
prometheus-kube-state-metrics         ClusterIP      10.8.14.24    <none>            8080/TCP       107s
prometheus-prometheus-node-exporter   ClusterIP      10.8.13.36    <none>            9100/TCP       107s
prometheus-prometheus-pushgateway     ClusterIP      10.8.3.220    <none>            9091/TCP       107s
prometheus-server                     ClusterIP      10.8.15.224   <none>            80/TCP         107s

$ kubectl get svc -n prometheus
NAME                                  TYPE           CLUSTER-IP    EXTERNAL-IP       PORT(S)        AGE
grafana                               LoadBalancer   10.8.6.245    173.255.115.213   80:31163/TCP   116s
prometheus-alertmanager               ClusterIP      10.8.2.42     <none>            9093/TCP       2m17s
prometheus-alertmanager-headless      ClusterIP      None          <none>            9093/TCP       2m17s
prometheus-kube-state-metrics         ClusterIP      10.8.14.24    <none>            8080/TCP       2m17s
prometheus-prometheus-node-exporter   ClusterIP      10.8.13.36    <none>            9100/TCP       2m17s
prometheus-prometheus-pushgateway     ClusterIP      10.8.3.220    <none>            9091/TCP       2m17s
prometheus-server                     ClusterIP      10.8.15.224   <none>            80/TCP         2m17s
~~~

- loadbalancer에의해 생성된 외부아이피와 포트로 접속가능하다.



- datasource를 prometheus로 설정한다.

![스크린샷 2023-06-16 오후 4.13.48](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-16 오후 4.13.48.png)





- 상단에 + 버튼클릭 후 import 를 통해 이미 만들어진 대쉬보드를 불러올 수 있는데,
- 315가 괜찮다
- 13770은 한국인이 만든것 중 괜찮은 구성정보.

![스크린샷 2023-06-16 오후 4.16.51](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-16 오후 4.16.51.png)

![스크린샷 2023-06-16 오후 4.17.04](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-16 오후 4.17.25.png)





## Istio를 활용한 네트워크 메시 모니터링



[구성사이트](https://blog.naver.com/isc0304/221892105612)



~~~
curl -L https://istio.io/downloadIstio | sh -
  cd istio-1.18.0/
   export PATH=$PWD/bin:$PATH
   
    istioctl # kubect
     istioctl install --set profile=demo --skip-confirmation
     kubectl get pod
     kubectl get svc
~~~







배포가 두개씩 되었다.

하나는 원래 프로젝트에서 사용, 하나는 프록시 역할을 하는 pod 이다.

~~~
$ kubectl label namespace default istio-injection=enabled
kubectl apply -f samples/bookinfo/platform/kube/bookinfo.yaml
kubectl apply -f samples/bookinfo/networking/bookinfo-gateway.yaml

chxortnl@cloudshell:~/istio-1.18.0 (staek-2023-04-08)$ kubectl get pod -n istio-system
NAME                                    READY   STATUS    RESTARTS   AGE
istio-egressgateway-fcbd87c44-fcd8h     1/1     Running   0          3m50s
istio-ingressgateway-6986b4768d-5gcq4   1/1     Running   0          3m50s
istiod-66b8c7bfb-87jg7                  1/1     Running   0          3m58s

chxortnl@cloudshell:~/istio-1.18.0 (staek-2023-04-08)$ kubectl get svc -n istio-system
NAME                   TYPE           CLUSTER-IP    EXTERNAL-IP      PORT(S)                                                                      AGE
istio-egressgateway    ClusterIP      10.8.10.150   <none>           80/TCP,443/TCP                                                               4m26s
istio-ingressgateway   LoadBalancer   10.8.14.69    35.225.155.170   15021:31593/TCP,80:31349/TCP,443:32006/TCP,31400:30374/TCP,15443:31965/TCP   4m26s
istiod                 ClusterIP      10.8.15.106   <none>           15010/TCP,15012/TCP,443/TCP,15014/TCP                                        4m34s

~~~



- /productpage 입력 해야 함

![스크린샷 2023-06-16 오후 6.38.15](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-16 오후 6.38.15.png)



~~~
kubectl apply -f samples/addons/kiali.yaml
kubectl apply -f samples/addons/prometheus.yaml

~~~







~~~
$ istioctl dashboard kiali 
                                                                 
http://localhost:20001/kiali
Failed to open browser; open http://localhost:20001/kiali in your browser.
~~~



- gcp cloud shell 웹미리보기에서 선택하면 kiali console에 접근할 수 있다/

![스크린샷 2023-06-16 오후 6.43.34](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-16 오후 6.43.34.png)



- 데이터 수집할 namespace 를 수집하고

- /productpage 에 계속 접근하면, 프록시가 데이터를 수집하므로
- 그래프가 형성된다.

![스크린샷 2023-06-16 오후 6.45.08](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-16 오후 6.45.08.png)



## EFK를 활용한 k8s 로그 모니터링 개요와 시스템 구축





### 리소스문제인지 실행이 안됨 ㅠㅠ



pv가 없어 도되는 형태로 yaml을 구성해 주었다.

하고 싶다면 따로 해볼것 



~~~
wget https://download.blog.naver.com/open/44d158e8fda3a07c50bed1efdc3b443c99ca36d0df/zBoum6h-tQRSGn8rvXiHce_Xeea04i_3FvEhhNcpe8LGbdIIgk4deSDfz_58UsNHJ7C-92_XM-SvO8ZnhIu1PQ/efk.zip
~~~



## 키바나 대시보드를 활용한 로그 시각화



### 리소스문제인지 실행이 안됨 ㅠㅠ





## Jaeger를 활용한 애플리케이션 트레이싱 튜토리얼

- cncf 프로젝트

### docker 가 안되는데 나중에 다시 ㄱㄱ











































































