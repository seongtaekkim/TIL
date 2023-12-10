# item50 적시에 방어적 복사본을 만들라



자바는 기본적으로 안전하지만 클라이언트가 불변식을 깨뜨린다고 가정하고 방어적 프로그래밍을 해야한다.

1. 악의적인 보안 뚫기
2. 프로그래머 실수로 인한 오동작



### Period 클래스를 불변 클래스로 구성하였다.

~~~java
public final class Period {
    private final Date start;
    private final Date end;

    /**
     * @param  start the beginning of the period
     * @param  end the end of the period; must not precede start
     * @throws IllegalArgumentException if start is after end
     * @throws NullPointerException if start or end is null
     */
    public Period(Date start, Date end) {
        if (start.compareTo(end) > 0)
            throw new IllegalArgumentException(
                    start + " after " + end);
        this.start = start;
        this.end   = end;
    }

    public Date start() {
        return start;
    }
    public Date end() {
        return end;
    }
  ...
}
~~~

- Date 인스턴스 자체가 불변데이터가 아니기 때문에 Period 클래스는 불변이 될 수 없다.

~~~java
Date start = new Date();
Date end = new Date();
Period p = new Period(start, end);
end.setYear(78); 
System.out.println(p);
~~~

- Date 대신 불변 class인 Instant (혹은 LocalDateTime, ZoneDatetTime) 를 사용하면 된다.





### Period 공격 - 생성자

외부공격으로부터 Period 내부를 보호하려면 생성자에서 받은 가변 매개변수 각각을 방어적으로 복사하여 사용해야 한다.

- 새로 작성한 생성자를 사용하면 앞의 공격은 방어가 된다.
- **코드를 보면 매개변수 유효성을 검사(item49)하기 전에 방어적복사본을 만들고, 유효성 검사를 했다.**
  - 복사본먼저 생성한 이유 : 멀티쓰레딩 환경에서 원본객체 유효성을 검사한 후 복사본을 만드는 찰나에 다른 스레드가 원본객체를 수정할 수 있기 때문.
  - 검사시점/사용시점 (time-of-check/time-of-use) 공격 혹은 TOCTOU 공격

~~~java
public Period(Date start, Date end) {
    this.start = new Date(start.getTime());
    this.end   = new Date(end.getTime());

    if (this.start.compareTo(this.end) > 0)
        throw new IllegalArgumentException(
                this.start + " after " + this.end);
}
~~~

Date의 clone 메서드 사용하지 않은 이유

- Date는 final이 아니므로 clone이 하위클래스의 인스턴스를 반환할 수 있다.
  - 확장될 수 있는 타입은 방어적복사본을 만들 때 clone 을 사용하면 안된다.



### Period 공격 - 인스턴스 변경

- 인스턴스 변경이 가능하다.
- 해결방법: 접근자가 가변필드의 방어적 복사본을 반환하면 된다.

~~~java
start = new Date();
end = new Date();
p = new Period(start, end);
p.end().setYear(78);
System.out.println(p);
~~~

- 생성자와 달리 접근자는 clone을 사용해도 된다. (신뢰할 수 있는 Date class임이 확실하기 때문.)
  - 하지만 item13에서 언급한 것처럼 생성자, 정적 팩터리가 좋음.

~~~java
public Date start() {
    return new Date(start.getTime());
}

public Date end() {
    return new Date(end.getTime());
}
~~~





### 방어적 복사의 목적

매개변수를 방어적으로 복사하는 목적

- 불변객체를 만들기 위해
- 클래스 내 자료구조를 관리할 때 변경 여부를 고려해야 한다.

내부 객체를 클라이언트에 전달하기 전에 방어적 복사

- 클래스가 불변이든 가변이든 내부객체를 반환할 때 주의해야 한다.
- 확실하지 않다면 방어적 복사본을 반환 혹은 불변뷰를 반환해야 한다.

되도록 불변객체들을 조합해 객체를 구형해야 방어적 복사를 할 일이 줄어든다 (item17)





### 방어적복사에는 성능저하가 따른다. 방어적복사 하지 않을 수 있는 경우는?

**같은패키지 사용**

- 호출자가 컴포넌트 내부를 수정하지 않는다 확신하면 방어적복사 생략 가능
- 문서화 기재 필요.

**다른 패키지에서 사용**

- 넘겨받은 가변 매개변수를 항상 방어적으로 복사저장하는 건 아니다.
- 메서드나 생성자의 매개변수로 넘기는 행위가 그 객체 통제권을 이전함을 뜻하기도 한다.
  - 이런경우 객체를 직접 수정하지 않음을 약속해야 한다.
  - 문서에 기재 필요.

** 통제권을 넘겨받기로 한 메서드,생성자를 가진 클래스는 클라이언트 공격에 취약하다. **

방어적복사 생략 케이스는

- 클래스와 클라이언트가 상호 신뢰하는 경우
- 불변식이 깨지더라도 그 영향이 클라이언트로 국한될 경우
  - 래퍼클래스 패턴(item18) : 클라이언트는 래퍼에 넘긴 객체에 직접접근할 수 있지만, 변경에 대한 영향은 오직 클라이언트만 받는다.





## 핵심 정리

클래스가 클라이언트로부터 받는 혹은 클라이언트로 반환하는 구성요소가 가변이라면
그 요소는 반드시 방어적으로 복사해야 한다.
복사 비용이 너무 크거나 클라이언트가 그 요소를 잘못 수정할 일이 없음을 신뢰한다면 방어적 복사를 수행하는 대신 해당 구성요소를 수정했을 때의 책임이 클라이엍느에 있음을 문서에 명시하자.