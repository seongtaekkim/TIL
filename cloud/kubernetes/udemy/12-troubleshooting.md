## Application Failure



~~~
curl http://web-service-ip:node-port
~~~



~~~
kubectl describe service web-service
~~~



~~~
kubectl logs web -f --previous
~~~





## Test



- deault namespace 세팅

~~~
k config --help # set-context
k config set-context --help
k config set-context --current --namespace=alpha
~~~



~~~
k get svc
curl http://localhost:30081 # 확인
~~~





## Control Plane Failure





~~~
kubectl logs kube-apiserver-master -n kube-system
sudo journalctl -u kube-apiserver
~~~





## test



document > cheat sheet 검색



~~~
kubectl scale deploy app --replicas=2
~~~



~~~
 kubectl logs -n kube-system kube-controller-manager-controlplane
~~~



pod, deploy, rs 에 문제가 없는경우

node가 할당이 안되었기때문에 kube-system 쪽을 잘 살펴보는게 좋다.

describe, logs 로 파악해본다.

오타, key, volume 할당 등 이 잘못되어 있을 수있다.









## Worker Node Failure



~~~
k get nodes
k describe node worker-1
top
df -h

service kubelet status
sudo journalctl -u kubelet -f
openssl x509 -in /var/lib/kubelet/worker-1.crt -text
~~~



## Test



- 실행안된 데몬 확인후 실행

~~~
k get nodes # not ready 확인
ssh node01
systemctl status containerd
systemctl status kubelet
~~~



- config 문제

~~~
ssh node01
sudo journalctl -u kubelet -f
systemctl status kubelet # config 위치 확인
/var/lib/kubelet/config.yaml  에서 수정.
~~~



~~~
systemd/system/kubelet.service.d/kubelet.conf # api-server 연결포트 정의
systemctl restart kubelet
~~~





## Network Troubleshooting





~~~
~~~





네트워크 문제 해결

#### **Network Plugin in Kubernetes**

**--------------------**

*There are several plugins available and these are some.*



**1. Weave Net:**



To install,



```
kubectl apply -f https://github.com/weaveworks/weave/releases/download/v2.8.1/weave-daemonset-k8s.yaml
```



You can find details about the network plugins in the following documentation :

https://kubernetes.io/docs/concepts/cluster-administration/addons/#networking-and-network-policy



**2. Flannel :**



 To install,

 

```
kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/2140ac876ef134e0ed5af15c65e414cf26827915/Documentation/kube-flannel.yml
```

  

*Note: As of now flannel does not support kubernetes network policies.*



**3. Calico :**

  

  To install,

  `curl https://raw.githubusercontent.com/projectcalico/calico/v3.25.0/manifests/calico.yaml -O`

 *Apply the manifest using the following command.*

   `kubectl apply -f calico.yaml`



  Calico is said to have most advanced cni network plugin.



In CKA and CKAD exam, you won't be asked to install the CNI plugin. But if asked you will be provided with the exact URL to install it.

*Note: If there are multiple CNI configuration files in the directory, the kubelet uses the configuration file that comes first by name in lexicographic order.*





#### **DNS in Kubernetes**

#### **-----------------**

Kubernetes uses **CoreDNS**. **CoreDNS** is a flexible, extensible DNS server that can serve as the Kubernetes cluster DNS.



**Memory and Pods**

In large scale Kubernetes clusters, CoreDNS's memory usage is predominantly affected by the number of Pods and Services in the cluster. Other factors include the size of the filled DNS answer cache, and the rate of queries received (QPS) per CoreDNS instance.



Kubernetes resources for **coreDNS** are:  

1. *a service account named* ***coredns\****,*
2. *cluster-roles named* ***coredns\*** *and* ***kube-dns\***
3. *clusterrolebindings named* ***coredns\*** *and* ***kube-dns\****,* 
4. *a deployment named* ***coredns\****,*
5. *a configmap named* ***coredns\*** *and a*
6. *service named* ***kube-dns\****.*



While analyzing the coreDNS deployment you can see that the the ***Corefile plugin\*** consists of important configuration which is defined as a ***configmap\***.



Port **53** is used for for *DNS resolution*.



```
    kubernetes cluster.local in-addr.arpa ip6.arpa {       pods insecure       fallthrough in-addr.arpa ip6.arpa       ttl 30    }
```



This is the backend to k8s for *cluster.local and reverse domains*.



```
proxy . /etc/resolv.conf
```



Forward out of cluster domains directly to right *authoritative DNS server*.





#### Troubleshooting issues related to coreDNS

1. If you find **CoreDNS** pods in pending state first check network plugin is installed.
2. coredns pods have **CrashLoopBackOff or Error state**

If you have nodes that are running SELinux with an older version of Docker you might experience a scenario where the coredns pods are not starting. To solve that you can try one of the following options:

a)Upgrade to a newer version of Docker.

b)Disable **SELinux.**

c)Modify the coredns deployment to set **allowPrivilegeEscalation** to *true*:



```
kubectl -n kube-system get deployment coredns -o yaml | \  sed 's/allowPrivilegeEscalation: false/allowPrivilegeEscalation: true/g' | \  kubectl apply -f -
```

d)Another cause for **CoreDNS** to have CrashLoopBackOff is when a **CoreDNS** Pod deployed in Kubernetes detects a loop.



 There are many ways to work around this issue, some are listed here:



- Add the following to your kubelet config yaml: ***resolvConf: <path-to-your-real-resolv-conf-file>\*** This flag tells ***kubelet\*** to pass an alternate ***resolv.conf\*** to Pods. For systems using **systemd-resolved**, ***/run/systemd/resolve/resolv.conf\*** is typically the location of the ***"real" resolv.conf\***, although this can be different depending on your distribution.

- Disable the local DNS cache on host nodes, and restore ***/etc/resolv.conf\*** to the original.

- A quick fix is to edit your **Corefile**, replacing forward ***. /etc/resolv.conf\*** with the IP address of your upstream DNS, for example forward **. 8.8.8.8**. But this only fixes the issue for **CoreDNS**, ***kubelet\*** will continue to forward the invalid ***resolv.conf\*** to all default dnsPolicy Pods, leaving them unable to resolve DNS.

  

- If **CoreDNS** pods and the **kube-dns** service is working fine, check the **kube-dns** service has valid ***endpoints\***.

​       *kubectl -n kube-system get ep kube-dns*

If there are no endpoints for the service, inspect the service and make sure it uses the correct selectors and ports.





#### **Kube-Proxy**

#### **---------**

**kube-proxy** is a network proxy that runs on each node in the cluster. **kube-proxy** maintains *network rules on nodes*. These network rules allow network communication to the Pods from network sessions inside or outside of the cluster.



In a cluster configured with **kubeadm**, you can find **kube-proxy** as a ***daemonset\***.



**kubeproxy** is responsible for watching *services and endpoint associated with each service*. When the client is going to connect to the service using the *virtual IP* the **kubeproxy** is responsible for *sending traffic to actual pods*.



If you run a `kubectl describe ds kube-proxy -n kube-system` you can see that the **kube-proxy** binary runs with following command inside the kube-proxy container.



```
    Command:      /usr/local/bin/kube-proxy      --config=/var/lib/kube-proxy/config.conf      --hostname-override=$(NODE_NAME)
```

 

  So it fetches the configuration from a configuration file ie, ***/var/lib/kube-proxy/config.conf\*** and we can override the hostname with the node name of at which the pod is running.

 

 In the config file we define the **clusterCIDR, kubeproxy mode, ipvs, iptables, bindaddress, kube-config** etc.

 

#### Troubleshooting issues related to kube-proxy

1. Check **kube-proxy** pod in the **kube-system** namespace is running.
2. Check **kube-proxy** logs.
3. Check **configmap** is correctly defined and the config file for running **kube-proxy** binary is correct.
4. **kube-config** is defined in the **config map**.
5. check **kube-proxy** is *running* inside the container

```
# netstat -plan | grep kube-proxytcp        0      0 0.0.0.0:30081           0.0.0.0:*               LISTEN      1/kube-proxytcp        0      0 127.0.0.1:10249         0.0.0.0:*               LISTEN      1/kube-proxytcp        0      0 172.17.0.12:33706       172.17.0.12:6443        ESTABLISHED 1/kube-proxytcp6       0      0 :::10256                :::*                    LISTEN      1/kube-proxy
```





***References:\***

Debug Service issues:

​           [`*https://kubernetes.io/docs/tasks/debug-application-cluster/debug-service/*`](https://kubernetes.io/docs/tasks/debug-application-cluster/debug-service/)

DNS Troubleshooting:

​           [`*https://kubernetes.io/docs/tasks/administer-cluster/dns-debugging-resolution/*`](https://kubernetes.io/docs/tasks/administer-cluster/dns-debugging-resolution/)

























