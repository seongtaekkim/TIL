

## AWS EC2에 Docker 서버 설치하기



~~~sh
sudo yum install  docker
~~~



~~~sh
sudo usermod –aG docker ec2-user
~~~



~~~sh
sudo systemctl enable docker.socket
sudo systemctl start docker.socket # docker server 시작
~~~



~~~sh
docker version # client, server 모두 확인
~~~







