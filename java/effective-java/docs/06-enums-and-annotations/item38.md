# item38 확장할 수 있는 열거타입이 필요하면 인터페이스를 사용하라





###  타입안전열거패턴 

- 클래스를 이용하고, 생성자를 private로 만들어 최초 정의된 객체만 참조가능.
- 확장가능. (그거빼고 enum 보다 나은게 없다.)

~~~java
public class TypesafeOperation {
    private final String type;
    private TypesafeOperation(String type) {
        this.type = type;
    }

    public String toString() {
        return type;
    }
    
    public static final TypesafeOperation PLUS = new TypesafeOperation("+");
    public static final TypesafeOperation MINUS = new TypesafeOperation("-");
    public static final TypesafeOperation TIMES = new TypesafeOperation("*");
    public static final TypesafeOperation DIVIDE = new TypesafeOperation("/");
}
~~~



### 열거타입 확장 문제점

- 열거 타입 자체는 확장할 수 없다. 
- 기반타입과 확장된 타입들의 원소 모두를 순회할 방법이 어려움
- 열거 타입끼리는 상속이 불가능하기에 공통기능은 다음과 같이 사용할 수 있다. 
  - (Stateless인 경우) 인터페이스에 Default Method를 구현한다.
  - (Stateful인 경우)별도의 도우미 클래스나 정적 도우미 메서드로 분리한다



## 열거타입 확장

- 인터페이스를 확장하여 사용.

### 예제

~~~java
public interface Operation {
    default double apply(double x, double y) {
        return x * y;
    }
}
~~~

~~~java
public enum BasicOperation implements Operation {
    PLUS("+") {
        public double apply(double x, double y) { return x + y; }
    },
    MINUS("-") {
        public double apply(double x, double y) { return x - y; }
    },
    TIMES("*") ,
    DIVIDE("/") {
        public double apply(double x, double y) { return x / y; }
    };

...
~~~





## Enum Opcode 사용방법

### 한정적 타입토큰 이용

~~~java
test(BasicOperation.class, x, y);
~~~

~~~java
private static <T extends Enum<T> & Operation>
void test(Class<T> opEnumType, double x, double y) {
    for (Operation op : opEnumType.getEnumConstants()) {
        System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
    }
}
~~~

- 확장한 Operation 리터럴을 인자로 처리하는 방식
- Enum 의 상속이면서 Operation 구현체 인 타입을 표기해야하는 복잡함이 있음.



### 한정적 와일드카드 타입 이용

~~~java
test(Arrays.asList(ExtendedOperation.values()), x, y);
~~~

~~~java
private static void
test(Collection<? extends Operation> opSet, double x, double y) {
    for (Operation op : opSet)
        System.out.printf("%f %s %f = %f%n", x, op, y, op.apply(x, y));
}
~~~

- 코드가 좀 덜 복잡하고 test메서드가 좀 더 유연해졌다. 
- 특정 연산에서 EnumMap이나 EnumSet을 사용할 수 없다는 단점이 있다.





## 정리

열거타입 자체는 확장할 수 없지만, 인터페이스와 그 인터페이스를 구현하는 기본 열거타입을 함께 사용해 같은 효과를 낼 수 있다.
이렇게 하면 클라이언트는 이 인터페이스를 구현해 자신만의 열거타입을 만들 수 있다.
그리고 API가 인터페이스 기반으로 작성되었다면 기본 열거타입의 인스턴스가 쓰이는 모든 곳을 새로 확장한 열거타입의 인스턴스로 대체해 사용할 수 있다.
