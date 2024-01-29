# HPA



[k8s docs](https://kubernetes.io/ko/docs/tasks/run-application/horizontal-pod-autoscale/), [k8s test](https://kubernetes.io/ko/docs/tasks/run-application/horizontal-pod-autoscale-walkthrough/) 참고



##### m5.large

~~~
2 vCPU, 8 GiB Memory
~~~

##### m5.xlarge

~~~
4 vCPU, 16 GiB Memory
~~~



### metrics server

- 각 파드의 CPU/Memory 이용현황을 수집합니다.
- hpa가 이것을 기준으로 pod를 확장할 지 축소할 지 판단합니다.

~~~sh
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
~~~



~~~sh
$ k get deploy -n kube-system metrics-server
NAME             READY   UP-TO-DATE   AVAILABLE   AGE
metrics-server   1/1     1            1           3h37m
~~~



- 부하테스트 샘플을 실행한다.

~~~
$ kubectl apply -f https://k8s.io/examples/application/php-apache.yaml
~~~

- cpu 50% 넘으면 최대 10개의 pod까지 생성하도록 설정한다.

~~~
$ kubectl autoscale deployment php-apache --cpu-percent=50 --min=1 --max=10 
~~~

- 현재 hpa 현황을 조회할 수 있다.

~~~sh
$ k get hpa
NAME         REFERENCE               TARGETS         MINPODS   MAXPODS   REPLICAS   AGE
php-apache   Deployment/php-apache   <unknown>/50%   1         10        0          4s
~~~



- 부하테스트 시작

~~~sh
$ kubectl run -i --tty load-generator --rm --image=busybox --restart=Never -- /bin/sh -c "while sleep 0.01; do wget -q -O- http://php-apache; done"
~~~



- cpu 현황을 조회한다.

~~~sh
$ kubectl get hpa -w
NAME         REFERENCE               TARGETS   MINPODS   MAXPODS   REPLICAS   AGE
php-apache   Deployment/php-apache   0%/50%    1         10        1          53s
php-apache   Deployment/php-apache   195%/50%   1         10        1          75s
php-apache   Deployment/php-apache   242%/50%   1         10        4          90s
~~~

- autoscale 되고 있다.

~~~sh
$ k get pod -w
NAME                             READY   STATUS    RESTARTS   AGE
kube-ops-view-5586c796bd-cqj4v   1/1     Running   0          4h46m
load-generator                   1/1     Running   0          20s
php-apache-5bdbb8dbf8-wxn7g      1/1     Running   0          68s
php-apache-5bdbb8dbf8-rrmtb      0/1     Pending   0          0s
php-apache-5bdbb8dbf8-rrmtb      0/1     Pending   0          0s
php-apache-5bdbb8dbf8-rrmtb      0/1     ContainerCreating   0          0s
php-apache-5bdbb8dbf8-8nmvf      0/1     Pending             0          0s
php-apache-5bdbb8dbf8-gr9gk      0/1     Pending             0          0s
php-apache-5bdbb8dbf8-8nmvf      0/1     Pending             0          0s
php-apache-5bdbb8dbf8-gr9gk      0/1     Pending             0          0s
php-apache-5bdbb8dbf8-gr9gk      0/1     ContainerCreating   0          0s
php-apache-5bdbb8dbf8-8nmvf      0/1     ContainerCreating   0          0s
~~~



- cleanup

~~~sh
$ kubectl delete -f https://k8s.io/examples/application/php-apache.yaml
$ kubectl delete hpa php-apache
$ kubectl delete -f test-autoscaler.yaml
~~~



