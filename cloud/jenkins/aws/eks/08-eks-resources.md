# eks resources



- 이미지는 CI에서 push 했던 경로를 작성한다.

~~~yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: springboot-helloworld
  labels:
    app: springboot-helloworld
spec:
  replicas: 1
  selector:
    matchLabels:
      app: springboot-helloworld
  template:
    metadata:
      labels:
        app: springboot-helloworld
    spec:
      containers:
        - name: springboot-helloworld
          image: 738612635754.dkr.ecr.ap-northeast-2.amazonaws.com/stecr:latest
          ports:
            - containerPort: 8080

~~~

- 위 이미지는 springboot jar파일을 image로 만들어 push 한 정보이다.

~~~dockerfile
FROM openjdk:17
WORKDIR /usr/src/myapp
COPY target/eks-springboot-0.0.1-SNAPSHOT.jar /usr/src/myapp
ENTRYPOINT ["java", "-jar", "/usr/src/myapp/eks-springboot-0.0.1-SNAPSHOT.jar"]
~~~

- service 설정

~~~yaml
apiVersion: v1
kind: Service
metadata:
  name: springboot-helloworld
spec:
  ports:
    - name: "8080"
      port: 80
      targetPort: 8080
  selector:
    app: springboot-helloworld
  type: LoadBalancer

~~~

