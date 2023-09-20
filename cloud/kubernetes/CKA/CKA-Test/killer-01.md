

### 4

libeness



### 5

~~~
kubectl get pod -A --sort-by=.metadata.uid

~~~



### 7 리소스 문제 모름\

~~~
kubectl top pod --containers=true
~~~



~~~
Task weight: 1%

Use context: kubectl config use-context k8s-c1-H

The metrics-server has been installed in the cluster. Your college would like to know the kubectl commands to:

show Nodes resource usage
show Pods and their containers resource usage
Please write the commands into /opt/course/7/node.sh and /opt/course/7/pod.sh.
~~~

weave new 사용법인듯



## 8

~~~
ask weight: 2%

Use context: kubectl config use-context k8s-c1-H

Ssh into the controlplane node with ssh cluster1-controlplane1. Check how the controlplane components kubelet, kube-apiserver, kube-scheduler, kube-controller-manager and etcd are started/installed on the controlplane node. Also find out the name of the DNS application and how it's started/installed on the controlplane node.

Write your findings into file /opt/course/8/controlplane-components.txt. The file should be structured like:

# /opt/course/8/controlplane-components.txt
kubelet: [TYPE]
kube-apiserver: [TYPE]
kube-scheduler: [TYPE]
kube-controller-manager: [TYPE]
etcd: [TYPE]
dns: [TYPE] [NAME]
Choices of [TYPE] are: not-installed, process, static-pod, pod
~~~



### 9

~~~
Task weight: 5%

Use context: kubectl config use-context k8s-c2-AC

Ssh into the controlplane node with ssh cluster2-controlplane1. Temporarily stop the kube-scheduler, this means in a way that you can start it again afterwards.

Create a single Pod named manual-schedule of image httpd:2.4-alpine, confirm it's created but not scheduled on any node.

Now you're the scheduler and have all its power, manually schedule that Pod on node cluster2-controlplane1. Make sure it's running.

Start the kube-scheduler again and confirm it's running correctly by creating a second Pod named manual-schedule2 of image httpd:2.4-alpine and check if it's running on cluster2-node1.
~~~



### 12



스킵



### 13



### 14

















