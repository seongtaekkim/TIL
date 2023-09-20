



리눅스에서 도커를 사용해야 성능이 가장 좋다. 윈도우에서 사용하면 하이버바이저를 사용한다고하는데 .. 엥?이해안감

윈도우,macos 에서 도커를 사용하면 단점이 무엇이 있지 ? 



아무os설치안한것  : 베어메탈



~~~
sudo -i
~~~





~~~
$ apt install docker.io
$ docker # 도움말
# docker search tomcat # 허브에서 원하는 서비스 찾기
# docker run -d -p 8080:8080 --name -name tc consol/tomcat7.0 # 이거 안됨. 아마 arm64라서 그런듯
$ docker run -d -p 8888:8888 --name ng nginx
~~~





## 3. 내가 원하는 이미지 찾기 도커 레지스트리

업로드 하는방법은 나중에 알려주겠다.

지금은 사용하는 방법만 알려주겠다.



hub.docker.com



**이미지는 static 한상태 로 받아진다.(실행불가) -> 컨테이너를 실행할 수 있다.**



**docker 허브에서 레지스트리 내용을 확인할 수 있다.**

- 경로가 없으면 오피셜 이미지(tomcat)

- 경로가 있으면 사용자가 올린 이미지 (xxxxx/tomcat)



**레지스트리는 private 한 공간을 설정할 수 도 있다.**

- 업로드 하려면 id가 필요하다.
- 1개는 무료, 그 이상은 유료



### pulling

- image 다운로드

~~~
sudo docker serarch nginx # search in hub
docker pull "image" # image download
docker images # download list 
~~~





## 4. 도커 라이프사이클 이해하기

![img](https://gasbugs.notion.site/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2F88036ed7-dde8-4f01-8ea9-4be6c7602f59%2FUntitled.png?id=14048913-6371-43f7-aa2b-0078fd4378f6&table=block&spaceId=6c6bc534-f820-444c-9de9-0ff0fe485f99&width=1800&userId=&cache=v2)





## 5. 도커 라이프 사이클 명령어 실습



- 4에서 학습한 라이프사이클 각각의 명령어를 실습해봄.
- pull, create, start, stop, rm, rmi, -f, run



~~~
docker pull nbinx
docker create -p 80:80 --name nx nginx
docker start  nx
docker stop nx
docker rm nx # 실행중인것은 stop 이후 가능.

# run : create + start

docker images
docker rmi nginx # image remove

# contianer 무시하고 image 삭제
docker rmi -f nx

~~~



## 6. (중요)이미지 비밀 레이어



~~~
$ docker info
~~~



~~~
$ cd /var/lib/docker
$ du -sh image
$ du -sh overlay2 # 실질적인 데이터가 배치되는 곳이다.
$ du -sh containers
~~~



- imae 정보 확인하는 방법

~~~
$ docker images
$ docker inspect nginx
~~~

- RootFS : 실질적인 filesystem 구성하는 부분.
  - layer 실제 정보 참조해쉬
  - -> /var/lib/docker/image/overlay2/layerdb/sha256 에서 해쉬값으로 참조.
  - -> 해당해쉬폴더에 접근해서 cache-id 를 조회하면, overay2 해쉬 참조값을 찾을 수 있다.

- Cmd : 실행 명령어



~~~
ls -R imagedb # imagedb 디렉토리리 하위 정보를 재귀로 모두 보여줌
du -sh /var/lib/docker # 전체적인 용량
~~~

- imagedb정보는 layrdb에 있고, layerdb정보는 overlay2 변경사항에 있고, , 변경사항정보는 l 에 있다. 

  - image/imagedb

  - image/layerdb

  - overay2/변경사항 

  - /l => 실질적인 파일시스템이 저장되어 있다.

- 레이어가 커지면 실질적으로 데이터가 커지는 부분은 overay2 이다.



## 7. (중요)도커의 유용한 명령어



- 컨테이너 조회, stop

~~~
docker ps -a -q # containers id
docker stop 'docker ps -a -q'
~~~



- os와 container 간 파일 복사가 가능하다.

~~~
docker cp "filename" "container name":"directory"
docker cp "filename" tf:/
~~~





- id를 추출해서 한번에 stop, rm 할 수 있다.

~~~
$ docker stop `docker ps -a -q`
d0739727f82d
43e6a970590d
$ docker rm `docker ps -a -q`
~~~



- stop 만 해도 nginx 가 삭제되는 것을 알 수 있다.

~~~
$ docker run -d --name nx -p 80:8080 --rm nginx
$ docker stop nx
$ docker ps -a
~~~





## 8. 도커 컨테이너 실행 연습문제

- jenkins 이미지를 다운로드 받아서 컨테이너에 올려보는 실습.



### 연습문제

~~~
다음 이미지를 사용 : jenkins/jenkins:lts-jdk11

1. 기존에 설치된 모든 컨테이너와 이미지 정지 및 삭제
2. 도커 기능을 사용해 Jenkins 검색
3. Jenkins를 사용하여 설치
4. Jenkins 포트로 접속하여 웹 서비스 열기
5. Jenkins의 초기 패스워드 찾아서 로그인하기
~~~

- run 할때 --rm 명령어를 주면, 컨테이너가 내려갈때 컨테이너가 자동으로 삭제된다.



~~~
docker rmi `docker images -a -q`

docker serach jenkins
docker pull jenkins/jenkins
docker images
docker inspect jk # port정보, directory 등 확인
docker create -p 8080:8080 --name jk jenkins/jenkins # portforward 포함해서 컨테이너 생성
docker start jk
docker logs jk # init password 검색 후 취득
docker exec -it jk bash 
~~~





1. 기존에 설치된 모든 컨테이너와 이미지 정지 및 삭제

   ```bash
   sudo docker stop `sudo docker ps -a -q`
   sudo docker rm `sudo docker ps -a -q`
   sudo docker rmi `sudo docker images -q`
   ```

2. 도커 기능을 사용해 Jenkins 검색

   ```bash
   sudo docker search jenkins
   ```

3. Jenkins를 사용하여 설치

   ```bash
   sudo docker pull jenkins
   sudo docker inspect jenkins
   sudo docker run -d -p 8080:8080 --name jk jenkins/jenkins:lts-jdk11 test
   ```

4. Jenkins 포트로 접속하여 웹 서비스 열기

   ```bash
   firefox 127.0.0.1:8080
   브라우저에 캐시가 남아있는 경우에는 ctl + shift + del
   ```

5. Jenkins의 초기 패스워드 찾아서 로그인하기

   ```bash
   sudo docker exec -it jk cat /var/jenkins_home/secrets/initialAdminPassword
   sudo docker logs jk
   ```







## 9. 환경 변수 사용해 MySQL 서비스 구축하기



~~~
docker run -d --name nx -e env_name=test1234 --rm nginx # nginx에 환경변수 세팅
docker exec -it nx bash
echo $env_name

# mysql컨테이너 실행 시  root password를 환경변수로 설정해야 한다.
docker run --name ms -e MYSQL_ROOT_PASSWORD=1234 -d --rm mysql
docker exec -it ms bash
~~~

- mysql 컨테이너가 요구하는 환경변수 설정을 하지 않으면 에러가 발생함.

~~~
2023-04-06 14:44:15+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.32-1.el8 started.
2023-04-06 14:44:15+00:00 [Note] [Entrypoint]: Switching to dedicated user 'mysql'
2023-04-06 14:44:15+00:00 [Note] [Entrypoint]: Entrypoint script for MySQL Server 8.0.32-1.el8 started.
2023-04-06 14:44:15+00:00 [ERROR] [Entrypoint]: Database is uninitialized and password option is not specified
    You need to specify one of the following as an environment variable:
    - MYSQL_ROOT_PASSWORD
    - MYSQL_ALLOW_EMPTY_PASSWORD
    - MYSQL_RANDOM_ROOT_PASSWORD
~~~





## 10. 볼륨 마운트하여 Jupyter LAB 서비스 구축



### 볼륨마운트 형식

~~~
docker run -v <호스트 경로>:<컨테이너 내 경로>:<권한> # /tmp:home/user:ro
~~~

- rw : 읽고쓰기
- ro : 읽기



### nginx 볼륨마운트 

~~~
docker run -d -p 8888:80 --rm -v /var/www:/usr/share/nginx/html:ro  nginx
~~~

- host 의 /var/www에 index.html 파일을 만들면 적용되어 브라우저에 표출된다.



### jupyter 볼륨마운트

~~~
 docker run --rm -p 9999:8888 -e JUPYTER_ENABLE_LAB=yes -v "$PWD":/home/seongtki/work:rw jupyter/datascience-notebook
~~~



- token을 주피터 패스워드란에 입력한다.

~~~
[I 2023-04-07 00:04:45.396 ServerApp] Jupyter Server 2.5.0 is running at:
[I 2023-04-07 00:04:45.396 ServerApp] http://8218cee91ebd:8888/lab?token=bdfe4530f27aef4d458130f7b7048e59fba13da7916769fc
[I 2023-04-07 00:04:45.396 ServerApp]     http://127.0.0.1:8888/lab?token=bdfe4530f27aef4d458130f7b7048e59fba13da7916769fc
~~~







## 11. 직접 도커 이미지 빌드하기





### 소켓프로그램 생성

~~~python
# test_server.py
import socket

with socket.socket() as s:
  s.bind(("0.0.0.0", 12345))
  s.listen()
  print("server is started")
  conn, addr = s.accept()
  # conn 클라이언트와 통신할 소켓
  # addr 클라이언트의 정보가 들어있음
  with conn:
    print("Connected by", addr)
    while True:
      data = conn.recv(1024)
      if not data: break
      conn.sendall(data)
~~~



~~~bash
python3 test_server.py

---

nc 127.0.0.1 12345
~~~





### 도커 파일 생성

별도의 디렉토리를 생성하고 dockfile과 위에서 생성한 python파일을 새 디렉토리에 배치한다.

```bash
mkdir my_first_project
mv test_server.py ./my_first_project/
cd my_first_project/
gedit dockerfile
```

dockerfile

```docker
FROM python:3.7

RUN mkdir /echo
COPY test_server.py /echo

CMD ["python", "/echo/test_server.py"]
```

빌드 후 테스트

```bash
ls 
dockerfile test_server.py

sudo docker build -t ehco_test .
sudo docker images
sudo docker run -t -p 12345:12345 --name et --rm echo_test
nc 127.0.0.1 12345
```





## 12. 도커 이미지 푸시와 히스토리 확인





### 도커 이미지 태그 변경 후 푸시

먼저 회원가입을 하도록 한다.

https://hub.docker.com/

회원가입한 정보를 토대로 다음 내용을 작성한다. login을 하지 않으면 정상 동작하지 않는다. 

push를 위해서는 이미지앞에 `아이디/` 를 추가해야 한다.

```bash
sudo docker login
sudo docker tag echo_test seongtaekkim/echo_test:v3.7 # 이미지는 같은데 이름만 복사된 거 같다.
sudo docker images
sudo docker push seongtaekkim/echo_test:v3.7
```

https://hub.docker.com/ 에 접속하여 내 레파지토리에 도커가 잘 등록됐는지 확인해본다.

모든 이미지 삭제 후 다시 gasbugs/echo_test 실행하여 잘 다운로드돼 실행되는지 테스트한다.

```bash
sudo docker rmi `docker images -q` -f # conflict 가 나면 -f 로 지울 수 있다/
sudo docker run -t -p 12345:12345 --name et --rm seongtaekkim/echo_test:v3.7
```



### 도커 이미지 히스토리 확인

다음 명령을 사용하면 도커 이미지가 어떤 히스토리를 가졌는지 확인할 수 있다. 제일 상단에 앞서 우리가 dockerfile을 빌드해서 추가한 정보가 있다.

```bash
docker history seongtaekkim/echo_test
```





## 13. 프라이베이트 레지스트리 서버 구현 및 사용

private registry 만들기

```bash
docker run -d --name docker-registry -p 5000:5000 registry
curl 127.0.0.1:5000/v2
```

브라우저를 켜서 127.0.0.1:5000/v2/ 서비스 접속 가능한지 확인



프라이베이트 레지스트리에 이미지 푸시하기

```bash
sudo docker tag echo_test 127.0.0.1:5000/echo_test
sudo docker push 127.0.0.1:5000/echo_test
```

도커 API 관련 링크: https://docs.docker.com/registry/spec/api/

인증 관련 참고 링크: https://docs.docker.com/registry/configuration/#auth





## 14. 풀스택 워드프레스 컨테이너 이미지 만들기



도커에서 제공하는 워드프레스와 MySQL은 따로 떨어진 형태로 존재한다. 여기서는 하나의 컨테이너에서 워드프레스와 MySQL을 동작시킬 수 있도록 만들어본다. 일단 가장 먼저 할 일은 PHP와 DB가 공존하는 환경을 찾는 것이다. 다양한 솔루션들이 있는데 그중에 XAMPP는 도커로 이미 만들어져있어 유용하게 사용할 수 있다. xampp는 apache, MariaDB, php가 설치돼 있다. 여기에 워드프레스만 올리면 바로 컨테이너를 만들 수 있다.

먼저 도커 허브에서 다음 사이트를 찾아내자.

https://hub.docker.com/r/tomsik68/xampp

이 사이트의 컨테이너를 불러온 뒤 워드프레스 설치 과정을 진행하도록 한다. 컨테이너에서 SSH와 다앙한 포트를 지원하지만 우리는 80포트만 사용할 예정이다.

```bash
sudo docker run --name WP -p 80:80 -d tomsik68/xampp
```

이제 가상환경은 준비됐으니 워드프레스를 설치하는 작업만 남았다. "워드프레스 다운로드"를 검색하자.

워드프레스 바로가기: https://ko.wordpress.org/download/

wget을 사용해 다운로드하고 압축을 푼다.

```bash
wget <https://ko.wordpress.org/latest-ko_KR.tar.gz>
tar -xf latest-ko_KR.tar.gz
```

컨테이너 내의 웹 파일 정리

```bash
sudo docker exec -it WP bash
chown daemon. /opt/lampp/htdocs 
cd /opt/lampp/htdocs/
mkdir backup
mv * ./backup/
exit
```

워드프레스 파일을 컨테이너에  복사하고 웹 루트 디렉토리에 배치

```python
sudo docker cp wordpress WP:/opt/lampp/htdocs
sudo docker exec -it WP bash
mv /opt/lampp/htdocs/wordpress/* /opt/lampp/htdocs/
exit
sudo docker restart WP
```

127.0.0.1/phpmyadmin으로 접속해서 wordpress 데이터베이스 생성한다.



마지막으로 127.0.0.1로 접속하여 워드프레스 설치를 웹 브라우저 화면으로 진행하면 된다.



















