## Download Presentation Deck



## 01. Monitor Cluster Components



## 02. Test



~~~
git clone https://github.com/kodekloudhub/kubernetes-metrics-server.git
~~~



~~~
k create -f ./kubernetes-metrics-server
~~~



~~~
controlplane ~ ➜  k top node
NAME           CPU(cores)   CPU%   MEMORY(bytes)   MEMORY%   
controlplane   307m         0%     1186Mi          0%        
node01         26m          0%     379Mi           0%        

controlplane ~ ➜  kubectl top pod
NAME       CPU(cores)   MEMORY(bytes)   
elephant   20m          32Mi            
lion       1m           18Mi   
~~~





## 03. Managing Application Logs



~~~
docker run kodekloud/event-simulator
~~~



~~~
docker run -d kodekloud/event-simulator
docker logs -f ecf
~~~

~~~
kubectl create –f event-simulator.yaml
kubectl logs –f event-simulator-pod # container 가 하나인 경우 가능
~~~



~~~
kubectl logs –f event-simulator-pod event-simulator # 컨테이너가 여러개인 경우 인자에 명시해야 함
~~~





## 04. Test



- log 를 활용해서 applicatioin 실패 원인을 파악한다.



















