# 냄새7. 뒤엉킨변경 (Divergent Change)

- 소프트웨어는 변경에 유연하게(soft) 대처할 수 있어야 한다.
- 어떤 한 모듈이 (함수 또는 클래스가) 여러가지 이유로 다양하게 변경되어야 하는 상황.
  - 예) 새로운 결제 방식을 도입하거나, DB를 변경할 때 동일한 클래스에 여러 메소드를 수정해야 하는 경우.
- 서로 다른 문제는 서로 다른 모듈에서 해결해야 한다.
  - 모듈의 책임이 분리되어 있을수록 해당 문맥을 더 잘 이해할 수 있으며 다른 문제는 신경쓰지 않아도 된다.
- 관련 리팩토링 기술
  - **“단계 쪼개기 (Split Phase)”**를 사용해 서로 다른 문맥의 코드를 분리할 수 있다.
  - **“함수 옮기기 (Move Function)”**를 사용해 적절한 모듈로 함수를 옮길 수 있다.
  - 여러가지 일이 하나의 함수에 모여 있다면 “함수 추출하기 (Extract Function)”를 사용할 수 있다.
  - 모듈이 클래스 단위라면 **“클래스 추출하기 (Extract Class)”**를 사용해 별도의 클래스로 분리할 수 있다.



## 리팩토링 24. 단계 쪼개기 (Split Phase)

- 서로 다른 일을 하는 코드를 각기 다른 모듈로 분리한다.
  - 그래야 어떤 것을 변경해야 할 때, 그것과 관련있는 것만 신경쓸 수 있다.
- 여러 일을 하는 함수의 처리 과정을 각기 다른 단계로 구분할 수 있다.
  - 예) 전처리 -> 주요 작업 -> 후처리
  - 예) 컴파일러: 텍스트 읽어오기 -> 실행 가능한 형태로 변경
- 서로 다른 데이터를 사용한다면 단계를 나누는데 있어 중요한 단서가 될 수 있다.
- 중간 데이터(intermediate Data)를 만들어 단계를 구분하고 매개변수를 줄이는데 활용할 수 있다.

1. 로직의 처리과정을 함수로 나누어 보자.
2. 함수로 나누는 과정에서 중간데이터를 만들어보자.

```java
public class PriceOrder {

    public double priceOrder(Product product, int quantity, ShippingMethod shippingMethod) {
        final double basePrice = product.basePrice() * quantity;
        final double discount = Math.max(quantity - product.discountThreshold(), 0)
                * product.basePrice() * product.discountRate();
        final double shippingPerCase = (basePrice > shippingMethod.discountThreshold()) ?
                shippingMethod.discountedFee() : shippingMethod.feePerCase();
        final double shippingCost = quantity * shippingPerCase;
        final double price = basePrice - discount + shippingCost;
        return price;
    }
}
```

1. basePrice, discound 계산부분과 shipping cost 계산부분을 나눈다.
2. 나눈 로직을 연결해줄 중간 값객체를 생성한다 - PriceData
3. PriceOrder 는 price를 계산하고, shipping cost를 계산하는 로직의 의도를 볼 수 있게 되었다.

```java
public class PriceOrder {

    public double priceOrder(Product product, int quantity, ShippingMethod shippingMethod) {
        PriceData priceData = calculatePrice(product, quantity);
        return applyShipping(priceData, shippingMethod);
    }

    private PriceData calculatePrice(Product product, int quantity) {
        final double basePrice = product.basePrice() * quantity;
        final double discount = Math.max(quantity - product.discountThreshold(), 0)
                * product.basePrice() * product.discountRate();
        final PriceData priceData = new PriceData(basePrice, quantity, discount);
        return priceData;
    }

    private double applyShipping(PriceData priceData, ShippingMethod shippingMethod) {
        final double shippingPerCase = (priceData.basePrice() > shippingMethod.discountThreshold()) ?
                shippingMethod.discountedFee() : shippingMethod.feePerCase();
        final double shippingCost = priceData.quantity() * shippingPerCase;
        final double price = priceData.basePrice() - priceData.discount() + shippingCost;
        return price;
    }
}
public record PriceData(double basePrice, int quantity, double discount) {
}
```



## 리팩토링 25. 함수 옮기기 (Move Function)

- 모듈화가 잘 된 소프트웨어는 최소한의 지식만으로 프로그램을 변경할 수 있다.
- 관련있는 함수나 필드가 모여있어야 더 쉽게 찾고 이해할 수 있다.
- 하지만 관련있는 함수나 필드가 항상 고정적인 것은 아니기 때문에 때에 따라 옮겨야 할 필요가 있다.
- 함수를 옮겨야 하는 경우
  - 해당 함수가 다른 문맥 (클래스)에 있는 데이터 (필드)를 더 많이 참조하는 경우.
  - 해당 함수를 다른 클라이언트 (클래스)에서도 필요로 하는 경우.
  - 함수를 옮겨갈 새로운 문맥 (클래스)이 필요한 경우에는 “여러 함수를 클래스로 묶기 (Combine Functions infoClass)” 또는 “클래스 추출하기 (Extract Class)”를 사용한다.
- 함수를 옮길 적당한 위치를 찾기가 어렵다면, 그대로 두어도 괜찮다. 언제든 나중에 옮길 수 있다.

1. overdraftCharge()  내부에서 Account Type 을 참조하고 있으므로, move function 을 고려해 변경해보자.

```java
public class Account {

    private int daysOverdrawn;

    private AccountType type;

    public Account(int daysOverdrawn, AccountType type) {
        this.daysOverdrawn = daysOverdrawn;
        this.type = type;
    }

    public double getBankCharge() {
        double result = 4.5;
        if (this.daysOverdrawn() > 0) {
            result += this.overdraftCharge();
        }
        return result;
    }

    private int daysOverdrawn() {
        return this.daysOverdrawn;
    }

    private double overdraftCharge() {
        if (this.type.isPremium()) {
            final int baseCharge = 10;
            if (this.daysOverdrawn <= 7) {
                return baseCharge;
            } else {
                return baseCharge + (this.daysOverdrawn - 7) * 0.85;
            }
        } else {
            return this.daysOverdrawn * 1.75;
        }
    }
}
public class AccountType {
    private boolean premium;

    public AccountType(boolean premium) {
        this.premium = premium;
    }

    public boolean isPremium() {
        return this.premium;
    }
}
```

1. overdraftCharge()을 move function 하여 옮겨 왔을 때, 로직이 Account를 참조해야 한다.
2. Account를 참조하도록 할 수 도 있지만, 그렇게 되면 Account class에 있는 게 더 옳바르므로, 사용되는 변수인 daysOverdrawn 만 파라메터로 사용하도록 한다.

```java
public class AccountType {
    private boolean premium;

    public AccountType(boolean premium) {
        this.premium = premium;
    }

    public boolean isPremium() {
        return this.premium;
    }

    public double overdraftCharge(int daysOverdrawn) {
        if (isPremium()) {
            final int baseCharge = 10;
            if (daysOverdrawn <= 7) {
                return baseCharge;
            } else {
                return baseCharge + (daysOverdrawn - 7) * 0.85;
            }
        } else {
            return daysOverdrawn * 1.75;
        }
    }
}
public class Account {

    private int daysOverdrawn;

    private AccountType type;

    public Account(int daysOverdrawn, AccountType type) {
        this.daysOverdrawn = daysOverdrawn;
        this.type = type;
    }

    public double getBankCharge() {
        double result = 4.5;
        if (this.daysOverdrawn() > 0) {
            result += this.type.overdraftCharge(this.daysOverdrawn);
        }
        return result;
    }

    private int daysOverdrawn() {
        return this.daysOverdrawn;
    }

}
```



## 리팩토링 26. 클래스 추출하기 (Extract Class)

- 클래스가 다루는 책임(Responsibility)이 많아질수록 클래스가 점차 커진다.
- 클래스를 쪼개는 기준
  - 데이터나 메소드 중 일부가 매우 밀접한 관련이 있는 경우
  - 일부 데이터가 대부분 같이 바뀌는 경우
  - 데이터 또는 메소드 중 일부를 삭제한다면 어떻게 될 것인가?
- 하위 클래스를 만들어 책임을 분산 시킬 수도 있다.

1. 클래스의 기능이 커질 때, 책임을 분리하기 위해 클래스를 생성하여 기능을 분산시키자.

```java
public class Person {

    private String name;

    private String officeAreaCode;

    private String officeNumber;

    public String telephoneNumber() {
        return this.officeAreaCode + " " + this.officeNumber;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String officeAreaCode() {
        return officeAreaCode;
    }

    public void setOfficeAreaCode(String officeAreaCode) {
        this.officeAreaCode = officeAreaCode;
    }

    public String officeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }
}
public class Person {

    private final TelephoneNumber telephoneNumber;
    private String name;

    public Person(TelephoneNumber telephoneNumber, String name) {
        this.telephoneNumber = telephoneNumber;
        this.name = name;
    }

    public String telephoneNumber() {
        return this.telephoneNumber.toString();
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TelephoneNumber getTelephoneNumber() {
        return telephoneNumber;
    }
}
public class TelephoneNumber {
    private String officeAreaCode;
    private String officeNumber;

    public TelephoneNumber(String officeAreaCode, String officeNumber) {
        this.officeAreaCode = officeAreaCode;
        this.officeNumber = officeNumber;
    }

    public String officeAreaCode() {
        return officeAreaCode;
    }

    public String officeNumber() {
        return officeNumber;
    }

    public void setOfficeAreaCode(String officeAreaCode) {
        this.officeAreaCode = officeAreaCode;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    @Override
    public String toString() {
        return this.officeAreaCode + " " + this.officeNumber;
    }
}
```