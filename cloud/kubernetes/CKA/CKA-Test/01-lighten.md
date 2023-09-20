

~~~
apt-get update

apt-get install kubeadm=1.27.0-00

k drain controlplane --ignore-daemonsets

kubeadm upgrade apply v1.27.0

apt-get install kubelet=1.27.0-00 


systemctl daemon-reload
systemctl restart kubelet


k uncordon  controlplane

kubectl taint node controlplane node-role.kubernetes.io/control-plane:NoSchedule-
~~~





~~~
 k get deploy -n admin2406 --sort-by=.metadata.labels.app -o=custom-columns=DEPLOYMENT:.metadata.labels.app,CONTAINER_IMAGE:.spec.template.spec.containers[0].image,READY_REPLICAS:.spec.replicas,NAMESPACE:.metadata.namespace
~~~





### 3

~~~
A kubeconfig file called admin.kubeconfig has been created in /root/CKA. There is something wrong with the configuration. Troubleshoot and fix it.
~~~



### 4

~~~
Create a new deployment called nginx-deploy, with image nginx:1.16 and 1 replica. Next upgrade the deployment to version 1.17 using rolling update.
~~~







### 5. pv, pvc

~~~
A new deployment called alpha-mysql has been deployed in the alpha namespace. However, the pods are not running. Troubleshoot and fix the issue. The deployment should make use of the persistent volume alpha-pv to be mounted at /var/lib/mysql and should use the environment variable MYSQL_ALLOW_EMPTY_PASSWORD=1 to make use of an empty root password.

Important: Do not alter the persistent volume.
~~~



### 6

~~~
Take the backup of ETCD at the location /opt/etcd-backup.db on the controlplane node.
~~~

~~~
ETCDCTL_API=3 etcdctl --endpoints=https://[127.0.0.1]:2379 \
--cacert=/etc/kubernetes/pki/etcd/ca.crt \
--cert=/etc/kubernetes/pki/etcd/server.crt \
--key=/etc/kubernetes/pki/etcd/server.key \
snapshot save /opt/etcd-backup.db
~~~



### 7

~~~
Create a pod called secret-1401 in the admin1401 namespace using the busybox image. The container within the pod should be called secret-admin and should sleep for 4800 seconds.

The container should mount a read-only secret volume called secret-volume at the path /etc/secret-volume. The secret being mounted has already been created for you and is called dotfile-secret.
~~~

~~~
apiVersion: v1
kind: Pod
metadata:
  creationTimestamp: null
  labels:
    run: secret-1401
  name: secret-1401
  namespace: admin1401
spec:
  containers:
  - image: busybox
    volumeMounts:
    - name: secret-volume
      mountPath: "/etc/secret-volume"
      readOnly: true
    name: secret-admin
    command: ['sleep', '4800']
    resources: {}
  volumes:
  - name: secret-volume
    secret:
      secretName: dotfile-secret
  dnsPolicy: ClusterFirst
  restartPolicy: Always
status: {}

~~~



~~~
k create secret generic dotfile-secret
~~~



