## Enumeration



- 상수목록을 담을 수 있는 데이터 타입.
- 특정한 변수가 가질 수 있는 값을 제한할 수 있다. (type-safety)를 보장할 수 있다.
  - 다른타입으로 정의할 경우 다른의미로 사용할 때의 예외처리가 필수이다.![스크린샷 2023-08-05 오후 1.58.22](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-08-05 오후 1.58.22.png)
- 싱글톤 패턴을 구현할 때 사용하기 도 한다.



~~~
public enum OrderStatus {

    PREPARING(0), SHIPPED(1), DELIVERING(2), DELIVERED(3);
    ...
}
~~~

- jvm 내에서 인스턴스가 딱 하나만 생성이 된다.



- == 로 비교. (인스턴스가 한개이기 때문.)
- equals 의 nullPointerException 을 피할 수 있다.

~~~
  Order order = new Order();
  if (order.orderStatus == OrderStatus.DELIVERED) {
      System.out.println("delivered");
  }
~~~





### Reference

[[Java\] Enum, 자바의 열거타입을 알아보자](https://scshim.tistory.com/253)

https://techblog.woowahan.com/2527/