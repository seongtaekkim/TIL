#include <errno.h>
#include <sched.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <stdio.h>
#include <sys/ioctl.h>
#include <net/if.h>
#include <string.h>
#include <arpa/inet.h>

main(int argc, char* argv[]) {
    int i;
    char nspath[1024];
    char *namespaces[] = { "ipc", "uts", "net", "pid", "mnt" };

    if (geteuid()) { fprintf(stderr, "%s\n", "abort: you want to run this as root"); exit(1); }

    if (argc != 2) { fprintf(stderr, "%s\n", "abort: you must provide a PID as the sole argument"); exit(2); }

    for (i=0; i<5; i++) {
        sprintf(nspath, "/var/run/netns/overns");
        int fd = open(nspath, O_RDONLY);

        if (setns(fd, 0) == -1) { 
            fprintf(stderr, "setns? on %s namespace failed: %s\n", namespaces[i], strerror(errno));
        } else {
            fprintf(stdout, "setns on %s namespace succeeded\n", namespaces[i]);

            struct ifreq ifr;
            char ipstr[40];
            int s;

            s = socket(AF_INET, SOCK_DGRAM, 0);
            strncpy(ifr.ifr_name, "br0", IFNAMSIZ);

            if (ioctl(s, SIOCGIFADDR, &ifr) < 0) {
                printf("Error");
            } else {
                inet_ntop(AF_INET, ifr.ifr_addr.sa_data+2,
                        ipstr,sizeof(struct sockaddr));
                printf("myOwn IP Address is %s\n", ipstr);
            }
        }

        close(fd);
    }
}
