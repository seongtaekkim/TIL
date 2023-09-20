# 냄새 4. 긴 매개변수 목록 (Long Parameter List)

- 어떤 함수에 매개변수가 많을수록 함수의 역할을 이해하기 어려워진다.
  - 과연 그 함수는 한가지 을 하고 있는게 맞는가 ?
  - 불필요한 매개변수는 없는가?
  - 하나의 레코드로 뭉칠 수 있는 매개변수목록은 없는가?
- 어떤 매개변수를 다른 매개변수를 통해 알아낼 수 있다면, “매개변수를 질의 함수로 바꾸기 (Replace Parameter with Query)”를 사용할 수 있다.
- 기존 자료구조에서 세부적인 데이터를 가져와서 여러 매개변수로 넘기는 대신, “객체 통째로 넘기기 (Preserve Whole Object)”를 사용할 수 있다.
- 일부 매개변수들이 대부분 같이 넘겨진다면, “매개변수 객체 만들기 (Introduce Parameter Object)”를 적용할 수 있다.
- 매개변수가 플래그로 사용된다면, “플래그 인수 제거하기 (Remove Flag Argument)”를 사용할 수 있다.
- 여러 함수가 일부 매개변수를 공통적으로 사용한다면 “여러 함수를 클래스로 묶기 (Combine Functions into Class)”를 통해 매개변수를 해당 클래스의 필드로 만들고 매서드에 전달해야 할 매개변수 목록을 줄일 수 있다.



## **리팩토링 14. 매개변수를 질의 함수로 바꾸기 (Replace Parameter with Query)**

- 함수의 매개변수 목록은 함수의 다양성을 대변하며, 짧을수록 이해하기 좋다.
- 어떤 한 매개변수를 다른 매개변수를 통해 알아낼 수 있다면 “중복 매개변수”라 생각할 수있다.
- 매개변수에 값을 전달하는 것은 “함수를 호출하는 쪽”의 책임이다. 가능하면 함수를 호출하는 쪽의 책임을 줄이고 함수 내부에서 책임지도록 노력한다.
- `“임시 변수를 질의 함수로 바꾸기”`와 `“함수 선언 변경하기”`를 통해 이 리팩토링을 적용한다.

1. 호출함수 finalPrice()는 discountLevel 까지 책임을 가지고 있는 함수이다. (discounttLevel은 basePrice를 통해 알아낼 수 있는 인자 이다.)
2. disCountLevel을 계산하는 함수를 만들고, 실제 기능을 사용하는 discountPrice() 함수에서 discountLevel 함수를 호출하도록하여 책임을 이동시키도록 한다.

```java
public class Order {

    private int quantity;

    private double itemPrice;

    public Order(int quantity, double itemPrice) {
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public double finalPrice() {
        double basePrice = this.quantity * this.itemPrice;
        int discountLevel = this.quantity > 100 ? 2 : 1;
        return this.discountedPrice(basePrice, discountLevel);
    }

    private double discountedPrice(double basePrice, int discountLevel) {
        return discountLevel == 2 ? basePrice * 0.9 : basePrice * 0.95;
    }
}
public double finalPrice() {
        double basePrice = this.quantity * this.itemPrice;
        return this.discountedPrice(basePrice);
    }

    private int discountLevel() {
        return this.quantity > 100 ? 2 : 1;
    }

    private double discountedPrice(double basePrice) {
        return discountLevel() == 2 ? basePrice * 0.9 : basePrice * 0.95;
    }
```



## **리팩토링 15. 플래그 인수 제거하기 (Remove Flag Argument)**

- 플래그는 보통 함수에 매개변수로 전달해서, 함수 내부의 로직을 분기하는데 사용한다.
- 플래그를 사용한 함수는 차이를 파악하기 어렵다.
  - bookConcert(customer, false), bookConcert(customer, true)
  - bookConcert(customer), premiumBookConcert(customer)
- 조건문 분해하기 (Decompose Condition)를 활용할 수 있다.

1. 플래그는, 함수호출부분에서 어떤 역할을 하는지 알 수 없어 구현부를 살펴보아야 하고, 구현부는 플래그에 의해 분기하는 로직이 있을테니 복잡하다.
2. 이를 decompose condition 으로 로직을 단순화하면서 가독석을 높이는 코드로 만들 수 있다.

```java
public class Shipment {

    public LocalDate deliveryDate(Order order, boolean isRush) {
        if (isRush) {
            int deliveryTime = switch (order.getDeliveryState()) {
                case "WA", "CA", "OR" -> 1;
                case "TX", "NY", "FL" -> 2;
                default -> 3;
            };
            return order.getPlacedOn().plusDays(deliveryTime);
        } else {
            int deliveryTime = switch (order.getDeliveryState()) {
                case "WA", "CA" -> 2;
                case "OR", "TX", "NY" -> 3;
                default -> 4;
            };
            return order.getPlacedOn().plusDays(deliveryTime);
        }
    }
}
public class Shipment {

    public LocalDate regularDeliveryDate(Order order) {
        int deliveryTime = switch (order.getDeliveryState()) {
            case "WA", "CA" -> 2;
            case "OR", "TX", "NY" -> 3;
            default -> 4;
        };
        return order.getPlacedOn().plusDays(deliveryTime);
    }

    public LocalDate rushDeliveryDate(Order order) {
        int deliveryTime = switch (order.getDeliveryState()) {
            case "WA", "CA", "OR" -> 1;
            case "TX", "NY", "FL" -> 2;
            default -> 3;
        };
        return order.getPlacedOn().plusDays(deliveryTime);
    }
}
```



## **리팩토링 16. 여러 함수를 클래스로 묶기 (Combine Functions into Class)**

- 비슷한 매개변수 목록을 여러 함수에서 사용하고 있다면 해당 메소드를 모아서 클래스를 만들 수 있다.
- 클래스 내부로 메소드를 옮기고, 데이터를 필드로 만들면 메소드에 전달해야 하는 매개변수 목록도 줄일 수 있다.
- **섹션3의 리팩토링 10. 함수를 명령으로 바꾸기 와 같은 내용이다. (같은소스이므로 생략)**
- StudyPrinter class를 만들고, markdown print 하는 함수들을 move 한 후, 자주 사용되는 매개변수들을 맴버함수로 변경하여 보다 간결해보이도록 로직을 재구성 한다.