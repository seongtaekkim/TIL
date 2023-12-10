# item49 매개변수가 유효한 지 검사하라



- 메서드와 생성자 대부분은 입력 매개변수의 값이 특정 조건을 만족하기를 바란다.
- 제약사항은 문서화 돼야 한다.
- 메서드 몸체가 시작되기 전에 검사되어야 한다.
  - 이는 오류는 가능한 빨리 잡아야 한다는 일반 원칙에 한 사례이기도 한다.
  - 그렇지 못하면 해당 오류를 감지하기 어렵고, 발생지점을 찾기 어렵다.



### 매개변수 검사를 못했을 경우 ?

- 메서드가 수행되는 중간에 모호한 예외를 던지며 실패할 수 있다.
- 메서드가 잘 수행되지만 잘못된 결과 리턴
- 메서드가 잘 수행되었지만, 미래에 관련없는 오류를 발생함
- => 매개변수 검사에 실패하면 실패원자성(76) 을 어길 수 있다.
  - 실패원자성: 객체가 메서드 호출에 실패하더라도 상태가 이전과 동일해야 한다는 것




### 메서드 유효성검사 - 문서화

- **public, protected method**는 매개변수값이 잘못 됐을 때 던지는 예외를 문서화해야한다.

- @throws java doc (item74)
  - ex) IllegalArgumentException, IndexOutOfBoundsException, NullPointerException (item72)
  
- 예제
  
  - BigInteger 가 Null이면 문서에 없는 NPE가 발생하는데, 이는 BigInteger 수준에서 작성했기 때문이다.
    - 클래스 수준 주석은 모든 pulbic 메서드에 해당하므로 깔끔하지 못함
  
  - @Nullable 등을 사용해 특정 매개변수가 null임을 알려줄 수 있지만 표준적이지 않다.
  
  ~~~java
  // BigInteger.java
  /**
   * Returns a BigInteger whose value is {@code (this mod m}).  This method
   * differs from {@code remainder} in that it always returns a
   * <i>non-negative</i> BigInteger.
   *
   * @param  m the modulus.
   * @return {@code this mod m}
   * @throws ArithmeticException {@code m} &le; 0
   * @see    #remainder
   */
  public BigInteger mod(BigInteger m) {
      if (m.signum <= 0)
          throw new ArithmeticException("BigInteger: modulus not positive");
  
      BigInteger result = this.remainder(m);
      return (result.signum >= 0 ? result : result.add(m));
  }
  ~~~



- java7 에 추가된 Objects.requireNonNull method는 유연하고 사용하기에 편해서 null검사를 수동으로 하지 않아도 된다.

~~~java
class NullTest {}

public class Test {
    public static void main(String[] args) {
        NullTest nullTest = null;
        Objects.requireNonNull(nullTest, "널났다!ㄴ");
    }
}

~~~

- java9에서는 Objects에 범위 검사도 더해졌다.

- checkFromIndexSize, checkFromToIndex, checkIndex 메서드
  - 예외메세지작성x, 리스트, 배열전용으로 설계되었다.
  - closed range 를 다룰 수 없다.



### 메서드 유효성검사 - assert

**public 이 아닌 메서드**라면 assert를 사용해 매개변수 유효성검증을 사용하여 상황을 통제할 수 있다.

- assert는 실패하면 AssertionError를 던진다.
- 런타임에 아무런 효과도, 성능저하도 없다. (-ea, --enableassertions 설정 시 런타임에 영항을 준다.)

~~~java
public class SortTest {

    private static void sort(long a[], int offset, int length) {
        assert a != null;
        assert offset >= 0 && offset <= a.length;
        assert length >= 0 && length <= a.length - offset;

        Arrays.sort(a, offset, offset+length);
    }
    public static void main(String[] args) {
        long[] data = new long[] {7,6,5,4,3,2,1};
        sort(data,2,4);
        Arrays.stream(data).forEach(System.out::println);
    }
}
~~~





### 메서드 유효성검사 - 생성자

- 생성자는 나중에 쓰려고 저장하는 매개변수의 유효성을 검사하라 는 원칙의 특수한 사례다.
- 생성자 매개변수의 유효성 검사는 클래스 불변식을 어기는 객체가 만들어지지 않게 하는데 꼭 필요하다.



### 메서드 유효성검사 - 예외

- 메서드 몸체 실행 전 매개변수 유효성을 검사해야 한다는 규칙에 예외도 있다.
- 유효성 검사 비용이 지나치게 높을 때, 실용적이지 않을때
- 계산 중 암묵적으로 검사가 실행될 때 - Collections.sort(List) 는 비교 불가 시 ClassCastException 을 던진다.
- 하지만 암묵적 검사에 너무 의존하면 실패원자성(아이템76) 위배가능함.

~~~java
public static void sort(List<Person> list) {
    for (Person p : list) {
        if (p.getName() == null)
            throw new NullPointerException();
    }
    Collections.sort(list);
}

public static void main(String[] args) {

    List<Person> list = new ArrayList<>();
    list.add(new Person("apple"));
    list.add(new Person("strawberry"));
    list.add(new Person());
    sort(list);
    list.forEach(System.out::println);
}
~~~



### 메서드 유효성검사 - API 예외번역

로직계산 시 유효성검사 후 잘못된 예외 던질 경우

- 계산 중 잘못된 매개변수 값사용 에 대한 예외와 API 문서에서 던지기로 한 예외가 다름
- **아이템73 의 예외번역 관용구**를 이용해서 API문서에 기재된 예외로 번역해야한다.



### 오해하지 마라

- 매개변수 제약을 두는게 좋다 가 아니다.
- 메서드는 최대한 범용적으로 설계 하되 구현 개념 자체가 특정 제약을 내재한 경우도 있다는 것.



## 정리

메서드나 생성자들 작성할 때면 그 매개변수들에 어떤 제약이 있을 지 생각해야 한다.
그 제약들을 문서화하고 메서드 코드 시작 부분에서 명시적으로 검사해야 한다.
이런 습관을 반드시 기르도록 하자.
그 노력은 유효성 검사가 실제 오류를 처음 걸러낼 때 충분히 보상받을 것이다.

