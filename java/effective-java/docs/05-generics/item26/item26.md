# Item26: raw type을 사용하지 말라

- 매개변수화 타입을 사용해라!

> 1. 런타임이 아닌 컴파일 타임에 문제를 찾을 수 있다. (안정성)
> 2. 제네릭을 활용하면 이 정보가 주석이 아닌 타입 선언 자체에 녹아든다. (표현력)
> 3. raw type을 사용하면 안정성과 표현력을 잃는다.
> 4. 그렇다면 자바는 로 타입을 왜 지원하는가?
> 5. List, List<Object> 차이점
> 6. Set, Set<?> 차이점
> 7. 예외: class리터럴과 instanceof 연산자





## rawtype 문제점, 제네릭을 사용해야 하는 이유

클래스와 인터페이스 선언에 타입 매개변수가 쓰이면, 이를 제네릭 클래스 혹은 제네릭 인터페이스라 한다.
List는 원소 타입을 나타내는 타입 매개변수 E 를 받는다. (List<E>)
**제네릭 클래스와 제네릭 인터페이스를 통틀어 제네릭 타입 이라 한다.**

List<String> 은 원소의 타입이 String 인 리스트를 뜻하는 매개변수화 타입이다.
여기서 String 이 정규타입 매개변수 E에 해당하는 실제타입 매개변수 이다.
제네릭 타입을 하나 정의하면 그에 딸린 raw type도 함께 정의된다.
raw type이란 제네릭타입에서 타입매개변수를 전혀 사용하지 않을 때를 말한다. (List<E> 의 raw type 은 List)
raw type은 타입선언에서 제네릭 타입정보가 전부 지워진것 처럼 동작하는데, 제네릭이 도래하기 전 코드와 호환되도록 하기위한 궁여지책이다.

제네릭을 지원하기전에는 컬랙션을 다음과 같이 선언했다.

```java
private final Collection stamps = ...;
```



이 코드를 사용하면 실수로 Stamp 대신 Coin 을 넣어도 compile된다.

~~~java
stamp.add(new Coin(...));
~~~



```java
for (Iterator i = stamps.iterator(); i.hasNext();) {
  Stamp stamp = (Stamp) i.next(); // ClassCastException
  stamp.cancel();
}
```

오류는 가능한 한 발생 즉시(fail fast), 이상적으로는 컴파일할 때 발견하는 것이 좋다.
이 예에서는 런타임에 오류를 알아챌 수 있다.

제네릭은 컴파일시점에 오류를 발견 할 수 있다.

~~~java
private final Collection<Stamp> stampes = ...;
stamp.add(new Coin(...)); // complile error!
~~~



컴파일러는 컬렉션에서 원소를 꺼낼 때 형변환을 하여 절대 실패하지 않음을 보장한다.

로타입을 쓰면 제네릭의 장점을 모두 읽데 되므로 사용하지 말자.

- 쓰면 안되는 로타입이 있는 이유는 호환성 때문이다.
- 제네릭이 없을 시절에 작성된 코드와의 호환성을 위해..
- 이를 위 해 로타입을 지원하고 제네릭 구현에는 소거(아이템 28)을 사용한다.



이 코드는 컴파일은 되지만 로타입인 List를 사용하여 다음과 같은 경고가 발생한다.

~~~java
RawtypeTest2.java:24: warning: [unchecked] unchecked call to add(E) as a member of the raw type List
        list.add(o);
                ^
  where E is a type-variable:
    E extends Object declared in interface List
1 warning
~~~



- 데이터를 꺼낼 때 Integer 를 String으로 캐스팅 에러가 발생한다.

~~~sh
Exception in thread "main" java.lang.ClassCastException: class java.lang.Integer cannot be cast to class java.lang.String (java.lang.Integer and java.lang.String are in module java.base of loader 'bootstrap')
	at me.staek.chapter05.item26.RawtypeTest2.main(RawtypeTest2.java:19)
~~~

- List<Object>로 변경하면 컴파일 오류가 발생한다. ??? 왜 ?? List<String> 으로 바까야지 





## 비한정적 와일드 카드 타입은 Set<?>

이쯤되면 로타입을 쓰고 싶을 수 있다.

~~~java
// 안정적이지 않음
static int numElementsInCommon(Set s1, Set s2) {
    int result = 0;
    for (Object o1 : s1) {
        if (s2.contains(o1)) {
            result++;
        }
    }
    return result;
}
~~~



- **이것이 어떤 타입이라도 담을 수 있는 가장 범용적인 매개변수화 Set타입이다.** : Set<?>

~~~java
static int numElementsInCommon(Set<?> s1, Set<?> s2) {
    int result = 0;
    for (Object o1 : s1) {
        if (s2.contains(o1)) {
            result++;
        }
    }
    return result;
}
~~~

- 와일드카드 타입은 안전하고 로타입은 그렇지 않다.
- 로 타입 컬렉션에는 아무 원소나 넣을 수 있는 타입 불변식을 훼손하기 쉽다.
- 반면 Collection<?>에는 어떤 원소도 넣을수 없다, (null 은 가능)
- 다른원소로 시도하면 compile error가 발생한다.
- -> 컬렉션의 타입불변식을 훼손 못하게 막았다. (.add 불가능)
  - 구체적으로는 null이외의 어떤 원소도 Collection<?>에 넣지 못하게 했으며 컬렉션에서 꺼낼 수 있는 객체의 타입도 전혀 알 수 없게 했다.
- **이러한 제약을 받아드릴 수 없다면 제네릭 메서드 (아이템 30)이나 한정적 와일드카드 타입(아이템 31)을 사용하면 된다.**







## rawtype을 사용하는 경우

### 예외1. class 리터럴에는 로타입을 써야 한다.
자바 명세에 의하면  class리터럴에 매개변수화 타입을 사용 하지 못한다. (배열과 기본 타입은 허용한다) [JLS, 15.8.2]

~~~java
System.out.println(UseRawTypeCase<String>.class); // compile error
~~~

~~~java
System.out.println(UseRawTypeCase.class);
~~~



### 예외2. instanceof

런타임에는 제네릭 타입 정보가 지워지므로 instanceof 연산자는 비한정적 와일드카드 타입 이외의 매개변수화 타입에는 적용할 수 없다.

그리고 로 타입이든 비한정적 와일드 타입이든 instanceof는 완전히 똑같이 동작한다.
비한정적 와일드카드 타입의 꺾쇠괄호와 물음표는 아무런 역할 없이 코드만 지저분하게 만드므로,
차라리 로타입을쓰는 편이 깔끔하다.

~~~java
if (o instanceof Set) {
	Set<?> s = (Set<?>)o; 
	..
}
~~~

- o의 타입이 Set임을 확인한 다음 와일드카드 타입인 Set<?>로 형변환 해야 한다.
  (로 타입인 Set이 아니다)
  이는 checked cast이므로 컴파일러 경고가 뜨지 않는다.



## Generic Dao

- 코드참조 effective-java > me.staek.chapter05.item26.genericdao





## 정리

rawtype을 사용하면 런타임에 예외가 일어날 수 있으니 사용하면 안 된다. 
rawtype은 제네릭이 도입되기 이전코드와의 호환성을 제공될 뿐이다.
빠르게 훑어보자면, Set<Object>는 어떤 타입의 객체도 저장할 수 있는 매개변수화 타입이고, Set<?>는 모종의 타입객체만 지정할 수 있는 와일드카드 타입이다.
그리고 이들의rawtype Set은 제네릭 타입 시스템에 속하지 않는다.
Set<<Object>와 Set>?>는 안전하지만, rawtype인 Set은 안전하지 않다.



| 한글용어                 | 영문용어               | 예                               | 아이템        |
| ------------------------ | ---------------------- | -------------------------------- | ------------- |
| 매개변수화 타입          | parameerized type      | List<String>                     | 아이템 26     |
| 실제타입 매개변수        | actual type parameter  | String                           | 아이템 26     |
| 제네릭 타입              | generic type           | List<E>                          | 아이템 26, 29 |
| 정규타입 매개변수        | formal type parameter  | E                                | 아이템 26     |
| 비한정적 와일드카드 타입 | unbouned wildcard type | List<?>                          | 아이템 26     |
| 로 타입                  | raw type               | List                             | 아이템 26     |
| 한정적 타입 매개변수     | bounded type parameter | <E extends Number>               | 아이템 29     |
| 재귀적 타입 한정         | recursive tye bound    | <T extends Comparable<T>>        | 아이템 30     |
| 한정적 와일드 카드 타입  | bounded wildcard type  | List<? extends Number>           | 아이템 31     |
| 제네릭 메서드            | generic method         | static <E> List<E> asList(E[] a) | 아이템 30     |
| 타입 토큰                | type token             | String.class                     | 아이템 33     |







































































