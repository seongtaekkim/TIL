

### 3

~~~
k get pod --context cluster1 -n kube-system kube-apiserver-cluster1-controlplane -o jsonpath='{.metadata.labels.component}'
~~~



### 4

## restore

~~~
 ssh root@cluster1-controlplane
~~~



~~~
cd /tmp
export RELEASE=$(curl -s https://api.github.com/repos/etcd-io/etcd/releases/latest | grep tag_name | cut -d '"' -f 4)

wget https://github.com/etcd-io/etcd/releases/download/${RELEASE}/etcd-${RELEASE}-linux-amd64.tar.gz
tar xvf etcd-${RELEASE}-linux-amd64.tar.gz ; cd etcd-${RELEASE}-linux-amd64

mv etcd etcdctl  /usr/local/bin/

etcdctl snapshot restore --data-dir /root/default.etcd /opt/cluster1_backup_to_restore.db 
~~~





### 7

ingress 수정

~~~
#
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  creationTimestamp: "2023-07-12T08:31:26Z"
  generation: 3
  name: nodeapp-ing-cka08-trb
  namespace: default
  resourceVersion: "6102"
  uid: f6ab1521-b553-4a91-b440-b0391ce6aa07
spec:
  ingressClassName: nginx
  rules:
  - host: kodekloud-ingress.app
    http:
      paths:
      - backend:
          service:
            name: nodeapp-svc-cka08-trb
            port:
              number: 3000
        path: /
        pathType: Prefix
status:
  loadBalancer:
    ingress:
    - ip: 192.12.209.8

~~~





### 15

storage class ,pv, pvc

affinity 모르겟음

~~~
apiVersion: v1
kind: PersistentVolume
metadata:
  name: orange-pv-cka07-str
spec:
  capacity:
    storage: 150Mi
  accessModes:
  - ReadWriteOnce
  persistentVolumeReclaimPolicy: Retain
  storageClassName: orange-stc-cka07-str
  local:
    path: /opt/orange-data-cka07-str
  nodeAffinity:
    required:
      nodeSelectorTerms:
      - matchExpressions:
        - key: kubernetes.io/hostname
          operator: In
          values:
          - cluster1-controlplane
~~~















