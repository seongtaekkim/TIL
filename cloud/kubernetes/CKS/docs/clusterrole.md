# clusterrole



- secure access to the kube-apiserver

  1. localhost
     - port 8080
     - no TLS
     - default IP is localhost, change with `--insecure-bind-address`
  2. secure port
     - default is 6443, change with `--secure-port`
     - set TLS certificate with `--tls-cert-file`
     - set TLS certificate key with `--tls-private-key-file` flag

- Control anonymous requests to Kube-apiserver by using `--anonymous-auth=false`

  - example for adding anonymous access

    ```yaml
    apiVersion: rbac.authorization.k8s.io/v1
    kind: ClusterRole
    metadata:
      name: anonymous-review-access
    rules:
    - apiGroups:
      - authorization.k8s.io
      resources:
      - selfsubjectaccessreviews
      - selfsubjectrulesreviews
      verbs:
      - create
    ---
    apiVersion: rbac.authorization.k8s.io/v1
    kind: ClusterRoleBinding
    metadata:
      name: anonymous-review-access
    roleRef:
      apiGroup: rbac.authorization.k8s.io
      kind: ClusterRole
      name: anonymous-review-access
    subjects:
    - kind: User
      name: system:anonymous
      namespace: default
    ```

    

    ```sh
    #check using anonymous
    kubectl auth can-i --list --as=system:anonymous -n default
    #check using yourown account
    kubectl auth can-i --list
    ```

- [api-server-ports](https://kubernetes.io/docs/concepts/security/controlling-access/#api-server-ports-and-ips)
- [anonymouse](https://kubernetes.io/docs/reference/access-authn-authz/authentication/#anonymous-requests)
- [kubectl-proxy](https://kubernetes.io/docs/tasks/access-application-cluster/access-cluster/#without-kubectl-proxy)
- [kubelet-authentication](https://kubernetes.io/docs/reference/command-line-tools-reference/kubelet-authentication-authorization/#kubelet-authentication)



