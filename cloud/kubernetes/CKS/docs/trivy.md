# trivy



### Vulnerability scanning tools are Trivy

- Scan an image using trivy
  - `trivy image busybox:1.33.1`
- It results Low, Medium, High, Critical & Vulnarability ID
  - `trivy image-- severity CRITICAL,HIGH busybox:1.33.1`
  - `kubectl get pods -A -o jsonpath="{..image}" | tr -s '[[:space:]]' '\n' | sort -u`



### reference

- [scan image](https://kubernetes.io/blog/2018/07/18/11-ways-not-to-get-hacked/#10-scan-images-and-run-ids)

- [trivy](https://github.com/aquasecurity/trivy)

- [webhook](https://github.com/linuxacademy/content-cks-trivy-k8s-webhook)

