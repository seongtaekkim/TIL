# item05.  Prefer dependency injection to hardwiring resources



### 사용하는 자원에 따라 동작이 달라지는 클래스는 [item04(정적 유틸리티 클래스)](../item04/item04.md)나 item3([싱글턴 방식](../item03/item03.md))이 적합하지 않다.

~~~java
public class SpellChecker {

    private static final Dictionary dictionary = new DefaultDictionary();

    private SpellChecker() {}
    public static boolean isValid(String word) { }
    public static List<Str ing> suggestions(String typo) { }
}

~~~

- 자원이 오직 하나의 클래스에서 케어 되지 않고 교체될 수 있고, 테스트용 클래스가 필요할 수 도 있다.
- 자원 변경에 따라 나머지 코드들이 영향을 받는다면 적합하지 않다.
- 해당 클래스가 자원을 결정하기 때문에 테스트코드 유연성이 떨어진다. (오직 하나의 정해진 자원(class)으로 테스트 해야함.)



~~~java
public class SpellChecker {

    private final Dictionary dictionary = new DefaultDictionary();
    private SpellChecker() {}
    public static final SpellChecker INSTANCE = new SpellChecker();

    public static boolean isValid(String word) { }
    public static List<String> suggestions(String typo) { }
}
~~~

- 싱글턴도 마찬가지로 이미 자원이 정해져 있는 상태이기 때문에 적합하지 않다.



### 의존 객체 주입이란 인스턴스를 생성할 때 필요한 자원을 넘겨주는 방식이다.

~~~java
public class SpellChecker {

    private final Dictionary dictionary;

    public SpellChecker(Dictionary dictionary) {
        this.dictionary = dictionary;
    }
  ...
~~~

- 의존객체주입을 확장하여 [팩터리 메소드 패턴 ](../../../../../design-pattern/docs/02-factory-method.md) 을 사용할 수 있다. ([dictionary factory example](./item05-factory-method.md))
- 의존 객체가 많은 경우에 Dagger, Guice, [스프링](./item05-string-ioc.md) 같은 의존 객체 주입 프레임워크 도입을 고려할 수 있다.



### 의존 객체 주입을 사용하면 클래스의 유연성, 재사용성, 테스트 용이성을 개선할 수 있다.

~~~java
  @Test
  void isValid() {
      SpellChecker spellChecker = new SpellChecker(MockDictionary::new);
      //SpellChecker spellChecker = new SpellChecker(() -> new MockDictionary());
      spellChecker.isValid("test");
  }
~~~

- dictionary를 상속받는 mock class 를 통해 테스트를 쉽게 할 수 있다.



## 정리

클래스가 내부적으로 하나 이상의 자원에 의존하고, 그 자원이 클래스 동작에 영향을 준다면 singleton과 static utility class는 사용하지 않는것이 좋다.
이 자원들을 클래스가 직접 만들게 해서도 안된다.
대신 필요한 자원을 (혹은 자원을 만드는 팩토리) 생성자에 (혹은 정적 팩터리나 빌더에 ) 넘겨주자.
의존 객체 주입이라 하는 이 기법은 클래스의 유연성, 재사용성, 테스트용이성을 개선해준다.











































