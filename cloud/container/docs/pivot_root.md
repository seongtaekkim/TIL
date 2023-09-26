# pivot_root

- root filesystem을 바꿔서 기존의 root 디렉토리로 이동 못하게 할 수 있다.



### chroot 문제점

- isolation 불가능
- root 권한 탈취 가능
- resource 제한 블가능



### pivot_root

- 사용법: pivot_root "new-root" "old-root"
- 새로운 new-root를 "/"에 붙이고 기존 root filesystem을 old-root에 지정한다.



### mount

- root filesystem tree에 다른 파일시스템을 붙이는 명령

- 사용법: mount -t "filesystem type" "device name" "directory mount point"

- 옵션 -t tmpfs (temporary filesystem: 임시로 메모리에 생성됨, /proc/filesystem에서 타입 조회가능)
- 옵션 -o size=1m (용량 지정)



### unshare

- 새로운 네임스페이스를 만들고 프로그램을 실행하는 프로그램

- 사용법: unshare "options" "program "arguments""
- 새로운 namespace 생성
- 특정 프로그램 실행

- 옵션: -m : mount namespace를 분리하여 프로세스를 실행해 준다.
  pivot_root는 root filesystem의 mount point를 변경하기 때문에
  호스트에 영향을 주지 않기 위하여 unshare -m을 실행하여 호스트와 mount namespace를 분리한다.



### mount test

- mount-root 폴더를 만들고 1G 크기의 **tmpfs 파일시스템을 mount-root 폴더로 지정**.

~~~sh
mkdir mount-root
mount -n -t tmpfs -o size=1G none ./mount-root
~~~

- mount 확인

~~~sh
root@seongtki:/# mount | grep mount-root
none on /mount-root type tmpfs (rw,relatime,size=1048576k)
~~~

- pivot_root로 기존 root filesystem을 mount할 mount point로 old-root를 mount-root폴더 하위에 생성

~~~sh
cp -r nginx-root/* mount-root/ # image 복사
mkdir mount-root/old-root
~~~

~~~sh
cd mount_root
unshare -m # host와 현재 디렉토리를 격리한다

pivot_root . old-root # 현재 디렉토리를 root 디렉토리로 지정하고, 그 폴더를 old-root로 한다.
cd /
ls
cd / && cd ../../../
ls
~~~



- 원래 root로 벗어날 수 없다.
- root-path를 변경했던 chroot와 달리 unshare는 root filesystem의 mount point 를 변경한다.

~~~sh
root@seongtki:/# a.out
# ls
bin   docker-entrypoint.d   etc   lib32   media     opt   run	sys  var
boot  docker-entrypoint.sh  home  lib64   mnt	    proc  sbin	tmp
dev   escape		    lib   libx32  old-root  root  srv	usr
# exit
root@seongtki:/# ls
bin   docker-entrypoint.d   etc   lib32   media     opt   run   sys  var
boot  docker-entrypoint.sh  home  lib64   mnt       proc  sbin  tmp
dev   escape                lib   libx32  old-root  root  srv   usr
~~~



- mount 해제

~~~sh
umount -f "directory name"
~~~

























































