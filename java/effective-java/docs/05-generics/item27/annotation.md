# Annotation

- @Retention 의 RetentionPolicy에 대해 알아보자.
- @Type의 ElementType 종류에 대해 알아보자.



## RetentionPolicy

- 세가지 레벨로 나뉘어 해당 에노테이션의 scope를 지정할 수 있다.

~~~java
public enum RetentionPolicy {
    /**
     * Annotations are to be discarded by the compiler.
     */
    SOURCE,

    /**
     * Annotations are to be recorded in the class file by the compiler
     * but need not be retained by the VM at run time.  This is the default
     * behavior.
     */
    CLASS,

    /**
     * Annotations are to be recorded in the class file by the compiler and
     * retained by the VM at run time, so they may be read reflectively.
     *
     * @see java.lang.reflect.AnnotatedElement
     */
    RUNTIME
}
~~~



### SOURCE

- 컴파일 시점까지만 존재한다.
- 컴파일 시 에러 등을 체크하거나, Annotation Processor 등을 수행할 때 사용한다.

~~~java
@Retention(RetentionPolicy.SOURCE)
public @interface MyAnnotation {
}
~~~

- class 파일에 지정했었던 에노테이션이 없다는걸 확인할 수 있다.

~~~java
// MyClass.class
public class MyClass {
    public MyClass() {
    }

    public static void main(String[] args) {
        Stream var10000 = Arrays.stream(MyClass.class.getAnnotations());
        PrintStream var10001 = System.out;
        Objects.requireNonNull(var10001);
        var10000.forEach(var10001::println);
    }
}
~~~

- SuppressWarnings은 컴파일 시에 동작하는 annotation임을 알 수 있다.

~~~java
@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE, MODULE})
@Retention(RetentionPolicy.SOURCE)
public @interface SuppressWarnings {
    ...
    String[] value();
}
~~~





### CLASS, RUNTIME

- CLASS는 컴파일 후 바이트코드에 존재하나, 런타임 시 메모리에 존재하지 않는다.
  - 리플렉션 등의 코드로 조회 불가
  - ASM 등에서 bytecode 를 참조하는 경우 사용한다.
- RUNTIME은 바이트코드에 존재하면서 메모리에도 존재한다.
  - 리플렉션 등의 코드로 조회 가능
  - 런타임에서 리플렉션 등을 사용하고 싶을 때 사용한다.

- CLASS, RUNTIME은 둘 다 class 파일에 에노테이션 코드가 존재한다. (차이점은 아래 예제에 bytecode에서 찾을 수 있다.)

~~~java
@Retention(RetentionPolicy.CLASS)
public @interface MyAnnotation {
}
~~~

~~~java
// MyClass.class
@MyAnnotation
public class MyClass {
    public MyClass() {
    }

    @MyAnnotation
    public static void main(String[] args) {
        Stream var10000 = Arrays.stream(MyClass.class.getAnnotations());
        PrintStream var10001 = System.out;
        Objects.requireNonNull(var10001);
        var10000.forEach(var10001::println);
    }
}
~~~

### CLASS bytecode 

- RuntimeInvisibleAnnotations
  - runtime 참조 불가

~~~sh
$ javap -v MyClass

...
SourceFile: "MyClass.java"
RuntimeInvisibleAnnotations:
  0: #17()
    me.staek.chapter05.item27.MyAnnotation
...
~~~

### RUNTIME bytecode

- RuntimeVisibleAnnotations
  - runtime 참조 가능

~~~sh
$ javap -v MyClass

SourceFile: "MyClass.java"
RuntimeVisibleAnnotations:
  0: #17()
    me.staek.chapter05.item27.MyAnnotation
InnerClasses:
~~~

- RUMETIME 은 리플렉션 코드로 정보를 가져올 수 있다.

```java
@MyAnnotation
public static void main(String[] args) {
    Arrays.stream(MyClass.class.getAnnotations()).forEach(System.out::println);
}
```

~~~sh
@me.staek.chapter05.item27.MyAnnotation()
~~~





## Type

~~~
ElementType.PACKAGE : 패키지 선언
ElementType.TYPE : 타입 선언
ElementType.ANNOTATION_TYPE : 어노테이션 타입 선언
ElementType.CONSTRUCTOR : 생성자 선언
ElementType.FIELD : 멤버 변수 선언
ElementType.LOCAL_VARIABLE : 지역 변수 선언
ElementType.METHOD : 메서드 선언
ElementType.PARAMETER : 전달인자 선언
ElementType.TYPE_PARAMETER : 전달인자 타입 선언
ElementType.TYPE_USE : 타입 선언
~~~





















