



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

- REST TEST를 할 수 있는 크롬 확장프로그램





- resource > application.properties 에서 톰캣 port 설정 가능

```properties
server.port=9090
```







# GET API





### @GetMapping은 다음과같은 인자를 사용할 수 있다.

```java
public @interface GetMapping {

	/**
	 * Alias for {@link RequestMapping#name}.
	 */
	@AliasFor(annotation = RequestMapping.class)
	String name() default "";

	/**
	 * Alias for {@link RequestMapping#value}.
	 */
	@AliasFor(annotation = RequestMapping.class)
	String[] value() default {};

	/**
	 * Alias for {@link RequestMapping#path}.
	 */
	@AliasFor(annotation = RequestMapping.class)
	String[] path() default {};

	/**
	 * Alias for {@link RequestMapping#params}.
	 */
	@AliasFor(annotation = RequestMapping.class)
	String[] params() default {};

	/**
	 * Alias for {@link RequestMapping#headers}.
	 */
	@AliasFor(annotation = RequestMapping.class)
	String[] headers() default {};

	/**
	 * Alias for {@link RequestMapping#consumes}.
	 * @since 4.3.5
	 */
	@AliasFor(annotation = RequestMapping.class)
	String[] consumes() default {};

	/**
	 * Alias for {@link RequestMapping#produces}.
	 */
	@AliasFor(annotation = RequestMapping.class)
	String[] produces() default {};

}
```







### @GetMapping  인자 사용법을 알아보자.

```java
@RestController
@RequestMapping("/api/get")
public class GetApiController {

    @GetMapping(path = "/hello") 
    // http://localhost:9090/api/get/hello // 현재 사용하는 방식
    public String hello() {
        return "get Hello";
    }
    @RequestMapping(path = "/hi", method= RequestMethod.GET) 
    // RequestMapping은get / post / put / delete 모두 동작하므로 지정해서 사용해야함
    public String hi(){
        return "hi";
    }

    // http://localhost:9090/api/get/path-variable/{name}
    // getmapping 인자의 {name}과 함수인자의 @PathVariable String name 변수명이 같거나
    // {name}과 함수인자의 @PathVariable(name="name") String pathName 이 같으면 됨
    // (@Pathvariable의 name key)
    @GetMapping("/path-variable/{name}")
    public String pathVariable(@PathVariable(name="name") String pathName) {
        System.out.println("PathVariable : " + pathName);
        return pathName;
    }

    // http://localhost:9090/api/get/query-param?user=steve&email=steve@gmail.com&age=30
}
```









