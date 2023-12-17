#include <errno.h>
#include <sched.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>

main(int argc, char* argv[]) {
    int i;
    char nspath[1024];
    char *namespaces[] = { "ipc", "uts", "net", "pid", "mnt" };

    if (geteuid()) { fprintf(stderr, "%s\n", "abort: you want to run this as root"); exit(1); }

    if (argc != 2) { fprintf(stderr, "%s\n", "abort: you must provide a PID as the sole argument"); exit(2); }

    for (i=0; i<5; i++) {
        sprintf(nspath, "/proc/%s/ns/%s", argv[1], namespaces[i]);
        int fd = open(nspath, O_RDONLY);

        if (setns(fd, 0) == -1) { 
            fprintf(stderr, "setns on %s namespace failed: %s\n", namespaces[i], strerror(errno));
        } else {
            fprintf(stdout, "setns on %s namespace succeeded\n", namespaces[i]);
        }

        close(fd);
    }
}
