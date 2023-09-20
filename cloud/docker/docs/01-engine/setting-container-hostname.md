

~~~
Let's look at what a container hostname is. Earlier we learned that while running a container we can provide a container name using the name flag.

It is currently set to webapp. This is the name of the container itself and not the hostname of the container. When you run the hostname command inside the container,

we see that it is set to the short version of the unique ID of the container, which is by default the hostname

set on that container. Now, some applications rely on the container's hostname, which may be used in, say for example, the URL

of the web applications or while writing logs to files. It uses the hostname. We don't want it to be the container ID.

We want it to be a name that makes more sense. If you wish to set the hostname of the container, we could

use another attribute called --hostname, with the name you wish to set for your container hostname. We set it to webapp. Now, this creates a container.

When we go into the container, we run hostname, we see that the hostname is set to webapp.

~~~



~~~sh
docker container run -it --name=webapp ubuntu
docker container run -it --name=webapp --hostname=webapp ubuntu
~~~





## Copying Contents into Container



~~~sh
docker container cp /tmp/web.conf webapp:/etc/web.conf # SRC_PATH -> DEST_PATH
docker container cp webapp:/root/dockerhost /tmp/

docker container cp /tmp/web.conf webapp:/etc/
docker container cp /tmp/web.conf webapp:/etcccc/ 
docker container cp /tmp/app/ webapp:/opt/app
~~~





