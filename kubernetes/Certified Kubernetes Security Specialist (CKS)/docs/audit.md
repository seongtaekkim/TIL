# audit



When API Server performs action, it creates **stages**

- **RequestReceived** - The stage for events generated as soon as the audit handler receives the request
- **ResponseStarted** - response headers are sent & response body is sent. Only for long running (Ex: watch)
- **ResponseComplete** - response body has been completed
- **Panic** - Events generated when a panic occurred



### audit 설정

```yaml
apiVersion: audit.k8s.io/v1 # This is required.
kind: Policy
# Don't generate audit events for all requests in RequestReceived stage.
omitStages:
  - "RequestReceived"
rules:
  - namespace: ["prod-namespace"]
    verb: ["delete"]
    resources:
    - groups: " "
      resources: ["pods"]
      resourceNames: ["webapp-pod"]
    #None/Metadata/Request/RequestResponse
    level: RequestResponse
```



### kube-apiserver 설정

```sh
- --audit-log-path=/var/log/k8-audit.log
- --audit-policy-file=/etc/kubernetes/audit-policy.yaml
- --audit-log-maxage=10
- --audit-log-maxbackup=5
- --audit-log-maxsize=100
```

```yaml
...
volumeMounts:
  - mountPath: /etc/kubernetes/audit-policy.yaml
    name: audit
    readOnly: true
  - mountPath: /var/log/kubernetes/audit/
    name: audit-log
    readOnly: false
```

```yaml
...
volumes:
- name: audit
  hostPath:
    path: /etc/kubernetes/audit-policy.yaml
    type: File
- name: audit-log
  hostPath:
    path: /var/log/kubernetes/audit/
    type: DirectoryOrCreate
```



### debug, log

```sh
/var/log/pods
/var/log/containers
docker ps
docker logs
/var/log/syslog or journalctl -u kubelet
```

- [audit ref](https://kubernetes.io/docs/tasks/debug-application-cluster/audit/)
- [audit-policy ref](https://cloud.google.com/kubernetes-engine/docs/concepts/audit-policy)



