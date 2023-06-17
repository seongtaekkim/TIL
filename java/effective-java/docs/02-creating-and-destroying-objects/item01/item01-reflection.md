## Reflection

- 클래스로더를 통해 읽어온 클래스 정보를 사용하는 기술.
- 리플렉션을 사용해 클래스를 읽어오거나, 인스턴스를 만들거나, 메소드를 실행하거나, 필드의 값을 가져오거나 변경하는 것이 가능하다.

- 사용
  - 특정 애노테이션이 붙어있는 필드 또는 메소드 읽어오기 (JUnit, Spring)
  - 특정 이름 패턴에 해당하는 메소드 목록 가져와 호출하기 (getter, setter)




- 클래스정보를 가져와서 인스턴스를 생성한다.

```java
 Class<?> aClass = Class.forName("me.whiteship.hello.ChineseHelloService");
 Constructor<?> constructor = aClass.getConstructor();
 HelloService helloService = (HelloService) constructor.newInstance();
 System.out.println(helloService.hello());
```

- 특정 클래스의 method 정보를 읽어온다.

~~~java
Class<?> hello = HelloService.class;
Arrays.stream(hello.getMethods()).forEach(System.out::println);
~~~

~~~sh
public static me.whiteship.chapter01.item01.HelloService me.whiteship.chapter01.item01.HelloService.of(java.lang.String)
public static java.lang.String me.whiteship.chapter01.item01.HelloService.hi()
public static java.lang.String me.whiteship.chapter01.item01.HelloService.hi1()
public default java.lang.String me.whiteship.chapter01.item01.HelloService.bye()
public static java.lang.String me.whiteship.chapter01.item01.HelloService.hi2()
public abstract java.lang.String me.whiteship.chapter01.item01.HelloService.hello()
~~~

















