# item65 리플렉션보다는 인터페이스를 사용해라



java.lang.reflect 를 이용하면 프로그램에서 임의의 클래스에 접근할 수 있다.

Constructor, Method, Field 인스턴스를 이용해 객체의 값을 조작할 수 있다.

이를 이용해서 런타임에 존재하지 않던 클래스를 이용할 수도 있다.



## 단점



### 컴파일타임 타입검사 불가능

- 리플렉션은 기본적으로 런타임에서 동작하기에 예외처리등을 컴파일타임에 검사할 수 없다.

### 코드가 장황해진다

- 여러 클래스를 이용해야하니 복잡해진다.

### 성능이 떨어진다

- 메소드 사용측면에서 일반적인 객체보다 리플렉션 호출에 의한 시간이 더 걸린다



## 사용하는 곳

코드분석도구나 의존관계 주입 프레임워크가 그렇긴한데, 가능하면 리플렉션 사용을 줄이려 한다. (단점이 명확해서)



### 사례1) 컴파일타임에 이용할 수 없는 클래스를 사용해야 하는경우

- 리플렉션으로 인스턴스를 만들고, 그에 대한 참조는 인터페이스 등으로 사용하자.

~~~java
/**
 * args example below
 * java.util.HashSet 1 2 test 4
 */
public class ReflectiveInstantiation {
    public static void main(String[] args) {
        Class<? extends Set<String>> cl = null;
        try {
            cl = (Class<? extends Set<String>>)  // Unchecked cast!
                    Class.forName(args[0]);
        } catch (ClassNotFoundException e) {
            fatalError("Class not found.");
        }

        // Get the constructor
        Constructor<? extends Set<String>> cons = null;
        try {
            cons = cl.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            fatalError("No parameterless constructor");
        }

        // Instantiate the set
        Set<String> s = null;
        try {
            s = cons.newInstance();
        } catch (IllegalAccessException e) {
            fatalError("Constructor not accessible");
        } catch (InstantiationException e) {
            fatalError("Class not instantiable.");
        } catch (InvocationTargetException e) {
            fatalError("Constructor threw " + e.getCause());
        } catch (ClassCastException e) {
            fatalError("Class doesn't implement Set");
        }

        // Exercise the set
        s.addAll(Arrays.asList(args).subList(1, args.length));
        System.out.println(s);
    }

    private static void fatalError(String msg) {
        System.err.println(msg);
        System.exit(1);
    }
}
~~~

### 사례1 단점 두가지

1. 런타임에 6개나 되는 예외를 던질 수 있다. (컴파일 타임에 못잡아서)
2. 코드가 복잡하다.

- 단점이 존재하지만, 리플렉션은 인스턴스 생성에만 사용하고, 그이후는 일반적인 경우와 같아 영향을 주는 부분이 매우 적다.



### 사례2)

- 리플렉션은 런타임에 존재하지 않을 수 도 있는 다른 클래스등의 의존성을 관리할 때 적합하다.
  - 버전이 여러개 존재하는 외부 패키지를 다룰 때 적합하다.
  - 가장 오래된 버전으로 컴파일하고, 이후버전은 런타임에 리플렉션으로 접근하는 방식이다.







## 정리

리플렉션은 복잡한 특수 시스템을 개발할 때 필요한 강력한 기능이지만 단점도 많다

컴파일타임에는 알 수 없는 클래스를 사용하는 프로그램을 작성한다면 리플렉션을 사용해야 할 것이다.

단, 되도록 객체 생성에만 사용하고, 생성한 객체를 이용할 때는 적절한 인터페이스나 컴파일타임에 알 수 있는 상위 클래스로 형변환해 사용해야 한다.