# item39 명명 패턴보다 애너테이션을 사용하라



## 명명패턴 단점

- 메서드 등의 이름 패턴을 기준으로 어떤 기능을 실행함

1. 오타가 나면 안된다.

2. 올바른 프로그램 요소에서만 사용되리라 보증할 방법이 없다는 것이다.

3. 프로그램 요소를 매개변수로 전달할 마땅한 방법이 없다.





## marker annotation

- 아무 매개변수 없이 단순히 대상에 마킹 한다 는 뜻에서 marker annotation 이라 한다.

### annotation

- 메타애노테이션을 생략하면 테스트 도구는 @Test 를 인식할 수 없다. : runtime에 메모리를 조회함. (reflect)

~~~java
@Retention(RetentionPolicy.RUNTIME) // runtime에 조회해야 함
@Target(ElementType.METHOD) // method element 에만 작성가능.
public @interface Test {
}
~~~



### 테스트코드

```java
@Test
public static void m1() { }
```

### 테스트코드 실행 코드

~~~java
if (m.isAnnotationPresent(Test.class)) {
    tests++;
    try {
        m.invoke(null);
        passed++;
    } catch (InvocationTargetException wrappedExc) {
        Throwable exc = wrappedExc.getCause();
        System.out.println(m + " failed: " + exc + " " + wrappedExc.getMessage());
    } catch (Exception exc) {
        System.out.println("Invalid @Test: " + m + " " + exc.getMessage());
    }
}
~~~









## Annotation with param

### annotation

~~~java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTest {
    Class<? extends Throwable> value();
}
~~~

### 테스트코드

~~~java
@ExceptionTest(ArithmeticException.class)
public static void m3() { }
~~~

### 테스트코드 실행 코드

~~~java
Class<? extends Throwable> excType =
        m.getAnnotation(ExceptionTest.class).value();
if (excType.isInstance(exc)) {
    passed++;
~~~





## Annotation with array param

### Annotation 작성

~~~java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTest {
    Class<? extends Throwable>[] value(); 
}
~~~

### 테스트코드 작성

~~~java
@ExceptionTest({ IndexOutOfBoundsException.class,
                 NullPointerException.class, ArithmeticException.class })
public static void doublyBad() {
    List<String> list = new ArrayList<>();
    list.addAll(5, null);
}
~~~

### 테스트코드 실행 작성

- m.getAnnotation(ExceptionTest.class).value() 으로 배열을 가져와 검사할 수 있다.

~~~java
Class<? extends Throwable>[] excTypes =
        m.getAnnotation(ExceptionTest.class).value();
for (Class<? extends Throwable> excType : excTypes) {
    if (excType.isInstance(exc)) {
        passed++;
        break;
    }
}
~~~









## Repeatable Annotation

- **반복가능 애너테이션**
  - 반복사용 대상 애너테이션
  - @Repeatable("컨테이너에너테이션".class) 작성 후 사용가능
- **컨테이너 애너테이션**
  - 반복가능 애너테이션에 대한 컨테이너 애너테이션
  - 반복가능 애너테이션을 반복 작성하게 할 수 있다.
  - ExceptionTest[] value(); 작성 후 사용가능

### 반복가능 애너테이션

~~~java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ExceptionTestContainer.class)
public @interface ExceptionTest {
    Class<? extends Throwable> value();
}
~~~

### 컨테이너 애너테이션

~~~java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTestContainer {
    ExceptionTest[] value();
}
~~~

### 테스트코드 작성

- 반복가능 애너테이션 작성.

~~~java
@ExceptionTest(IndexOutOfBoundsException.class)
@ExceptionTest(NullPointerException.class)
public static void doublyBad() {
    List<String> list = new ArrayList<>();

    list.addAll(5, null);
}
~~~

### 테스트 실행코드 유의

- isAnnotationPresent : 반복가능 애너테이션, 컨테이너 에너테이션을 구분함.
  - 반복가능 애너테이션 한개 작성 시 m.isAnnotationPresent(ExceptionTest.class) : true
  - 반복가능 애너테이션 여러개 작성 시 m.isAnnotationPresent(ExceptionTestContainer.class) : true
- getAnnotationsByType : 구분없이 모든 ExceptionTest.class 배열을 가져옴.

~~~java
for (Method m : testClass.getDeclaredMethods()) {

    if (m.isAnnotationPresent(ExceptionTest.class)
            || m.isAnnotationPresent(ExceptionTestContainer.class)) {
        tests++;
        try {
            m.invoke(null);
            System.out.printf("Test %s failed: no exception%n", m);
        } catch (Throwable wrappedExc) {
            Throwable exc = wrappedExc.getCause();
            int oldPassed = passed;
            ExceptionTest[] excTests =
                    m.getAnnotationsByType(ExceptionTest.class);
            for (ExceptionTest excTest : excTests) {
                if (excTest.value().isInstance(exc)) {
                    passed++;
                    break;
                }
            }
            if (passed == oldPassed)
                System.out.printf("Test %s failed: %s %n", m, exc);
        }
    }
~~~

### 장단점

- 같은 애너테이션을 여러 번 달 때의 코드 가독성이 높다.
- 애너테이션을 선언하고 이를 처리할때 코드 복잡도가 올라간다.





## 정리

- 애너테이션이 명명패턴보다 낫다.
- 다른 프로그래머가 소스코드에 추가 정보를 제공할 수 있는 도구를 만드는 일을 한다면 적당한 애너테이션 타입도 함께 정의해 제공하자. 애너테이션으로 할 수 있는 일을 명명 패턴으로 처리할 이유는 없다.
- 도구제작자를 제외하고는 일반 프로그래머가 애너테이션 타입을 직접 정의할 일은 거의없다.
- 하지만 자바 프로그래머라면 예외없이 자바가 제공하는 애너테이션 타입을 사용해야 한다.







