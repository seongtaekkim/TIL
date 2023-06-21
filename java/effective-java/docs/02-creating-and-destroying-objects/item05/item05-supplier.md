

## Supplier

~~~java
@FunctionalInterface
public interface Supplier<T> {
    /**
     * Gets a result.
     *
     * @return a result
     */
    T get();
}
~~~







### Factory method pattern

### 일반적인 factory class

~~~java
public class DictionaryFactory {
    public static DefaultDictionary get() {
        return new DefaultDictionary();
    }
}
~~~

~~~java
public class SpellChecker {

    private final Dictionary dictionary;

    public SpellChecker(DictionaryFactory factory) {
        this.dictionary = factory.get();
    }

~~~

- 구현클래스를 생성하는 로직을 갖고있는 factory 를 인자로 전달해서 자원을 할당해준다.



### supplier factory

~~~java
public class SpellChecker {

    private final Dictionary dictionary;

    public SpellChecker(Supplier<Dictionary> dictionarySupplier) {
        this.dictionary = dictionarySupplier.get();
    }

    ... 
}

~~~

- factory class 에 인자 등 역할이 없다면, Supplier가 factory class 를 대신해서 사용할 수 있다.



~~~java
    public SpellChecker(Supplier<? extends Dictionary> dictionarySupplier) {
    ...
~~~

- 한정자를 사용할 수 있긴한데, Dictionary가 interface이면 없어도 구현체는 모두 생성 가능하다.

  (한정적 와일드카드 타입을 사용해 팩터리의 타입 매개변수를 제한할 수 있다.)





### Lazy Evaluation

~~~java
public class LazyEvaluation {

    public static void main(String[] args) {
        printValueIndex(1, ()->getValue());
        printValueIndex(0, ()->getValue());
        printValueIndex(-2, ()->getValue());
    }

    private static void printValueIndex(int i, Supplier<String> s) {
        if (i > 0)
            System.out.println("value is " + s.get());
        else
            System.out.println("false");
    }

    private static String getValue()  {
        try {
            TimeUnit.SECONDS.sleep(2);
            return "True";
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
~~~

- 실제 필요한 시점에 Supplier 객체의 함수가 실행하므로, Supplierr없이 수행하는 로직에 비해 비용을 아낄 수 있다.

















