# static analysis



- Tips for Static Analysis

  - Avoid Host namespaces such as `hostNetwork: true, hostIPC: true, hostPID: true`
  - Avoid running pod as `securityContext.privileged: true`
  - Avoid running pod as root or 0 in securityContext.runAsUser
    - use nobody or some user xxxx

- Do not use `:latest` tag instead use specific version

- Static YAML analysis using poular tool Kubesec

  - Kubesec scans and gives the risk score

  - Run Kubesec locally using

    ```sh
    # run scan using kubesec
    kubesec scan pod.yaml
    # run kubesec locally on 8080 port
    kubesec http 8080 &
    # kubesec API invoke and scan (https://v2.kubesec.io/scan)
    curl -sSX POST --data-binary @test/asset/score-0-cap-sys-admin.yml http://localhost:8080/scan
    ```

    

  -  https://kubernetes.io/blog/2018/07/18/11-ways-not-to-get-hacked/#7-statically-analyse-yaml

  -  https://kubesec.io/