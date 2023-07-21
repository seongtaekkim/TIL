# 냄새8. 산탄총수술 (Shotgun Surgery)

- 어떤 한 변경 사항이 생겼을 때 여러 모듈을 (여러 함수 또는 여러 클래스를) 수정해야 하는 상황.
  - “뒤엉킨 변경” 냄새와 유사하지만 반대의 상황이다.
  - 예) 새로운 결제 방식을 도입하려면 여러 클래스의 코드를 수정해야 한다.
- 변경 사항이 여러곳에 흩어진다면 찾아서 고치기도 어렵고 중요한 변경 사항을 놓칠 수 있는 가능성도 생긴다.
- 관련 리팩토링 기술
  - “함수 옮기기 (Move Function)” 또는 **“필드 옮기기 (Move Field)”**를 사용해서 필요한 변경 내역을 하나의 클래스로 모을 수 있다,
  - 비슷한 데이터를 사용하는 여러 함수가 있다면 “여러 함수를 클래스로 묶기 (Combine Functions into Class)”를 사용할 수 있다.
  - “단계 쪼개기 (Split Phase)”를 사용해 공통으로 사용되는 함수의 결과물들을 하나로 묶을 수 있다.**“함수 인라인 (Inline Function)”**과 **“클래스 인라인 (Inline Class)”**로 흩어진 로직을 한 곳으로 모을 수도 있다.



## 리팩토링 27. 필드 옮기기 (Move Field)

- 좋은 데이터 구조를 가지고 있다면, 해당 데이터에 기반한 어떤 행위를 코드로 (메소드나 함수) 옮기는 것도 간편하고 단순해진다.
- 처음에는 타당해 보였던 설계적인 의사 결정도 프로그램이 다루고 있는 도메인과 데이터 구조에 대해 더 많이 익혀나가면서, 틀린 의사 결정으로 바뀌는 경우도 있다.
- 필드를 옮기는 단서:
  - 어떤 데이터를 항상 어떤 레코드와 함께 전달하는 경우.
  - 어떤 레코드를 변경할 때 다른 레코드에 있는 필드를 변경해야 하는 경우.
  - 여러 레코드에 동일한 필드를 수정해야 하는 경우(여기서 언급한 ‘레코드’는 클래스 또는 객체로 대체할 수도 있음)

1. discoundRate의 값이 CustomerContract에 의존하여 값이 변경된다면, 해당변수는 CustomerContract class 에서 관리해야 하므로, `필드 옮기기(Move Field) 리팩토링`을 진행한다.

```java
public class Customer {

    private String name;

    private double discountRate;

    private CustomerContract contract;

    public Customer(String name, double discountRate) {
        this.name = name;
        this.discountRate = discountRate;
        this.contract = new CustomerContract(dateToday());
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void becomePreferred() {
        this.discountRate += 0.03;
        // 다른 작업들
    }

    public double applyDiscount(double amount) {
        BigDecimal value = BigDecimal.valueOf(amount);
        return value.subtract(value.multiply(BigDecimal.valueOf(this.discountRate))).doubleValue();
    }

    private LocalDate dateToday() {
        return LocalDate.now();
    }
}
public class CustomerContract {

    private LocalDate startDate;

    public CustomerContract(LocalDate startDate) {
        this.startDate = startDate;
    }
}
```

1. 이후에도 discountRate 관련 method가 CustomerContract 에서 관리하는게 합당하다면, method 또한 옮기도록 한다.

```java
public class Customer {

    private String name;

    private CustomerContract contract;

    public Customer(String name, double discountRate) {
        this.name = name;
        this.contract = new CustomerContract(dateToday(), discountRate);
    }

    public double getDiscountRate() {
        return this.contract.getDiscountRate();
    }

    public void becomePreferred() {
        this.contract.setDiscountRate(this.contract.getDiscountRate() + 0.03);
        // 다른 작업들
    }

    public double applyDiscount(double amount) {
        BigDecimal value = BigDecimal.valueOf(amount);
        return value.subtract(value.multiply(BigDecimal.valueOf(this.contract.getDiscountRate()))).doubleValue();
    }

    private LocalDate dateToday() {
        return LocalDate.now();
    }
}
public class CustomerContract {

    private LocalDate startDate;

    private double discountRate;

    public CustomerContract(LocalDate startDate, double discountRate) {
        this.startDate = startDate;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }
}
```



## 리팩토링 28. 함수 인라인 (Inline Function)

- “함수 추출하기 (Extract Function)”의 반대에 해당하는 리팩토링
  - 함수로 추출하여 함수 이름으로 의도를 표현하는 방법.
- 간혹, 함수 본문이 함수 이름 만큼 또는 그보다 더 잘 의도를 표현하는 경우도 있다.
- 함수 리팩토링이 잘못된 경우에 여러 함수를 인라인하여 커다란 함수를 만든 다음에 다시 함수 추출하기를 시도할 수 있다.
- 단순히 메소드 호출을 감싸는 우회형 (indirection) 메소드라면 인라인으로 없앨 수 있다.
- 상속 구조에서 오버라이딩 하고 있는 메소드는 인라인 할 수 없다. (해당 메소드는 일종의 규약이니까)

1. rating() 에서 moreThanFiveLateDeliveries() 호출하여 driver.getNumberOfLateDeliveries() 를 호출하는 형태이다.
2. 현재 상태에서도 함수들이 어떤일을 하는지 이름만으로도 잘 알 수 있지만, moreThanFiveLateDeliveries()로 호출을 하지 않더라도 driver.getNumberOfLateDeliveries()에서 이미 의도가 명확한 코드이기 때문에,
3. `inline Function 리팩토링`을 해줄 수 있다.

```java
public class Driver {

    private int numberOfLateDeliveries;

    public Driver(int numberOfLateDeliveries) {
        this.numberOfLateDeliveries = numberOfLateDeliveries;
    }

    public int getNumberOfLateDeliveries() {
        return this.numberOfLateDeliveries;
    }
}
public class Rating {

    public int rating(Driver driver) {
        return moreThanFiveLateDeliveries(driver) ? 2 : 1;
    }

    private boolean moreThanFiveLateDeliveries(Driver driver) {
        return driver.getNumberOfLateDeliveries() > 5;
    }
}
```

1. 이렇게 변경해도, 함수가 어떤 역할을 하는지 쉽게 알 수 있다.

```java
public class Rating {

    public int rating(Driver driver) {
        return driver.getNumberOfLateDeliveries() > 5 ? 2 : 1;
    }
}
```



## 리팩토링 29. 클래스 인라인 (Inline Class)

- “클래스 추출하기 (Extract Class)”의 반대에 해당하는 리팩토링
- 리팩토링을 하는 중에 클래스의 책임을 옮기다보면 클래스의 존재 이유가 빈약해지는 경우가 발생할 수 있다.
- 두개의 클래스를 여러 클래스로 나누는 리팩토링을 하는 경우에, 우선 “클래스 인라인”을 적용해서 두 클래스의 코드를 한 곳으로 모으고 그런 다음에 “클래스 추출하기”를 적용해서 새롭게 분리하는 리팩토링을 적용할 수 있다.

1. TrackingInformation class의 역할을 Shipment class 가 충분히 해도 괜찮다면, inline class 리팩토링을 진행한다.
2. 맴버변수를 옮기고, 적절한 생성자를 생성한 다음 method를 옮겨주고
3. TrackingInformation에 의존된 코드를 모두 삭제해주면 된다.

```java
public class Shipment {

    private TrackingInformation trackingInformation;

    public Shipment(TrackingInformation trackingInformation) {
        this.trackingInformation = trackingInformation;
    }

    public TrackingInformation getTrackingInformation() {
        return trackingInformation;
    }

    public void setTrackingInformation(TrackingInformation trackingInformation) {
        this.trackingInformation = trackingInformation;
    }

    public String getTrackingInfo() {
        return this.trackingInformation.display();
    }
}
public class TrackingInformation {

    private String shippingCompany;

    private String trackingNumber;

    public TrackingInformation(String shippingCompany, String trackingNumber) {
        this.shippingCompany = shippingCompany;
        this.trackingNumber = trackingNumber;
    }

    public String display() {
        return this.shippingCompany + ": " + this.trackingNumber;
    }

    public String getShippingCompany() {
        return shippingCompany;
    }

    public void setShippingCompany(String shippingCompany) {
        this.shippingCompany = shippingCompany;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
}
public class Shipment {

    private String shippingCompany;
    private String trackingNumber;

    public Shipment(String shippingCompany, String trackingNumber) {
        this.shippingCompany = shippingCompany;
        this.trackingNumber = trackingNumber;
    }

    public String getTrackingInfo() {
        return this.shippingCompany + ": " + this.trackingNumber;
    }

    public String getShippingCompany() {
        return shippingCompany;
    }

    public void setShippingCompany(String shippingCompany) {
        this.shippingCompany = shippingCompany;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
}
```