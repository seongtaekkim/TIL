# item04. Enforce noninstantiability with a private constructor



### 정적 메서드와 정적 필드만을 담은 클래스를 만들 때 사용한다.

- 정적 메서드만 담은 유틸리티 클래스는 인스턴스로 만들어 쓰려고 설계한 클래스가 아니다. 
- 추상 클래스로 만드는 것으로는 인스턴스화를 막을 수 없다. 
  - 상속 할 경우 인스턴스가 생성됨.
- private 생성자를 추가하면 클래스의 인스턴스화를 막을 수 있다. 
- 생성자에 주석으로 인스턴스화 불가한 이유를 설명하는 것이 좋다. 
- 상속을 방지할 때도 같은 방법을 사용할 수 있다.



- private 생성자를 작성해서 인스턴스생성을 막아준다.
  - 같은파일에서 생성할 경우, 혹은 리플렉션에 의해 생성될 수 있으니 에러처리한다. ([AssertionError](./item04-assertionerror.md))

- java.util.Arrays, java.util.Collections 등에서 사용하는 방법이다.
- 상속이 불가능하다.

~~~java
public class UtilityClass {

    /**
     * 이 클래스는 인스턴스를 만들 수 없습니다.
     */
    private UtilityClass() {
        throw new AssertionError();
    }

    public static String hello() {
        return "hello";
    }

    public static void main(String[] args) {
        String hello = UtilityClass.hello();
        UtilityClass utilityClass = new UtilityClass(); // compile error
        utilityClass.hello();
    }
}

~~~



- spring api 등에도 Utility class가 많은데, abstract 로 정의된게 많아서 상속후 인스턴스를 생성할수 있는게 있다.
- 추상클래스로 만드는것으로는 인스턴스화를 막을 수 없다. 하위클래스를 만들면 그만이다. 이를 본 사용자는 상속해서 쓰라고 오해할 수 있으니 그또한 문제다 ([아이템19]())

~~~java
public class ConfigUtilityClass extends AnnotationConfigUtils  {
    public static void main(String[] args) {
        ConfigUtilityClass o = new ConfigUtilityClass();
        o.registerAnnotationConfigProcessors(null);
    }
}
~~~

~~~java
public abstract class AnnotationConfigUtils {
		...
    public AnnotationConfigUtils() {}
    ...
}
~~~

























