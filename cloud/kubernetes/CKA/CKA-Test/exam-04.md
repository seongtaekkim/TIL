10번 ingress

2번



~~~
kubectl --context cluster1 get pod -n kube-system kube-apiserver-cluster1-controlplane  -o jsonpath='{.metadata.labels.component}'
~~~





### 8

~~~
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  creationTimestamp: "2023-07-12T16:18:01Z"
  generation: 3
  name: cyan-np-cka28-trb
  namespace: cyan-ns-cka28-trb
  resourceVersion: "14499"
  uid: fb82c12b-1714-4607-a898-f89a75fb1e7d
spec:
  egress:
  - ports:
    - port: 80
      protocol: TCP
    to:
    - ipBlock:
        cidr: 0.0.0.0/0
  ingress:
  - from:
    - namespaceSelector:
        matchLabels:
          kubernetes.io/metadata.name: default
      podSelector:
        matchLabels:
          app: cyan-white-cka28-trb
    ports:
    - port: 80
      protocol: TCP
  podSelector:
    matchLabels:
      app: cyan-app-cka28-trb
  policyTypes:
  - Ingress
  - Egress
status: {}
~~~

~~~
kubectl exec -it cyan-white-cka28-trb -- sh
curl cyan-svc-cka28-trb.cyan-ns-cka28-trb.svc.cluster.local
~~~

##   ingressClassName: nginx ???





### 10

- kubesystm coredns 가 없어서 접속이 안되느 경우가 발생함 꼭 확인!!

~~~
k get deploy -n kube-system
 kubectl scale --replicas=2 deployment coredns -n kube-system
~~~



### 15

- pv 만들때 특정 노드에 잇어야한다면 아래처럼 만들어야함

~~~
  nodeAffinity:
    required:
      nodeSelectorTerms:
        - matchExpressions:
            - key: kubernetes.io/hostname
              operator: In
              values:
                - cluster1-node01
~~~





### 20

ingress

~~~
kubectl get ingress # ip
ssh cluster3-controlplane
curl -I "ip"
~~~





## 17?

~~~
export IP_ADDR=$(ifconfig eth0 | grep inet | awk '{print $2}')
~~~



~~~
apiVersion: v1
kind: Endpoints
metadata:
  # the name here should match the name of the Service
  name: external-webserver-cka03-svcn
subsets:
  - addresses:
      - ip: $IP_ADDR
    ports:
      - port: 9999
~~~

