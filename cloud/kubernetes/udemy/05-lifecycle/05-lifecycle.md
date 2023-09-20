

## 01. Application Lifecycle Management - Section Introduction



## 02. Rollout and Versioning 

- rollout, history, rollback 에 대해 설명한다.



## 02. Test



- http 요청 shell

~~~
for i in {1..35}; do
   kubectl exec --namespace=kube-public curl -- sh -c 'test=`wget -qO- -T 2  http://webapp-service.default.svc.cluster.local:8080/info 2>&1` && echo "$test OK" || echo "Failed"';
   echo ""
done

~~~



- image변경은 k edit deploy 로 변경할 수 도 있고, 아래와 같이 할 수도 있다.

~~~ 
k set image --help
k set image deploy frontend simple-webapp="" # simple-wabapp 은 컨테이너 name 이다.
~~~



## 03. Configure Applications

Configuring applications comprises of understanding the following concepts:

- Configuring Command and Arguments on applications
- Configuring Environment Variables
- Configuring Secrets

We will see these next





## 04. Commands, Argument, Test

- docker 의 entrypoint, cmd 에 대해 설명하고
- kubernetes 에서 각각 대칭되는 (command, args ) 에 대해 설명한다.



~~~
# edit 로 변경했을때 실패한경우나 재실행해야 하는경우,
# 아래처럼하면 기존 pod을 부수고 실행한다.
k replace --force -f "pod.yaml"
~~~



~~~
k run --help # --command, -- 옵션을 통해서 생성할 수 있다.
k run wepapp-green --image="" -- --color green # 인수 옵션 확장
~~~



## 05. Configure Environment Variables in Applications





## 06.  Configuring ConfigMaps in Applications

- Configmap 여러 사용법



## 07. Test



~~~
# data.yaml
apiVersion: v1
data:
  APP_COLOR: darkblue
  APP_OTHER: disregard
kind: ConfigMap
metadata:
  name: webapp-config-map
~~~



~~~
envFrom:
- configMapRef:
    name: webapp-config-map
~~~



~~~
---
apiVersion: v1
kind: Pod
metadata:
  labels:
    name: webapp-color
  name: webapp-color
  namespace: default
spec:
  containers:
  - env:
    - name: APP_COLOR
      valueFrom:
       configMapKeyRef:
         name: webapp-config-map
         key: APP_COLOR
    image: kodekloud/webapp-color
    name: webapp-color
~~~



~~~
k create configmap "name" --from-literal="key"="value"
~~~





## 08. Configure Secrets in Applications



- secret 은 etcd 에 저장되고, 이 데이터는 허가된 pod에서만 볼 수 있다.

~~~
echo -n admin > username
echo -n 12341234 > password
# echo -n 'admin' | base64
# echo -n '1234' | base64
kubectl create secret generic db-user-pass --from-file=username --from-file=password
kubectl get secret -o yaml db-user-pass # base64가 기본인듯?
echo "암호화된값" | base64 --decode # decode 값 출력

~~~



- envars-secret.yaml

~~~
apiVersion: v1
kind: Pod
metadata:
  name: envar-secret
  labels:
    purpose: demonstrate-envars
spec:
  containers:
  - name: envar-demo-container
    image: gcr.io/google-samples/node-hello:1.0
    env:
    - name: user
      valueFrom:
        secretKeyRef:
          name: db-user-pass
          key: username
    - name: pass
      valueFrom:
        secretKeyRef:
          name: db-user-pass
          key: password
~~~



~~~
kubectl create -f envars-secret.yaml
kubectl exec -it envars-secret -- bash
printenv # user, pass 가 decode 되어 환경변수에 저장되어 있다.
~~~



### A note about Secrets!

Remember that secrets encode data in base64 format. Anyone with the base64 encoded secret can easily decode it. As such the secrets can be considered as not very safe.

The concept of safety of the Secrets is a bit confusing in Kubernetes. The [kubernetes documentation](https://kubernetes.io/docs/concepts/configuration/secret) page and a lot of blogs out there refer to secrets as a "safer option" to store sensitive data. They are safer than storing in plain text as they reduce the risk of accidentally exposing passwords and other sensitive data. In my opinion it's not the secret itself that is safe, it is the practices around it. 

Secrets are not encrypted, so it is not safer in that sense. However, some best practices around using secrets make it safer. As in best practices like:

- Not checking-in secret object definition files to source code repositories.
- [Enabling Encryption at Rest ](https://kubernetes.io/docs/tasks/administer-cluster/encrypt-data/)for Secrets so they are stored encrypted in ETCD. 



Also the way kubernetes handles secrets. Such as:

- A secret is only sent to a node if a pod on that node requires it.
- Kubelet stores the secret into a tmpfs so that the secret is not written to disk storage.
- Once the Pod that depends on the secret is deleted, kubelet will delete its local copy of the secret data as well.

Read about the [protections ](https://kubernetes.io/docs/concepts/configuration/secret/#protections)and [risks](https://kubernetes.io/docs/concepts/configuration/secret/#risks) of using secrets [here](https://kubernetes.io/docs/concepts/configuration/secret/#risks)



Having said that, there are other better ways of handling sensitive data like passwords in Kubernetes, such as using tools like Helm Secrets, [HashiCorp Vault](https://www.vaultproject.io/). I hope to make a lecture on these in the future.







## 09. Test

- webapp에서 mysql 에 접근할 환경변수를 가져오기위해 secert 객체를 만들고 세팅한다. 

![스크린샷 2023-07-01 오후 6.46.57](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-01 오후 6.46.57.png)



- literal 형태로 encode 한 secret 을 생성한다.

~~~
k create secret generic db-secret --from-literal=DB_Host=sql01 --from-literal=DB_User=root --from-literal=DB_Password=password123 
~~~



- webapp에 secret 배치함.

~~~
# envFrom > secretRef 는 image 내부에서 해야함.
apiVersion: v1 
kind: Pod 
metadata:
  labels:
    name: webapp-pod
  name: webapp-pod
  namespace: default 
spec:
  containers:
  - image: kodekloud/simple-webapp-mysql
    imagePullPolicy: Always
    name: webapp
    envFrom:
    - secretRef:
        name: db-secret'
~~~



## 10. Demo: Encrypting Secret Data at Rest

- 이거 나중에 듣자 어려울듯



## 11.  Multi Container Pods

- 컨테이너 두개 세팅하는거.



## 12. Test



- app에서 발생한 로그를 elasticsearch에 전달하기 위한 sidecar container를 설정하는게 문제임.

![스크린샷 2023-07-01 오후 7.07.14](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-01 오후 7.07.14.png)



~~~
kubectl -n elastic-stack logs kibana
~~~



~~~
k exec app -n elastic-stack -- cat /log/app.log # 방법1

k logs app -n elastic-stack # 방법2
~~~



![스크린샷 2023-07-01 오후 7.17.03](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-07-01 오후 7.17.03.png)



## 13. InitContainers

In a multi-container pod, each container is expected to run a process that stays alive as long as the POD's lifecycle. For example in the multi-container pod that we talked about earlier that has a web application and logging agent, both the containers are expected to stay alive at all times. The process running in the log agent container is expected to stay alive as long as the web application is running. If any of them fails, the POD restarts.

But at times you may want to run a process that runs to completion in a container. For example a process that pulls a code or binary from a repository that will be used by the main web application. That is a task that will be run only one time when the pod is first created. Or a process that waits for an external service or database to be up before the actual application starts. That's where **initContainers** comes in.



An **initContainer** is configured in a pod like all other containers, except that it is specified inside a `initContainers` section, like this:

```
apiVersion: v1kind: Podmetadata:  name: myapp-pod  labels:    app: myappspec:  containers:  - name: myapp-container    image: busybox:1.28    command: ['sh', '-c', 'echo The app is running! && sleep 3600']  initContainers:  - name: init-myservice    image: busybox    command: ['sh', '-c', 'git clone <some-repository-that-will-be-used-by-application> ; done;']
```



When a POD is first created the initContainer is run, and the process in the initContainer must run to a completion before the real container hosting the application starts. 

You can configure multiple such initContainers as well, like how we did for multi-containers pod. In that case each init container is run **one at a time in sequential order**.

If any of the initContainers fail to complete, Kubernetes restarts the Pod repeatedly until the Init Container succeeds.

```
apiVersion: v1kind: Podmetadata:  name: myapp-pod  labels:    app: myappspec:  containers:  - name: myapp-container    image: busybox:1.28    command: ['sh', '-c', 'echo The app is running! && sleep 3600']  initContainers:  - name: init-myservice    image: busybox:1.28    command: ['sh', '-c', 'until nslookup myservice; do echo waiting for myservice; sleep 2; done;']  - name: init-mydb    image: busybox:1.28    command: ['sh', '-c', 'until nslookup mydb; do echo waiting for mydb; sleep 2; done;']
```

Read more about initContainers here. And try out the upcoming practice test.

https://kubernetes.io/docs/concepts/workloads/pods/init-containers/



## 14.  Test



- 오류 로그

~~~
k logs orange
k logs orange -c "container name"
~~~





## 15.  Self Healing Applications



Kubernetes supports self-healing applications through ReplicaSets and Replication Controllers. The replication controller helps in ensuring that a POD is re-created automatically when the application within the POD crashes. It helps in ensuring enough replicas of the application are running at all times.

Kubernetes provides additional support to check the health of applications running within PODs and take necessary actions through Liveness and Readiness Probes. However these are not required for the CKA exam and as such they are not covered here. These are topics for the Certified Kubernetes Application Developers (CKAD) exam and are covered in the CKAD course.









