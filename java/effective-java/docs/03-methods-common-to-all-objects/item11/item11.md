# Item 11. Always override hashCode when you override equals



## synchronization

- equals를 재정의한 클래스 모두에서 hashcode도 재정의해야한다.
- 그렇지 않으면 hashCode 일반 규약을 어기게 되어 해당 클래스의 인스턴스를 hashmap 이나 hashset같은 컬렉션의 원소로 사용할 때 문제를 일으킬 것이다.



### hashcode 규약

- equals 비교에 사용되는 정보가 변경되지 않았다면, 앱이 실행되는 동안 그 객체의 hashcode 메서드는 몇번을 호출해도 일관되게 항상 같은 값을 반환해야 한다. 단, 앱을 다시 실행하면 이 값이 달라져도 상관없다.
- equals(object)가 두 객체를 같다고 판단했다면 두 객체의 hashcode는 똑같은 값을 반환해야 한다.
- equals(object)가 두 객체를 다르다고 판단햇더라도 두 객체의 hashcode 가 서로 다른 값을 반환할 필요는 없다.
  단, 다른 객체에 대해서는 다른 값을 반환해야 해시테이블의 성능이 좋아진다.



**hashcode 재 정의를 잘못했을 때 크게 문제가 되는 조항은 두번째다.**
**즉 논리적으로 같은 객체는 같은 해시코드를 반환해야 한다**
아이템10에서 보았듯이 equals는 물리적으로 같은 두 객체를 논리적으로 같다고 할 수 있다. 하지만 object의 기본 hashcode메서드는 이 둘이 전혀 다르다고 판단하여, 규약과 달리 무작위처럼 보이는 서로 다른 값을 반환한다.

예를 들어 아이템10의 PhoneNumber클래스의 인스턴스는 hashmap의 원소로 사용한다고 해보자.
Map<PhoneNumber, String> m  = new HashMap<>();
m.put(new PhoneNumber(707,867,5309), "제니");
이 코드에 다음에 m.get(new PhoneNumber(707,867,5309),"제니"); 를 실행하면 "제니" 가 나와야 할 것 같지만, 실제로는 null을 리턴한다.
여기에 2개의 PhoneNumber인스턴스가 사용되었다.
하나는 HashMap에 "제니"를 넣을 때 사용됐고, 논리적 동치인 두번 째는 이를 꺼내려 할 대 사용됐다.
PhoneNumber클래스는 hashcode를 재정의 하지 않았기 때문에 논리적 동치인 두 객체가 서로 다른 해시코드를 반환햐여 두번째 규약을 지키지 못한다.

그 결과 get메서드는 엉뚱한 해시버킷에 가서 객체를 찾으려 한 것이다. 설사 두 인스턴스를 같은 버킷에 담았더라도 get 메서드는 여전히 null을 반환하는데, hashmap은 해시코드가 달느 엔트리끼리는 동치성 비교를 시도조차 하지 않도록 최적화되어 있기 때문이다.



### hash를 못찾는 HashMap

- PhoneNumber class 는 equals 를 재정의했지만 hashcode 를 재정의하지 않았다.
- HashMap에 값이 같은 PhoneNumber인스턴스를 여러개 생성하고 put 하였다.
- 이때, 같은 값의 PhoneNumber 인스턴스를 get 하려고 한다면 버킷을 찾지못해 null을 리턴한다.

~~~java
public static void main(String[] args) {
    Map<PhoneNumber, String> map = new HashMap<>();

    PhoneNumber number1 = new PhoneNumber(123, 456, 7890);
    PhoneNumber number2 = new PhoneNumber(123, 456, 7890);

    System.out.println(number1.equals(number2));
    System.out.println(number1.hashCode());
    System.out.println(number2.hashCode());

    map.put(number1, "spring");
    map.put(number2, "java");

    String s1 = map.get(number2);
    System.out.println(s1);
    String s2 = map.get(new PhoneNumber(123, 456, 7890));
    System.out.println(s2);
}
~~~

~~~sh
# result
false
1915318863
1283928880
java
null
~~~







### 무조건 같은 해시값을 리턴해도 될까?

~~~java
@Override
public int hashCode() {
    return 42;
}
~~~

- 이 코드는 동치인 모든 객체에서 똑같은 해시코드를 반환하니 적법하다.
- 하지만 끔찍하게도 모든 객체에게 똑같은 값만 리턴하므로 모든 객체가 해시테이블의 버킷 하나에 담겨 마치 연결리스트 처럼 동작한다.
- 그 결과 평균수행시간이 o(1) -> o(n) 으로 느려진다.

~~~java
  int LIMIT = Integer.MAX_VALUE / 10000;
  System.out.println("개수: " + LIMIT);

  HashMap<PhoneNumber, Integer> hashMap = new HashMap<>(LIMIT * 2 );
  long beforeTime = System.currentTimeMillis();
  for (int i = 0; i < LIMIT ; i++ ) {
      hashMap.put(new PhoneNumber(i, 456, 7890), i);
  }
  long afterTime = System.currentTimeMillis();
  long secDiffTime = (afterTime - beforeTime);
  System.out.println("생성시간 : "+secDiffTime);


  beforeTime = System.currentTimeMillis();

  Integer i = hashMap.get(new PhoneNumber(100, 456, 7890));
  System.out.println("데이터: " + i);

  afterTime = System.currentTimeMillis();
  secDiffTime = (afterTime - beforeTime);
  System.out.println("조회시간 : "+secDiffTime);
~~~

~~~sh
# result
개수: 214748
생성시간 : 155228
데이터: 196708
조회시간 : 5
~~~







좋은 해시함수는 서로 다른 인스턴스에 대해 다른 해시코드를 반환한다.
이것이 바로 hashcode세번째 규약이 요구하는 속성이다.
이상적인 해시함수는 주어진 인스턴스들을 32비트 정수범위에 균일하게 분배해야 한다.
이상을 완벽히 실현하기는 어렵지만 비슷하게는 된다.

1. int 변수 result를 선언 후 값을 c로 초기화 한다. 이 때 c는 해당 객체의 첫번째 핵심필드를 단계 2.a방식으로 계산한 해시코드다 (핵심필드 : equals에서 사용한 필드)

2. 해당 객체의 나머지 핵심필드 f각각에 대해 다음 작업을 수행한다.

   a. 해당필드의 해시코드 c를 계산한다.

   ​	i.  기본타입필드라면 Type.hashcode(f)를 수행한다. 여기서 Type은 해당 기본타입의 박싱클래스다.
   ​	ii. 참조타입필드이면서 이 클래스의 equals 메서드가 이 필드의 equals 를 재귀적으로 호출해 비교한다면, 이 필드의 hashcode를 재귀적으로 호출한다. 계산이 더 복잡해질 것 같으면, 이 필드의 표준형(canonical representation)을 만들어 그 표준형의 hashcode를 호출한다. 필드의 값이 null이면 0을 사용한다. (다른 상수도 괜찮지만 전통적으로 0사용)

   ​	iii. 필드가 배열이라면, 핵심원소 각각을 별도필드처럼 다룬다. 이상의 규칙을 재귀적으로 적용해 각 핵심원소의 해시코드를 계산한 다음, 단계 2.b 방식으로 갱신한다. 배열에 핵심원소가 하나도 없다면 단순히 상수(0)르 ㄹ사용한다. 모든 원소가 핵심원소라면 Arrays.hashcode를 사용한다.

   b. 단계 2.a에서 계산한 해시코드 c로 result를 갱신한다. 코드로는 다음과 같다. 

   ~~~
   result = 31 * result + c;
   ~~~

3. result 반환.

hashcode를 다 구현했다면, 이 메서드가 동치인 인스턴스에 대해 똑같은 해시코드를 반환할지 자문해 보자. 그리고 여러분의 직관을 검증할 단위테스트를 작성하자. (equals, hashcode 메서드를 authvalue로 생성했다면 안해도 됨.)
동치인 인스턴스가 서로 다른 해시코드를 반환한다면 해결하자.

파생필드는 해시코드 계산에서 제외해도 된다.
즉, 다른 필드로부터 계산해 낼 수 있는 필드는 모두 무시해도 된다. 또
한 equals비교에 사용되지 않은 필드는 반드시 제외해야 한다.
그렇지 않으면 hashcode규약 두 번째를 어기게 될 위험이 있다



## hashcode 사용요령

### hashcode 기본 작성요령

~~~java
  @Override public int hashCode() {
        int result = Short.hashCode(areaCode); // 1
        result = 31 * result + Short.hashCode(prefix); // 2
        result = 31 * result + Short.hashCode(lineNum); // 3
        return result;
    }
~~~

### Intellij 자동완성

- 속도가 상대적으로 살짝 느리다고 한다.
- 입력인수를 담기 위한 배열이 만들어지고, 입력 중 기본타입이 있다면 박싱과 언박싱도 거쳐야 하기 때문이다.
- 성능에 민감하지 않은 상황에서 사용하기 좋은 메서드이다.

~~~java
@Override
public int hashCode() {
    return Objects.hash(areaCode, prefix, lineNum);
}
~~~

~~~java
public static int hash(Object... values) {
    return Arrays.hashCode(values);
}
~~~

~~~java
public static int hashCode(Object a[]) {
    if (a == null)
        return 0;

    int result = 1;

    for (Object element : a)
        result = 31 * result + (element == null ? 0 : element.hashCode());

    return result;
}
~~~

### guava

- 성능이 좀더 좋다고 하는 guava 코드이다.
- 의존성도 추가해야 하고,  뭔소린지 어렵긴 하다.

~~~xml
<!-- pom.xml -->
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>31.1-jre</version>
</dependency>
~~~

~~~java
public final int hashCode() {
    if (this.bits() >= 32) {
        return this.asInt();
    } else {
        byte[] bytes = this.getBytesInternal();
        int val = bytes[0] & 255;

        for(int i = 1; i < bytes.length; ++i) {
            val |= (bytes[i] & 255) << i * 8;
        }

        return val;
    }
}
~~~



### 불변객체

- 처음 객체를 생성할 때 hashcode 를 만들어 놓는다. 
  - 어차피 변경되지 않는 객체이므로 다시 계산하지 않아도 된다.
- hashcode를 항상사용하는게 아니라면, 지연초기화를 고려해 보자.
- 필드를 지연초기화하려면 그 클래스를 스레드안전하게 만들도록 신경써야 한다. (아이템83)
  - 여러 스레드가 동시에 접근할 수 있으니, double checked locking 하여 동시성을 고려하면 좋다.

~~~java
private volatile int hashCode;

@Override public int hashCode() {
    if (this.hashCode != 0) {
        return hashCode;
    }

    synchronized (this) {
        int result = hashCode;
        if (result == 0) {
            result = Short.hashCode(areaCode);
            result = 31 * result + Short.hashCode(prefix);
            result = 31 * result + Short.hashCode(lineNum);
            this.hashCode = result;
        }
        return result;
    }
}
~~~



### @EqualsAndHashCode

- Lombok 에서 equals와 hashcode 를 자동으로 만들어줄 수 있다.

- Lombok이 내부적으로 테스트 한 결과이니 두개 함수에 대해 따로 테스트를 하지 않아도 된다.

~~~java
// PhoneNumber.class file
public boolean equals(final Object o) {
    if (o == this) {
        return true;
    } else if (!(o instanceof PhoneNumber)) {
        return false;
    } else {
        PhoneNumber other = (PhoneNumber)o;
        if (this.areaCode != other.areaCode) {
            return false;
        } else if (this.prefix != other.prefix) {
            return false;
        } else {
            return this.lineNum == other.lineNum;
        }
    }
}

public int hashCode() {
    int PRIME = true;
    int result = 1;
    result = result * 59 + this.areaCode;
    result = result * 59 + this.prefix;
    result = result * 59 + this.lineNum;
    return result;
}
~~~





### 성능을 높인답시고 해시코드를 계산할 때 핵심필드를 생략해서는 안된다.

속도야 빨라지겠지만, 해시품질이 나빠져 해시테이블의 성능을 심각하게 떨어뜨릴 수 있다.
특히 어떤 필드는 특정 영역에 몰린 인스턴스들의 해시코드를 넓은 범위로 고르게 퍼뜨려주는 효과가 있을지도 모른다.
하필 이런필드를 생략한다면 해당 영역의 수많은 인스턴스가 단 몇개의 해시코드로 집중되어 해시테이블의 속도가 선형으로 느려질 것이다.
이 문제는 단지 이론에 그치지 않는다. 실제 자바 2 전의 String은 최대 16개 문자만 뽑아내 사용한 것이다.
URL처럼 계층적인 이름을 대략으로 사용한다면 이런 해시함수는 앞서 이야기한 심각한 문제를 고스란히 드러낸다.



### hashCode가 반환하는 값의 생성규칙을 API사용자에게 자세히 공표하지 말자. 

그래야 클라이언트가 이 값에 의지하지 않게되고, 추후에 계산방식을 바꿀수도 있다.
String, Integer를 포함해, 자바 라이브러리의 많은 클래스에서 hashcode메서드가 반환하는 정확한 값을 알려준다.
바람직하지 않은 실수지만 바로잡기에는 이미 늦었다.
향후 릴리스에서 해시기능을 개선할 여지도 없애버렸다. 자세한 규칙을 공표하지 않는다면, 해시기능에서 결함을 발견했거나 더 나은 해시 방식을 알아낸 경우 다음 릴리스에서 수정할 수 있다.



## 정리

- equals를 재정의할때는 hashcode도 반드시 재정의해야 한다.
- 그렇지 않으면 프로그램이 제대로 동작하지 않을 것이다.
- 재정의한 hashcode는 object의 API 문서에 기술된 일반 규약을 따라야 하며, 서로 다른 인스턴스라면 되도록 해시코드도 서로 다르게 구현해야 한다.
- 이렇게 구현하기가 어렵지는 않지만 조금 따분한 일이긴 하다 (68쪽 참고)
- 하지만 아이템10에서 이야기한 AutoValue 프레임워크를 사용하면 멋진 equals와 hashcode를 자동으로 만들어준다.
  혹은 intellij 도 만들어 준다.

























