## outline



[시험범위](https://training.linuxfoundation.org/training/cka-cks-exam-bundle/)

[시험등록](https://trainingportal.linuxfoundation.org/learn/dashboard)

https://github.com/cncf/curriculum



## objective

### CKA - 7/15 취득

### CKS - 8/1 취득



## reference

[후기 및 정보1](https://moaimoai.tistory.com/323)

[udemy - CKA](https://www.udemy.com/course/certified-kubernetes-administrator-with-practice-tests/)

[후기 및 정보2](https://sysnet4admin.blogspot.com/2020/07/cka-ver10.html#.YTBWbo4zaUk)

[후기 및 정보3](https://ikcoo.tistory.com/183) 	





### CKA - Certified Kubernetes Administrator 

Storage 10%

Understand storage classes, persistent volumes
Understand volume mode, access modes and reclaim policies for volumes
Understand persistent volume claims primitive
Know how to configure applications with persistent storage

![img](https://training.linuxfoundation.org/wp-content/mu-plugins/lf-owls-pdp/assets/images/pdp-fundamentals/course-chapter.png)Troubleshooting30%

Evaluate cluster and node logging
Understand how to monitor applications
Manage container stdout & stderr logs
Troubleshoot application failure
Troubleshoot cluster component failure
Troubleshoot networking

![img](https://training.linuxfoundation.org/wp-content/mu-plugins/lf-owls-pdp/assets/images/pdp-fundamentals/course-chapter.png)Workloads & Scheduling15%

Understand deployments and how to perform rolling update and rollbacks
Use ConfigMaps and Secrets to configure applications
Know how to scale applications
Understand the primitives used to create robust, self-healing, application deployments
Understand how resource limits can affect Pod scheduling
Awareness of manifest management and common templating tools

![img](https://training.linuxfoundation.org/wp-content/mu-plugins/lf-owls-pdp/assets/images/pdp-fundamentals/course-chapter.png)Cluster Architecture, Installation & Configuration25%

Manage role based access control (RBAC)
Use Kubeadm to install a basic cluster
Manage a highly-available Kubernetes cluster
Provision underlying infrastructure to deploy a Kubernetes cluster
Perform a version upgrade on a Kubernetes cluster using Kubeadm
Implement etcd backup and restore

![img](https://training.linuxfoundation.org/wp-content/mu-plugins/lf-owls-pdp/assets/images/pdp-fundamentals/course-chapter.png)Services & Networking20%

Understand host networking configuration on the cluster nodes
Understand connectivity between Pods
Understand ClusterIP, NodePort, LoadBalancer service types and endpoints
Know how to use Ingress controllers and Ingress resources
Know how to configure and use CoreDNS
Choose an appropriate container network interface plugin





### CKS - Certified Kubernetes Security Specialist

Cluster Setup10%

Use Network security policies to restrict cluster level access
Use CIS benchmark to review the security configuration of Kubernetes components (etcd, kubelet, kubedns, kubeapi)
Properly set up Ingress objects with security control
Protect node metadata and endpoints
Minimize use of, and access to, GUI elements
Verify platform binaries before deploying

![img](https://training.linuxfoundation.org/wp-content/mu-plugins/lf-owls-pdp/assets/images/pdp-fundamentals/course-chapter.png)Cluster Hardening15%

Restrict access to Kubernetes API
Use Role Based Access Controls to minimize exposure
Exercise caution in using service accounts e.g. disable defaults, minimize permissions on newly created ones
Update Kubernetes frequently

![img](https://training.linuxfoundation.org/wp-content/mu-plugins/lf-owls-pdp/assets/images/pdp-fundamentals/course-chapter.png)System Hardening15%

Minimize host OS footprint (reduce attack surface)
Minimize IAM roles
Minimize external access to the network
Appropriately use kernel hardening tools such as AppArmor, seccomp

![img](https://training.linuxfoundation.org/wp-content/mu-plugins/lf-owls-pdp/assets/images/pdp-fundamentals/course-chapter.png)Minimize Microservice Vulnerabilities20%

Setup appropriate OS level security domains e.g. using PSP, OPA, security contexts
Manage Kubernetes secrets
Use container runtime sandboxes in multi-tenant environments (e.g. gvisor, kata containers)
Implement pod to pod encryption by use of mTLS

![img](https://training.linuxfoundation.org/wp-content/mu-plugins/lf-owls-pdp/assets/images/pdp-fundamentals/course-chapter.png)Supply Chain Security20%

Minimize base image footprint
Secure your supply chain: whitelist allowed registries, sign and validate images
Use static analysis of user workloads (e.g.Kubernetes resources, Docker files)
Scan images for known vulnerabilities

![img](https://training.linuxfoundation.org/wp-content/mu-plugins/lf-owls-pdp/assets/images/pdp-fundamentals/course-chapter.png)Monitoring, Logging and Runtime Security20%

Perform behavioral analytics of syscall process and file activities at the host and container level to detect malicious activities
Detect threats within physical infrastructure, apps, networks, data, users and workloads
Detect all phases of attack regardless where it occurs and how it spreads
Perform deep analytical investigation and identification of bad actors within environment
Ensure immutability of containers at runtime
Use Audit Logs to monitor access