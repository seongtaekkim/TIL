# item34 int 상수 대신 열거 타입을 사용하라

- 열거타입은 일정 개수의 상수 값을 정의한 다음, 그 외의 값은 허용하지 않는 타입이다.



### 정수열거패턴 (int enum pattern)

- 열거타입 지원 전에는 다음처럼 정수상수를 한 한묶음 선언해서 사용하곤 했다.

~~~java
public static final int CODE1 = 0;
public static final int CODE2 = 2;
~~~

- 타입안전을 보장할 방법이 없으며 표현력도 좋지 않다.

~~~java
int i = (APPLE_FUJI - ORANGE_TEMPLE) / APPLE_PIPPIN; // 의미를 가지지 못한다.
~~~

- 평범한 상수를 나열한 것 뿐이라 [JLS, 4.12.4] 컴파일 하면 그 값이 클라이언트 파일에 그대로 새겨진다 [JLS, 13.1] 
  따라서 값이 변경되면 다시 컴파일 해야 한다.
- 정수 상수는 문자열 출력이 까다로다.
- 정수 대신 문자열 상수를 사용하는 패턴도 있다. 
  상수의 의미를 출력한다는 점은 좋지만 하드코딩하게 된다. 이려면 오타 등으로 인해 런타임 오류 등이 발생할 수 있다. 성능 저하는 덤이다.





## 이런 문제의 대안으로 열거타입이 등장했다. (JLS, 8.9)

- 자바의 열거타입은 완전한 형태의 클래스라서 다른 언어의 열거타입보다 강력하다.
  상수 하나당 자신의 인스턴스를 하나씩 만들어 public static final 필드로 공개한다.
  열거타입은 밖에서 접근할 수 있는 생성자가 없어서 사실상 final이다.
  따라서 열거타입의 인스턴스는 하나씩만 존재한다. 다시말해 열거타입은 인스턴스가 통제된다 (9쪽)

~~~java
public enum APPLE {FUJI, PIPPIN, GRANNY_SMITH}
public enum ORANGE { ..}
~~~



### 열거타입은 컴파일타임 타입안전성을 제공한다.

- Enum은 타입으로 동작하므로, 다른타입의 인스턴스에 할당을 하는등의 코드는 컴파일에러가 발생한다.
- 열거타입의 이름공간이 독립적이어서 같은 이름의 상수도 존재가 가능하다.

- 메서드, 필드, 인터페이스 구현
- 단순하게는 상수 모음이지만, 실제로는 클래스이므로 고차원 추상개념 하나를 표현할 수 있다.

~~~java
public enum Planet {
    MERCURY(3.302e+23, 2.439e6),
    VENUS  (4.869e+24, 6.052e6),
    EARTH  (5.975e+24, 6.378e6),
    MARS   (6.419e+23, 3.393e6),
    JUPITER(1.899e+27, 7.149e7),
    SATURN (5.685e+26, 6.027e7),
    URANUS (8.683e+25, 2.556e7),
    NEPTUNE(1.024e+26, 2.477e7);

    private final double mass;           // In kilograms
    private final double radius;         // In meters
    private final double surfaceGravity; // In m / s^2

    // Universal gravitational constant in m^3 / kg s^2
    private static final double G = 6.67300E-11;

    // Constructor
    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
        surfaceGravity = G * mass / (radius * radius);
    }

    public double mass()           { return mass; }
    public double radius()         { return radius; }
    public double surfaceGravity() { return surfaceGravity; }

    public double surfaceWeight(double mass) {
        return mass * surfaceGravity;  // F = ma
    }
}
~~~

- 열거타입상수 각각을 특정 데이터와 연결지으려면 **생성자에서 데이터를 받아 인스턴스 필드에 저장하면 된다.**
  - 열거타입은 근본적으로 불변 이라 모든 필드는 final 아이템17
  - 필드를 pulbic으로 선언해도 되지만 private 으로 두고 별도의 public 접근자를 두는게 낫다 아이템16

- Object 메서드들, Comparable, Serializable을 구현했다.



### planet에서 정의한 열거타입상수 하나 삭제하면

- 클라이언트에서 컴파일 시 에러 발생 기대
- 컴파일 안한다면 런타임에 클라이언트 참조에 의해 에러 기대





## 상수별 메서드 구현 (constant-specific method implementation)

### 상수별 다른 연산을 하는 enum

- 상수가 추가된다면 다른 부분 (swich) 에도 로직이 추가되어야 한다. (유지관리 이슈)

~~~java
public enum Operation1 {
    PLUS, MINUS, TIMES, DIVIDE;
    public double apply(double x, double y) {
        switch(this) {
            case PLUS: return x + y;
            case MINUS: return x - y;
            case TIMES: return x * y;
            case DIVIDE: return x / y;
        }
        throw new AssertionError("fail");
    }

    public static void main(String[] args) {
        double x = Double.parseDouble("5");
        double y = Double.parseDouble("10");
        for (Operation1 op : Operation1.values())
            System.out.printf("%f %s %f = %f%n",
                    x, op, y, op.apply(x, y));

    }
}
~~~



### abstract method

- 필요한 연산을 abstract method로 선언하면, 정의한 상수는 해당 method를 무조건 구현해야 한다.
- **마치 템플릿 메서드패턴 처럼 동작한다.**

~~~java
public enum Operation {
    PLUS("+") {
        public double apply(double x, double y) { return x + y; }
    },
    MINUS("-") {
        public double apply(double x, double y) { return x - y; }
    },
    TIMES("*") {
        public double apply(double x, double y) { return x * y; }
    },
    DIVIDE("/") {
        public double apply(double x, double y) { return x / y; }
    };
    public abstract double apply(double x, double y);
~~~

- 구현 안하면 컴파일 에러 발생 





### 상수별 메서드 구현 && 상수별 데이터 (values())

- 열거타입은 자신 안에 정의된 상수들의 값을 배열에 담아 반환하는 정적메서드인 values 를 제공한다.

~~~java
 public static void main(String[] args) {
    double earthWeight = Double.parseDouble("10");
    double mass = earthWeight / Planet.EARTH.surfaceGravity();
    for (Planet p : Planet.values())
       System.out.printf("Weight on %s is %f%n",
               p, p.surfaceWeight(mass));
 }
~~~



### enum to map

- 열거타입상수 생성 후 정적필드가 초기화될 때 생성된다.
- Enum의 valueOf 에 접근하려면 열거타입상수의 name() 을 통해서 접근해야하는데,
  valueOf 값 별 Enum map을 생성하여 간편하게 만들었다.

~~~java
public static final Map<String, Operation2> stringToEnum =
        Stream.of(values()).collect(toMap(Object::toString, e -> e));

// Returns Operation for string, if any
public static Optional<Operation2> fromString(String symbol) {
    return Optional.ofNullable(stringToEnum.get(symbol));
}
~~~





## 상수별 메서드 구현 > 열거타입상수 간 코드공유

### 코드별 함수출력

- 공휴일 등 새로운 코드를 추가했을 때 switch 문에도 관련 로직을 추가해야 하는데
  이 때 추가를 안하는 등 휴먼에러가 발생할 수 있다.
- 유지관리 이슈발생

~~~java
enum PayrollDay1 {
    MONDAY, TUESDAY, WEDNESDAY,
    THURSDAY, FRIDAY,
    SATURDAY, SUNDAY;

    private static final int MINS_PER_SHIFT = 8 * 60;

    int pay(int minsWorked, int payRate) {
        int basePay = minsWorked * payRate;

        int overtimePay;
        switch(this) {
            case SATURDAY :
            case SUNDAY :
                overtimePay = basePay / 2;
                break ;
            default :
                overtimePay = minsWorked <= MINS_PER_SHIFT ?
                        0 : (minsWorked - MINS_PER_SHIFT) * payRate / 2;
        }
        return basePay + overtimePay;
    }
  ...
~~~



### strategy enum pattern

* PayrollDay2 의 상수를 정의할 때 생성자를 계산을 담당하는 내부 enum class 상수로 지정한다.
* 상수 정의 시 계산을 담당하는 "전략"을 무조건 선택해야 하므로 안전하다.

~~~java
enum PayrollDay2 {
    MONDAY(WEEKDAY), TUESDAY(WEEKDAY), WEDNESDAY(WEEKDAY),
    THURSDAY(WEEKDAY), FRIDAY(WEEKDAY),
    SATURDAY(WEEKEND), SUNDAY(WEEKEND);

    private final PayType payType;

    PayrollDay2(PayType payType) { this.payType = payType; }

    int pay(int minutesWorked, int payRate) {
        return payType.pay(minutesWorked, payRate);
    }

    // The strategy enum type
    enum PayType {
        WEEKDAY {
            int overtimePay(int minsWorked, int payRate) {
                return minsWorked <= MINS_PER_SHIFT ? 0 :
                        (minsWorked - MINS_PER_SHIFT) * payRate / 2;
            }
        },
        WEEKEND {
            int overtimePay(int minsWorked, int payRate) {
                return minsWorked * payRate / 2;
            }
        };

        abstract int overtimePay(int mins, int payRate);
        private static final int MINS_PER_SHIFT = 8 * 60;

        int pay(int minsWorked, int payRate) {
            int basePay = minsWorked * payRate;
            return basePay + overtimePay(minsWorked, payRate);
        }
    }
~~~





### 언제사용하는게 바람직한가

필요한 원소를 컴파일타임에 다 알수 있는 상수집합이라면 ㅅ항상 열거타입을 사용하자.
(메뉴아이템, 연산코드, 명령줄 플래그 등)
열거타입에 정의된 상수 개수가 불변일 필요는 없다. 나중에 추가되어도 바이너리 수준에서 호환되도록 설계되었기 때문이다.






## 정리

열거타입은 확실히 정수상수보다 뛰어나다.
더 읽기 쉽고 안전하고 강력하다.
대다수 열거 타입이 명시적 생성자나 메서드 없이 쓰이지만, 각 상수를 특정 데이터와 연결짓거나 상수마다 다르게 동작하게 할 때는 필요하다.
드물게는 사하의 메서드가 상수별로 다르게 동작해야 할 때도 있다.
이런 열거타입에서는 switch 문 대신 상수별 메서드 구현을 사용하자.
**열거타입상수** 일부가 같은 동작을 공유한다면 **전략열거타입패턴을** 사용하자,













