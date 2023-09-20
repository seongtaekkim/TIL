## 1. 사이드카 컨테이너 개념과 실습

- busybox : 가벼운 리눅스

~~~
# nginx-sidecar.yaml
apiVersion: v1
kind: Pod
metadata:
  name: nginx-sidecar
spec:
  containers:
  - name: nginx
    image: nginx
    ports:
    - containerPort: 80
    volumeMounts:
    - name: varlognginx
      mountPath: /var/log/nginx
  - name: sidecar-access
    image: busybox
    args: [/bin/sh, -c, 'tail -n+1 -f /var/log/nginx/access.log']
    volumeMounts:
    - name: varlognginx
      mountPath: /var/log/nginx
  - name: sidecar-error
    image: busybox
    args: [/bin/sh, -c, 'tail -n+1 -f /var/log/nginx/error.log']
    volumeMounts:
    - name: varlognginx
      mountPath: /var/log/nginx
  volumes:
  - name: varlognginx
    emptyDir: {}
~~~



~~~
kubectl apply -f nginx-sidecar.yaml
kubectl get pod -w
kubectl logs nginx-sidecar # 컨테이너 하나만 조회하라는 에러가 발생한다.
kubectl logs nginx-sidecar nginx
kubectl exec nginx-sidecar -- curl 127.0.0.1
kubectl logs nginx-sidecar sidecar-access # 접속기록 조회
~~~





## 2. 어댑터 컨테이너 개념과 실습



- MainContainer : busybox 로 되어있으며 매 5초마다 특정파일에 로그를 작성함.
- Adapter Container : node.js 로 되어있어서 http 요청시 저장된 로그를 읽어서 출력해줌

~~~
kubectl apply -f https://github.com/bbachi/k8s-adaptor-container-pattern/blob/master/pod.yml
kubectl get pod
kubectl port-forward adapter-container-demo 8080:3080
sudo -i
curl 127.0.0.1:8080/logs # json 형태의 로그를 확인할 수 있다.
~~~







## 3. 앰배서더 컨테이너 개념과 실습



외부사용자가 요청하면, mainContainer는 pod 내부에 엠배서더에  요청을 한다. 엠배서더는 proxy_pass로 외부 서비스로 위임함.



### local test

다음 명령을 사용해 클러스터에 배포

```yaml
kubectl apply -f https://raw.githubusercontent.com/bbachi/k8s-ambassador-container-pattern/master/pod.yml
```

앰배서더 컨테이너로 요청

```yaml
$ kubectl exec -it ambassador-container-demo -c ambassador-container -- curl localhost:9000
<현재는 403으로 정상적으로 통신 불가>
```

로그에서 통신 정보 확인 (console.log)

```yaml
kubectl logs ambassador-container-demo main-container
```





## 4. Job을 활용한 파드 생성 개념과 실습



[docs](https://kubernetes.io/docs/concepts/workloads/controllers/job/#running-an-example-job)

- jobs



- job 예제 배포
- 병렬1개, 완료1개 (기본설정 값)
- 재실행 제한 : backoffLimit

```yaml
cat <<EOF | kubectl apply -f -
apiVersion: batch/v1
kind: Job
metadata:
  name: pi
spec:
  template:
    spec:
      containers:
      - name: pi
        image: perl
        command: ["perl",  "-Mbignum=bpi", "-wle", "print bpi(2000)"]
      restartPolicy: Never
  backoffLimit: 4
EOF
```



~~~
kubectl get pod,job
~~~



- job 병렬 실행 예제

```yaml
cat <<EOF | kubectl apply -f -
apiVersion: batch/v1
kind: Job
metadata:
  name: pi-parallelism
spec:
  completions: 5 # 목표 완료 파드 개수
  parallelism: 2 # 동시 실행 가능 파드 개수
  template:
    spec:
      containers:
      - name: pi
        image: perl
        command: ["perl",  "-Mbignum=bpi", "-wle", "print bpi(2000)"]
      restartPolicy: Never
  backoffLimit: 4
EOF
```



~~~
kubectl delete job pi # job 만 삭제해도 pod 도 같이 삭제됨을 알 수 있다.
~~~



## **5. CronJob을 활용한 예약 파드 생성 개념과 실습**



[docs](https://kubernetes.io/docs/concepts/workloads/controllers/cron-jobs/)

- crontab



- CronJob 예제 실행
- 매 분마다 실행된다.

```ㄴyaml
cat <<EOF | kubectl apply -f -
# cronjob-1.yaml
apiVersion: batch/v1
kind: CronJob
metadata:
  name: hello-1
spec:
  concurrencyPolicy: Allow
  schedule: "*/1 * * * *"
  jobTemplate:
    spec:
      template:
        spec:
          containers:
          - name: hello
            image: busybox
            args:
            - /bin/sh
            - -c
            - date; echo Hello from the Kubernetes cluster
          restartPolicy: OnFailure
EOF
```



~~~
kubectl get cronjob
kubectl get pod -w
~~~



~~~
kubctl delete cronjob hello-1 # pod도 같이 삭제됨.
~~~



- 동시성 정책

~~~
Allow 중복실행 허용
Replace 현재 크론잡을 내리고 새로운 크론잡 실행
Forbid 금지
~~~





- 리플레이스 정책을 적용한 CronJob 예제
  - 기존 pod이 죽고 새로운 pod이 생성된다.

~~~
cat <<EOF | kubectl apply -f -
# cronjob-1.yaml
apiVersion: batch/v1
kind: CronJob
metadata:
  name: hello-2
spec:
  concurrencyPolicy: Replace
  schedule: "*/1 * * * *"
  jobTemplate:
    spec:
      template:
        spec:
          containers:
          - name: hello
            image: busybox
            args:
            - /bin/sh
            - -c
            - date; echo Hello from the Kubernetes cluster; sleep 100;
          restartPolicy: OnFailure
EOF
~~~



~~~
kubetl get pod -w
~~~













































