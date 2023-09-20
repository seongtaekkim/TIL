

### 7

pod, secret 에 sa 가 perform을 못한다는데 이게 무슨말?

- role 을 확인하고 거기에 권한들을 추가해 준다.

~~~
kubectl get rolebinding -o yaml | grep -B 5 -A 5 thor-cka24-trb

~~~





### 8



### 15

아니 이게 ㅔ뭐지

~~~
error: a container name must be specified for pod olive-app-cka10-str-577ff99458-6mn5j, choose one of: [python sidecar]

~~~



### 17

~~~
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: tester-cka02-svcn
  name: tester-cka02-svcn
spec:
  containers:
  - image: registry.k8s.io/e2e-test-images/jessie-dnsutils:1.3
    name: tester-cka02-svcn
    resources: {}
    command: ['sleep','3600']
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}

~~~



~~~
   k exec -it tester-cka02-svcn -n dev-cka02-svcn -- nslookup kubernetes.default > /root/dns_output
~~~



### 18

!!!!!!!!!!!

~~~
kubectl get pods --show-labels -n spectra-1267
kubectl get pod -l mode=exam,type=external -n spectra-1267           kubectl create service clusterip service-3421-svcn -n spectra-1267 --tcp=8080:80 --dry-run=client -o yaml > service-3421-svcn.yaml                     
~~~

- 실행 후
- selector에 exam, type 두개를 추가해준다



~~~
 k get ep service-3421-svcn -n spectra-1267
~~~

~~~
  kubectl get pods -n spectra-1267 -o=custom-columns='POD_NAME:metadata.name,IP_ADDR:status.podIP' --sort-by=.status.podIP
~~~























### 20

## expose!!

~~~
 kubectl expose -n app-space deployment webapp-wear-cka09-svcn --type=LoadBalancer --name=wear-service-cka09-svcn --port=8080	
~~~



~~~apiVersion: v1
kind: Service
metadata:
  creationTimestamp: "2023-07-11T18:19:09Z"
  name: wear-service-cka09-svcn
  namespace: app-space
  resourceVersion: "2402"
  uid: 16037de8-8d95-4930-8f0d-a848d942a2bd
spec:
  allocateLoadBalancerNodePorts: true
  clusterIP: 10.43.187.19
  clusterIPs:
  - 10.43.187.19
  externalTrafficPolicy: Cluster
  internalTrafficPolicy: Cluster
  ipFamilies:
  - IPv4
  ipFamilyPolicy: SingleStack
  ports:
  - nodePort: 30590
    port: 8080
    protocol: TCP
    targetPort: 8080
  selector:
    app: webapp-wear-cka09-svcn
  sessionAffinity: None
  type: LoadBalancer
status:
  loadBalancer:
    ingress:
    - ip: 172.25.0.7
~~~













