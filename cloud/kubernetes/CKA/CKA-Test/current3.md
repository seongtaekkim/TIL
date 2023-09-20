### 1

~~~
k create secret generic secure-sec-cka12-arch -n secure-sys-cka12-arch --from-literal coler=darkblue --context cluster3
~~~



## clustr 를 붙여야 한다.

~~~
kubectl create secret generic secure-sec-cka12-arch --from-literal=color=darkblue -n secure-sys-cka12-arch --context cluster3
~~~





### 2

~~~
Find the pod that consumes the most memory and store the result to the file /opt/high_memory_pod in the following format cluster_name,namespace,pod_name.

The pod could be in any namespace in any of the clusters that are currently configured on the student-node.


~~~



~~~
k top pod -A --context cluster3
~~~







### 3

~~~
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
  name: pink-role-cka24-arch
aggregationRule:
  clusterRoleSelectors:
  - matchLabels:
      rbac.example.com/aggregate-to-monitoring: "true"
rules: [] 
~~~

~~~
k create clusterrolebinding pink-role-binding-cka24-arch --clusterrole=pink-role-cka24-arch --serviceaccount=default:pink-sa-cka24-arch
~~~



~~~
student-node ~ ➜  kubectl --context cluster1 auth can-i get deployments --as=system:serviceaccount:default:deploy-cka20-arch
yes
~~~



## 전체권한 이렇게

~~~
kubectl --context cluster1 create clusterrole pink-role-cka24-arch --resource=* --verb=*
~~~





### 4

~~~
 k logs beta-pod-cka01-arch -n beta-cka01-arch | grep ERROR: > /root/beta-pod-cka01-arch_errors
~~~



### 5

~~~
 k logs logger-complete-cka04-arch  > /root/logger-complete-cka04-arch
~~~



### 6

~~~
 k create clusterrolebinding deploy-cka19-trb-rolebinding --clusterrole=deploy-cka19-trb-role --serviceaccount=default:deploy-cka19-trb
~~~

~~~
 k auth can-i get deployments --as=system:serviceaccount:default:deploy-cka19-trb
~~~



### 7

ingress ?

~~~
There is a deployment called nodeapp-dp-cka08-trb created in the default namespace on cluster1. This app is using an ingress resource named nodeapp-ing-cka08-trb.


From cluster1-controlplane host we should be able to access this app using the command: curl http://kodekloud-ingress.app. However, it is not working at the moment. Troubleshoot and fix the issue.




Note: You should be able to ssh into the cluster1-controlplane using ssh cluster1-controlplane command.
~~~



### 8

deploy 수정 후 접근 어케 하는지 모르겡슴

-> selector 

~~~
kubectl get event --field-selector involvedObject.name=web-dp-cka06-trb-xxx

~~~



### 9

pvc 수정



### 10

~~~
 journalctl -u kubelet --since "30 min ago" | grep 'Error:'
~~~

~~~
vi /etc/kubernetes/kubelet.conf # 6443
~~~





### 11

tolerations 추가

~~~
     tolerations:
      - effect: NoSchedule
        key: node-role.kubernetes.io/master
        operator: Exists
      - effect: NoSchedule
        key: node-role.kubernetes.io/control-plane
        operator: Exists
~~~



### 12

secret 생성

pod 에 env 추가, container port 추가







### 13

~~~
curl http://cluster1-node01:30080
~~~





### 14

~~~
k create deploy app-wl01 --image=nginx --replicas=2
~~~



### 15

pvc 생성



### 16

모름 pvc 생성이 안됨





### 17



deploy + nodeport svc



### 18







### 19







### 20





