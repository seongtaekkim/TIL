# 냄새6. 가변데이터 (Mutable Data)

- 데이터를 변경하다보면 예상치 못했던 결과나 해결하기 어려운 버그가 발생하기도 한다.
- 함수형 프로그래밍 언어는 데이터를 변경하지 않고 복사본을 전달한다. 하지만 그밖의 프로그래밍 언어는 데이터 변경을 허용하고 있다. 따라서 변경되는 데이터 사용 시 발생할 수 있는 리스크를 관리할 수 있는 방법을 적용하는 것이 좋다.
- 관련 리팩토링
  - “변수 캡슐화하기 (Encapsulate Variable)”를 적용해 데이터를 변경할 수 있는 메소드를 제한하고 관리할 수 있다.
  - “**변수쪼개기 (split variable)”**을 사용해 여러 데이터를 저장하는 변수를 나눌 수 있다.
  - “코드정리하기 (slide statement)”를 사용해 데이터를 변경하는 코드를 분리하고 피할 수 있다.
  - “함수추출하기 (Extract function)”으로 데이터를 변경하는 코드로부터 사이드이펙트가 없는 코드를 분리할 수 있다.
  - “질의함수와 변경함수 분리하기 (Separate Query from modifier)”를 적용해서 클라이언트가 원하는 경우에만 사이드이펙트가 있는 함수를 호출하도록 api를 개선할 수 있다.;
  - 가능하다면 **“세터 제거하기 (Remove Setting Method)”**를 적용한다.
  - 계산해서 알아낼 수 있는 값에는 **“파생 변수를 질의 함수로 바꾸기 (Replace Derived Variable with Query)”**를 적용할 수 있다.
  - 변수가 사용되는 범위를 제한하려면 “여러 함수를 클래스로 묶기 (Combine Functions into Class)”또는 **“여러 함수를 변환 함수로 묶기 (Combine Functions into Transform)”을 적용할 수 있다.**
  - **“참조를 값으로 바꾸기 (Change Reference to Value)”**를 적용해서 데이터 일부를 변경하기 보다는 데이터 전체를 교체할 수 있다.



## 리팩토링 18. 변수 쪼개기 (Split Variable)

- 어떤 변수가 여러번 재할당 되어도 적절한 경우
  - 반복문에서 순회하는데 사용하는 변수 또는 인덱스
  - 값을 축적시키는데 사용하는 변수
- 그밖에 경우에 재할당 되는 변수가 있다면 해당 변수는 여러 용도로 사용되는 것이며 변수를 분리해야 더 이해하기 좋은 코드를 만들 수 있다.
  - 변수 하나 당 하나의 책임(Responsibility)을 지도록 만든다.
  - 상수를 활용하자. (자바스크립트의 const, 자바의 final)
- inputValue 는 input parameter 이므로, 연산 결과는 다른 변수에 할당하는게 좋다.

```java
public double discount(double inputValue, int quantity) {
        if (inputValue > 50) inputValue = inputValue - 2;
        if (quantity > 100) inputValue = inputValue - 1;
        return inputValue;
}
public double discount(double inputValue, int quantity) {
        double result = inputValue;
        if (inputValue > 50) result =- 2;
        if (quantity > 100) result =- 1;
        return result;
    }
```

- acc 변수에 1차연산, 2차연산 에 중복할당하게 하는 것보다 다른 변수에 이름을 구체적으로 작성해서 할당하는게 가독성이 좋다.
- 변경되지 않는 값은 final 로 설정하는것이 가독성이 좋다.

```java
public double distanceTravelled(int time) {
        double result;
        double acc = primaryForce / mass;
        int primaryTime = Math.min(time, delay);
        result = 0.5 * acc * primaryTime * primaryTime;

        int secondaryTime = time - delay;
        if (secondaryTime > 0) {
            double primaryVelocity = acc * delay;
            acc = (primaryForce + secondaryForce) / mass;
            result += primaryVelocity * secondaryTime + 0.5 * acc * secondaryTime + secondaryTime;
        }

        return result;
    }
public double distanceTravelled(int time) {
        double result;
        final double firstAcc = primaryForce / mass;
        final int primaryTime = Math.min(time, delay);
        result = 0.5 * firstAcc * primaryTime * primaryTime;

        int secondaryTime = time - delay;
        if (secondaryTime > 0) {
            final double primaryVelocity = firstAcc * delay;
            final double secondAcc = (primaryForce + secondaryForce) / mass;
            result += primaryVelocity * secondaryTime + 0.5 * secondAcc * secondaryTime + secondaryTime;
        }

        return result;
    }
```

- 다른 종류의 연산결과를 같은 임시변수에 할당하는것보다 이름을 구체적으로 명시하여 다른 변수를 만들어 할당하는게 가독성이 더 좋다.

```java
public void updateGeometry(double height, double width) {
        double temp = 2 * (height + width);
        System.out.println("Perimeter: " + temp);
        perimeter = temp;

        temp = height * width;
        System.out.println("Area: " + temp);
        area = temp;
    }
public void updateGeometry(double height, double width) {
        double perimeter = 2 * (height + width);
        System.out.println("Perimeter: " + perimeter);
        this.perimeter = perimeter;

        double area = height * width;
        System.out.println("Area: " + area);
        this.area = area;
    }
```



## 리팩토링 19. 질의 함수와 변경 함수 분리하기 (Separate Query from Modifier)

- “눈에 띌만한” 사이드 이팩트 없이 값을 조회할 수 있는 메소드는 테스트 하기도 쉽고, 메소 드를 이동하기도 편하다.
- 명령-조회 분리 (command-query separation) 규칙:
  - 어떤 값을 리턴하는 함수는 사이드 이팩트가 없어야 한다.
- “눈에 띌만한 (observable) 사이드 이팩트”
  - 가령, 캐시는 중요한 객체 상태 변화는 아니다. 따라서 어떤 메소드 호출로 인해, 캐시 데이터를 변경하더라도 분리할 필요는 없다.

```java
public double getTotalOutstandingAndSendBill() {
        double result = customer.getInvoices().stream()
                .map(Invoice::getAmount)
                .reduce((double) 0, Double::sum);
        sendBill();
        return result;
    }

    private void sendBill() {
        emailGateway.send(formatBill(customer));
    }
public double totalOutstanding() {
        double result = customer.getInvoices().stream()
                .map(Invoice::getAmount)
                .reduce((double) 0, Double::sum);
        return result;
    }

    public void sendBill() {
        emailGateway.send(formatBill(customer));
    }
public String alertForMiscreant(List<Person> people) {
        for (Person p : people) {
            if (p.getName().equals("Don")) {
                setOffAlarms();
                return "Don";
            }

            if (p.getName().equals("John")) {
                setOffAlarms();
                return "John";
            }
        }

        return "";
    }
public void alertForMiscreant(List<Person> people) {
        if (!findMiscreant(people).isBlank())
            setOffAlarms();
    }

    public String findMiscreant(List<Person> people) {
        for (Person p : people) {
            if (p.getName().equals("Don")) {
                return "Don";
            }

            if (p.getName().equals("John")) {
                return "John";
            }
        }

        return "";
    }
```



## 리팩토링 20. 세터 제거하기 (Remove Setting Method)

- 세터를 제공한다는 것은 해당 필드가 변경될 수 있다는 것을 뜻한다.
- 객체 생성시 처음 설정된 값이 변경될 필요가 없다면 해당 값을 설정할 수 있는 생성자를 만들고 세터를 제거해서 변경될 수 있는 가능성을 제거해야 한다.



## 리팩토링 21. 파생 변수를 질의 함수로 바꾸기 (Replace Derived Variable with Query)

- 변경할 수 있는 데이터를 최대한 줄이도록 노력해야 한다.
- 계산해서 알아낼 수 있는 변수는 제거할 수 있다.
  - 계산 자체가 데이터의 의미를 잘 표현하는 경우도 있다.
  - 해당 변수가 어디선가 잘못된 값으로 수정될 수 있는 가능성을 제거할 수 있다.
- 계산에 필요한 데이터가 변하지 않는 값이라면, 계산의 결과에 해당하는 데이터 역시 불변 데이터기 때문에 해당 변수는 그대로 유지할 수 있다.

1. discountedTotal 변수는 basetotal 과 discount의 연산결과인 파생 변수이다.
2. 굳이 이런 파생변수를 만들지 않고, 함수의 결과에 로직을 첨부하여 작성하도록 변경한다.

```java
public class Discount {

    private double discountedTotal;
    private double discount;

    private double baseTotal;

    public Discount(double baseTotal) {
        this.baseTotal = baseTotal;
    }

    public double getDiscountedTotal() {
        return this.discountedTotal;
    }

    public void setDiscount(double number) {
        this.discount = number;
        this.discountedTotal = this.baseTotal - this.discount;
    }
}
public class Discount {

    private double discount;

    private double baseTotal;

    public Discount(double baseTotal) {
        this.baseTotal = baseTotal;
    }

    public double getDiscountedTotal() {
        return this.baseTotal - this.discount;
    }

    public void setDiscount(double number) {
        this.discount = number;
    }
}
```

1. production 은 adjustments 합계를 나타내는 파생 변수이다.
2. production을 계산하는 함수 및 로직을 getProduction()에 작성하고, 파생변수를 삭제한다.

```java
public class ProductionPlan {

    private double production;
    private List<Double> adjustments = new ArrayList<>();

    public void applyAdjustment(double adjustment) {
        this.adjustments.add(adjustment);
        this.production += adjustment;
    }

    public double getProduction() {
        return this.production;
    }
}
```

- assert 키워드를 사용해서 변경한 로직이 정상적으로 테스트 되는 지 먼저 확인하고,
- inline method 등으로 연산을 합쳐서 마무리 한다.

```java
public class ProductionPlan {

    private double production;
    private List<Double> adjustments = new ArrayList<>();

    public void applyAdjustment(double adjustment) {
        this.adjustments.add(adjustment);
        this.production += adjustment;
    }

    public double getProduction() {
        assert this.production == calcuratedProduction();
        return this.production;
    }
    
    private double calcuratedProduction() {
        return this.adjustments.stream().reduce((double) 0, (a, b) -> a + b);
        //return this.adjustments.stream().reduce((double) 0, Double::sum);
        //return this.adjustments.stream().mapToDouble(Double::valueOf).sum();

    }
}
```



## 리팩토링 22. 여러 함수를 변환 함수로 묶기 (Combine Functions into Transform)

- 관련있는 여러 파생 변수를 만들어내는 함수가 여러곳에서 만들어지고 사용된다면 그러한 파생 변수를 “변환 함수 (transform function)”를 통해 한 곳으로 모아둘 수 있다.
- 소스 데이터가 변경될 수 있는 경우에는“여러 함수를 클래스로 묶기 (Combine Functions into Class)”를 사용하는 것이 적절하다.
- 소스 데이터가 변경되지 않는 경우에는 두 가지 방법을 모두 사용할 수 있지만, 변환 함수를 사용해서 불변 데이터의 필드로 생성해 두고 재사용할 수도 있다.

1. 파생변수인 base, taxableCharge 가 함수 여러곳에서 사용되 고 있다.
2. 이 파생변수들을 레코드를 만들어 묶어 놓고, 파생변수를 계산하는 함수를 하나의 부모 class 로 만든다.
3. 이 방법은 파생변수가 변경되지 않는 readonly 일 경우에 활용이 가능하다.

```java
public record Reading(String customer, double quantity, Month month, Year year) {
}
public class Client2 {

    private double base;
    private double taxableCharge;

    public Client2(Reading reading) {
        this.base = baseRate(reading.month(), reading.year()) * reading.quantity();
        this.taxableCharge = Math.max(0, this.base - taxThreshold(reading.year()));
    }

    private double taxThreshold(Year year) {
        return 5;
    }

    private double baseRate(Month month, Year year) {
        return 10;
    }

    public double getBase() {
        return base;
    }

    public double getTaxableCharge() {
        return taxableCharge;
    }
}
public class Client3 {

    private double basicChargeAmount;

    public Client3(Reading reading) {
        this.basicChargeAmount = calculateBaseCharge(reading);
    }

    private double calculateBaseCharge(Reading reading) {
        return baseRate(reading.month(), reading.year()) * reading.quantity();
    }

    private double baseRate(Month month, Year year) {
        return 10;
    }

    public double getBasicChargeAmount() {
        return basicChargeAmount;
    }
}
```

- 기존의 레코드 reading + 파생변수 baseCharge, taxableCharge를 묶어 새로운 레코드를 생성

```java
public record EnrichReading(Reading reading, double baseCharge, double taxableCharge) {

}
```

- 파생함수를 계산하는 함수들을 부모클래스로 묶어놓는다.

```java
public class ReadingClient {

    protected double taxThreshold(Year year) {
        return 5;
    }
    protected double baseRate(Month month, Year year) {
        return 10;
    }

    // 변하지 않는 파라메터를 변하지 않는 인스턴스를 만들어서 리턴
   protected EnrichReading enrichReading(Reading reading) {
        return new EnrichReading(reading, baseCharge(reading), taxableCharge(reading));
    }

    private double taxableCharge(Reading reading) {
        return Math.max(0, baseCharge((reading)) - taxThreshold(reading.year()));
    }

    private double baseCharge(Reading reading) {
        return baseRate(reading.month(), reading.year()) * reading.quantity();
    }
}
```

- 실제 사용

```java
public class Client2 extends ReadingClient {

    private double base;
    private double taxableCharge;

    public Client2(Reading reading) {
        EnrichReading enrichReading = enrichReading(reading);
        this.base = enrichReading.baseCharge();
        this.taxableCharge = enrichReading.taxableCharge();
    }

    public double getBase() {
        return base;
    }

    public double getTaxableCharge() {
        return taxableCharge;
    }
}
```



## 리팩토링 23. 참조를 값으로 바꾸기 (Change Reference to Value)

- 레퍼런스 (Reference) 객체 vs 값 (Value) 객체
  - https://martinfowler.com/bliki/ValueObject.html
- “Objects that are equal due to the value of their properties, in this case their x and y coordinates, are called value objects.”
- 값 객체는 객체가 가진 필드의 값으로 동일성을 확인한다.
- 값 객체는 변하지 않는다.
- 어떤 객체의 변경 내역을 다른 곳으로 전파시키고 싶다면 레퍼런스, 아니라면 값 객체를 사용한다.

1. 레퍼런스 객체가 예시로 주어진다. 이를 값 객체로 바꾸어 보자.

```java
public class Person {

    private TelephoneNumber officeTelephoneNumber;

    public String officeAreaCode() {
        return this.officeTelephoneNumber.areaCode();
    }

    public void officeAreaCode(String areaCode) {
        this.officeTelephoneNumber.areaCode(areaCode);
    }

    public String officeNumber() {
        return this.officeTelephoneNumber.number();
    }

    public void officeNumber(String number) {
        this.officeTelephoneNumber.number(number);
    }

}
public class TelephoneNumber {

    private String areaCode;

    private String number;

    public String areaCode() {
        return areaCode;
    }

    public void areaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String number() {
        return number;
    }

    public void number(String number) {
        this.number = number;
    }
}
```

1. setter 는 새로운 값객체를 생성하는 로직으로 변경한다.

```java
public class Person {

    private TelephoneNumber officeTelephoneNumber;

    public String officeAreaCode() {
        return this.officeTelephoneNumber.areaCode();
    }

    public void officeAreaCode(String areaCode) {
        this.officeTelephoneNumber = new TelephoneNumber(areaCode, this.officeTelephoneNumber.number());
    }

    public String officeNumber() {
        return this.officeTelephoneNumber.number();
    }

    public void officeNumber(String number) {
        this.officeTelephoneNumber = new TelephoneNumber(this.officeTelephoneNumber.areaCode(), number);
    }

}
```

1. 값 변경이 안되도록 변수를 final 해주고, 생성자를 만들어준다.
2. setter를 모두 삭제한다
3. 값의 동일성을 해 equals, hashcode를 override 한다.
   1. java 객체 비교는 기본이 레퍼런스 비교이기 때문에 오버라이드 해줘야함.

```java
public class TelephoneNumber {

    private final String areaCode;

    private final String number;

    public TelephoneNumber(String areaCode, String number) {
        this.areaCode = areaCode;
        this.number = number;
    }

    public String areaCode() {
        return areaCode;
    }

    public String number() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TelephoneNumber that = (TelephoneNumber) o;
        return Objects.equals(areaCode, that.areaCode) && Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(areaCode, number);
    }
}
```

- java11까지는 위와 같이 코드를 작성했지만, 14부터는 record를 사용하면 같은 기능을 수행한다.

```java
public record TelephoneNumber(String areaCode, String number) {

}
```

- 동일성 테스트

```java
class TelephoneNumberTest {

    @Test
    void test()
    {
        TelephoneNumber number1 = new TelephoneNumber("123" , "1234");
        TelephoneNumber number2 = new TelephoneNumber("123" , "1234");
        assertEquals(number1, number2);
    }
}
```