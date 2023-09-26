# bridge test

- namespace의 veth는 bridge를 통해서 네트워크 통신을 할 수 있도록 구성하는 테스트를 진행한다.

  1. namespace, bridge 를 구성하고

  2. netfilter로 데이터 전송에 대한 정책을 확인하고
  3. ping 테스트 및 dump 를 확인한다.
  4. 테스트 후에는 fdb, arp등을 확인한다.



### namespace, bridge 구성

~~~sh
# add red namespace, virtual ethenet, bridge
ip netns add red
ip link add veth-red type veth peer name brd-red
ip link set veth-red netns red

# add blue namespace, virtual ethenet, bridge
ip netns add blue
ip link add veth-blue type veth peer name brd-blue
ip link set veth-blue netns blue

# bridge를 추가하고, namesapce에 연결된 bridge선과 연결한다.
ip link add brd0 type bridge
ip link set brd-red master brd0
ip link set brd-blue master brd0

# 각 namespace의 virtual ethenet 에 ip를 할당.
ip netns exec red ip addr add 7.7.7.2/24 dev veth-red
ip netns exec blue ip addr add 7.7.7.3/24 dev veth-blue

# 모든 스위치를 켜준다.
ip netns exec red ip link set veth-red up
ip link set brd-red up
ip netns exec blue ip link set veth-blue up
ip link set brd-blue up
ip link set brd0 up
~~~



### ip address show

- Network interface 를 확인한다.
- bridge 선이 어디에 연결되어 있는 지 확인 가능하다.
  - 3: brd-red@if4 는 4: veth-red@if3 와 연결되어 있다.



- bridge

~~~sh
3: brd-red@if4: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue master brd0 state UP group default qlen 1000
    link/ether ba:0e:67:a9:5f:55 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet6 fe80::b80e:67ff:fea9:5f55/64 scope link
       valid_lft forever preferred_lft forever
       
5: brd-blue@if6: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue master brd0 state UP group default qlen 1000
    link/ether 06:31:1c:4b:e0:85 brd ff:ff:ff:ff:ff:ff link-netnsid 1
    inet6 fe80::431:1cff:fe4b:e085/64 scope link
       valid_lft forever preferred_lft forever
       
7: brd0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default qlen 1000
    link/ether 06:31:1c:4b:e0:85 brd ff:ff:ff:ff:ff:ff
    inet6 fe80::431:1cff:fe4b:e085/64 scope link
       valid_lft forever preferred_lft forever
~~~

- red

~~~sh
root@seongtki:~# nsenter --net=/var/run/netns/red
root@seongtki:~# ip address show
1: lo: <LOOPBACK> mtu 65536 qdisc noop state DOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
4: veth-red@if3: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default qlen 1000
    link/ether 92:1f:c1:d7:4c:d0 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 1.1.1.1/24 scope global veth-red
       valid_lft forever preferred_lft forever
    inet6 fe80::901f:c1ff:fed7:4cd0/64 scope link
       valid_lft forever preferred_lft forever
~~~

- blue

~~~sh
root@seongtki:~# nsenter --net=/var/run/netns/blue
root@seongtki:~# ip address show
1: lo: <LOOPBACK> mtu 65536 qdisc noop state DOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
6: veth-blue@if5: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default qlen 1000
    link/ether 22:6a:f6:44:98:6b brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 1.1.1.2/24 scope global veth-blue
       valid_lft forever preferred_lft forever
    inet6 fe80::206a:f6ff:fe44:986b/64 scope link
       valid_lft forever preferred_lft forever
~~~



### ip route show

- bridge

~~~sh
root@seongtki:~# ip route
default via 10.0.2.2 dev enp0s3 proto dhcp src 10.0.2.15 metric 100
10.0.2.0/24 dev enp0s3 proto kernel scope link src 10.0.2.15
10.0.2.2 dev enp0s3 proto dhcp scope link src 10.0.2.15 metric 100
~~~

- red

~~~sh
root@seongtki:~# ip route show
1.1.1.0/24 dev veth-red proto kernel scope link src 1.1.1.1
~~~

- blue

~~~sh
root@seongtki:~# ip route
1.1.1.0/24 dev veth-blue proto kernel scope link src 1.1.1.2
~~~





### bridge fdb show

- bridge 

~~~sh
root@seongtki:~# bridge fdb show
33:33:00:00:00:01 dev enp0s3 self permanent
01:00:5e:00:00:01 dev enp0s3 self permanent
33:33:ff:fb:6f:5e dev enp0s3 self permanent
01:80:c2:00:00:00 dev enp0s3 self permanent
01:80:c2:00:00:03 dev enp0s3 self permanent
01:80:c2:00:00:0e dev enp0s3 self permanent
ba:0e:67:a9:5f:55 dev brd-red vlan 1 master brd0 permanent
ba:0e:67:a9:5f:55 dev brd-red master brd0 permanent
33:33:00:00:00:01 dev brd-red self permanent
01:00:5e:00:00:01 dev brd-red self permanent
33:33:ff:a9:5f:55 dev brd-red self permanent
06:31:1c:4b:e0:85 dev brd-blue master brd0 permanent
06:31:1c:4b:e0:85 dev brd-blue vlan 1 master brd0 permanent
33:33:00:00:00:01 dev brd-blue self permanent
01:00:5e:00:00:01 dev brd-blue self permanent
33:33:ff:4b:e0:85 dev brd-blue self permanent
33:33:00:00:00:01 dev brd0 self permanent
01:00:5e:00:00:01 dev brd0 self permanent
33:33:ff:4b:e0:85 dev brd0 self permanent
~~~

- red

~~~sh
root@seongtki:~# bridge fdb show
33:33:00:00:00:01 dev veth-red self permanent
01:00:5e:00:00:01 dev veth-red self permanent
33:33:ff:d7:4c:d0 dev veth-red self permanent
~~~

- blue

~~~sh
root@seongtki:~# bridge fdb show
33:33:00:00:00:01 dev veth-blue self permanent
01:00:5e:00:00:01 dev veth-blue self permanent
33:33:ff:44:98:6b dev veth-blue self permanent
~~~



### netfilter 설정확인

- host에서 진행한다.
- 현재 os는 모두 ACCEPT 로 되어 있다.

~~~
root@seongtki:~# iptables -t filter -L  | grep policy
Chain INPUT (policy ACCEPT)
Chain FORWARD (policy ACCEPT)
Chain OUTPUT (policy ACCEPT)
~~~

- 하지만 아래처럼 FORWARD가 DROP으로 되어 있는 경우 정책을 변경해 주어야한다.

~~~
root@seongtki:~# iptables -t filter -L
Chain INPUT (policy ACCEPT)
target     prot opt source               destination

Chain FORWARD (policy ACCEPT)
target     prot opt source               destination
DROP       all  --  1.1.1.0/24           anywhere

Chain OUTPUT (policy ACCEPT)
target     prot opt source               destination
~~~

- 정책 변경
  - INPUT: 로컬로 들어오는 패킷
  - FORWARD: 외부 패킷이 방화벽을 통과해 다른 네트워크로 흘러가는 것을 제어
  - OUTPUT: 외부로 나가는 패킷

~~~sh
# -t : table
# -A : Append chain rule
# -s : src address
# -j : jump to ACCEPT
iptables -t filter -A FORWARD -s 1.1.1.0/24 -j ACCEPT
~~~

~~~sh
# 정책 삭제
iptables -D FORWARD 1 # 1은 rownum을 의미
~~~



### FORWARD: DROP  일 때 테스트

- ARP 까지 성공하지만, ICMP는 실패하여 ping 테스트를 할 수 없다.

- red

~~~sh
root@seongtki:~# ping -c 1 1.1.1.2
PING 1.1.1.2 (1.1.1.2) 56(84) bytes of data.
64 bytes from 1.1.1.2: icmp_seq=1 ttl=64 time=0.038 ms

--- 1.1.1.2 ping statistics ---
1 packets transmitted, 1 received, 0% packet loss, time 0ms
rtt min/avg/max/mdev = 0.038/0.038/0.038/0.000 ms
~~~

- bridge

~~~sh
root@seongtki:~# tcpdump -l -i brd0
tcpdump: verbose output suppressed, use -v or -vv for full protocol decode
listening on brd0, link-type EN10MB (Ethernet), capture size 262144 bytes
04:22:46.256635 IP one.one.one.one > 1.1.1.2: ICMP echo request, id 2338, seq 1, length 64
04:22:46.256661 IP 1.1.1.2 > one.one.one.one: ICMP echo reply, id 2338, seq 1, length 64
04:22:51.285442 ARP, Request who-has one.one.one.one tell 1.1.1.2, length 28
04:22:51.285451 ARP, Request who-has 1.1.1.2 tell one.one.one.one, length 28
04:22:51.285478 ARP, Reply one.one.one.one is-at 92:1f:c1:d7:4c:d0 (oui Unknown), length 28
04:22:51.285483 ARP, Reply 1.1.1.2 is-at 22:6a:f6:44:98:6b (oui Unknown), length 28
~~~

- blue

~~~sh
root@seongtki:~# tcpdump -l -i veth-blue
tcpdump: verbose output suppressed, use -v or -vv for full protocol decode
listening on veth-blue, link-type EN10MB (Ethernet), capture size 262144 bytes

04:22:46.256643 IP 1.1.1.1 > 1.1.1.2: ICMP echo request, id 2338, seq 1, length 64
04:22:46.256660 IP 1.1.1.2 > 1.1.1.1: ICMP echo reply, id 2338, seq 1, length 64
04:22:51.285435 ARP, Request who-has 1.1.1.1 tell 1.1.1.2, length 28
04:22:51.285471 ARP, Request who-has 1.1.1.2 tell 1.1.1.1, length 28
04:22:51.285482 ARP, Reply 1.1.1.2 is-at 22:6a:f6:44:98:6b (oui Unknown), length 28
04:22:51.285483 ARP, Reply 1.1.1.1 is-at 92:1f:c1:d7:4c:d0 (oui Unknown), length 28
~~~





### ping test (성공)

- red -> bridge -> blue

- arp, bridge fdb 정보를 이용해서 통신하고 iptables rule 으로 전송여부를 결정한다

  - 통신 후 arp, bridge fdb 에 통신한 virtual ethenet에 mac정보 확인

  

- red

~~~sh
root@seongtki:~# ping 1.1.1.2
PING 1.1.1.2 (1.1.1.2) 56(84) bytes of data.
64 bytes from 1.1.1.2: icmp_seq=1 ttl=64 time=0.065 ms
64 bytes from 1.1.1.2: icmp_seq=2 ttl=64 time=0.078 ms
64 bytes from 1.1.1.2: icmp_seq=3 ttl=64 time=0.073 ms
64 bytes from 1.1.1.2: icmp_seq=4 ttl=64 time=0.074 ms
~~~



- bridge

~~~sh
root@seongtki:~# tcpdump -l -i brd0
tcpdump: verbose output suppressed, use -v or -vv for full protocol decode
listening on brd0, link-type EN10MB (Ethernet), capture size 262144 bytes



04:03:41.050595 ARP, Request who-has 1.1.1.2 tell one.one.one.one, length 28
04:03:41.050625 ARP, Reply 1.1.1.2 is-at 22:6a:f6:44:98:6b (oui Unknown), length 28
04:03:41.050629 IP one.one.one.one > 1.1.1.2: ICMP echo request, id 2299, seq 1, length 64
04:03:41.050641 IP 1.1.1.2 > one.one.one.one: ICMP echo reply, id 2299, seq 1, length 64
~~~



- blue

~~~sh
root@seongtki:~# tcpdump -l -i veth-blue
tcpdump: verbose output suppressed, use -v or -vv for full protocol decode
listening on veth-blue, link-type EN10MB (Ethernet), capture size 262144 bytes


04:03:41.050600 ARP, Request who-has 1.1.1.2 tell 1.1.1.1, length 28
04:03:41.050623 ARP, Reply 1.1.1.2 is-at 22:6a:f6:44:98:6b (oui Unknown), length 28
04:03:41.050631 IP 1.1.1.1 > 1.1.1.2: ICMP echo request, id 2299, seq 1, length 64
04:03:41.050640 IP 1.1.1.2 > 1.1.1.1: ICMP echo reply, id 2299, seq 1, length 64
04:03:42.053818 IP 1.1.1.1 > 1.1.1.2: ICMP echo request, id 2299, seq 2, length 64
04:03:42.053850 IP 1.1.1.2 > 1.1.1.1: ICMP echo reply, id 2299, seq 2, length 64
04:03:43.056383 IP 1.1.1.1 > 1.1.1.2: ICMP echo request, id 2299, seq 3, length 64
04:03:43.056411 IP 1.1.1.2 > 1.1.1.1: ICMP echo reply, id 2299, seq 3, length 64
04:03:44.060404 IP 1.1.1.1 > 1.1.1.2: ICMP echo request, id 2299, seq 4, length 64
04:03:44.060432 IP 1.1.1.2 > 1.1.1.1: ICMP echo reply, id 2299, seq 4, length 64
~~~



### arp (ping 테스트 후 )

- ping test 후 arp 정보 확인



- red

~~~sh
root@seongtki:~# ip neigh show
1.1.1.2 dev veth-red lladdr 22:6a:f6:44:98:6b STALE
~~~

- blue

~~~sh
root@seongtki:~# ip neigh
1.1.1.1 dev veth-blue lladdr 92:1f:c1:d7:4c:d0 STALE
~~~





### bridge fdb show (ping 테스트 후 )

- ping 테스트 후에 fdb에 brd-red,  brd-blue에 mac주소가 남겨져음을 확인할 수 있다.

~~~sh
...

92:1f:c1:d7:4c:d0 dev brd-red master brd0

...

22:6a:f6:44:98:6b dev brd-blue master brd0

...
~~~

- red
  - mac주소 : link/ether 92:1f:c1:d7:4c:d0 brd ff:ff:ff:ff:ff:ff link-netnsid 0

~~~sh
root@seongtki:~# ip a
1: lo: <LOOPBACK> mtu 65536 qdisc noop state DOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
4: veth-red@if3: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default qlen 1000
    link/ether 92:1f:c1:d7:4c:d0 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 1.1.1.1/24 scope global veth-red
       valid_lft forever preferred_lft forever
    inet6 fe80::901f:c1ff:fed7:4cd0/64 scope link
       valid_lft forever preferred_lft forever
~~~

- blue
  - Mac주소 :  link/ether 22:6a:f6:44:98:6b brd ff:ff:ff:ff:ff:ff link-netnsid 0

~~~sh
root@seongtki:~# ip a
1: lo: <LOOPBACK> mtu 65536 qdisc noop state DOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
6: veth-blue@if5: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default qlen 1000
    link/ether 22:6a:f6:44:98:6b brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 1.1.1.2/24 scope global veth-blue
       valid_lft forever preferred_lft forever
    inet6 fe80::206a:f6ff:fe44:986b/64 scope link
       valid_lft forever preferred_lft forever
~~~









