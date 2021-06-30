



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







### @GetMapping  인자 사용법(1)

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

        // http://localhost:9090/api/get/query-param?			user=steve&email=steve@gmail.com&age=30
    // @RequestParam Map<String,String> queryParam 를 이용해서, 쿼리파라메터의 key,value를 추출 할 수 있다.
    @GetMapping(path = "/query-param")
    public String queryParam(@RequestParam Map<String,String> queryParam) {
        StringBuilder sb = new StringBuilder();
        queryParam.entrySet().forEach(entry -> {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            System.out.println("\n");

            sb.append(entry.getKey() + " " + entry.getValue() + "\n");
        });

        return sb.toString();
    }
    
    //@RequestParam String name 를 이용해서, 쿼리파라메터의 key를 추출할 수 있다.
    @GetMapping("query-param2")
    public String queryParam2(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam int age
    ) {
        System.out.println(name);
        System.out.println(email);
        System.out.println(age);

        return name + " " + email + " " + age;

    }
}
```



### @GetMapping  인자 사용법(2)

```java
    public class UserRequest {

    private String name;
    private String email;
    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
    
    // 인자로 어노테이션 없이 객체만 넣어도, 스프링에서 쿼리파라메터를 판단해서 매핑해줌.
    // 가장 자주 사용되는 방식.
    // 쿼리파라메터 key 중에 객체에 매핑되는 키가 없다면, 무시됨.
    @GetMapping("query-param3")
    public String queryParam3(
            UserRequest userRequest
    ) {
        System.out.println(userRequest.getName());
        System.out.println(userRequest.getEmail());
        System.out.println(userRequest.getAge());

        return userRequest.toString();

    }
```











# POST API



- 데이터를 주고받을 때는 주로 JSON 방식을 사용한다.

- JSON에서 사용하는 데이터 타입은 아래와 같다.

String : value

number : value

boolean : value {}

object : value

array : value [ 

{

​	"key" : "value"

}





- json 에서는 카멜케이스 보다 스네이크 케이스를 더 많이 사용한다.

  스네이크 케이스 : phone_number

  카멜케이스 : phoneNumber







### JSON 예제

```json
{
	"phone-number" : "010-1111-1111",	 // string
	"age":10,							 // int
	"isAgree" : false,					 // boolean
	"account" :{						 // object
		"email" : "steve@gmail.com",
		"password" : "1234"
	}
}

// user 조회하는 경우

{
	"user-list" : [						// array
		{
			"account" : "abcd",
			"password" : "1234"
		},
		{
			"account" : "aaaa",
			"password" : "1111"
		}
	]

}

```





### POST 사용법(1)

```java
    @PostMapping("/post")
    public void post(@RequestBody  Map<String,Object> requestData) {
        requestData.entrySet().forEach(stringObjectEntry -> {
            System.out.println("key : " +stringObjectEntry.getKey());
            System.out.println("value : " +stringObjectEntry.getValue());
        });
    }
```

- @RequestBody : Json 타입의 body를 파싱하여 java object로 변환한다.
- Map<String,Object> 타입을 key,value로 각각 분리하여 조회할 수 있다.





### POST 사용법(2)

```JAVA
    @PostMapping("/post")
    public void post(@RequestBody  postRequestDto requestData) {
       System.out.println(requestData.toString());
    }
```

- DTO를 사용하면, 일일이 바르지않아도  JSON을 파싱하여 매핑할수 있다.







### DTO 

```java
public class postRequestDto {
    private String account;
    private String email;
    private String address;
    private String password;

    // 파싱이 안될경우
    // json key가 다른 방법, 카멜도아니고 스네이크케이스도 아닌 경우 사용할 수 있다.
    @JsonProperty("phone_number")
    private String phoneNumber; // phone_number

    @JsonProperty("OTP")
    private String OTP;
```

- @JsonProperty  : json 파싱 타입을 설정할 수 있다.



































