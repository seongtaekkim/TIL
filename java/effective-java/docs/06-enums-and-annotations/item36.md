# item36 비트 필드 대신 EnumSet을 사용하라



열거한 값들이 주로 집합으로 사용될 경우 예전에는 각 상수에 서로 다른 2의 거듭제곱 값을 할당한 정수열거패턴(item34)을 사용해왔다.





### 비트필드 (bit field)

- 합집합, 교집합 등의 집합연산을 쉽게 수행

~~~java
text.applyStyles(STYLE_BOLD | STYLE_ITALIC);
~~~

**bit field 문제점**

- 비트필드 값은 그자체 의미가 없어 해석하기 어렵다.
- 비트필드 하나에 속한 모든 원소를 순회하기 어렵다.
- 최대 몇 비트가 필요한지 API 작성시 미리 예측하여 적절한 타입을 선택해야 한다.





### EnumSet

- Set 인터페이스 구현체
- 타입안전
- 다른 어떤 Set구현체와도 함께 사용할 수 있다.
- 내부는 비트벡터로 구현되어 있다.
  - 원소가 총 64개 이하라면 EnumSet 전체를 long 변수 하나로 표현하여 비트빌드에 비견되는 성능을 보여준
  - removeAll, retainAll 같은 대량 작업은 비트를 효율적으로 처리할 수 있는 산술 연산을 써서 구현했다

~~~java
public class Text {
    public enum Style {BOLD, ITALIC, UNDERLINE, STRIKETHROUGH}

    // Any Set could be passed in, but EnumSet is clearly best
    public void applyStyles(Set<Style> styles) {
        System.out.printf("Applying styles %s to text%n",
                Objects.requireNonNull(styles));
    }
}
~~~

~~~java
public static void main(String[] args) {
    Text text = new Text();
    text.applyStyles(EnumSet.of(Style.BOLD, Style.ITALIC));
}
~~~



- EnumSet 불변객체 예제

~~~java
/**
 * immutable EnumSet
 * type : java.util.Collections$UnmodifiableCollection
 */
Collection<Style> immutableStyles = Collections.unmodifiableCollection(styles);
System.out.println(immutableStyles.getClass().getName());;
immutableStyles.add(Style.BOLD); // UnsupportedOperationException
~~~



- 내부적으로 비트연산 함

~~~java
class RegularEnumSet<E extends Enum<E>> extends EnumSet<E> {
    @java.io.Serial
    private static final long serialVersionUID = 3411599620347842686L;
    /**
     * Bit vector representation of this set.  The 2^k bit indicates the
     * presence of universe[k] in this set.
     */
    private long elements = 0L;

    RegularEnumSet(Class<E>elementType, Enum<?>[] universe) {
        super(elementType, universe);
    }

    void addRange(E from, E to) {
        elements = (-1L >>>  (from.ordinal() - to.ordinal() - 1)) << from.ordinal();
    }

    void addAll() {
        if (universe.length != 0)
            elements = -1L >>> -universe.length;
    }

    void complement() {
        if (universe.length != 0) {
            elements = ~elements;
            elements &= -1L >>> -universe.length;  // Mask unused bits
        }
    }
~~~





## 정리

- 열거할 수 있는 타입을 한데 모아 집합 형태로 사용한다고 해도 비트필드를 사용할 이유는 없다.
- EnumSet 클래스가 비트필드 수준의 명료함과 성능을 제공하고 아이템34에서 설명한 열거타입 장점까지 선사하기  때문이다.
- EnumSet의 유일한 단점이라면 불변 EnumSet을 만들수 없다는 것이다.
- 불변이 될 때까지는 Collections.unmodifiableSet으로 EnumSet을 래핑해서 쓸 수 있다.
