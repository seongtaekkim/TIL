# dynamic overlay network



### hostA > green 생성

~~~sh
ip netns add green
ip link add dev red-veth2 mtu 1450 netns red type veth peer name green-veth mtu 1450 netns green

ip netns exec green ip link set dev green-veth address 02:42:c0:a8:00:04
ip netns exec green ip addr add dev green-veth 7.7.7.4/24

ip netns exec red ip link set red-veth2 master br0
ip netns exec red ip link set red-veth2 up
ip netns exec green ip link set green-veth up

 ip netns exec green ping -c 1 7.7.7.2 # 같은 host에 있는 다른 ns로 ping 테스트
~~~



### hostB > red

~~~sh
root@seongtki:~# ip neigh add 7.7.7.4 lladdr 02:42:c0:a8:00:04 dev vxlan1
root@seongtki:~# ip neigh
7.7.7.4 dev vxlan1 lladdr 02:42:c0:a8:00:04 PERMANENT
7.7.7.2 dev vxlan1 lladdr 02:42:c0:a8:00:02 PERMANENT
~~~



### hostA > green  ping test

~~~sh
root@seongtki:~#  ip netns exec green ping  7.7.7.3
PING 7.7.7.3 (7.7.7.3) 56(84) bytes of data.
64 bytes from 7.7.7.3: icmp_seq=1 ttl=64 time=3.18 ms
64 bytes from 7.7.7.3: icmp_seq=2 ttl=64 time=2.36 ms
64 bytes from 7.7.7.3: icmp_seq=3 ttl=64 time=1.91 ms
^C
--- 7.7.7.3 ping statistics ---
3 packets transmitted, 3 received, 0% packet loss, time 2012ms
rtt min/avg/max/mdev = 1.910/2.484/3.178/0.524 ms
~~~



### hostB > red

- fdb는 ping test 할때 자동으로 세팅됨  (learning option)

~~~sh
root@seongtki:~# bridge fdb | grep 02:42:c0:a8:00:04
02:42:c0:a8:00:04 dev vxlan1 master br0
02:42:c0:a8:00:04 dev vxlan1 dst 192.168.64.27 link-netnsid 1 self
~~~





## 추가는 아래와 같이 

해결되면 동적으로 됨

### 1. 노드(host) 별 설정

- overray namespace, vxlan, bridge

### 2. 컨테이너 생성

### 3. veth를 br0에 연결

### 4. ARP, FDB 정보등록





![스크린샷 2023-10-04 오후 12.24.42](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-10-04 오후 12.24.42.png)



RouTing NetLink



## Netlink

Netlink는 커널과 유저스페이스 프로세스 사이에서 정보교환을 하는데 사용됨
communication between kernel and user space
socket-based interface
datagram-oriented service
netlink_family selects the kernel module or netlink group to communicate with

  “User space 와 Kernel space 간의 불편한 ioctl 통신방법에 대한 좀 더 유연한 대안으로 만들어짐”

https://man7.org/linux/man-pages/man7/netlink.7.html



## RTNETLINK (RTNL)

### 라우팅과 링크 계층의 설정을 위한 인터페이스

- 커널의 라우팅 테이블을 읽거나 수정
- 커널과 커뮤니케이션하는 창구 역할
- 네트워크 제어를 제공
  - n/w routes, ip addresses, neighbor setups, queueing disciplines, traffic classes, …
  - NETLINK_ROUTE sockets 을 통해서 제어
- based on netlink messages



https://man7.org/linux/man-pages/man7/rtnetlink.7.html



## overay 생성

~~~sh
#!/bin/bash

# reset
sudo ip netns delete overns 2> /dev/null && echo "Deleting existing overlay"

# create overns
sudo ip netns add overns
sudo ip netns exec overns ip link add dev br0 type bridge
sudo ip netns exec overns ip addr add dev br0 7.7.7.1/24
sudo ip link add dev vxlan1 netns overns type vxlan id 42 proxy learning l2miss l3miss dstport 4789
sudo ip netns exec overns ip link set vxlan1 master br0
sudo ip netns exec overns ip link set vxlan1 up
sudo ip netns exec overns ip link set br0 up
~~~





## 컨테이너 생성

~~~sh
#!/bin/bash

containerns=${1:-pinkns}
ip=${2:-2}

# set host ip and interface
host_ipaddr=$(ifconfig enp0s1 | awk '{ print $2}' | grep -E -o "([0-9]{1,3}[\.]){3}[0-9]{1,3}")

# set virtual lladdr & ipaddr
mac=$ip
if [ "$ip" -lt 10 ] ; then
  mac="0${ip}"
fi
lladdr="02:42:c0:a8:00:${mac}"
ipaddr="7.7.7.${ip}"

# storage for arp/fdb
storage_home="/overlaynet/storage"
storage_arp="${storage_home}/arp"
storage_fdb="${storage_home}/fdb"

#------------------------------------------------#

# reset
cnt=$(sudo ip netns | grep $containerns | wc -l)
echo $cnt
if [ "$cnt" -eq 1 ] ; then
  echo "reset ${containerns}"
  sudo ip netns del $containerns
  rm "${storage_arp}/${ipaddr}"
  rm "${storage_fdb}/${lladdr}"
fi

# create netns
sudo ip netns add $containerns

# create veth interfaces - vethO (overns), eth0 (containerns)
vethO="vethO${ip}"
sudo ip link add dev $vethO mtu 1450 netns overns type veth peer name eth0 mtu 1450 netns $containerns

# attach first peer to the bridge in our overlay namespace
sudo ip netns exec overns ip link set $vethO master br0
sudo ip netns exec overns ip link set $vethO up

# move second peer tp container network namespace and configure it
sudo ip netns exec $containerns ip link set dev eth0 address $lladdr
sudo ip netns exec $containerns ip addr add dev eth0 "${ipaddr}/24"
sudo ip netns exec $containerns ip link set dev eth0 up

# write arp/fdb info to File
echo $lladdr > "${storage_arp}/${ipaddr}"
echo $host_ipaddr > "${storage_fdb}/${lladdr}"
~~~



### python 설치

~~~sh
 1035  apt-get update
 1037  apt-get install python
 1039  apt-get upgrade python3
 1041  apt-get install python3-pip
 1042  pip install pyroute2
 1043  pip3 --version
 1045  python3 l2l3miss.py
~~~





## l2l3miss

~~~py
#!/usr/bin/env python

# To use this without l2miss/l3miss from vxlan, enable app_solicit on your interface
# echo 1 | sudo tee -a  /proc/sys/net/ipv4/neigh/eth0/app_solicit

from pyroute2 import NetNS
from pyroute2.netlink.rtnl import ndmsg
from pyroute2.netlink.exceptions import NetlinkError
import logging

vxlan_ns="overns"
if_family = {2 : "AF_INET"}
nud_state = {
  0x01 : "NUD_INCOMPLETE",
  0x02 : "NUD_REACHABLE",
  0x04 : "NUD_STALE",
  0x08 : "NUD_DELAY",
  0x10 : "NUD_PROBE",
  0x20 : "NUD_FAILED",
  0x40 : "NUD_NOARP",
  0x80 : "NUD_PERMANENT",
  0x00 : "NUD_NONE"
}
type = {
  0 : "RTN_UNSPEC",
  1 : "RTN_UNICAST",
  2 : "RTN_LOCAL",
  3 : "RTN_BROADCAST",
  4 : "RTN_ANYCAST",
  5 : "RTN_MULTICAST",
  6 : "RTN_BLACKHOLE",
  7 : "RTN_UNREACHABLE",
  8 : "RTN_PROHIBIT",
  9 : "RTN_THROW",
  10 : "RTN_NAT",
  11 : "RTN_XRESOLVE"
}

logging.basicConfig(level=logging.DEBUG)

ns = NetNS(vxlan_ns)
ns.bind()

while True:
    msg = ns.get()
    for m in msg:
        logging.debug("Received an event: {}".format(m['event']))
        if m['event'] != "RTM_GETNEIGH" :
            continue
    if 'ifindex' not in m :
        continue

    ifindex=m['ifindex']
    ifname=ns.get_links(ifindex)[0].get_attr("IFLA_IFNAME")

    logging.debug("------------ ndmsg start ------------")
    logging.debug("Family: {}".format(if_family.get(m['family'],m['family'])))
    logging.debug("Interface {} index: {}".format(ifname,ifindex))
    logging.debug("State: {}".format(nud_state.get(m['state'],m['state'])))
    logging.debug("Flags: {}".format(m['flags']))
    logging.debug("Type: {}".format(type.get(m['ndm_type'],m['ndm_type'])))
    logging.debug("------------ ndmsg end --------------")

    if m.get_attr("NDA_DST") is not None:
      ipaddr=m.get_attr("NDA_DST")
      logging.info("L3Miss on {}: Who has IP: {}? Check arp table on {}".format(ifname,ipaddr,vxlan_ns))

    if m.get_attr("NDA_LLADDR") is not None:
      lladdr=m.get_attr("NDA_LLADDR")
      logging.info("L2Miss on {}: Who has MAC: {}? Check bridge fdb on {}".format(ifname,lladdr,vxlan_ns))
~~~





## aprd

~~~py
#!/usr/bin/env python

# To use this without l2miss/l3miss from vxlan, enable app_solicit on your interface
# echo 1 | sudo tee -a  /proc/sys/net/ipv4/neigh/eth0/app_solicit

from pyroute2 import NetNS
from pyroute2.netlink.rtnl import ndmsg
from pyroute2.netlink.exceptions import NetlinkError
import logging
import os

vxlan_ns="overns"
#logging.basicConfig(level=logging.INFO)
logging.basicConfig(level=logging.DEBUG)

ipr = NetNS(vxlan_ns)
ipr.bind()

storage_home = "/overlaynet/storage"
storage_arp = storage_home + "/arp"
storage_fdb = storage_home + "/fdb"

while True:
    msg = ipr.get()
    for m in msg:
        logging.debug("Received an event: {}".format(m['event']))
        if m['event'] != "RTM_GETNEIGH" :
            continue
    if 'ifindex' not in m :
        continue

    ifindex=m['ifindex']
    ifname=ipr.get_links(ifindex)[0].get_attr("IFLA_IFNAME")

    if m.get_attr("NDA_DST") is not None:
      ipaddr=m.get_attr("NDA_DST")
      logging.info("L3Miss on {}: Who has IP: {}?".format(ifname,ipaddr))

      arp_path = storage_arp + "/" + ipaddr
      if os.path.exists(arp_path):
        f = open(arp_path, 'r')
        mac_addr = f.readline().rstrip()
        f.close()
        logging.info("Populating ARP table from File: IP {} is {}".format(ipaddr,mac_addr))
        try:
          logging.info("ifindex {}, ipaddr {}, mac_addr {}".format(ifindex,ipaddr,mac_addr))
          ipr.neigh('add', dst=ipaddr, lladdr=mac_addr, ifindex=ifindex, state=ndmsg.states['permanent'])
        except NetlinkError as message:
          print(message)

    if m.get_attr("NDA_LLADDR") is not None:
      lladdr=m.get_attr("NDA_LLADDR")
      logging.info("L2Miss on {}: Who has MAC: {}?".format(ifname,lladdr))

      fdb_path = storage_fdb + "/" + lladdr
      if os.path.exists(fdb_path):
        f = open(fdb_path, 'r')
        dst_host=f.readline().rstrip()
        f.close()
        logging.info("Populating FIB table from File: MAC {} is on host {}".format(lladdr, dst_host))
        try:
          logging.info("ifindex {}, lladdr {}, dst_host {}".format(ifindex,lladdr,dst_host))
          ipr.fdb('add', ifindex=ifindex, lladdr=lladdr, dst=dst_host)
        except NetlinkError as message:
          print(message)
~~~





## 삭제

~~~sh
arr_ns=$(ip netns list | awk '{print $1}')

for i in $arr_ns
do
  `ip netns del "$i"`
done

storage_home="/overlaynet/storage"
rm -f ${storage_home}/*/*
~~~





### hostA 

~~~
# arp등록
# ip netns exec overns ip neigh add 11.11.11.3 lladdr 02:42:c0:a8:00:03 dev vxlan1 

# fdb 등록
# ip netns exec overns bridge fdb replace 02:42:c0:a8:00:03 dev vxlan1 self dst 192.168.104.3 vni 42 port 4789 

~~~

### hostB

~~~
# arp 등록
ip netns exec overns ip neigh add 11.11.11.2 lladdr 02:42:c0:a8:00:02 dev vxlan1    
~~~





​	

![스크린샷 2023-10-10 오전 10.52.10](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-10-10 오전 10.52.10.png)





SM-S911N

R3CW90JEESB



