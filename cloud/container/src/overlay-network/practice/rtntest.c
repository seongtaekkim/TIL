#include <stdio.h>
#include <string.h>
#include <netinet/in.h>
#include <linux/netlink.h>
#include <linux/rtnetlink.h>
#include <net/if.h>

int main()
{
    struct sockaddr_nl addr;
    int sock, len;
    char buffer[4096];
    struct nlmsghdr *nlh;

    if ((sock = socket(PF_NETLINK, SOCK_RAW, NETLINK_ROUTE)) == -1) {
        perror("couldn't open NETLINK_ROUTE socket");
        return 1;
    }

    memset(&addr, 0, sizeof(addr));
    addr.nl_family = AF_NETLINK;
    // Routing Message Group of IPv4 Inteface address로 그룹을 지정
    addr.nl_groups = RTMGRP_IPV4_IFADDR;

	// 해당 netlink 그룹으로 sock 바인딩.
    if (bind(sock, (struct sockaddr *)&addr, sizeof(addr)) == -1) {
        perror("couldn't bind");
        return 1;
    }

    nlh = (struct nlmsghdr *)buffer;
    // nlh라는 변수에 담을 예정.
    while ((len = recv(sock, nlh, 4096, 0)) > 0) {
    	// OK 이고, NLMSG_DONE 타입이 아니라면 진행.
        while ((NLMSG_OK(nlh, len)) && (nlh->nlmsg_type != NLMSG_DONE)) {
            if (nlh->nlmsg_type == RTM_NEWADDR) {
                struct ifaddrmsg *ifa = (struct ifaddrmsg *) NLMSG_DATA(nlh);
                struct rtattr *rth = IFA_RTA(ifa);
                int rtl = IFA_PAYLOAD(nlh);

                while (rtl && RTA_OK(rth, rtl)) {
                    if (rth->rta_type == IFA_LOCAL) {
                        char name[IFNAMSIZ];
                        if_indextoname(ifa->ifa_index, name);
                        char ip[INET_ADDRSTRLEN];
                        inet_ntop(AF_INET, RTA_DATA(rth), ip, sizeof(ip));
                        printf("interface %s ip: %s\n", name, ip);
                    }
                    rth = RTA_NEXT(rth, rtl);
                }
            }
            // 그다음 넷링크 메시지로.
            nlh = NLMSG_NEXT(nlh, len);
        }
    }
    return 0;
}
