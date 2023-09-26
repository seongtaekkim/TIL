# namespace test

- namespace, veth 등을 생성해서 1:1 통신을 해보자.



## 테스트

### namespace, virture ethernet, ip 설정

~~~sh
$ ip link
$ ip netns
$ ip netns add red
$ ip netns add blue
$ ip link add veth-red type veth peer name veth-blue
$ ip link set veth-red netns red
$ ip link set veth-blue netns blue
$ ip netns exec red ip link set veth-red up
$ ip netns exec blue ip link set veth-blue up
$ ip netns exec red ip addr add 1.1.1.1/24 dev veth-red
$ ip netns exec blue ip addr add 1.1.1.2/24 dev veth-blue
~~~



### Blue namesace 

- tcpdump 실행
- red ns 에서 ping 요청을 하면, arp table에 등록 후, 해당 mac 으로 응답을 보내고
- 이 후 icmp 요청에 대한 응답을 처리한다.

~~~sh
$ nsenter --net=/var/run/netns/blue
$ tcpdump -li veth-blue
tcpdump: verbose output suppressed, use -v or -vv for full protocol decode
listening on veth-blue, link-type EN10MB (Ethernet), capture size 262144 bytes
07:12:35.267019 ARP, Request who-has 1.1.1.2 tell 1.1.1.1, length 28
07:12:35.267034 ARP, Reply 1.1.1.2 is-at 4e:87:56:0c:9e:d0 (oui Unknown), length 28
07:12:35.267037 IP 1.1.1.1 > 1.1.1.2: ICMP echo request, id 1758, seq 1, length 64
07:12:35.267045 IP 1.1.1.2 > 1.1.1.1: ICMP echo reply, id 1758, seq 1, length 64
07:12:36.268644 IP 1.1.1.1 > 1.1.1.2: ICMP echo request, id 1758, seq 2, length 64
~~~

- arp 정보 조회

~~~sh
$ ip neigh show
1.1.1.1 dev veth-blue lladdr f6:e9:d4:cf:26:3a STALE
~~~

~~~
root@seongtki:~# arp
Address                  HWtype  HWaddress           Flags Mask            Iface
1.1.1.1                  ether   f6:e9:d4:cf:26:3a   C                     veth-blue
~~~

- 각종 정보 조회

~~~sh
$ ip address show
1: lo: <LOOPBACK> mtu 65536 qdisc noop state DOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
5: veth-blue@if6: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default qlen 1000
    link/ether 4e:87:56:0c:9e:d0 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 1.1.1.2/24 scope global veth-blue
       valid_lft forever preferred_lft forever
    inet6 fe80::4c87:56ff:fe0c:9ed0/64 scope link
       valid_lft forever preferred_lft forever

$ bridge fdb show
33:33:00:00:00:01 dev veth-blue self permanent
01:00:5e:00:00:01 dev veth-blue self permanent
33:33:ff:0c:9e:d0 dev veth-blue self permanent

$ ip route show
1.1.1.0/24 dev veth-blue proto kernel scope link src 1.1.1.2

$ iptables -S
-P INPUT ACCEPT
-P FORWARD ACCEPT
-P OUTPUT ACCEPT
~~~





### red namesace 

- blue ns로 ping을 보낸다. 
- 처음엔 arp 정보가 없는데, arp 응답이 오게 되면 arp table 등록 후 icmp 통시을 하게 된다.

~~~sh
$ nsenter --net=/var/run/netns/red

PING 1.1.1.2 (1.1.1.2) 56(84) bytes of data.
64 bytes from 1.1.1.2: icmp_seq=1 ttl=64 time=0.041 ms
64 bytes from 1.1.1.2: icmp_seq=2 ttl=64 time=0.045 ms
64 bytes from 1.1.1.2: icmp_seq=3 ttl=64 time=0.061 ms
~~~

- arp 정보 조회

~~~sh
$ ip neigh show
1.1.1.2 dev veth-red lladdr 4e:87:56:0c:9e:d0 STAL
~~~

~~~sh
$ arp
Address                  HWtype  HWaddress           Flags Mask            Iface
1.1.1.2                  ether   4e:87:56:0c:9e:d0   C                     veth-red
~~~

- 각종 정보 조회

~~~sh
$ ip address show
1: lo: <LOOPBACK> mtu 65536 qdisc noop state DOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
6: veth-red@if5: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default qlen 1000
    link/ether f6:e9:d4:cf:26:3a brd ff:ff:ff:ff:ff:ff link-netnsid 1
    inet 1.1.1.1/24 scope global veth-red
       valid_lft forever preferred_lft forever
    inet6 fe80::f4e9:d4ff:fecf:263a/64 scope link
       valid_lft forever preferred_lft forever

$ bridge fdb show
33:33:00:00:00:01 dev veth-red self permanent
01:00:5e:00:00:01 dev veth-red self permanent
33:33:ff:cf:26:3a dev veth-red self permanent

$ ip route show
1.1.1.0/24 dev veth-red proto kernel scope link src 1.1.1.1

$ iptables -S
-P INPUT ACCEPT
-P FORWARD ACCEPT
-P OUTPUT ACCEPT
~~~





### 삭제

- 테스트를 후 생성한 리소들을 삭제해준다.

~~~sh
$ ip -n "netns name" link del "veth-red"
$ ip netns del "name"
~~~















