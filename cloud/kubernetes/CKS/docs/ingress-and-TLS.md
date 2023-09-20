# Ingress and TLS

- Secure an Ingress by specifying a Secret that contains a TLS private key and certificate
- The Ingress resource only supports a single TLS port, 443, and assumes TLS termination at the ingress point



### Create TLS Certificate & key

```sh
openssl req -x509 -newkey rsa:4096 -sha256 -nodes -keyout tls.key -out tls.crt -subj "/CN=learnwithgvr.com" -days 365
openssl req -nodes -new -x509 -keyout tls-ingress.key -out tls-ingress.crt -subj "/CN=learnwithgvr.com -days 365
```

### Create a Secret

```sh
kubectl create secret tls learnwithgvr-sec --cert=tls.crt --key=tls.key
```



### create ingress with TLS

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: ingress-tls
  namespace: ingresstest
data:
  tls.crt: |
    $(base64-encoded cert data from tls-ingress.crt)
  tls.key: |
    $(base64-encoded key data from tls-ingress.key)
type: kubernetes.io/tls
---
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: tls-example-ingress
  namespace: ingresstest
spec:
  tls:
  - hosts:
      - learnwithgvr.com
    secretName: learnwithgvr-sec
  rules:
  - host: learnwithgvr.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: service1
            port:
              number: 80
```



~~~sh
curl -kv https://learnwithgvr.com
~~~

- self signed 내용을 볼 수 있다.



