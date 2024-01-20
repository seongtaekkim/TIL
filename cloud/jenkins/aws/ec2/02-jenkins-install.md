

## AWS EC2에 Jenkins 서버 설치하기

- 주의) 리소스가 기본적으로 적게들진 않는거 같다.
- 이 예제에서는 t3.small, linux2 로 진행함.
  - [ec2 jenkins swap](https://akku-dev.tistory.com/83) - 너무 부족하면 swap 사용도 고려하자.



### jdk install

~~~sh
sudo yum install java-17-amazon-corretto
~~~



### maven install

~~~sh
sudo wget https://mirror.navercorp.com/apache/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz

sudo tar -xvf apache-maven-3.9.6-bin.tar.gz
~~~



- maven path 설정

~~~sh
vi ~/.bash_profile

PATH=$PATH:$HOME/.local/bin:$HOME/bin
M2_HOME=/opt/maven
PATH=$PATH:$M2_HOME:$M2_HOME/bin
export PATH
~~~



- git 설치

~~~sh
sudo yum install git -y
~~~



- [jenkins install](https://pkg.jenkins.io/redhat-stable/)

~~~sh
sudo wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo
sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io-2023.key
sudo yum install jenkins
~~~

- jenkins 실행
- jenkins init password

~~~sh
sudo systemctl start jenkins.service
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
~~~



