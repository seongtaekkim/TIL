# 인터페이스는 타입을 정의하는 용도로만 사용해라



> 인터페이스는 자신을 구현한 클래스의 인스턴스를 참조할 수 있는 타입 역할을 한다.
> 클래스가 어떤 인터페이스를 구현한다는 것은,  자신의 인스턴스로 무엇을 할 수 있는지를 클라이언트에 이야기 해주는 것이다.
> 인터페이스는 오로지 이 용도로만 사용해야 한다.



### 이 지침에 맞지 않는 소위 상수 인터페이스가 있다.
- 메서드 없이, 상수를 뜻하는 static final 필드만 가득 찬 인터페이스를 말한다.

```java
public interface PhysicalConstants {
    // 아보가드로 수 (1/몰)
    static final double AVOGADROS_NUMBER   = 6.022_140_857e23;

    // 볼츠만 상수 (J/K)
    static final double BOLTZMANN_CONSTANT = 1.380_648_52e-23;

    // 전자 질량 (kg)
    static final double ELECTRON_MASS      = 9.109_383_56e-31;
}
```

- **상수인터페이스 안티패턴은 인터페이스를 잘못 사용한 예다.**

> 클래스 내부에서 사용하는 상수는 외부 인터페이스가 아니라 내부구현에 해당한다.
> 따라서 상수인터페이스를 구현하는 것은 이 내부구현을 클래스의 API로 노출하는 행위다.
> 클래스가 어떤 상수 인터페이스를 사용하든 사용자에게는 아무런 의미가 없다.
> 오히려 사용자에게 혼란을 주기도 하며 더 심하게는 클라이언트 코드가 내부구현에 해당하는 이 상수들에 종속되게 한다.
>
> 그래서 다음 릴리스에 서 이 상수들을 더는 쓰지 않게 되더라도 바이너리 호환성을 위해 여전히 상수 인터페이스를 구현하고 있어야 한다.
> final이 아닌 클래스가 상수 인터페이스를 구현한다면 모든 하위클래스의 이름공간이 그 인터페이스가 정의한 상수들로 오염되어 버린다.



### 자바 플랫폼 라이브러리에도 상수 인터페이스가 존재한다.  ( 인터페이스를 잘못 활용한 예이니 따라해서는 안된다.)

~~~java
// java.io.ObjectStreamConstants.java

/**
 * Constants written into the Object Serialization Stream.
 * @since 1.1
 */
public interface ObjectStreamConstants {

    static final short STREAM_MAGIC = (short)0xaced;

    static final short STREAM_VERSION = 5;

    ...
~~~



### 상수를 공개할 목적이라면 더 합당한 게 있다.

- 특정 클래스나 인터페이스와 강하게 연관된 상수라면 그 클래스나 인터페이스 자체에 추가해야 한다.

1. Integer, Double에 선언된 MIN_VALUE, MAX_VALUE 같은 예다

~~~java
// java.lang.Double.java
public final class Double extends Number
        implements Comparable<Double>, Constable, ConstantDesc {

    public static final double POSITIVE_INFINITY = 1.0 / 0.0;
    public static final double NEGATIVE_INFINITY = -1.0 / 0.0;
    ...
~~~



2. 열거타입으로 나타내기 적합한 상수라면 열거타입으로 만들어 공개하면 된다**(아이템 34)**



3. 유틸리티 클래스**(아이템 4)** 

```java
// 코드 22-2 상수 유틸리티 클래스 (140쪽)
public class PhysicalConstants {
  private PhysicalConstants() { }  // 인스턴스화 방지

  // 아보가드로 수 (1/몰)
  public static final double AVOGADROS_NUMBER = 6.022_140_857e23;

  // 볼츠만 상수 (J/K)
  public static final double BOLTZMANN_CONST  = 1.380_648_52e-23;

  // 전자 질량 (kg)
  public static final double ELECTRON_MASS    = 9.109_383_56e-31;
}
```

- 자바7 부터 숫자 밑줄 이 가능했다. 가독성이 좋다.
- 유틸리티 클래스에 정의된 상수를 클라이언트에서 사용하려면 클래스 이름까지 함께 명시해야 한다.
  - **PhysicalConstants.AVOGADROS_NUMBER**
- 유틸리티 클래스의 상수를 빈번히 사용한다면 정적임포트하여 클래스 이름은 생략할 수 있다.



### 정리

인터페이스는 타입을 정의하는 용도로만 사용해야 한다. 
상수공개용 수단으로 사용하지 말자.



























































































