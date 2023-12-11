# item61 박싱된 기본 타입보다는 기본타입을 사용하라



## 기본타입과 박싱 차이점

### 1. 식별성 (identity)

박싱된 기본타입의 두 인스턴스는 값이 같아도 서로 다르다고 식별될 수 있다.

### 2. 박싱타입은 null을 가질 수 있다.

- 기본타입 값은 언제나 유효사지만 박싱된 기본타입은 null 을 가질 수 있다.

### 3. 메모리 효율

- 기본타입이 박싱된 기본타입보다 시간, 메모리 사용면에서 효율적이다.



### 오사용 예시(1)

- Boxing 상태의 인스턴스는 == 연산에 대해서만 객체 identity 비교를 하고
- `>, <` 은 값을 비교한다 (intValue())

~~~java
public class BrokenComparator {
    public static void main(String[] args) {

        /**
         * > < 의 경우, UnBoxing 후 값을 비교한다. (intValue() 가 호출되어 비교함)
         * == 의 경우, 객체 식별을 검사한다.
         */
        Comparator<Integer> naturalOrder =
                (i, j) -> (i < j) ? -1 : (i == j ? 0 : 1);


        /**
         * UnBoxking 후 primitive type으로 비교하면 생각한 대로 된다
         */
//        Comparator<Integer> naturalOrder = (iBoxed, jBoxed) -> {
//            int i = iBoxed, j = jBoxed;
//            return i < j ? -1 : (i == j ? 0 : 1);
//        };

       // new Integer() 보다는 캐싱을 사용하는 valueOf를 사용하자.
        int result = naturalOrder.compare(new Integer(42), new Integer(43));
        System.out.println(result);
    }
}
~~~



### 오사용 예시(2)

- 기본타입과 박싱된 기본타입을 혼용한 연산에서는 박싱된 기본타입의 박싱이 자동으로 풀린다.
- null 참조를 언박싱하면 NPE 발생.

```java
public class Unbelievable {
    static Integer i;

    public static void main(String[] args) {
        if (i == 42)
            System.out.println("Unbelievable");
    }
}
```



### 오사용 예시(3)

- 기본 타입과 박싱된 기본 타입을 혼용한 연산에서는 박싱된 기본 타입의 박싱이 자동으로 풀린다.
- 아래코드는 연산 할 때마다 Boxing, UnBoxing 이 일어나 낭비가 심하다.
  - Boxing : new Long(..);
  - UnBoxing : Long.longValue();

~~~java
    static Long sum = 0L;
    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        for (int i=0 ; i<Integer.MAX_VALUE ; i++) {
	            sum += i; // 박싱 + 언박싱
        }
        long end = System.currentTimeMillis();
        System.out.println((end-start)/1000);
        System.out.println(sum);
    }
~~~





## 박싱된 기본타입 사용 사례

### 1. 매개변수화 타입, 매개변수화 메서드의 타입 매개변수

- 자바는 타입매개변수로 기본타입을 지원하지 않기 때문.

### 2. 리플렉션

- 리플렉션을 통해 메서드를 호출할 때.





## 캐싱

- Boxing 타입은 어느정도의 개수가 이미 캐싱되어 있어서 객체 생성 비용을 아낄 수 있다.
  - 기본캐싱범위 : -128 ~ 127
  - VM 옵션으로 캐싱범위를 늘릴 수 있다.
    - VM option : -XX:AutoBoxCacheMax=size
    - Integer는 되는데 Long 은 안된다.

~~~java
public class IntegerCache {
    public static void main(String[] args) {

        /**
         * 객체 itentity 비교
         */
        Integer a0 = new Integer(127);
        Integer b0 = new Integer(127);
        System.out.println(a0 == b0);

        /**
         *  캐싱정보가 불려와
         *  객체 identity가 같다.
         */
        Integer b = Integer.valueOf(127);
        Integer a = Integer.valueOf(127);
        System.out.println(a == b);


        /**
         * 캐싱이 없어서 인스턴스가 생성되어 비교됨
         */
        Integer a2 = Integer.valueOf(128);
        Integer b2 = Integer.valueOf(128);
        System.out.println(a2 == b2);
    }
}
~~~





## 정리

기본타입과 박싱된 기본 타입중 하나를 선택하야 한다면 가능하면 기본타입을 사용하라.

기본타입은 간단하고 빠르다.

오토박싱은 박싱된 기본타입을 쉽게 사용하게 해주지만, 위험할 수 있다.

- 박싱된 기본타입을 == 으로 비교하면 의도하지 않게 객체비교가 될 것이다.
- 기본타입과 박싱된 기본타입을 혼용하면, 언박싱이 되는데, 이과정에서 NPE 가 발생할 수 있다.
- 박싱은 불필요한 객체생성의 부작용이 있다.