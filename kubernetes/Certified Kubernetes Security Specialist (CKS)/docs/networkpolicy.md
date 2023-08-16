

# networkpolicy





## Protect node metadata and endpoints

- NetworkPolicy 설정해서  서버 접근 못하게 하는 문제. 네임스페이스 만 허용하도록 하는 변형 가능해보임.
- 클라우드 프로바이더 metadata 서버 쪽으로 접근 못하게 설정. `podSelector`를 통해 특정 파드는 접근 열 수 있음.

- Restrict control plane ports (6443, 2379, 2380, 10250, 10251, 10252)
- Restrict worker node ports(10250, 30000-32767)
- for Cloud, Using Kubernetes network policy to restrict pods access to cloud metadata

### 169.254.169.254 만 접근가능한 정책

```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: deny-only-cloud-metadata-access
spec:
  podSelector: {}
  policyTypes:
  - Egress
  egress:
  - from:
    - ipBlock:
      cidr: 0.0.0.0/0
      except:
      - 169.254.169.254/32
```

- https://kubernetes.io/docs/tasks/administer-cluster/securing-a-cluster/#restricting-cloud-metadata-api-access







## Use Network security policies to restrict cluster level access

### default-deny-all

```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: default-deny-all
spec:
  podSelector: {}
  policyTypes:
  - Egress
  - Ingress
```



### Create default deny all NetworkPolicy & allow required traffic

```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: default-deny-ingress
  namespace: default
spec:
  podSelector: {}
  policyTypes:
  - Ingress
```



### Create ingress/egress NetPol - for ns, pod, port matching rules

```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: test-network-policy
  namespace: default  #target namespace
spec:
  podSelector:  #target pod
    matchLabels:
      role: db
  policyTypes:
  - Ingress
  - Egress
  ingress:
  - from:
    - ipBlock:
        cidr: 172.17.0.0/16
        except:
        - 172.17.1.0/24
    - namespaceSelector:
        matchLabels:
          project: myproject
    - podSelector:
        matchLabels:
          role: frontend
    ports:
    - protocol: TCP
      port: 6379
  egress:
  - to:
    - ipBlock:
        cidr: 10.0.0.0/24
    ports:
    - protocol: TCP
      port: 5978
      endPort: 6000
```

- [reference](https://kubernetes.io/docs/concepts/services-networking/network-policies/)

- [networkpolicy editor](https://editor.cilium.io/)






## Minimize external access to the network

- by default any one has access cluster can comminicate all pods and services
- by defualt limit access to cluster  from outside
- All pods can talk to all pods in all namespaces

```yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: deny-external-egress
spec:
  podSelector: {}
  policyTypes:
  - Egress
  egress:
    to:
    - namespaceSelector: {}
```







