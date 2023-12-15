# item56 공개된 API요소에는 항상 문화 주석을 작성하라



- javadoc 생성

~~~
 javadoc -tag "implSpec:a:Implementation Requirements:" -private -d doc DocExamples.java
~~~



### 여러분의 API를 올바로 문서화하려면 공개된 모든 클래스, 인터페이스, 메서드, 필드 선언에 문서화 주석을 달아야 한다.

- 문서가 잘 갖춰지지 않은 API는 쓰기 헷갈려서 오류의 원인이 되기 쉽다.
- 기본생성자에는 문서화 주석을 달 방법이 없으니 공개 클래스에는 절대 기본생성자를 사용하면 안된다.
- 유지보수까지 고려한다면 공개되지 않은 클래스,인터페이스,생성자,메서드,필드에도 문서화 주석을 달아야 한다.



### 메서드용 문서화 주석에는 해당 메서드와 클라이언트 사이의 규약을 명료하게 기술해야 한다.

- 상속용으로 설계된 클래스(item19)의 메서드가 아니라면 (그 메서드가 어떻게 동작하는지가 아니라) 무엇을 하는 지 기술해야 한다.
- 문서화 주석에는 클라이언트가 해당 메서드를 호출하기 위한 전제조건(precondition) 은 모두 나열해야 한다
  - @throws 태그로 비검사 예외를 선언하여 암시적으로 기술한다.
  - @param 태그를 이용해 그 조건에 영향받는 매개변수에 기술할 수 있다.
- 메서드가 성공적으로 수행된 후에 만족해야 하는 사후조건(postcondition) 도 모두 나열해야 한다.
- 부작용 작성
  - 부작용이란 사후조건으로 명확히 나타나지는 않지만 시스템의 상태에 어떤 변화를 가져오는 것을 뜻한다.
    - 예시) 백그라운드 스레드를 시작하는 메서드라면 문서에 작성해야 한다.
- 메서드 계약을 완벽히 기술하려면 아래 태그를 달아야 한다.
  - 모든 매개변수 태그에 `@param` 
    - 마침표를 붙이지 않는다.
    - 명사구를 쓴다.
  - 반환타입이 void가 아니라면 `@return` 
    - 마침표를 붙이지 않는다.
    - 명사구를 쓴다.
  - 모든 예외에 `@throws`
    - 마침표를 붙이지 않는다.
    - if로 시작해 조건을 설명하는 절을 작성한다.

~~~java
/**
 * Returns the element at the specified position in this list.
 *
 * <p>This method is <i>not</i> guaranteed to run in constant
 * time. In some implementations it may run in time proportional
 * to the element position.
 *
 * @param  index index of element to return; must be
 *         non-negative and less than the size of this list
 * @return the element at the specified position in this list
 * @throws IndexOutOfBoundsException if the index is out of range
 *         ({@code index < 0 || index >= this.size()})
 */
E get(int index) {
    return null;
}
~~~

- `{@code}` 1. 태그로 감싼 내용을 코드용 폰트로 랜더링한다. 2. HTML요소나 다른 자바독 태그를 무시한다.
- `this list` 인스턴스 메서드에 문서화 주석에 쓰인 "this" 는 호출된 메서드가 자리하는 객체를 가리킨다.
- 첫번 째 문장은 요약설명 에 해당한다. 
  - 한 클래스에서 요약설명이 같은 멤버(혹은 생성자) 가 있으면 안된다.





~~~java
/**
 * Returns true if this collection is empty.
 *
 * @implSpec This implementation returns {@code this.size() == 0}.
 *
 * @return true if this collection is empty
 */
public boolean isEmpty() {
    return false;
}
~~~

- `@implSpec` 클래스를 상속용으로 설계할 때에는 self-use pattern 에 대해 문서에 남겨 다른 프로그레머에게 그 메서드를 올바로 재정의하는 방법을 알려줘야 한다.
- 일반적인 문서화주석은 해당 메서드와 클라이언트 사이의 계약을 설명
- `@implSpec` 는 해당 메서드와 구현 클래스 사이의 계약을 설명.
- `-tag "implSpec:a:Implementation Requirements:"` : javadoc 옵션으로 사용해야 에러가 안남





~~~java
  /**
   * A geometric series converges if {@literal |r| < 1}.
   */
  public void fragment() {}
~~~

- `{@literal}` :   `<, >, &`등의 html 메타문자를 포함하기 위해 사용
  - html 마크업이나 자ㅂ독 태그를 무시하게 해준다.



  

~~~java
/**
 * A suspect, such as Colonel Mustard or {@literal Mrs. Peacock}.
 */
public enum FixedSuspect {
    MISS_SCARLETT, PROFESSOR_PLUM, MRS_PEACOCK, MR_GREEN, COLONEL_MUSTARD, MRS_WHITE
}
~~~

- 요약설명은 마침표에 주의해야 하는데, "."  를 주의해야 한다. (javadoc이 요약이 끝나는 기준으로 판단)
  - 요약끝나는 기준 : <마침표> <공백> <다음문장시작> 패턴의 마침표 이다.
    - 공백 : 스페이스, 탭, 줄바꿈
    - 다음문장시작: 소문자가 아닌 문자
- `{@literal}`태그로 감싸서 실수를 예뱡할 수 있따.
- java10 부터 `{@summar}`를 사용하면 깔끔하다.
- 메서드와 생성자의 요약설명은 주어가 없는 동사구여야 한다.
  - ArrayList(int initialCapacity) : `Constructs an enpty list with the specified initial capacity`
  - Collection.size() : `Returns the number of elements in this collection.`
- 클래스, 인터페이스, 필드의 요약설명은 명사절 이어야 한다.
  - Instant: `An instantaneous point on the time-line`
  - Math.PI: `The double value that is closer than any other to pi, the ratio of the circumference of a circle to its diameter.`





문서화 주석에서 제네릭, 열거타입, 애너테이션은 특별히 주의해야 한다.

제네릭 타입이나 제네릭 메서드를 문서화할 때는 모든 타입 매개변수에 주석을 달아야 한다.

~~~java
/**
 * An object that maps keys to values. A map cannot contain
 * duplicate keys; each key can map to at most one value.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public interface Map<K, V> {}
~~~





### 열거 타입을 문서화할 때는 상수들에도 주석을 달아야 한다.

- 열거타입 자체와 public 메서드도 달아야 한다.

~~~java
/**
 * An instrument section of a symphony orchestra.
 */
public enum OrchestraSection {
    /** Woodwinds, such as flute, clarinet, and oboe. */
    WOODWIND,

    /** Brass instruments, such as french horn and trumpet. */
    BRASS,

    /** Percussion instruments, such as timpani and cymbals. */
    PERCUSSION,

    /** Stringed instruments, such as violin and cello. */
    STRING;
}
~~~





### 애너테이션 타입을 문서화할 때는 멤버들에도 모두 주석을 달아야 한다.

- 필드설명 : **명사구**
- 에너테이션 타입 요약설명 : 프로그램 요소에 이 에너테이션을 작성한다는 것이 어떤 의미인 지 설명하는 **동사구** 로 한다.

~~~java
/**
 * Indicates that the annotated method is a test method that
 * must throw the designated exception to pass.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionTest {
    /**
     * The exception that the annotated test method must throw
     * in order to pass. (The test is permitted to throw any
     * subtype of the type described by this class object.)
     */
    Class<? extends Throwable> value();
}
~~~





### package-info.java

- 패키지를 설명하는 문서화 주석
- 모듈은 module-info.java 에 작성한다.



### 직렬화, 스레드안전

클래스 혹은 정적 메서드가 스레드 안전하든 아니든 스레드 안전수준을 API 설명에 포함해야 한다.
직렬화 할 수 있는 클래스라면 직렬화 형태도 API 설명에 기술해야 한다.



### {@inheritDoc}

- 태그를 사용해 상위타입의 문서화 주석일부를 상속할 수 있다.
- 클래스는 확장한 인터페이스의 문서화주석을 재사용할 수있다. 다만 사용하는 데 제약이 있다고 한다. [doc](https://docs.oracle.com/javase/6/docs/technotes/tools/solaris/javadoc.html#inheritingcomments)



[javadoc-guide](https://www.oracle.com/technical-resources/articles/java/javadoc-tool.html)

잘 쓰인 문서인지 확인하는 유일한 방법
javadoc 유틸리티가 생성한 웹페이지를 읽어보는 길 뿐이다.

- 다른사람이 사용할 API라면 반드시 모든 API요소를 검토하라.



## 정리

문서화 주석은 여러분 API를 문서화하는 가장 훌륭하고 효과적인 방법이다.
공개 API라면 빠짐없이 설명을 달아야 한다.
표준규약을 일관되게 지키자.
문서화 주석에 임의의 HTML 태그를 사용할 수 있음을 기억하라.
단, HTML 메타문자는 특별하게 취급해야 한다.





