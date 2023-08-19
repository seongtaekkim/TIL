# Item06. Avoid creating unnecessary objects





## 문자열

- 똑같은 기능의 객체를 매번 생성하기보다는 객체 하나를 재사용하는 편이 나을 때가 많다.
- **불변 객체(아이템 17)**는 언제든 재사용할 수 있다.

~~~java
String s = new String("AEWf");
~~~

- 이 문장은 실행될 따마다 String 인스턴스를 새로 만든다.
- 생성자에 넘겨진 문자열 자체가 이 생성자로 만들어내려는 String과 기능적으로 완전히 똑같다.

~~~java
String s = "Wef"; // literal
~~~

- 이 코드는 새로운 인스턴스를 매번 만드는 대신 하나의 String 인스턴스를 사용한다.
- 나아가 이 방식을 사용한다면 같은 가상머신 안에서 이와 똑같은 문자열 리터럴을 사용하는 모든 코드가 같은 객체를 재사용함이 보장된다.(JLS, 3.10.5)
- [item02 - immutable 참고](../item02/item02-immutable-string.md)



### 생성자 대신 정적팩터리 메서드(아이템 1)를 제공하는 불변 클래스에서는 정적 팩터리 메서드를 사용해 불필요한 객체 생성을 피할 수 있다.

- 예컨대 Boolean(String) 생성자 대신 Boolean.valueOf(String) 팩터리 메서드를 사용하는 것이 좋다. 
  (그래서 이생성자는 자바9에서 **사용 자제(deprecated) API로 지정되었다.)**

~~~java
Boolean b = new Boolean(false);
~~~

~~~java
@Deprecated(since="9", forRemoval = true)
public Boolean(boolean value) {
    this.value = value;
}
~~~



## 정규식

### 생성비용이 아주 비싼 객체도 더러 있다. 이런 비싼객체가 반복해서 필요하다면 캐싱해서 재사용하길 권한다.

- 안타깝게도 자신이 만드는 객체가 비싼 객체인지를 매번 명확히 알 수는 없다.

- 예를들어 주어진 문자열이 유효한 로마 숫자인지를 확인하는 메서드를 작성한다고 해보자.
  ~~~java
      static boolean isRomanNumeralSlow(String s) {
          return s.matches("^(?=.)M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");
      }
  ~~~

  - 이방식의 문제는 String.matches 메서드를 사용한다는데 있다.
  - String.matches는 정규표현식으로 문자열 형태를 확인하는 가장 쉬운방법이지만, 성능이 중요한 상황에서 반복해 사용하기에 적합치 않다.





**이 메서드는 Pattens 인스턴스를 한번쓰고 버려서 곧바로 GC 대상이 된다.** [GC test](./item06-gc.md)

~~~java
public static boolean matches(String regex, CharSequence input) {
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(input);
    return m.matches();
}
~~~

- Pattern 객체는 메서드 scope 을 벗어나 GC 대상이 된다.



성능을 개선하려면 필요한 정규 표현식을 표현하는 (불변인) Pattern 인스턴스를 클래스 초기화(정적초기화) 과정에서 직접생성해 캐싱해두고, 나중에 isRomanNumeral 메서드가 호출될 때마다 이 인스턴스를 재사용한다.

~~~java
private static final Pattern ROMAN = Pattern.compile(
        "^(?=.)M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$");

static boolean isRomanNumeralFast(String s) {
    return ROMAN.matcher(s).matches();
}
~~~

- 이렇게 개선하면 isRomanNumeral이 빈번히 호출되는 상황에서 성능을 상당히 끌어올릴 수 있다.
- isRomanNumeral메서드가 처음 호출될 때 필드를 초기화하려는 지연초기화 (**lazy initialization, 아이템83**)로 불필요한 초기화를 없앨 수는 있지만, 권하지 않는다.
- 지연초기화는 코드를 복잡하게 만드는데, 성능은 크게 개선되지 않을때가 많기 때문이다.(**아이템 67**)





## 오토박싱

- 불필요한 객체를 만들어내는 또 다른 예로 오토박싱을 들 수 있다.
- 오토박싱은 프로그래머가 기본타입과 박싱된 기본타입을 섞어 쓸 때 자동으로 상호변환해주는 기술이다.
- 오토박싱은 기본타입과 그에 대응하는 박싱된 기본 타입의 구분을 흐려주지만, 완전히 없애주는 것은 아니다.
- 의미상으로는 별다를 것 없지만 성능에서는 그렇지 않다.(**아이템 61**).

**박싱된 기본 타입보다는 기본타입을 사용하고, 의도치 않은 오토박싱이 숨어들지 않도록 주의하자.**

~~~java
private static long sum() {
    //long sum = 0L;
  	Long sum = 0L;
    for (long i = 0; i <= Integer.MAX_VALUE; i++)
        sum += i;
    return sum;
}

public static void main(String[] args) {
    long start = System.nanoTime();
    long x = sum();
    long end = System.nanoTime();
    System.out.println((end - start) / 1_000_000. + " ms.");
    System.out.println(x);
}
~~~







## 이번 아이템을 객체생성은 비싸니 피해야 한다. 로 오해하면 안된다. 

특히나 요즘의 JVM 에서는 별다른 일을 하지 않는 작은 객체를 생성하고 회수하는 일이 크게 부담되지 않는다.
프로그램의 명확성,간결성, 기능을 위해서 객체를 추가로 생성하는 것이라면 일반적으로 좋은 일이다.
거꾸로, 아주 무거운 객체가 아닌 다음에야 단순히 객체 생성을 피하고자 여러분만의 객체 풀을 만들지는 말자.
물론 객체 풀을 만드는게 나은 예가 있긴하다.
**데이터베이스 연결 같은 경우 생성비용이 워낙 비싸니 재사용하는 편이 낫다.**
하지만 일반적으로는 자체 객체풀은 코드를 헷갈리게 만들고 메모리사용량을 늘리고 성능을 떨어뜨린다.
**요즘 JVM의 GC는 상당히 잘 최적화되어서 가벼운 객체용을 다룰 때는 직접 만든 객체풀보다 훨씬 빠르다.**





[Item 50](../)

이번 아이템은 **방어적복사 (defensive copy)를 다루는 아이템50**과 대조적이다. 
이번 아이템이 **기존 객체를 재사용해야 한다면 새로운 객체를 만들지 마라**. 라면, 
**아이템 50은 새로운 객체를 만들어야 한다면, 기존 객체를 재사용하지 마**라 이다.
방어적 복사가 필요한 상황에서 객체를 재사용 했을 때의 피해가, 필요없는 객체를 반복생성했을 때의 피해보다 훨씬 크다는 사실을 기억하자.
방어적 복사에 실패하면 언제 터져 나올지 모르는 버그와 보안 구멍으로 이어지지만, 불필요한 객체 생성은 그저 코드 형태와 성능에만 영향을 준다.















































