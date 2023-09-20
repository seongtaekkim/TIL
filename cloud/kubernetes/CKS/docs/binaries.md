# binaries

- `sha512sum` 으로 바이너리 해시값을 비교한다.

- kubectl, kubeadm, kubelets, etcd 가 비교 대상이다.
- before using binaries compare checksum with its official sha256/sha512 cryptographic hash value

~~~sh
# Print SHA Checksums - mac
shasum -a 512 kubernetes-client-darwin-arm64.tar.gz
# Print SHA Checksums - linux
sha512sum kubernetes-client-darwin-arm64.tar.gz
~~~



~~~sh
kubectl version --short --client

#download checksum for kubectl for linux - (change version)
curl -LO "https://dl.k8s.io/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl.sha256"

#download old version
curl -LO "https://dl.k8s.io/v1.22.1/bin/linux/amd64/kubectl.sha256"

#download checksum for kubectl for mac - (change version)
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/darwin/amd64/kubectl.sha256"

#insall coreutils (for mac)
brew install coreutils

#verify kubectl binary (for linux)
echo "$(<kubectl.sha256) /usr/bin/kubectl" | sha256sum --check
#verify kubectl binary (for mac)
echo "$(<kubectl.sha256) /usr/local/bin/kubectl" | sha256sum -c
~~~



