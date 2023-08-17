# dashboard by RBAC 



- Creating a Service Account User

```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: admin-user
  namespace: kubernetes-dashboard
```



- Create ClusterRoleBinding

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: admin-user
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
- kind: ServiceAccount
  name: admin-user
  namespace: kubernetes-dashboard
```



- Retrieve Bearer Token & Use

```sh
kubectl -n kubernetes-dashboard get secret $(kubectl -n kubernetes-dashboard get sa/admin-user -o jsonpath="{.secrets[0].name}") -o go-template="{{.data.token | base64decode}}"
```

```sh
#steps to create serviceaccount & use
$ kubectl create serviceaccount simple-user -n kube-system
$ kubectl create clusterrole simple-reader --verb=get,list,watch --resource=pods --resource=pods,deployments,services,configmaps
$ kubectl create clusterrolebinding cluster-simple-reader --clusterrole=simple-reader --serviceaccount=kube-system:simple-user

SEC_NAME=$(kubectl get serviceAccount simple-user -o jsonpath='{.secrets[0].name}')
USER_TOKEN=$(kubectl get secret $SEC_NAME -o json | jq -r '.data["token"]' | base64 -d)

cluster_name=$(kubectl config get-contexts $(kubectl config current-context) | awk '{print $3}' | tail -n 1)
$ kubectl config set-credentials simple-user --token="${USER_TOKEN}"
$ kubectl config set-context simple-reader --cluster=$cluster_name --user simple-user
$ kubectl config set-context simple-reader
```

- use `kubectl proxy` to access to the Dashboard

~~~sh
http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/
~~~





### reference

 https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/#accessing-the-dashboard-ui

 https://github.com/kubernetes/dashboard/blob/master/docs/user/access-control/creating-sample-user.md



