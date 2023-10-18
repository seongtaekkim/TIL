# item40 @Override 애너테이션을 일관되게 사용하라



### @Override

- 메서드 선언에만 달 수 있다.
- 상위 타입의 메서드를 재정의 했음을 의미한다.
- 잘 사용하면 여러 버그를 예방한다.





### @Override 안하면 ?

* equals()를 override 의도로 작성했으나,  잘못 작성하여, overloading 한게 될 수 있다.
* HashSet 사용 시 의도와 다르게 결과가 나옴.

~~~java
public class _01_Bigram {
    private final char first;
    private final char second;

    public _01_Bigram(char first, char second) {
        this.first  = first;
        this.second = second;
    }

    public boolean equals(_01_Bigram b) {
        return b.first == first && b.second == second;
    }

    public int hashCode() {
        return 31 * first + second;
    }

    /**
     * 예상 : 26
     * 결과 : 260
     */
    public static void main(String[] args) {
        Set<_01_Bigram> s = new HashSet<>();
        for (int i = 0; i < 10; i++)
            for (char ch = 'a'; ch <= 'z'; ch++)
                s.add(new _01_Bigram(ch, ch));
        System.out.println(s.size());
    }
}
~~~



### @Override 를 일괄적으로 사용하자.

- 구체클래스에서 상위클래스의 추상 메서드를 재정의할 때는 굳이 @Override 를 달지 안하도 된다.
- 하지만 일괄적으로 달아주면, 표현력도 좋고 실수할 일도 없다.

~~~java
public class _02_Bigram {
    private final char first;
    private final char second;

    public _02_Bigram(char first, char second) {
        this.first  = first;
        this.second = second;
    }

    @Override public boolean equals(Object o) {
        if (!(o instanceof _02_Bigram))
            return false;
        _02_Bigram b = (_02_Bigram) o;
        return b.first == first && b.second == second;
    }

    public int hashCode() {
        return 31 * first + second;
    }

    public static void main(String[] args) {
        Set<_02_Bigram> s = new HashSet<>();
        for (int i = 0; i < 10; i++)
            for (char ch = 'a'; ch <= 'z'; ch++)
                s.add(new _02_Bigram(ch, ch));
        System.out.println(s.size());
    }
}
~~~





**추상클래스나 인터페이스에서는 상위 클래스나 상위 인터페이스의 메서드를 재정의하는 모든 메서드에 @Override 를 다는 게 좋다.**

- Set 인터페이스는 Collection 인터페이스를 확장했지만 새로 추가한 메서드는 없다. 따라서 모든 메서드 선언에 @Override 를 달아 추가한 메서드가 없음을 보장했다.
- java9 이후 of 메서드 새로 추가함.
- @Override 는 default 메서드에 대해서 재정의할 때 달려있다.



**@Override 는 클래스 뿐 아니라 인터페이스의 메서드를 재정의할 때도 사용한다.**

- 디폴트 메서드를 지원하면서 인터페이스 메서드를 구현한 메서드에도 @Override 를 다는 습관을 들이면 시그니처가 올바른지 확인할 수 있다.





## 정리

재정의한 모든 메서드에 @Override 를 의식적으로 달면 휴먼에러를 컴파일단계에서 잡을 수 있다.
예외는 구체클래스에서 상위클래스의 추상메서드를 재정의한 경우엔 이 애너테이션을 달지 않아도 된다. (달아도 됨)

