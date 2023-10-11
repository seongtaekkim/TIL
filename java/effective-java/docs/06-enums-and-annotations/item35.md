# item35 original 메서드 대신 인스턴스 필드를 사용하라

- 대부분 열거타입상수는 하나의 정숫값에 대응된다.
- 그리고 몇번 째 위치인지를 반환하는 original 메서드를 제공한다.

~~~java
//Returns the ordinal of this enumeration constant (its position in its enum declaration, where the initial constant is assigned an ordinal of zero).
int	ordinal()
~~~





### ordinal 사용 예제, 단점

~~~java
public enum EnumWithOriginal {
        SOLO, DUET, TRIO, QUARTET, QUINTET,
        SEXTET, SEPTET, OCTET, DOUBLE_QUARTET,
        NONET, DECTET, TRIPLE_QUARTET;

    public int numberOfMusicians() {
            return ordinal() + 1;
    }
}
~~~

- 동작은하지만 유지보수하기 어렵다.
- 상수 선언 순서를 바꾸는 순간 오동작하고 이미 사용중인 정수와 값이 같은 상수는 추가할 수 없다.
- 또한 값을 중간에 비울 수 없다. 더미상수를 추가할 수 있지만 깔끔하지 않다. 
- 수가 많아질 수록 실용성이 떨어진다.



### 개선 예제

~~~java
public enum Ensemble {
    SOLO(1), DUET(2), TRIO(3), QUARTET(4), QUINTET(5),
    SEXTET(6), SEPTET(7), OCTET(8), DOUBLE_QUARTET(8),
    NONET(9), DECTET(10), TRIPLE_QUARTET(12);

    private final int numberOfMusicians;
    Ensemble(int size) { this.numberOfMusicians = size; }
    public int numberOfMusicians() { return numberOfMusicians; }

    public static void main(String[] args) {
        System.out.println(Ensemble.SOLO.numberOfMusicians());
    }
}
~~~

- 열거타입 상수에 연결된 값은 ordinal메서드로 얻지 말고, 인스턴스 필드에 저장하자.
- Enum의 API 문서를 보면 ordinal 에대해 대부분은 사용할 일이 없다고 되어있다.
- EnumSet, EnumMap 같이 열거타입 기반 범용 자료구조에 쓸 목적으로 설계되었기 때문에 이런 목적이 아니면 사용하면 안된다.



### [Enum API](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Enum.html)

> - #### ordinal
>
>   ```
>   public final int ordinal()
>   ```
>
>   Returns the ordinal of this enumeration constant (its position in its enum declaration, where the initial constant is assigned an ordinal of zero). Most programmers will have no use for this method. It is designed for use by sophisticated enum-based data structures, such as [`EnumSet`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/EnumSet.html) and [`EnumMap`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/EnumMap.html).
>
>   - **Returns:**
>
>     the ordinal of this enumeration constant



































