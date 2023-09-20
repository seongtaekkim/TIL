### 1

![스크린샷 2023-07-09 오후 7.41.06](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-09 오후 7.41.06.png)



~~~
  k create secret generic secure-sec-cka12-arch  --from-literal color=darkblue -n secure-sys-cka12-arch
~~~





### 2

![스크린샷 2023-07-09 오후 7.49.58](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-09 오후 7.49.58.png)

~~~
k describe pod > /root/logger-cka03-arch-all
~~~



~~~
kubectl logs logger-cka03-arch --context cluster3 > /root/logger-cka03-arch-all
~~~



### 3

![스크린샷 2023-07-09 오후 7.55.17](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-09 오후 7.55.17.png)

리소스 보는법 모름.



- 클러스터 하나씩 전부 검색해서 나오는걸 저장한다.

~~~
 kubectl top pods -A --context cluster1 --no-headers | sort -nr -k3 | head -1
kube-system   kube-apiserver-cluster1-controlplane            30m   258Mi   

student-node ~ ➜  kubectl top pods -A --context cluster2 --no-headers | sort -nr -k3 | head -1
kube-system   metrics-server-7cd5fcb6b7-fhdrl           5m    18Mi   

student-node ~ ➜  kubectl top pods -A --context cluster3 --no-headers | sort -nr -k3 | head -1
kube-system   metrics-server-7cd5fcb6b7-zvfrg           5m    18Mi   

student-node ~ ➜  kubectl top pods -A --context cluster4 --no-headers | sort -nr -k3 | head -1
kube-system   metrics-server-7cd5fcb6b7-zvfrg           5m    18Mi   

~~~



~~~
echo cluster1,kube-system,kube-apiserver-cluster1-controlplane > /opt/high_cpu_pod
~~~





### 4

![스크린샷 2023-07-09 오후 7.57.20](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-09 오후 7.57.20.png)

~~~
k create secret generic db-user-pass-cka17-arch --from-file=/opt/db-user-pass
~~~



### 5

![스크린샷 2023-07-09 오후 8.00.22](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-09 오후 8.00.22.png)

etcd 설치방법 모름..

~~~
wget https://github.com/etcd-io/etcd/releases/download/v3.5.9/etcd-v3.5.9-linux-amd64.tar.gz
tar -xf etcd-v3.5.9-linux-amd64.tar.gz
cd etcd-v3.5.9-linux-amd64
~~~

~~~
mv etcd etcdctl  /usr/local/bin/
~~~





### 6

![스크린샷 2023-07-09 오후 8.06.35](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-09 오후 8.06.35.png)

~~~
k replace --force -f /tmp/kubectl-edit-664902349.yaml
# busybox /bin/bash -> /bin/sh
~~~



### 7

~~~
One of the nginx based pod called cyan-pod-cka28-trb is running under cyan-ns-cka28-trb namespace and it is exposed within the cluster using cyan-svc-cka28-trb service.

This is a restricted pod so a network policy called cyan-np-cka28-trb has been created in the same namespace to apply some restrictions on this pod.


Two other pods called cyan-white-cka28-trb1 and cyan-black-cka28-trb are also running in the default namespace.


The nginx based app running on the cyan-pod-cka28-trb pod is exposed internally on the default nginx port (80).



Expectation: This app should only be accessible from the cyan-white-cka28-trb1 pod.


Problem: This app is not accessible from anywhere.


Troubleshoot this issue and fix the connectivity as per the requirement listed above.


Note: You can exec into cyan-white-cka28-trb and cyan-black-cka28-trb pods and test connectivity using the curl utility.


You may update the network policy, but make sure it is not deleted from the cyan-ns-cka28-trb namespace.
~~~



포트 80으로 바꾸고 내부에 pot match레이블 고정했는데 둘다 접근이 됨..





### 8

![스크린샷 2023-07-09 오후 8.20.26](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-09 오후 8.20.26.png)

- pvc 용량 수정
- deploy command 수정



- liveness port를 80으로 해야함.

~~~
     containers:
      - image: httpd:latest
        imagePullPolicy: Always
        livenessProbe:
          failureThreshold: 3
          httpGet:
            path: /
            port: 81
            scheme: HTTP
          initialDelaySeconds: 3
          periodSeconds: 3
          successThreshold: 1
          timeoutSeconds: 1
~~~



- 오류 로그 얻기

~~~
kubectl get event --field-selector involvedObject.name=web-dp-cka17-trb-7fdb86c96c-c7npj
~~~









### 9

![스크린샷 2023-07-09 오후 8.30.54](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-09 오후 8.30.54.png)

- pvc 용량 수정



### 10

![스크린샷 2023-07-09 오후 8.41.42](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-09 오후 8.41.42.png)



모르겟음





~~~
kubectl taint nodes cluster2-node01 node.kubernetes.io/not-ready=true:NoExecute

kubectl taint nodes cluster2-node01 node.kubernetes.io/unreachable=true:NoExecute
~~~





### 11





### 12





### 13





### 14





### 15





### 16





### 17





### 18





### 19



### 20













































































