# Item17. Minimize mutability



불변클래스란 간단히 말해 그 인스턴스의 내부 값을 수정할 수 없는 클래스다.
불변 인스턴스에 간직된 정보는 고정되어 객체가 파괴되는 순간까지 절대 달라지지 않는다.
자바 플랫폼 라이브러리에도 다양한 불변 클래스가 있따.
String, 기본타입의 박싱된 클래스들, BigInteger, BigDecimal이 여기 속한다.

이런 클래스들을 블변으로 설계한 데는 그럴만한 이유가 있다. 불변 클래스는 가변 클래스보다 설계하고 구현하고 사용하기 쉬우며, 오류가 생길여지가 적고 훨씬 안전하다.

**규칙 5가지** (Rule.java)

- **객체의 상태를 변경하는 메서드(변경자)를 제공하지 않는다.**
- **클래스를 확장할 수 없도록 한다**. 하위 클래스에서 부주의하게 혹은 나쁜의도로 객체의 상태를 변하게 만드는 사태를 막아준다. 상속을 막는 대표적인 방법은 클래스를 final로 선언하는 것이지만, 다른 방법도 뒤에 살펴볼것이다.
- **모든 필드를 final로 선언한다.** 시스템이 강제하는 수단을 이욯애 설계자의 의도를 명확히 드러내는 방법이다.
  새로 생성된 인스턴스를 동기화 없이 다른 스레드로 건네도 문제없이 동작하게끔 보장하는데도 필요하다.
- **모든 필드를 private 으로 선언한다.** 필드가 참조하는 가변객체를 클라이언트에서 직접 접근해 수정하는 일을 막아준다. 기술적으로는 기본타입필드나 불변 객체를 참조한느 필드를 public final로만 선언해도 불변 객체가 되지만, 이렇게 하면 다음 릴리스에서내부 표현을 바꾸지 못하므로 권하지는 않는다(**아이템 15**, **아이템 16**)
- **자신 외에는 내부의 가변 컴포넌트에 접근할 수 없도록 한다.** 클래스에 가변 객체를 참조하는 필드가 하나라도 있다면 클라이언트에서 그 객체의 참조를 얻을 수 없도록 해야한다.
  이런 필드는 절대 클라이언트가 제공한 객체참조를 가리키게 해서는 안되며, 접근자 메서드가 그 필드를 그대로 반환해서도 안된다. 생성자, 접근자, readObject 메서드 (**아이템 88**) 모두에서 방어적 복사를 수행해라.

~~~java
/**
 * TODO 불변객체규칙 Person class
 *      1. 객체의 상태를 변경하는 메서드(변경자)를 제공하지 않는다. - setter 없음
 *      2. 클래스를 확장할 수 없도록 한다 - final class
 *      3. 모든 필드를 final로 선언한다.
 *      4. 모든 필드를 private 으로 선언한다.
 *      5. 자신 외에는 내부의 가변 컴포넌트에 접근할 수 없도록 한다. - 가변객체 Companay를 접근할 때 복사하여 전달하면, 변경에 안전하다.
 */
public class Rule {
    public static void main(String[] args) {
        Company c = new Company();
        c.setName("naver");
        c.setLocation("seoul");
        Person p = new Person("kim", c);
        System.out.println(p);
        p.getCompany().setName("kakao");

        System.out.println(p);
    }
}

final class Person {

    private final String name;
    private final Company company;
    public Person(String name, Company company) {
        this.name = name;
        this.company = company;
    }

    public Company getCompany() {
        Company c = new Company();
        c.setName(c.getName());
        c.setLocation(c.getLocation());
        return c;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", company=" + company +
                '}';
    }
}

class Company {
    private String name;
    private String location;


    public String getName() {
        return name;
    }


    public String getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
~~~





### 불변 복소수 클래스 (Complex.java)

~~~java
/**
 * 불변객체의 장점
 * - 함수형 프로그래밍에 적합하다. (피연산자에 함수를 적용한 결과를 반혼하지만 피연산자가 바뀌지는 않는다.)
 *   ->>> plus 함수가 객체를 생성리턴하는게 아닌, 기존변수에 더하는 식으로 진행된다면, 함수형 프로그래밍이 아니다.
 * - 불변객체는 단순한다.
 *   ->>> 가변객체에 비해 고려해야할 사항이 줄어듬
 * - 불변객체는 근본적으로 스레드 안전하여 따로 동기화할 필요가 없다.
 *   ->>> 여러 스레드에서 하나의 인스턴스 필드를 변경할 때 사용에 대한 우려사항이 없다.
 * - 불변객체는 안심하고 공유할 수 있다. (public static final)
 *   ->>> 스레드간, 함수간 재사용 및 공유가 가능함. final이 레퍼런스룰 참조하더라도 setter가 없으니 안전하다.
 * - 불변객체끼리는 내부데이터를 공유할 수 있다.
 *   ->>> BigIngeterEx.java 참조
 * - 객체를 만들 때 불변객체로 구성하면 이점이 많다.
 * - 실패원자성을 제공한다. (아이템 76, p407)
 *   ->>> 어떤 작업을 진행하다가 예외 등이 발생해 실패해도, 내부에 어떤 값도 변경되지 않음이 보장된다.
 * - 객체생성에 여러단계 필요: 다단계연산
 *   ->>> plus, minus, divideby 를 모두 샐행해야 한다면 현재로서는 인스턴스가 세개 만들어진다.
 *   ->>> 이 세개를 합쳐서 하나의 인스턴스가 나오도록 하면 된다.
 *   ->>> 혹은 가변동반클래스를 이용한다. (string -> StringBuilder)
 */
public final class Complex {
    private final double re;
    private final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double realPart()      { return re; }
    public double imaginaryPart() { return im; }

    public Complex plus(Complex c) {
        return new Complex(re + c.re, im + c.im);
    }

    public Complex minus(Complex c) {
        return new Complex(re - c.re, im - c.im);
    }

    public Complex times(Complex c) {
        return new Complex(re * c.re - im * c.im,
                re * c.im + im * c.re);
    }
    
    public Complex dividedBy(Complex c) {
        double tmp = c.re * c.re + c.im * c.im;
        return new Complex((re * c.re + im * c.im) / tmp,
                (im * c.re - re * c.im) / tmp);
    }
    
    @Override public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Complex))
            return false;
        Complex c = (Complex) o;

        // == 대신 compare를 사용하는 이유는 63쪽을 확인하라.
        return Double.compare(c.re, re) == 0
                && Double.compare(c.im, im) == 0;
     }
    
     @Override public int hashCode() {
        return 31 * Double.hashCode(re) + Double.hashCode(im);
     }

     @Override public String toString() {
        return "(" + re + " + " + im + "i)";
   }
~~~

- **이처럼 피 연산자에 함수를 적용해 그 결과를 반환하지만, 피연산자 자체는 그대로인 프로그래밍 패턴을 함수형 프로그래밍이라 한다.** 
  - plus(), minus() ..
  - 메서드이름 **전치사** (객체의 값을 변경하지 않는다는 사실을 강조)
  - BigDecimal 참조
- 명령형 프로그래밍
  - 피연산자인 자신을 수정해 상태가 변한다.
  - 메서드 이름 **동사**




### **불변객체는 근본적으로 스레드 안전하여 따로 동기화할 필요 없다.**

- 여러 쓰레드가 동시에 사용해도 절대 훼손되지 않는다.
- 사실 클래스를 스레드 안전하게 만드는 가장 쉬운 방법이기도 하다.
- 불변객체에 대해서는 그 어떤 스레드도 다른 스레드에 영향을 줄 수 없으니 **불변객체는 안심하고 공유할 수 있다.**

따라서 불변클래스라면 한번 만든 인스턴스를 최대한 재활용하기를 권한다.
가장 쉬운 재활용 방법은 자주 쓰이는 값들을 상수로 제공하는 것이다. 예컨대 Complex 클래스는 다음 상수들을 제공할 수 있다.

~~~java
public static final Complex ZERO = new Complex(0, 0);
public static final Complex ONE  = new Complex(1, 0);
public static final Complex I    = new Complex(0, 1);
~~~



### 불변 클래스는 자주 사용되는 인스턴스를 캐싱하여 정적팩터리(아이템1) 제공

- 박싱된 기본 타입 클래스 전체와 BigInteger.
- 정적팩터리를 구현해놓으면 클라이언트를 수정하지 않고도 필요에 따라 캐시 기능을 나중에 덧붙일 수 있다.
- 방어적복사(아이템50)도 필요 없음
- clone 메서드나 복사생성자(아이템 13) 제공 불필요



**불변객체는 자유롭게 공유할 수 있음을 물론, 불변 객체끼리는 내부 데이터를 공유할 수 있다.**
예컨대 BigInteger 클래스는 내부에서 값의 부호와 크기를 따로 표현한다.
부호에는 int 변수를, 크기(절대값)에는 int 배열을 사용하는 것이다.
한편 negate 메서드는 크기가 같고 부호만 반대인 새로운 BigInteger를 생성하는데, 이 때 배열은 비록 가변이지만 복사하지 않고 원본 인스턴스와 공유해도 된다.
그 결과 새로 만든 BigInteger 인스턴스도 원본 인스턴스가 가리키는 내부 배열을 그대로 가리킨다.

**객체를 만들 때 다른 불변 객체들을 구성요소로 사용하면 이점이 많다.**
값이 바뀌지 않는 구성요소들로 이뤄진 객체라면 그 구조가 아무리 복잡하더라도 불변식을 유지하기 훨씬 수월하기 때만이다.
좋은 예로, 불변 객체는 맵의 키와 집합의 원소로 쓰기에 안성맞춤이다.
맵이나 집합은 안에 담긴 값이 바뀌면 불변식이 허물어지는데, 불변객체를 사용하면 그런 걱정안 안해도 된다.

**불변 객체는 그 자체로 실패 원자성을 제공한다 (아이템 76)**
상태가 절대 변하지 않으니 잠깐이라도 불일치 상태에 빠질 가능성이 없다.

**불변 클래스에도 단점은 있다. 값이 다르면 반드시 독립된 객체로 만들어야 한다는 것이다.**
값의 가지수가 많다면 이들을 모두 만드는데 큰 비용을 치러야 한다. 예커대 백만 비트짜리 BigInteger 에서 비트 하나를 바꿔야 한다고 해보자.

~~~java
BigInteger moby = ...;
moby = moby.flipBit(0); // 새로운 객체를 생성해 비용이 크다.
~~~







### 객체가 만들어지는 단계가 많아서 중간 단계 객체가 버려질수도 있다.

1. 흔히 쓰일 다단계 연산들을 예측하여 기본기능으로 제공하는 방법이다.
   이러한 연산을 기본으로 제공한다면 더이상 단계마다 객체를 생성하지 않아도 된다. 
   (Complex예제에서 plus, minus 등을 하나의 함수로 만들어 제공)
   
1. 불변객체는 내부적으로 아주 영리한 방식으로 구현할 수 있기 때문이다.
   예컨대 BigInteger 는 모듈러지수 같은 다단계연산 속도를 높여주는 **가변 동반 클래스** (companion class)를 package-private 기반으로 두고 있다.
   
   ~~~java
   // String 의 가변동반클래스는 Stringbuilder 이다.
   String name = "kim";
   
   StringBuilder nameBuilder = new StringBuilder(name);
   nameBuilder.append(" seongtaek");
   ~~~





### 불변클래스를 만드는 또다른 방법

모든 생성자를 private, package-priavate로 만들고 public 정적 팩터리로 제공한다.(**아이템 1)**

~~~java
public class Complex {
    private final double re;
    private final double im;

    private Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }
    ...
~~~

- 이방법이 최선일 때가 많다.  패키지 바깥의 클라이언트가 본 이 객체는 사실상 final이다.





만약 신뢰할 수 없는 클라이언트로부터 BigInteger, BigDecimal 인스턴스를 인수로 받는다면 주의해야 한다.
이 값들이 불변이어야 클래스의 보안을 지킬 수 있다면 인수로 받은 객체가 진짜 BigInteger인지 반드시 확인해야 한다.
다시말해 신뢰할 수 없는 하위 클래스의 인스턴스라고 확인되면 이 인수들은 가변이라 가정하고 방어적으로 복사해 사용해야 한다.

~~~java
public static BigInteger safeInstance(BigInteger val) {
  return val.getClass() == BigInteger.class ? val : new BigInteger(val.toByteArray());
}
~~~







### 불변객체 생성규칙 완화

이번 아이템 초입에서 나열한 불변 클래스 규칙 목록에 따르면 필드가 final이고 어떤 메서드도 그 객체를 수정할 수 없어야 한다. 
**이 규칙은 너무 과해서 성능을 위해 다음처럼 완화할 수 있다.**

**어떤 메서드도 객체의 상태 중 외부에 비치는 값을 변경할 수 없다.**

- 어떤 불변클래스는 계산 비용이 큰 값을 나중에 (처음쓰일때)계산하여 final이 아닌 필드에 캐시해놓기도 한다.
똑같은 값을 다시 요청하면 캐시해둔 값을 반환하여 계산 비용을 절감하는 것이다.
이 묘수는 순전히 그 객체가 불변이기 때문에 부릴수 있는데 몇번을 계산해도 항상 같은 결과가 만들어짐이 보장되기 때문이다.





예컨대 PhoneNumber의 hashCode 메서드(아이템11)는 처음 불렸을 때 해시값을 계산해 캐시한다.
지연초기화 (아이템 83)의 예이기도 한 이 기법을 String도 사용한다.



> 직렬화 할때는 추가로 주의할 점이 있다. Serializable을 구현하는 불변 클래스의 내부에 가변 객체를 참조하는 필드가 있다면 readObject나 readResolve 메서드를 반드시 제공하거나, ObjctOutputStream.writeUnshared와 ObjectInputStream.readUnshared 메서드를 사용해야 한다. 플랫폼이 제공하는 기본 직렬화 방법이면 충분하더라도 말이다. 그렇지 않으면 공격자가 이 클래스로부터 가변 인스턴스를 만들어낼 수 있다. (아이템 88)

정리해보자. getter가 있다고 해서 무조건 setter를 만들지말자.
**클래스는 꼭 필요한 경우가 아니라면 불변이어야 한다.** 불변 클래스는 장점이 많으며, 단점이라곤 특정 상황에서의 잠재적 성능 저하 뿐이다.
PhoneNumber와 Complex 같은 단순한 값 객체는 항상 불변으로 만들자.
String과 BigInteger 처럼 무거운 값 객체도 불변으로 만들 수 있는지 고심해야 한다.
성능 때문에 어쩔수 없다면 (아이템 67) 불변 클래스와 쌍을 이루는 가변 동작 클래스를 public 클래스로 제공하도록 하자.

**불변으로 만들 수 없는 클래스라도 변경할 수 있는 부분을 최소한으로 줄이자.**
꼭 변경해야 할 필드를 뺀 나머지 모두를 final로 선언하자.
이번 아이템과 아이템15 의 조언을 종합하면 **다른 합당한 이유가 없다면 모든 필드는 private final 이어야 한다.**

**생성자는 불변식 설정이 모두 완료된, 초기화가 완벽히 끝난 상태의 객체를 생성해야 한다.**
확실한 이유가 없다면 **생성자와** **정적 팩터리** 외에는 그 어떤 초기화 메서드도 public 으로 제공해서는 안된다.
객체를 재활용할 목적으로 **상태를 다시 초기화하는 메서드도 안된다**. 복잡성만 커지고 성능 이점은 거의 없다.









java.util.concurrent 패키지의 CountDownLatch 클래스가 이상의 원칙을 잘 방증한다.
비록 가변 클래스지만 가질 수 있는 상태의 수가 많지 않다.
인스턴스를 생성해 한번 사용하고 그걸로 끝이다.
카운트가 0에 도달하면 더는 재사용할 수 없는 것이다.















































