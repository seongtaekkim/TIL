# secret



Types of Secrets

- Opaque(Generic) secrets
- Service account token Secrets
- Docker config Secrets
  - `kubectl create secret docker-registry my-secret --docker-server=DOCKER_REGISTRY_SERVER --docker-username=DOCKER_USER --docker-password=DOCKER_PASSWORD --docker-email=DOCKER_EMAIL`
- Basic authentication Secret
- SSH authentication secrets 
- TLS secrets 
  - `kubectl create secret tls tls-secret --cert=path/to/tls.cert --key=path/to/tls.key`
- Bootstrap token Secrets 



- Secret as Data to a Container Using a Volume

  ```sh
  kubectl create secret generic mysecret --from-literal=username=devuser --from-literal=password='S!B\*d$zDsb='
  ```

  Decode Secret

  ```sh
  kubectl get secrets/mysecret --template={{.data.password}} | base64 -d
  ```

  

  ```yaml
  apiVersion: v1
  kind: Pod
  metadata:
    name: mypod
  spec:
    containers:
    - name: mypod
      image: redis
      volumeMounts:
      - name: secret-volume
        mountPath: "/etc/secret-volume"
        readOnly: true
    volumes:
    - name: secret-volume
      secret:
        secretName: mysecret
  ```

  

- Secret as Data to a Container Using Environment Variables

  ```yaml
  apiVersion: v1
  kind: Pod
  metadata:
    name: secret-env-pod
  spec:
    containers:
    - name: mycontainer
      image: redis
      # refer all secret data
      envFrom:
        - secretRef:
          name: mysecret-2
      # refer specific variable
      env:
        - name: SECRET_USERNAME
          valueFrom:
            secretKeyRef:
              name: mysecret
              key: username
        - name: SECRET_PASSWORD
          valueFrom:
            secretKeyRef:
              name: mysecret
              key: password
    restartPolicy: Never
  ```

  

- [secret](https://kubernetes.io/docs/concepts/configuration/secret)

- [encrypt-data](https://kubernetes.io/docs/tasks/administer-cluster/encrypt-data)

- [configmap-secret](https://kubernetes.io/docs/tasks/configmap-secret/managing-secret-using-kubectl/)

