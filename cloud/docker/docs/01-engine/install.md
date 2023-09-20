## Install by ubuntu



~~~
$ sudo cat /etc/lsb-release 

DISTRIB_ID=Ubuntu
DISTRIB_RELEASE=20.04
DISTRIB_CODENAME=focal
DISTRIB_DESCRIPTION="Ubuntu 20.04.6 LTS"
~~~



~~~
sudo apt-get remove docker docker-engine docker.io containerd runc
sudo apt-get update
sudo apt-get install apt-transport-https ca-certificates curl gnupg-agent software-properties-common 
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo apt-key fingerprint 0EBFCD88
sudo apt-get-repository "dev [arch=amd64] https://download.docker.com/linux/ubuntu \
$(lsb_release -cs) stable"
sudo add-apt-repository "dev [arch=amd64] https://download.docker.com/linux/ubuntu \
$(lsb_release -cs) stable"
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
$(lsb_release -cs) stable"
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io
sudo dpkg -l | grep -i docker
~~~



~~~
$ sudo systemctl status docker


● docker.service - Docker Application Container Engine
     Loaded: loaded (/lib/systemd/system/docker.service; enabled; vendor preset: enabled)
     Active: active (running) since Wed 2023-08-02 10:53:17 UTC; 1min 4s ago
TriggeredBy: ● docker.socket
       Docs: https://docs.docker.com
   Main PID: 3752 (dockerd)
      Tasks: 9
     Memory: 26.0M
     CGroup: /system.slice/docker.service
             └─3752 /usr/bin/dockerd -H fd:// --containerd=/run/containerd/containerd.sock

Aug 02 10:53:16 docker-1 systemd[1]: Starting Docker Application Container Engine...
Aug 02 10:53:16 docker-1 dockerd[3752]: time="2023-08-02T10:53:16.850129741Z" level=info msg="Starting up"
Aug 02 10:53:16 docker-1 dockerd[3752]: time="2023-08-02T10:53:16.851057191Z" level=info msg="detected 127.0.0>
Aug 02 10:53:16 docker-1 dockerd[3752]: time="2023-08-02T10:53:16.923421337Z" level=info msg="Loading containe>
Aug 02 10:53:17 docker-1 dockerd[3752]: time="2023-08-02T10:53:17.103532779Z" level=info msg="Loading containe>
Aug 02 10:53:17 docker-1 dockerd[3752]: time="2023-08-02T10:53:17.124929468Z" level=info msg="Docker daemon" c>
Aug 02 10:53:17 docker-1 dockerd[3752]: time="2023-08-02T10:53:17.125066568Z" level=info msg="Daemon has compl>
Aug 02 10:53:17 docker-1 dockerd[3752]: time="2023-08-02T10:53:17.154812496Z" level=info msg="API listen on /r>
Aug 02 10:53:17 docker-1 systemd[1]: Started Docker Application Container Engine.

~~~



~~~
$ sudo docker --version
$ sudo docker version
$ sudo docker system info
~~~

















