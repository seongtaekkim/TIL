### 1

~~~
Find the node across all clusters that consumes the most CPU and store the result to the file /opt/high_cpu_node in the following format cluster_name,node_name.

The node could be in any clusters that are currently configured on the student-node.
~~~

~~~
 echo cluster1,kube-apiserver-cluster1-controlplane > /opt/high_cpu_node
~~~



### 2

~~~
k logs logger-complete-cka04-arch > /root/logger-complete-cka04-arch
~~~





### 3



~~~
 echo VGhpcyBpcyB0aGUgc2VjcmV0IQo= | base64 --decode > /opt/beta-sec-cka14-arch
~~~



### 4

~~~
  k logs logger-cka03-arch > /root/logger-cka03-arch-all
~~~



### 5

![스크린샷 2023-07-10 오후 3.17.39](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-10 오후 3.17.39.png)

~~~
  - name: sidecar
    image: busybox:1.28
    args: [/bin/sh, -c, 'tail -f  /var/log/elastic-app.log']
    volumeMounts:
    - name: varlog
      mountPath: /var/log
~~~



### 6



- pod 에서 volume 잘못된거 확인하고
- deploy에서 수정.

~~~
k describe pod
k edit deploy
~~~





### 7

![스크린샷 2023-07-10 오후 3.30.02](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-10 오후 3.30.02.png)

인그레스 아직 안배움



### 8

![스크린샷 2023-07-10 오후 3.47.48](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-10 오후 3.47.48.png)

- pod 의 로그 위치, shell 실행 오류 변경
- nginx image 변경.
- svc selector 확인후 변경



### 9

![스크린샷 2023-07-10 오후 4.01.26](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-10 오후 4.01.26.png)

- deploy 의 db 접속정보 확인하고
- k get secret , edit secret 으로 키 정보를 확인해서
- 오타를 수정함.



### 10

![스크린샷 2023-07-10 오후 4.42.30](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-10 오후 4.42.30.png)

~~~
control 로 이동
k get pod -n kube-system # 오류 검색
k logs "pod name" -n kube-system
오타 변경
~~~





### 11

![스크린샷 2023-07-10 오후 4.47.25](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-10 오후 4.47.25.png)

affinity 모름



### 12

![스크린샷 2023-07-10 오후 4.49.29](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-10 오후 4.49.29.png)

- deploy image 버전 변경



### 13

![스크린샷 2023-07-10 오후 5.22.53](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-10 오후 5.22.53.png)

~~~
 k create secret generic db-secret-wl05 --from-literal DB_Host=mysql-svc-wl05 --from-literal DB_User=root --from-literal DB_Password=password123 -n canara-wl05
~~~



~~~
  containers:
  - env:
    - name: DB_Host
      valueFrom:
        secretKeyRef:
          key: DB_Host
          name: db-secret-wl05
    - name: DB_User
      valueFrom:
        secretKeyRef:
          key: DB_User
          name: db-secret-wl05
    - name: DB_Password
      valueFrom:
        secretKeyRef:
          key: DB_Password
          name: db-secret-wl05
~~~

























