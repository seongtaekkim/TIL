# item60 정확한 답이 필요하다면 float와 double은 피하라



float, double 타입은 과학과 공학 계산용으로 설계되었다.

이진 부동소수점 연산에 쓰이며, 넓은 범위의 수를 빠르게 정밀한 근사치로 계산하도록 설계되었다.

따라서 정확한 결과가 필요할 때 사용하면 안된다.

특히 금융관련 계산과 맞지 않다.

- 0.1 혹은 10의 음의 거듭제곱을 표현할 수 없기 때문.

~~~java
System.out.println(1.03 - 0.42); // 0.6100000000000001
System.out.println(1.00 - 9 * 0.10); // 0.09999999999999998
~~~

 

### double, float 문제점

- 금융계산 등에 전혀 맞지 않는다.
- BigDecimal, int , long을 사용해야 한다.

~~~java
public static void main(String[] args) {
    double funds = 1.00;
    int itemsBought = 0;
    for (double price = 0.10; funds >= price; price += 0.10) {
        funds -= price;
        itemsBought++;
    }
    System.out.println(itemsBought + " items bought."); // 3
    System.out.println("Change: $" + funds); // 0.3999999999999999
}
~~~



### 해결방안

1. BigDecimal
   - 단점 : 클래스 사용법을 익혀야 하고, 비교적 느리다

~~~java
final BigDecimal TEN_CENTS = new BigDecimal(".10");

int itemsBought = 0;
BigDecimal funds = new BigDecimal("1.00");
for (BigDecimal price = TEN_CENTS;
     funds.compareTo(price) >= 0;
     price = price.add(TEN_CENTS)) {
    funds = funds.subtract(price);
    itemsBought++;
}
System.out.println(itemsBought + " items bought.");
System.out.println("Money left over: $" + funds);
~~~

2. 기본타입 사용

   - int, long을 쓰자.

   - 범위가 제한되고 소수점을 직접 관리해야 한다.

~~~java
// 기본타입을 사용하기 위해 달러를 센트로 변경하여 소수점 문제를 해결.
public static void main(String[] args) {
    int itemsBought = 0;
    int funds = 100;
    for (int price = 10; funds >= price; price += 10) {
        funds -= price;
        itemsBought++;
    }
    System.out.println(itemsBought + " items bought.");
    System.out.println("Cash left over: " + funds + " cents");
}
~~~





## 정리

정확한 답이 필요한 계산에는 float이나 double을 피하라.

코딩할 때 불편함이나 성능저하를 신경 안쓰고 싶으면 BigDecimal 을 사용하라.

- 8가지 반올림모드를 사용할 수 있다.

- 성능이 중요하고 소수점을 직접 추적할 수 있고 숫자가 너무 크지 않다면 int, long을 사용하라.

- int : 9자리 십진수로 표현가능할 때 사용

- long : 18자리 십진수로 표현 가능할 때 사용

- BigDecimal : 18보다 큰 십진수 표현해야 할 때 사용

  