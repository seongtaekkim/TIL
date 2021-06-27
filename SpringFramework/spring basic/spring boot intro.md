



# Spring Boot



- Spring Boot 는 단순히 실행되며, 프로덕션 제품 수준의 스프링 기반 어플리케이션을 쉽게 만들 수 있다..



- Spring Boot 어플리케이션에는 Spring 구성이 거의 필요하지 않다.



- Spring Boot java -jar로 실행하는 java 어플리케이션을 만들 수 있다.



### 주요목표



- Spring 개발에 대해 빠르고, 광범위하게 적용할 수 있는 환경



- 기본값 설정이 있지만 설정을 바꿀 수 있다.



- 대규모 프로젝트에 공통적인 비 기능 제공 (보안, 모니터링 등등)



- XML 구성 요구사항이 전혀 없음 (어노테이션으로 정부 바뀌었다.)



| Name   | version                      |
| ------ | ---------------------------- |
| Maven  | 3.3+                         |
| Gradle | 4.x(4.4. and later)  and 5.x |



| name         | servlet version |
| ------------ | --------------- |
| Tomcat 9.x   | 3.3             |
| Jetty9.4     | 3.1             |
| Undertow 2.0 | 4               |
| Netty        | -               |

 

https://start.spring.io

spring initializr 에서 프로젝트 config 설정할 수 있음 ( intellj 에서 설정이 가능하기 때문에 필요없음)





- 어플리케이션 개발에 필수 요소들만 모아두었다.

- 간단한 설정으로 개발 및 커스텀이 가능하다.
- 간단하고, 빠르게 어플리케이션 실행 및 배포가 가능하다.
- 대규모 프로젝트(운영환경)에 필요한 비 기능적 기능도 제공한다.
- 오랜 경험에서 나오는 안정적인 운영이 가능하다.
- Spring에서 불편한 설정이 없어졌다.  (xml 등등)







# REST Client 설치하기



크롬 웹스토어에서 Talend API Tester - Free Edition를 설치.









resource > application.propterties





- resource > application.properties 에서 톰캣 port 설정 가능

```properties
server.port=9090
```













