# role



## Use Role Based Access Controls to minimize exposure

Roles live in namespace, RoleBinding specific to ns ClusterRoles live across all namespace, ClusterRoleBidning ServiceAccount should have only necessary RBAC permissions



- Create virtual users using ServiceAccount for specific namespace
- Create Role in specific namespace
  - has resources (ex: deployment)
  - has verbs (get, list, create, delete))
- Create RoleBinding in specific namespace & link Role & ServiceAccount
  - can be user, group or service account
- specify service account in deployment/pod level

```yaml
# add to pod
spec:
  serviceAccountName: deployment-viewer-sa
```

[role reference](https://kubernetes.io/docs/reference/access-authn-authz/rbac/)





## Exercise caution in using service accounts e.g. disable defaults, minimize permissions on newly created ones

- Create ServiceAccount to not to automount secret to any pod

-  `automountServiceAccountToken: false`

  ```yaml
  apiVersion: v1
  kind: ServiceAccount
  metadata:
    name: test-svc
    namespace: dev
  automountServiceAccountToken: false
  ```

  

- Create Role for service account `kubectl create role test-role -n dev --verb=get,list,watch,create --resource=pods --resource=pods,deployments,services,configmaps`

- Create RoleBinding for role & service account `kubectl create rolebinding cluster-test-binding -n dev --role=test-role --serviceaccount=dev:test-svc`

- Create Pod with serviceAccountName: test-svc. (note: pod level can be overridden to automountServiceAccountToken: true)

  ```yaml
  apiVersion: v1
  kind: Pod
  metadata:
    name: web
    namespace: dev
  spec:
    containers:
    - image: web
      name: web
    serviceAccountName: test-svc
    automountServiceAccountToken: false
  ```

  

  ```sh
  # hack
  curl -k https://kubernetes.default/api/v1/namespaces/kube-system/secrets 
    -H "Authorization: Bearer $(cat /run/secrets/kubernetes.io/serviceaccount/token)" 
  ```

[reference](https://kubernetes.io/docs/tasks/configure-pod-container/configure-service-account/#use-the-default-service-account-to-access-the-api-server)

