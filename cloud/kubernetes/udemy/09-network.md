

## Prerequisite - Switching Routing



## Prerequisite - DNS



~~~
nslookup www.google.com
dig www.google.com
~~~



### Prerequisite - CoreDNS

In the previous lecture we saw why you need a DNS server and how it can help manage name resolution in large environments with many hostnames and Ips and how you can configure your hosts to point to a DNS server. In this article we will see how to configure a host as a DNS server.



We are given a server dedicated as the DNS server, and a set of Ips to configure as entries in the server. There are many DNS server solutions out there, in this lecture we will focus on a particular one – CoreDNS.



So how do you get core dns? CoreDNS binaries can be downloaded from their Github releases page or as a docker image. Let’s go the traditional route. Download the binary using curl or wget. And extract it. You get the coredns executable.



![img](https://img-c.udemycdn.com/redactor/raw/2019-04-17_03-55-27-20b6c5e30d8eca52bb8fe74b628f74ef.PNG)



Run the executable to start a DNS server. It by default listens on port 53, which is the default port for a DNS server.



Now we haven’t specified the IP to hostname mappings. For that you need to provide some configurations. There are multiple ways to do that. We will look at one. First we put all of the entries into the DNS servers /etc/hosts file.



And then we configure CoreDNS to use that file. CoreDNS loads it’s configuration from a file named Corefile. Here is a simple configuration that instructs CoreDNS to fetch the IP to hostname mappings from the file /etc/hosts. When the DNS server is run, it now picks the Ips and names from the /etc/hosts file on the server.

![img](https://img-c.udemycdn.com/redactor/raw/2019-04-17_03-56-22-3add142ffd4675a839e4ea8717e8a43d.PNG)



CoreDNS also supports other ways of configuring DNS entries through plugins. We will look at the plugin that it uses for Kubernetes in a later section.

Read more about CoreDNS here:

https://github.com/kubernetes/dns/blob/master/docs/specification.md

https://coredns.io/plugins/kubernetes/





## Cluster Networking





### Important Note about CNI and CKA Exam

**An important tip about deploying Network Addons in a Kubernetes cluster.**



In the upcoming labs, we will work with Network Addons. This includes installing a network plugin in the cluster. While we have used weave-net as an example, please bear in mind that you can use any of the plugins which are described here:

[**https://kubernetes.io/docs/concepts/cluster-administration/addons/**](https://kubernetes.io/docs/concepts/cluster-administration/addons/)

[**https://kubernetes.io/docs/concepts/cluster-administration/networking/#how-to-implement-the-kubernetes-networking-model**](https://kubernetes.io/docs/concepts/cluster-administration/networking/#how-to-implement-the-kubernetes-networking-model)



In the CKA exam, for a question that requires you to deploy a network addon, unless specifically directed, you may use any of the solutions described in the link above.



***However,\*** the documentation currently does not contain a direct reference to the exact command to be used to deploy a third party network addon.

The links above redirect to third party/ vendor sites or GitHub repositories which cannot be used in the exam. This has been intentionally done to keep the content in the Kubernetes documentation vendor-neutral.

At this moment in time, there is still one place within the documentation where you can find the exact command to deploy weave network addon:



https://v1-22.docs.kubernetes.io/docs/setup/production-environment/tools/kubeadm/high-availability/#steps-for-the-first-control-plane-node (step 2)







## Test



~~~
k get nodes -o wide # internal-ip
~~~



~~~
ip address # internal ip 하고 매칭된 걸 찾는다.
~~~



~~~
ip address show type bridge
~~~



~~~
ip route # default via "ip" dev eth1
~~~



~~~
netstat --help
netstat -npl | grep -i shceduler
~~~



~~~
netstat -npa | grep -i etcd
~~~



~~~
netstat -npa | grep -i etcd | grep -i 2379 | wc -i
~~~





## Pod Networking



## CNI in kubernetes



## Note CNI Weave

### Note CNI Weave

**Important Update: -**

Before going to the CNI weave lecture, we have an update for the Weave Net installation link. They have announced the end of service for Weave Cloud.

To know more about this, read the blog from the link below: -

https://www.weave.works/blog/weave-cloud-end-of-service

As an impact, the old weave net installation link won’t work anymore: -

**kubectl apply -f "https://cloud.weave.works/k8s/net?k8s-version=$(kubectl version | base64 | tr -d '\n')"**

Instead of that, use the below latest link to install the **weave net**: -

**kubectl apply -f https://github.com/weaveworks/weave/releases/download/v2.8.1/weave-daemonset-k8s.yaml**

Reference links: -

1. https://www.weave.works/docs/net/latest/kubernetes/kube-addon/#-installation
2. https://github.com/weaveworks/weave/releases





## Weave



## Test



### Inspect the kubelet service and identify the container runtime endpoint value is set for Kubernetes.

~~~
ps -aux | grep kubelet | grep --color container-runtime-endpoint
~~~



### What is the path configured with all binaries of CNI supported plugins?

~~~
/opt/cni/bin
~~~



### Identify which of the below plugins is not available in the list of available CNI plugins on this host?



### What is the CNI plugin configured to be used on this kubernetes cluster?

~~~
cd /etc/cni/net.d
cat 10-flannel.conf
~~~





### What binary executable file will be run by kubelet after a container and its associated namespace are created?





## Test	



### In this practice test we will install `weave-net` POD networking solution to the cluster. Let us first inspect the setup.

We have deployed an application called `app` in the default namespace. What is the state of the pod?





### Deploy `weave-net` networking solution to the cluster.

**NOTE: -** We already have provided a weave manifest file under the `/root/weave` directory.




Run the below command to deploy the `weave` on the cluster: -

```sh
kubectl apply -f /root/weave/weave-daemonset-k8s.yaml
```

Now check if the weave pods are created **and** let's also check the status of our `app` pod now:

```sh
root@controlplane:/# kubectl get pods -A | grep weave
kube-system   weave-net-q7m6s                        2/2     Running   0          21s

root@controlplane:/# kubectl get pods
NAME   READY   STATUS    RESTARTS   AGE
app    1/1     Running   0          25m
```







## IP Address Management - Weave







##  Service Networking

- 노드, 서비스, pod의 네트워크대역을 설명해줌.



 



## Test



- node network 대역 확인

~~~
k get nodes
ip add # eth0 의 inet을 확인
~~~







- pod network 대역 확인

~~~
 k logs weave-net-ssfzm   weave -n kube-system
~~~



- service network 대역 확인

~~~
cat /etc/kubernetes/manifests/kube-apiserver.yaml | grep cluster-ip-range
# 혹인 service-cluster-ip-range 에서 확인
~~~



- kube proxy type 확인
- iptables, ipvs, userspace

~~~
  k logs kube-proxy-kc4p7 -n kube-system
~~~









## DNS in kubernetes



서비스를 생성했을때, 다른 pod에서 curl 등으로 접근할 때 아이피가 아닌 svc 이름으로 접근가능.

다른 네임스페이스일 때도 접근 가능한데, 정규화된 도메인규칙으로 접근하면 된다.

~~~
curl http://"".namespace.svc.cluster.local
~~~





## CoreDNS in Kubernetes

- coreDNS에 대해서 설명한다.

kubelet 의 config 에서 cordns ip를 세팅하고 시작한다.

/etc/resolv.conf 에 nameserver 로 coredns ip를 세팅한다.



host web-service 를 작성하면 전체 도메인 주소가 출력된다. 하지만 pod (10-244-2-5)은 일부만 입력하면 출력되지 않는다.

coredns는 pod이 생성,삭제될때마다 자동으로 갱신한다.

pod은 ip를 dot 대신 - 를, service 는 service name을 키로 한다.



## Test



~~~
 ps -aux | grep coredns

kubectl -n kube-system describe deployments.apps coredns | grep -A2 Args | grep Corefile
~~~



### 6

~~~
coredns pod 을 yaml으로 열어보면, volmuem mount에 /etc/coredns 내부에
configmap 으로 Corefile 에 데이터를 쌓는걸 알 수 있다.
~~~



### 8

~~~
k describe config coredns -n kube-system # cluster.local 나와있음
~~~





- coreDns object 확인

~~~
kubectl get configmap -n kube-system
~~~



### 14

- default ns 의 deploy에서 DB_HOST 를 mysql로 함.
- 해당 mysql 서비스는 payroll ns 에 존재함.
- host는 mysql.payroll로 변경해야 접근이 가능함.



### 15

- From the `hr` pod `nslookup` the `mysql` service 

~~~
controlplane ~ ➜  k exec -it hr -- nslookup mysql.payroll
Server:         10.96.0.10
Address:        10.96.0.10#53

Name:   mysql.payroll.svc.cluster.local
Address: 10.104.152.30

~~~





## Ingress



























































