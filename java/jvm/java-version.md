# java-version



### Macos M1

- jdk 저장 위치

~~~sh
/Library/Java/JavaVirtualMachines # path

$ ls -l
drwxr-xr-x  3 root   wheel   96  3  6  2022 jdk-11.0.14.jdk
drwxr-xr-x@ 4 staek  staff  128  5 29 19:25 jdk-17.0.2.jdk
~~~

- jdk 버전, 실행위치 조회

~~~sh
$ /usr/liexec/java_home -V

Matching Java Virtual Machines (2):
    17.0.2 (x86_64) "Oracle Corporation" - "OpenJDK 17.0.2" /Library/Java/JavaVirtualMachines/jdk-17.0.2.jdk/Contents/Home
    11.0.14 (x86_64) "Oracle Corporation" - "Java SE 11.0.14" /Library/Java/JavaVirtualMachines/jdk-11.0.14.jdk/Contents/Home
/Library/Java/JavaVirtualMachines/jdk-17.0.2.jdk/Contents/Home
~~~

- 특정 jdk 버전 조회

~~~sh
$ /usr/libexec/java_home -v 11
/Library/Java/JavaVirtualMachines/jdk-11.0.14.jdk/Contents/Home

$ /usr/libexec/java_home -v 17
/Library/Java/JavaVirtualMachines/jdk-17.0.2.jdk/Contents/Home
~~~

- jdk 사용버전 변경.

~~~sh
vi ~/.zshrc
export JAVA_HOME=$(/usr/libexec/java_home -v 11) # add line
source ~/.zshrc
java -version # 11 버전 출력.
~~~









