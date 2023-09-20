### Pre-Requisites - JSON PATH

In the upcoming lecture we will explore some advanced commands with kubectl utility. But that requires JSON PATH. If you are new to JSON PATH queries get introduced to it first by going through the lectures and practice tests available here.

https://kodekloud.com/p/json-path-quiz



Once you are comfortable head back here:



I also have some JSON PATH exercises with Kubernetes Data Objects. Make sure you go through these:

https://mmumshad.github.io/json-path-quiz/index.html#!/?questions=questionskub1

https://mmumshad.github.io/json-path-quiz/index.html#!/?questions=questionskub2





## Test

### 01

Use the command `kubectl get nodes -o json > /opt/outputs/nodes.json`



### 02

Use the command `kubectl get node node01 -o json > /opt/outputs/node01.json`



### 03

kubectl get nodes -o jsonpath='{.items[*].status.nodeInfo.osImage}' > /opt/outputs/nodes_os.txt



### 04

kubectl config view --kubeconfig=my-kube-config -o jsonpath="{.users[*].name}" > /opt/outputs/users.txt



### 05

kubectl get pv --sort-by=.spec.capacity.storage > /opt/outputs/storage-capacity-sorted.txt



### 06

kubectl get pv --sort-by=.spec.capacity.storage -o=custom-columns=NAME:.metadata.name,CAPACITY:.spec.capacity.storage > /opt/outputs/pv-and-capacity-sorted.txt



### 07

kubectl config view --kubeconfig=my-kube-config -o jsonpath="{.contexts[?(@.context.user=='aws-user')].name}" > /opt/outputs/aws-context-name

























