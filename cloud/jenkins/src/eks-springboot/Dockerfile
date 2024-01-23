FROM openjdk:17
WORKDIR /usr/src/myapp
COPY target/eks-springboot-0.0.1-SNAPSHOT.jar /usr/src/myapp
ENTRYPOINT ["java", "-jar", "/usr/src/myapp/eks-springboot-0.0.1-SNAPSHOT.jar"]
