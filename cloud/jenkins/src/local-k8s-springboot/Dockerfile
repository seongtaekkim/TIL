FROM openjdk:17
WORKDIR /usr/src/myapp
COPY target/local-k8s-springboot-0.0.1-SNAPSHOT.jar /usr/src/myapp
ENTRYPOINT ["java", "-jar", "/usr/src/myapp/local-k8s-springboot-0.0.1-SNAPSHOT.jar"]
