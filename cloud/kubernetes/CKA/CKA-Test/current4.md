

### 4

- busy 박스 pod 명령어

~~~
    command: ["/bin/sh", "-c"]
    args:
    - "while true; do echo hello; sleep 10;done"
~~~



### 10

~~~
There is a pod called pink-pod-cka16-trb created in the default namespace in cluster4. This app runs on port tcp/5000 and it is exposed to end-users using an ingress resource called pink-ing-cka16-trb in such a way that it is supposed to be accessible using the command: curl http://kodekloud-pink.app on cluster4-controlplane host.


However, this is not working. Troubleshoot and fix this issue, making any necessary to the objects.




Note: You should be able to ssh into the cluster4-controlplane using ssh cluster4-controlplane command.
~~~

ssh 흠.. 이해안됨 404





### 11

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





### 12ㅎ

햇는데 접속이 안됨 ;;

이해불가 하.. 나는 configMapKeyReff 로 햇음

~~~
containers:
      - image: kodekloud/webapp-color
        name: webapp-color-wl10
        envFrom:
        - configMapRef: 
            name: webapp-wl10-config-map
~~~



### 14

리소스확인 어케하나??



### 17 

접속안됨	

?????????????????????????????????

~~~
curlpod-cka-1-svcn
kubectl exec curlpod-cka01-svcn -- curl curlme-cka01-svcn

서비스를 지우고
expose 로 올리면 그냥 됫음
k expose ~ --port=80
~~~

### 19

ingress

~~~
  annotations:
    ingress.kubernetes.io/ssl-redirect: "false"
~~~

~~~
 k get ingress
ssh cluster3-controlplane
curl -I  172.25.0.68 
~~~



