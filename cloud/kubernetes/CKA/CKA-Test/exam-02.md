

### 4

```sh
kubectl config use-context cluster1
```

~~~
There is a script located at /root/pod-cka26-arch.sh on the student-node. Update this script to add a command to filter/display the label with value component of the pod called kube-apiserver-cluster1-controlplane (on cluster1) using jsonpath.



~~~



### 6

용량문제인거같은데 확인하는방법알아보자

~~~
k logs -f green-deployment-cka15-trb-5b8946d8f9-gwnx7
~~~





### 11

~~~
 k label node cluster2-node01 node=cluster2-node01

echo -n kodecloud  | base64 -w 0


~~~

### 12



~~~
k create -f 12.yaml --record=true
k edit deploy ocean-tv-wl09 --record=true
~~~



### 17

~~~
k expose deployment messaging-cka07-svcn --type=ClusterIP --name=messaging-service-cka07-svcn --port=6379
~~~



### 18

~~~
'apiVersion: v1
kind: Service
metadata:
  name: service-3421-svcn
  namespace: spectra-1267
spec:
  selector:
    mode: exam
    type: external
  ports:
  - name: name-of-service-port
    protocol: TCP
    port: 8080
    targetPort: 80'
~~~

~~~
kubectl get pods -n spectra-1267 -o=custom-columns='POD_NAME:metadata.name,IP_ADDR:status.podIP' --sort-by=.status.podIP > /root/pod_ips_cka05_svcn
~~~



### 19

10.50.64.3

~~~
 k exec -it busybox -- nslookup 10.50.64.3.default.pod.cluster.local
~~~



~~~
 k exec -it busybox -- nslookup 10-109-146-30.default.pod.cluster.local
~~~



~~~
 kubectl run test-nslookup --image=busybox:1.28 --rm -it --restart=Never -- nslookup $IP.default.pod > /root/CKA/nginx.pod.cka06.svcn
~~~




