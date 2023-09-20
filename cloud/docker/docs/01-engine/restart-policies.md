~~~
	Let's look at restart policies in Docker. Now, a Docker container may stop due to different reasons. One is when the job it is supposed to do ends successfully.

For example, if the job of the container was to perform a mathematical calculation, the container stops when the calculation is completed.

In this case, the exit code is zero. A container may stop due to a failure. For instance, here is an incorrect parameter that's passed,

so it ends with an exit code, one. This is a failure. A running container can also be stopped manually

by running the Docker container stop command, which as we learned earlier. Now, first sense SIGTERM signal followed by the SIGKILL signal


and results in a forced termination of the application. Now at which point depending on the image and how the software within the image is configured to run the container

and exits with a specific code. For example, if the process running inside the container is programmed to handle the termination signal sent by Docker stop command,

then it gracefully shuts down, but then the exit code is zero. If it did not gracefully shut down and Docker had to send the kill signal,

then it may result in another exit code, which may not be zero. We can configure a container to restart automatically

in any of these cases. Say, for example, you have a web app that randomly crashes. Now when it crashes,

you must, of course, review the logs and troubleshoot and identify the issue, but what if you just wanted to restart and not stay crashed,

as you have users waiting to be served. You can always go back and look at the logs and figure out why it failed,

but you want it to immediately restart if it were to crash as you don't want the users to be impacted. To configure a container to automatically restart,

you may use the restart option with one of these flags. The default setting is no, which means containers are never automatically restarted.

The next one is on-failure. This restarts the container only when it fails, this can be determined by the exit code zero.


If the exit code is zero, it's not a failure. If it's anything other than zero, it is a failure. Then, in that case, it would restart automatically.

The next option is always meaning the container will be restarted always whether it exited successfully or on failure, or if it was manually stopped.


Now when it is manually stopped, there is a small catch, it does not immediately restart the container once it is manually stopped.

It is restarted only when the Docker daemon restarts, so keep that in mind. Because if an admin ran the Docker container stop command

to stop the container, then he probably did that for a good reason. Then you don't want it to automatically restart immediately.

Now the last option is the unless-stopped option which works just like always, where it restarts if the container exited successfully or on failure,

but it does not restart if it was manually stopped by running the Docker container stop command and not even when the Docker daemon restarts.

Now, remember that all of these policies are only applicable if container starts successfully on the first place, meaning that the container starts and it is up for at least 10 seconds.

If the container does not start at all on the first place, then we don't want it to go into a restart loop.

Now let's look at another scenario. Say we were to run a web application on our Docker host. What happens to our web server when the Docker daemon stops?

We know that the Docker host already runs a service, which is our Docker daemon, and it is that service that manages our containers and images.


When we run the Docker container run command, it's the Docker daemon that pulls the image and starts the container.

Now, what happens when the daemon itself goes down? By default, when the Docker daemon goes down, it takes down all the containers along with it.

All the containers crash as well when the Docker daemon itself is stopped. Now you could configure the Docker engine

to not shut down the containers when it terminates and leave the applications running when the daemon crashes or during planned outages.

It's called a live restore. The live restore option can be configured in the Docker daemon configuration file at /etc/docker/daemon.jason Into this file,

we add a new property called live restore, and we set its value to true. Now restart the Docker service or reload the service if it's already started,

and then we run the webserver again. Now, if the Docker daemon stops, the container will continue to run.


~~~



### docker restart option(1)

~~~sh
docker container run ubuntu expr 3 + 5 # success
docker container run ubuntu expr a + b # fail
docker container stop httpd # stop

docker container run --restart=no ubuntu      # 어떤경우든 종료된 컨테이너를 재실행하지 않는다.
docker container run --restart=on-failure     # 컨테이너 에러로 인해 다운된 경우만 재실행
docker container run --restart=always         # 어떤 경우든 재 실행
docker container run --restart=unless-stopped # stop signal 이외 모든경우 재실행 
~~~



### docker restart option(2)

~~~json
// /etc/docker/daemon.json
{
 "debug": true,
  "hosts": ["tcp://192...:2376"],
  "live-restore": true
}

~~~

~~~
docker container run --name=web httpd
systemctl stop docker
systemctl start docker
docker container run --name web httpd
systemctl stop docker 
~~~

- live-restore 설정이 되어있다면, 도커를 재실행했을 때 기존 컨테이너가 자동으로 실행된다.



























































