

- count-httpd.yaml

~~~
apiVersion: v1
kind: Pod
metadata:
  name: count
spec:
  containers:
  - image: gasbugs/count
    name: html-generator
    volumeMounts:
    - name: html
      mountPath: /var/htdocs
  - image: httpd
    name: web-server
    volumeMounts:
    - name: html
      mountPath: /usr/local/apache2/htdocs
      readOnly: true
    ports:
    - containerPort: 80
      protocol: TCP
  volumes:
  - name: html
    emptyDir: {}
~~~





~~~
$ kubectl create -f count-httpd.yaml 
pod/count created
$ kubectl get pod -w
NAME                       READY   STATUS    RESTARTS   AGE
count                      2/2     Running   0          18s
http-go-647985c674-r8lvs   1/1     Running   0          178m

$ kubectl get pod -o wide
NAME                       READY   STATUS    RESTARTS   AGE    IP          NODE                                       NOMINATED NODE   READINESS GATES
count                      2/2     Running   0          37s    10.4.1.14   gke-cluster-1-default-pool-cb7d7c70-xhdv   <none>           <none>
http-go-647985c674-r8lvs   1/1     Running   0          178m   10.4.1.13   gke-cluster-1-default-pool-cb7d7c70-xhdv   <none>           <none>

$ kubectl exec -it http-go-647985c674-r8lvs -- curl 10.4.1.14
Running loop seq 8

~~~



### 임시 디렉토리는 직접 접근을 어떻게 해야 하나???

