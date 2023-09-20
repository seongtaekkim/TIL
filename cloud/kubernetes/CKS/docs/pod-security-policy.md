# pod security policy

### kubernetes 1.25 version  -> depricated

대안 : **Pod Security Admission(PSA)**





- POD level using securityContext; key logic `readOnlyRootFilesystem = true,  privileged=false`

  ```yaml
  apiVersion: v1
  kind: Pod
  metadata:
    name: security-context-demo
  spec:
      volumes:
      - name: sec-ctx-vol
        emptyDir: {}
      containers:
      - name: sec-ctx-demo
        image: busybox
        command: [ "sh", "-c", "echo hello > /data/demo/hello.txt; sleep 1h" ]
        securityContext:
          privileged: true
          runAsUser: 0
          allowPrivilegeEscalation: true
          readOnlyRootFilesystem: false
        volumeMounts:
        - name: sec-ctx-vol
          mountPath: /data/demo
  ```

  

- Enforce using PSP(Pod Security Policies) - key logic `readOnlyRootFilesystem = true,  privileged=false; runAsUser=NonRoot`

- 전체 pod에 적용할 수 있음. (role, rolebinding에 등록하고, 등록한 serviceaccount를 pod에 작성했을 겨우)

  ```yaml
  apiVersion: policy/v1beta1
  kind: PodSecurityPolicy
  metadata:
    name: psp-denial-sa
  spec:
    privileged: false
    readOnlyRootFilesystem: true
    runAsUser:
      rule: RunAsNonRoot
    seLinux:
      rule: RunAsAny
    supplementalGroups:
      rule: RunAsAny
    runAsUser:
      rule: RunAsNonRoot
    fsGroup:
      rule: RunAsAny
  ```

- Ref: https://kubernetes.io/blog/2018/03/principles-of-container-app-design/



- 작성한 psp에 대한 리소스를 clusterrole에 등록한다.
- serviceaccount 생성.
- clusterrolebinding 에 등록하고, pod에 serviceaccountName 을 등록하면, psp가 적용된다.

~~~yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRole
metadata:
name: deny-access-role
rules:
- apiGroups: ['policy']
resources: ['podsecuritypolicies']
verbs: ['use']
resourceNames:
- "psp-denial-sa"


$ k create sa psp-denial-sa -n development


apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: restrict-access-bing
roleRef:
kind: ClusterRole
name: deny-access-role
apiGroup: rbac.authorization.k8s.io
# Authorize specific
service accounts:
- kind: ServiceAccount
  name: psp-denial-sa
namespace: development

~~~

