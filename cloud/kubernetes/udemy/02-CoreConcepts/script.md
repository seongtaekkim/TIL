[TOC]





## 18. kube scheduler

~~~
-: Hello, and welcome to this lecture.

In this lecture, we will talk about kube-scheduler.

Earlier, we discussed that the Kubernetes scheduler

is responsible for scheduling pods on nodes.

Now don't let the graphic mislead you.

Remember, the scheduler is only responsible

for deciding which pod goes on which node.

It doesn't actually place the pod on the nodes.

That's the job of the kubelet.

The kubelet, or the captain on the ship,

is who creates the pod on the ships.

The scheduler only decides which pod goes where.

Let's look at how the scheduler does that

in a bit more detail.

First of all, why do you need a scheduler?

When there are many ships and many containers,

you wanna make sure that the right container

ends up on the right ship.

For example, there could be different

sizes of ships and containers.

You wanna make sure the ship has sufficient capacity

to accommodate those containers.

Different ships may be going to different destinations.

You wanna make sure your containers are placed

on the right ships so they end up in the right destination.

In Kubernetes, the scheduler decides which nodes

the pods are placed on depending on certain criteria.

You may have pods with different resource requirements.

You can have nodes in the cluster

dedicated to certain applications.

So how does the scheduler assign these pods?

The scheduler looks at each pod

and tries to find the best node for it.

For example, let's take one of these pods, the big blue one.

It has a set of CPU and memory requirements.

The scheduler goes through two phases

to identify the best node for the pod.

In the first phase, the scheduler tries to filter out

the nodes that do not fit the profile for this pod.

For example, the nodes that do not have sufficient CPU

and memory resources requested by the pod.

So the first two small nodes are filtered out.

So we are now left with the two nodes

on which the pod can be placed.

Now how does the scheduler pick one from the two?

The scheduler ranks the nodes

to identify the best fit for the pod.

It uses a priority function to assign a score

to the nodes on a scale of zero to 10.

For example, the scheduler calculates

the amount of resources that would be free

on the nodes after placing the pod on them.

In this case, the one on the right

would have six CPUs free if the pod was placed on it,

which is four more than the other one.

So it gets a better rank, and so it wins.

So that's how a scheduler works at a high level.

And, of course, this can be customized,

and you can write your own scheduler as well.

There are many more topics to look at,

such as resource requirements, limits,

taints and tolerations, node selectors, affinity rules,

et cetera, which is why we have an entire section

dedicated to scheduling coming up in this course

where we will discuss each of these in much more detail.

For now, we will continue to focus on the scheduler

as a process at a high level.

So how do you install the kube-scheduler?

Download the kube-scheduler binary

from the Kubernetes release page,

extract it, and run it as a service.

When you run it as a service,

you specify the scheduler configuration file.

So how do you view the kube-scheduler server options?

Again, if you set it up with the kubeadm tool,

the kubeadm tool deploys the kube-scheduler

as a pod in the kube system namespace on the master node.

You can see the options within the pod definition file

located at /etc/kubernetes/manifest/folder.

You can also see the running process

and the effective options by listing the process

on the master node and searching for kube-scheduler.

Well, that's it for this lecture.

I will see you in the next.


~~~







## 19. kubelet

~~~
In this lecture, we will look at kubelet.

Earlier, we discussed that the kubelet is like the captain on the ship.

They lead all activities on a ship.

They're the ones responsible for doing all the paperwork necessary to become part of the cluster.

They're the sole point of contact from the mastership.

They load or unload containers on the ship as instructed by the scheduler on the master.

They also send back reports at regular intervals on the status of the ship and the containers on them.

The kubelet in the Kubernetes worker node registers the node with a Kubernetes cluster.

When it receives instructions to load a container or a pod on the node,
it requests the container runtime engine, which may be Docker, to pull the required image
and run an instance.

The kubelet then continues to monitor the state of the pod and containers in it and reports to the kube API server on a timely basis.

So how do you install the kubelet?

If you use the kubeadm tool to deploy your cluster, it does not automatically deploy the kubelet.

Now that's the difference from other components.

You must always manually install the kubelet on your worker nodes.

Download the installer, extract it, and run it as a service.

You can view the running kubelet process and the effective options by listing the process
on the worker node and searching for kubelet.

We will look more into kubelets, how to configure kubelets, generate certificates, and, finally,
how to TLS Bootstrap kubelets later in this course.
~~~





## 20. kube proxy

~~~
Within a Kubernetes cluster, every pod can reach every other pod.

This is accomplished by deploying a pod networking solution to the cluster.

A pod network is an internal virtual network that spans across all the nodes in the cluster
to which all the pods connect to.

Through this network, they're able to communicate with each other.

There are many solutions available for deploying such a network.

In this case, I have a web application deployed on the first node
and a database application deployed on the second.

The web app can reach the database simply by using the IP of the pod, but there is no guarantee that the IP of the database pod will always remain the same.

If you've gone through the lecture on services, as discussed in the beginner's course,
you must know that a better way for the web application to access the database is using a service.

So we create a service to expose the database application across the cluster.

The web application can now access the database using the name of the service, DB.

The service also gets an IP address assigned to it.

Whenever a pod tries to reach the service using its IP or name, it forwards the traffic to the backend pod, in this case, the database.

But what is this service, and how does it get an IP?

Does the service join the same pod network?

The service cannot join the pod network because the service is not an actual thing.

It is not a container like pods, so it doesn't have any interfaces or an actively listening process.

It is a virtual component that only lives in the Kubernetes memory.

But then, we also said that the service should be accessible across the cluster from any nodes.

So how is that achieved?

That's where kube-proxy comes in.

Kube-proxy is a process that runs on each node in the Kubernetes cluster.

Its job is to look for new services, and every time a new service is created, it creates the appropriate rules on each node to forward traffic to those services to the backend pods.

One way it does this is using iptables rules.

In this case, it creates an iptables rule on each node in the cluster to forward traffic
heading to the IP of the service, which is 10.96.0.12, to the IP of the actual pod, which is 10.32.0.15.

So that's how kube-proxy configures a service.

We discuss lot more about networking, and services, and kube-proxy, and pod networking later in this course.

Again, we have a large section just for networking.

This is a high-level overview for now.

We will now see how to install kube-proxy.

Download the kube-proxy binary from the Kubernetes release page, extract it, and run it as a service.

The kubeadm tool deploys kube-proxy as pods on each node.

In fact, it is deployed as a DaemonSet, so a single pod is always deployed on each node in the cluster.

Well, if you don't know about DaemonSet yet, don't worry.

We have a lecture on that coming up in this course.

We have now covered a high-level overview of the various components in the Kubernetes control plane.

As mentioned, we will look at some of these in much more detail at various sections in this course.

Well, that's it for this lecture.

I will see you in the next section.
~~~



## 21. pod

~~~
Before we head into understanding pods, we would like to assume that the following have been set up already.

At this point, we assume that the application is already developed
and built into Docker images, and it is available on a Docker repository like Docker Hub so Kubernetes can pull it down.

We also assume that the Kubernetes cluster has already been set up and is working.

This could be a single-node setup or a multi-node setup.

Doesn't matter.

All the services need to be in a running state.

As we discussed before, with Kubernetes, our ultimate aim is to deploy our application in the form of containers on a set of machines that are configured as worker nodes in a cluster.

However, Kubernetes does not deploy containers directly on the worker nodes.

The containers are encapsulated into a Kubernetes object known as pods.

A pod is a single instance of an application.

A pod is the smallest object that you can create in Kubernetes.

Here we see the simplest of simplest cases where you have a single-node Kubernetes cluster
with a single instance of your application running in a single Docker container encapsulated in a pod.

What if the number of users accessing your application increase and you need to scale your application?

You need to add additional instances of your web application to share the load.

Now, where would you spin up additional instances?

Do we bring up new container instance within the same pod?

No, we create new pod altogether with a new instance of the same application.

As you can see, we now have two instances of our web application running on two separate pods
on the same Kubernetes system or node.

What if the user base further increases and your current node has no sufficient capacity?

Well, then you can always deploy additional pods on a new node in the cluster.

You will have a new node added to the cluster to expand the cluster's physical capacity.

So what I'm trying to illustrate in this slide is that pods usually have a one-to-one relationship
with containers running your application.

To scale up, you create new pods, and to scale down, you delete existing pod.

You do not add additional containers to an existing pod to scale your application.

Also, if you're wondering how we implement all of this and how we achieve load balancing
between the containers et cetera, we will get into all of that in a later lecture.

For now, we are only trying to understand the basic concepts.

We just said that pods usually have a one-to-one relationship with the containers, but are we restricted to having a single container in a single pod?

No, a single pod can have multiple containers except for the fact that they're usually not multiple containers of the same kind.

As we discussed in the previous slide, if our intention was to scale our application, then we would need to create additional pods, but sometimes you might have a scenario where you have a helper container
that might be doing some kind of supporting task for our web application such as processing a user and their data, processing a file uploaded by the user, et cetera and you want these helper containers
to live alongside your application container.

In that case, you can have both of these containers part of the same pod so that when a new application container is created, the helper is also created, and when it dies, the helper also dies
since they're part of the same pod.

The two containers can also communicate with each other directly by referring to each other as local host
since they share the same network space, plus they can easily share the same storage space as well.

If you still have doubts in this topic, I would understand if you did because I did the first time I learned these concepts.

We could take another shot at understanding pods from a different angle.

Let's, for a moment, keep Kubernetes out of our discussion and talk about simple Docker containers.

Let's assume we were developing a process or a script to deploy our application on a Docker host.

Then we would first simply deploy our application using a simple Docker run Python app command
and the application runs fine and our users are able to access it.

When the load increases, we deploy more instances of our application by running the Docker run commands many more times.

This works fine, and we are all happy.

Now, sometime in the future, our application is further developed, undergoes architectural changes
and grows and gets complex.

We now have a new helper container that helps our web application by processing or fetching data from elsewhere.

These helper containers maintain a one-to-one relationship with our application container and thus needs to communicate the application containers directly and access data from those containers.

For this, we need to maintain a map of what app and helper containers are connected to each other.

We would need to establish network connectivity between these containers ourselves using links and custom networks.

We would need to create shareable volumes and share it among the containers.

We would need to maintain a map of that as well, and most importantly, we would need to monitor the state
of the application container and when it dies, manually kill the helper container as well as it's no longer required.

When a new container is deployed, we would need to deploy the new helper container as well.

With pods, Kubernetes does all of this for us automatically.

We just need to define what containers a pod consists of and the containers in a pod by default
will have access to the same storage, the same network namespace and same fit as in they will be created together and destroyed together.

Even if our application didn't happen to be so complex and we could live with a single container,
Kubernetes still requires you to create pods, but this is good in the long run as your application is now equipped for architectural changes and scale in the future.

However, also note that multi containers pod are a rare use case, and we are going to stick to single containers per pod in this course.

Let's us now look at how to deploy pods.

Earlier we learned about the kubectl run command.

What this command really does is it deploys a Docker container by creating a pod, so it first creates a pod automatically and deploys an instance of the NGINX Docker image, but where does it get the application image from?

For that, you need to specify the image name using the dash dash image parameter.

The application image, in this case, the NGINX image, is downloaded from the Docker Hub repository.

Docker Hub, as we discussed, is a public repository where latest Docker images of various applications are stored.

You could configure Kubernetes to pull the image from the public Docker Hub or a private repository
within the organization.

Now that we have a pod created, how do we see the list of pods available?

The kubectl get pods command helps us see the list of pods in our cluster.

In this case, we see the pod is in a container creating state and soon changes to a running state
when it is actually running.

Also, remember that we haven't really talked about the concepts on how a user can access the NGINX web server, and so in the current state, we haven't made the web server accessible to external users.

You can, however, access it internally from the node.

For now, we will just see how to deploy a pod, and in a later lecture, once we learn about networking and services, we will get to know how to make this service accessible to end users.

That's it for this lecture.
~~~





## 22. pods with YAML

~~~
In this lecture, we will talk about creating a pod using a YAML based configuration file.

In the previous lecture, we learnt about YAML files in general.

Now we will learn how to develop YAML files specifically for Kubernetes.

Kubernetes uses YAML files as inputs for the creation of objects such as pods, replicas, deployments, services, et cetera.

All of these follow similar structure.

A Kubernetes definition file always contains four top level fields; the API version, kind, metadata, and spec.

These are the top level or root level properties.

These are also required fields so you must have them in your configuration file.

Let us look at each one of them.

The first one is the API version.

This is the version of the Kubernetes API we are using to create the object.

Depending on what we are trying to create, we must use the right API version.

For now, since we are working on pods, we will set the API version as V1.

Few other possible values for this field are apps/V1beta, extensions/V1beta, et cetera.

We will see what these are for later in this course.

Next is the kind.

The kind refers to the type of object we are trying to create, which in this case happens to be a pod, so we will set it as pod.

Some other possible values here could be replica said or deployment or service, which is what you see
in the kind field in the table on the right.

The next is metadata.

The metadata is data above the object like its name, labels, et cetera.

As you can see, unlike the first two where you have specified a string value, this is in the form of a dictionary, so everything under metadata is intended to the right a little bit and so names and labels are children of metadata.

The number of spaces before the two properties, name and labels, doesn't matter but they should be the same as they are siblings.

In this case, labels has more spaces on the left than name and so it is now a child of the name property instead of a sibling, which is incorrect.

Also, the two properties must have more spaces than its parent, which is metadata, so that it's intended to the right a little bit.

In this case, all three of them have the same number of spaces before them, and so they are all siblings which is not correct.

Under metadata, the name is a string value, so you can name your pod, My App Pod, and the labels is a dictionary.

So labels is a dictionary within the metadata dictionary and it can have any key and value pairs as you wish.

For now, I have added a label app with the value my app.

Similarly, you could add other labels as you see fit which will help you identify these objects
at a later point in time.

Say for example, there are hundreds of pods running a front end application and hundreds of pods running a backend application or a database.

It will be difficult for you to group these pods once they're deployed.

If you label them now as front end, backend or database, you will be able to filter the pods based
on this label at a later point in time.

It's important to note that under metadata you can only specify name or labels or anything else
that Kubernetes expects to be under metadata.

You cannot add any other property as you wish under this.

However, under labels, you can have any kind of key or value pairs as you see fit.

So it's important to understand what each of these parameters expect.

So far, we have only mentioned the type and name of the object we need to create which happens to be a pod with a name, My App Pod, but we haven't really specified the container or image we need in the pod.

The last section in the configuration file is the specification section which is written as spec.

Depending on the object we are going to create, this is where we would provide additional information
to Kubernetes pertaining to that object.

This is going to be different for different objects so it's important to understand or refer to
the documentation section to get the right format for each.

Since we are only creating a pod with a single container in it, it is easy.

Spec is a dictionary so add a property under it called containers.

Containers is a list or an array.

The reason this property is a list is because the pods can have multiple containers within them
as we learned in the lecture earlier.

In this case though we will only add a single item in the list, since we plan to have only a single container in the pod.

The dash right before the name indicates that this is the first item in the list.

The item in the list is a dictionary so add a name and image property.

The value for image is N G I N X which is the of the docker image in the docker repository.

Once the file is created, run the command cube CTL create dash F followed by the file name, which is pod definition dot YAML and Kubernetes creates the pod.

So to summarize, remember the four top level properties;

API version, kind, metadata, and spec.

Then start by adding values to those depending on the object you are going to create.

Once we create the pod, how do you see it?

Use the cube CTL get pods command to see a list of pods available.

In this case, it's just one.

To see detailed information about the pod, run the cube CTL describe pod command.

This will tell you information about the pod when it was created, what labels are assigned to it,
what docker containers are part of it and the events associated with that pod.

~~~





## 31. Deployment

~~~
we will discuss about Kubernetes deployment.

For a minute, let us forget about pods,
and ReplicaSets, and other Kubernetes concepts,
and talk about how you might want to deploy your application in a production environment.

Say for example,
you have a web server that needs to be deployed in a production environment.

You need not one, but many such instances of the web server running for obvious reasons.

Secondly, whenever newer versions of application bills become available on the Docker Registry,
you would like to upgrade your Docker instances seamlessly.

However, when you upgrade your instances, you do not want to upgrade all of them at once as we just did.

This may impact users accessing our applications so you might want to upgrade them one after the other.
And that kind of upgrade is known as rolling updates.

Suppose one of the upgrades you performed resulted in an unexpected error

and you're asked to undo the recent change.

You would like to be able to roll back the changes that were recently carried out.

Finally, say for example,
you would like to make multiple changes to your environment,
such as, upgrading the underlying web server versions, as well as scaling your environment
and also modifying the resource allocations, et cetera.

You do not want to apply each change immediately after the command is run.

Instead, you would like to apply a pause to your environment, make the changes,
and then resume so that all the changes are rolled out together.

All of these capabilities are available with the Kubernetes deployments.

So far in this course, we discussed about pods, which deploy single instances of our application,
such as the web application in this case.

Each container is encapsulated in pods.

Multiple such pods are deployed, using replication controllers or ReplicaSets.

And then comes deployment which is a Kubernetes object that comes higher in the hierarchy.

The deployment provides us with the capability to upgrade the underlying instances seamlessly
using rolling updates, undo changes, and pause, and resume changes as required.

So, how do we create a deployment?

As with the previous components, we first create a deployment definition file.

The contents of the deployment definition file are exactly similar to the ReplicaSet definition file,
except for the kind which is now going to be deployment.

If we walk through the contents of the file, it has an API version, which is apps/v1,
metadata, which has name and labels, and a spec that has template, replicas, and selector.

The template has a pod definition inside it.

Once the file is ready, run the cube control create command, and specify the deployment definition file.

Then, run the cube control get deployment command to see the newly created deployment.

The deployment automatically creates a ReplicaSet
so if you run the cube control, get ReplicaSet command, you will be able to see a new ReplicaSet
in the name of the deployment.

The ReplicaSets ultimately create parts, so if you run the cube control get parts command
you will be able to see the pods with the name of the deployment and the ReplicaSet.

So far, there hasn't been much of a difference between ReplicaSet and deployments,
except for the fact that deployment created a new Kubernetes object called deployments.

We will see how to take advantage of the deployment using the use cases we discussed
in the previous slide in the upcoming lectures.

And one more note before we end this lecture, to see all the created objects at once,
run the cube control get all command.

And in this case we can see that the deployment was created, and then we have the ReplicaSet followed
by three parts that were created as part of the deployment.

That's it for this lecture.
~~~





