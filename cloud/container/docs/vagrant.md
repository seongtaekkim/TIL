# vagrant





~~~
vagrant up --provider qume
vagrant global-status --prune
 vagrant destroy 218e85c
~~~









~~~
➜  make-container-without-docker git:(overlay-nw) ✗ lsof -PiTCP -sTCP:LISTEN
COMMAND     PID  USER   FD   TYPE             DEVICE SIZE/OFF NODE NAME
rapportd    545 staek    3u  IPv4 0x3fbb58cc69bdcc07      0t0  TCP *:50354 (LISTEN)
rapportd    545 staek    4u  IPv6 0x3fbb58c79c7d04af      0t0  TCP *:50354 (LISTEN)
ControlCe   599 staek   18u  IPv4 0x3fbb58cc69bdf647      0t0  TCP *:7000 (LISTEN)
ControlCe   599 staek   19u  IPv6 0x3fbb58c79c7d126f      0t0  TCP *:7000 (LISTEN)
ControlCe   599 staek   20u  IPv4 0x3fbb58cc69bdc177      0t0  TCP *:5000 (LISTEN)
ControlCe   599 staek   21u  IPv6 0x3fbb58c79c7d194f      0t0  TCP *:5000 (LISTEN)
postgres   1001 staek    7u  IPv6 0x3fbb58c79c7d202f      0t0  TCP localhost:5432 (LISTEN)
postgres   1001 staek    8u  IPv4 0x3fbb58cc69bbe127      0t0  TCP localhost:5432 (LISTEN)
Google     1046 staek   61u  IPv6 0x3fbb58c79c7d270f      0t0  TCP localhost:7679 (LISTEN)
qemu-syst 13097 staek   19u  IPv4 0x3fbb58cc6a752127      0t0  TCP *:50022 (LISTEN)
~~~

