# ETCD



### 데이터 조회

~~~sh
ETCDCTL_API=3 etcdctl --endpoints 127.0.0.1:2379 \
  --cert=/etc/kubernetes/pki/etcd/server.crt \
  --key=/etc/kubernetes/pki/etcd/server.key \
  --cacert=/etc/kubernetes/pki/etcd/ca.crt \
get /registry/secrets/"namespace"/"secretName" | hexdump -C

~~~

- etcdctl 설치되어 있는 node 에서 secret 조회 가능.
- get + /registry/secrets/"namespace"/"secretName"
- hexdump -C 로 포맷.
  - 데이터구분을 위해 사용함.



### encryption data

- resources는 secret, configmap 등을 선ㅐㄱ한다.
- providers 는 여러종류의 암호화 방식이 존재한다. key와 secret을 임의로 선택하여 작성한다.

~~~yaml
apiVersion: apiserver.config.k8s.io/v1
kind: EncryptionConfiguration
resources:
  - resources:
      - secrets
    providers:
      - aescbc:
          keys:
            - name: key1
              secret: <BASE 64 ENCODED SECRET>
      - identity: {}
~~~

~~~sh
head -c 32 /dev/urandom | base64 # 임의 문자열을 인코딩 한 결과를 위에 secret에 넣음
~~~





### apiserver 수정

- 위에 작성한 EncryptionConfiguration 위치를 --encryption-provider-config 에 작성하고
- volume을 설정해준다.
- 이후 apiserver가 재기동하며 앞으로 생성하는 secret은 암호화되어 저장된다.

~~~yaml
- --encryption-provider-config=/etc/kubernetes/enc/enc.yaml  # add this line

volumeMounts:
- name: enc                           # add this line
  mountPath: /etc/kubernetes/enc      # add this line
  readonly: true                      # add this line
      
volumes:
- name: enc                             # add this line
  hostPath:                             # add this line
    path: /etc/kubernetes/enc           # add this line
    type: DirectoryOrCreate             # add this line
~~~

~~~sh
kubectl get secrets --all-namespaces -o json | kubectl replace -f - # 이전 오브젝트에도 적용할 수 있다.
~~~

