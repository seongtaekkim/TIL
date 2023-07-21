# 냄새9. 기능편애 (Feature Envy)

- 어떤 모듈에 있는 함수가 다른 모듈에 있는 데이터나 함수를 더 많이 참조하는 경우에 발생한다.
  - 예) 다른 객체의 getter를 여러개 사용하는 메소드
- 관련 리팩토링 기술
  - “함수 옮기기 (Move Function)”를 사용해서 함수를 적절한 위치로 옮긴다.
  - 함수 일부분만 다른 곳의 데이터와 함수를 많이 참조한다면 “함수추출하기(Extract function)”로 함수를 나눈 다음에 함수를 옮길 수 있다.
- 만약에 여러 모듈을 참조하x고 있다면? 그 중에서 가장 많은 데이터를 참조하는 곳으로 옮기거나, 함수를 여러개로 쪼개서 각 모듈로 분산 시킬 수도 있다.
- 데이터와 해당 데이터를 참조하는 행동을 같은 곳에 두도록 하자.
- 예외적으로, 데이터와 행동을 분리한 디자인 패턴 (전략 패턴 또는 방문자 패턴)을 적용할 수도 있다.

1. Bill class 에서 전기요금과 가스요금을 직접계산 후 합계를 계산하는 로직이 있다.
2. 전기요금과 가스요금은 각각의 class에서 계산하는게 맞는데도 Bill class 가 기능을 과하게 크게 사용하고 있어서(Feature Envy) 이를 각각의 class에 적절하게 기능을 분산한다.

```java
public class Bill {

    private ElectricityUsage electricityUsage;

    private GasUsage gasUsage;

    public double calculateBill() {
        var electicityBill = electricityUsage.getAmount() * electricityUsage.getPricePerUnit();
        var gasBill = gasUsage.getAmount() * gasUsage.getPricePerUnit();
        return electicityBill + gasBill;
    }

}
public class ElectricityUsage {

    private double amount;

    private double pricePerUnit;

    public ElectricityUsage(double amount, double pricePerUnit) {
        this.amount = amount;
        this.pricePerUnit = pricePerUnit;
    }

    public double getAmount() {
        return amount;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }
}
public class GasUsage {

    private double amount;

    private double pricePerUnit;

    public GasUsage(double amount, double pricePerUnit) {
        this.amount = amount;
        this.pricePerUnit = pricePerUnit;
    }

    public double getAmount() {
        return amount;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }
}
```

1. 전기요금은 ElectricityUsage에서, 가스요금은 GasUsage에서 계산후 결과값에서 다시 계산하는 형태로 기능을 분산하도록 한다.

```java
public class Bill {

    private ElectricityUsage electricityUsage;

    private GasUsage gasUsage;

    public double calculateBill() {
        double electicityBill = electricityUsage.getElecticityBill();
        double gasBill = gasUsage.getGasBill();
        return electicityBill + gasBill;
    }
}
public class ElectricityUsage {

    private double amount;

    private double pricePerUnit;

    public ElectricityUsage(double amount, double pricePerUnit) {
        this.amount = amount;
        this.pricePerUnit = pricePerUnit;
    }

    public double getAmount() {
        return amount;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public double getElecticityBill() {
        return this.getAmount() * this.getPricePerUnit();
    }
}
public class GasUsage {

    private double amount;

    private double pricePerUnit;

    public GasUsage(double amount, double pricePerUnit) {
        this.amount = amount;
        this.pricePerUnit = pricePerUnit;
    }

    public double getAmount() {
        return amount;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public double getGasBill() {
        return this.getAmount() * this.getPricePerUnit();
    }
}
```