## 01. Cluster Maintenance - Section Introduction





## 02. OS Upgrades

- drain, uncordon, cordon 에 대해 설명한다.



## 03. Test



~~~
k drain node01 --ignore-daemonsets
~~~



~~~
k uncordon node01
~~~





drain은 replicatinocontroller replicaset job daemonset statefueset 이 없는 pod인 경우 적용이안된다

--force 옵션으로 가능하긴한데

그 pod은 영영 삭제된다.

~~~
k drain node01 --ignore-daemonsets --force
~~~





- 관리되지 않는 pod 인데 삭제되지 말아야 한다면,
- 해당 노드를 unscheduling 하는 방법이 있다.
- 아래와 같이하면 배치된 pod은 그대로 있으면서 더이상 해당 노드로 스케쥴되지 않게 된다.

~~~
k cordon node01
~~~



## 04. Kubernetes Software Versions

- kubernetes 버전에 대 해 설명한다.

v1.27.3 

magor, minor, patch

kube-apiserver, controller-manager, kube-scheduler, kubelet, kube-proxy, kubectl 은 모두 같은 릴리즈 버전

etcd, coredns 는 고유 버전이 있고 호환범위는 git에 작성되어 있음.



### References

https://kubernetes.io/docs/concepts/overview/kubernetes-api/

Here is a link to kubernetes documentation if you want to learn more about this topic (You don't need it for the exam though):

https://github.com/kubernetes/community/blob/master/contributors/devel/sig-architecture/api-conventions.md

https://github.com/kubernetes/community/blob/master/contributors/devel/sig-architecture/api_changes.md



## 05. Cluster Upgrade Process



### kubeadm - upgrade

1. 마스터 다운. 업그레이드

전략1

​	모든 포드 내렸다가 올리기

전략2

하나씩  내렸다가 올리기

전략3

버전이 적용된 새로운 노드를 올리기



## 06. Cluster Upgrade

- 데모를 시연해준다.



## 07. Test



~~~
kubeadm upgrade plan
~~~



~~~
k describe node | grep Taints
~~~



~~~
kubeadm upgrade plan
~~~





~~~
 k drain  controlplane --ignore-daemonsets
~~~





### 노드 업그레이드 순서

This will update the package lists from the software repository.

```sh
apt-get update
```

This will install the kubeadm version 1.27.0.

```sh
apt-get install kubeadm=1.27.0-00
```

This will upgrade the `node01` configuration.

```sh
  kubeadm upgrade node
```

This will update the `kubelet` with the version `1.27.0`.

```sh
apt-get install kubelet=1.27.0-00 
```

You may need to reload the `daemon` and restart the `kubelet` service after it has been upgraded.

```sh
systemctl daemon-reload
systemctl restart kubelet
```



~~~
 k uncorden  controlplane
~~~





## 08. Backup and Restore Methods



Resource Configuration

- 파일 등으로 
- velero 에서 백업할 수 있다.



ETCD Cluster

- 스냅샷을 만들 수 있다.



### Working with ETCDCTL



`etcdctl` is a command line client for [**etcd**](https://github.com/coreos/etcd).



In all our Kubernetes Hands-on labs, the ETCD key-value database is deployed as a static pod on the master. The version used is v3.

To make use of etcdctl for tasks such as back up and restore, make sure that you set the ETCDCTL_API to 3.



You can do this by exporting the variable ETCDCTL_API prior to using the etcdctl client. This can be done as follows:

```
export ETCDCTL_API=3
```

On the **Master Node**:

![img](https://process.fs.teachablecdn.com/ADNupMnWyR7kCWRvm76Laz/resize=width:1000/https://www.filepicker.io/api/file/T7Y4a6aUTyOy9W2ZpfeV)



To see all the options for a specific sub-command, make use of the **-h or --help** flag.



For example, if you want to take a snapshot of etcd, use:

`etcdctl snapshot save -h` and keep a note of the mandatory global options.



Since our ETCD database is TLS-Enabled, the following options are mandatory:

`--cacert`                        verify certificates of TLS-enabled secure servers using this CA bundle

`--cert`                          identify secure client using this TLS certificate file

`--endpoints=[127.0.0.1:2379]`     This is the default as ETCD is running on master node and exposed on localhost 2379.

`--key`                           identify secure client using this TLS key file





Similarly use the help option for **snapshot restore** to see all available options for restoring the backup.

```
etcdctl snapshot restore -h
```

For a detailed explanation on how to make use of the etcdctl command line tool and work with the -h flags, check out the solution video for the Backup and Restore Lab.







## 09. Test 1



- At what address can you reach the ETCD cluster from the controlplane node?

  Check the ETCD Service configuration in the ETCD POD

~~~
Use the command kubectl describe pod etcd-controlplane -n kube-system and look for --listen-client-urls
~~~





- Where is the ETCD server certificate file located?

  Note this path down as you will need to use it later

~~~
$ kubectl describe pod etcd-controlplane -n kube-system
--cert-file=/etc/kubernetes/pki/etcd/server.crt
~~~



- Where is the ETCD CA Certificate file located?

  Note this path down as you will need to use it later.

~~~
 --trusted-ca-file=/etc/kubernetes/pki/etcd/ca.crt
~~~



### back up

~~~
ETCDCTL_API=3 etcdctl --endpoints=https://[127.0.0.1]:2379 \
--cacert=/etc/kubernetes/pki/etcd/ca.crt \
--cert=/etc/kubernetes/pki/etcd/server.crt \
--key=/etc/kubernetes/pki/etcd/server.key \
snapshot save /opt/snapshot-pre-boot.db
~~~



### Restore

**First Restore the snapshot:**

```sh
root@controlplane:~# ETCDCTL_API=3 etcdctl  --data-dir /var/lib/etcd-from-backup \
snapshot restore /opt/snapshot-pre-boot.db


2022-03-25 09:19:27.175043 I | mvcc: restore compact to 2552
2022-03-25 09:19:27.266709 I | etcdserver/membership: added member 8e9e05c52164694d [http://localhost:2380] to cluster cdf818194e3a8c32
root@controlplane:~# 
```



Note: In this case, we are restoring the snapshot to a different directory but in the same server where we took the backup **(the controlplane node)** As a result, the only required option for the restore command is the `--data-dir`.



Next, update the `/etc/kubernetes/manifests/etcd.yaml`:

We have now restored the etcd snapshot to a new path on the controlplane - `/var/lib/etcd-from-backup`, so, the only change to be made in the YAML file, is to change the hostPath for the volume called `etcd-data` from old directory (`/var/lib/etcd`) to the new directory (`/var/lib/etcd-from-backup`).

```sh
  volumes:
  - hostPath:
      path: /var/lib/etcd-from-backup
      type: DirectoryOrCreate
    name: etcd-data
```

With this change, `/var/lib/etcd` on the container points to `/var/lib/etcd-from-backup` on the `controlplane` (which is what we want).

When this file is updated, the `ETCD` pod is automatically re-created as this is a static pod placed under the `/etc/kubernetes/manifests` directory.



> Note 1: As the ETCD pod has changed it will automatically restart, and also `kube-controller-manager` and `kube-scheduler`. Wait 1-2 to mins for this pods to restart. You can run the command: `watch "crictl ps | grep etcd"` to see when the ETCD pod is restarted.

> Note 2: If the etcd pod is not getting `Ready 1/1`, then restart it by `kubectl delete pod -n kube-system etcd-controlplane` and wait 1 minute.

> Note 3: This is the simplest way to make sure that ETCD uses the restored data after the ETCD pod is recreated. You **don't** have to change anything else.



If you do change `--data-dir` to `/var/lib/etcd-from-backup` in the ETCD YAML file, make sure that the `volumeMounts` for `etcd-data` is updated as well, with the mountPath pointing to `/var/lib/etcd-from-backup` **(THIS COMPLETE STEP IS OPTIONAL AND NEED NOT BE DONE FOR COMPLETING THE RESTORE)**





## 10. Test 2 어렵다. 나중에 다시..



~~~
k config view
~~~



~~~
k config use-context cluster2
~~~



~~~
ssh etcd-server
ps -ef | grep -i etcd # --data-dir=/var/lib/etcd-data
~~~



- 1개.. ?//

~~~
ETCDCTL_API=3 etcdctl \
>  --endpoints=https://127.0.0.1:2379 \
>  --cacert=/etc/etcd/pki/ca.pem \
>  --cert=/etc/etcd/pki/etcd.pem \
>  --key=/etc/etcd/pki/etcd-key.pem \
>   member list
b6280c30b9293695, started, etcd-server, https://192.5.86.15:2380, https://192.5.86.15:2379, false
~~~





### backup

~~~
k get node
ssh cluster1-controlplane
~~~



### References

https://kubernetes.io/docs/tasks/administer-cluster/configure-upgrade-etcd/#backing-up-an-etcd-cluster

https://github.com/etcd-io/website/blob/main/content/en/docs/v3.5/op-guide/recovery.md

https://www.youtube.com/watch?v=qRPNuT080Hk











