# runtime class



- **Sandboxing** is concept of isoloation of containers from Host

- docker uses default SecComp profiles restrict previleges. Whitlist or Blacklist

- AppArmor finegrain control of that container can access. Whitlist or Blacklist

- If large number of apps in containers, then SecComp/AppArmor is not the case

- gVisor sits between container and Linux Kernel. Every container has their own gVisior

- gVisor has 2 different components

  - **Sentry**: works as kernel for containers
  - **Gofer**: is file proxy to access system files. Middle man betwen container and OS

- gVisor uses runsc to runtime sandbox with in hostOS (OCI comapliant)

  ```yaml
  apiVersion: node.k8s.io/v1
  kind: RuntimeClass
  metadata:
    name: gvisor
  handler: runsc
  ```

  ```yaml
  apiVersion: v1
  kind: Pod
  metadata:
    name: mypod
  spec:
    runtimeClassName: gvisor
    # ...
  ```

  - `kubectl exec mypod -- dmesg`

- https://gvisor.dev/docs/user_guide/install/

- https://sbulav.github.io/certifications/cks-gvisor/

- https://kubernetes.io/docs/concepts/containers/runtime-class
