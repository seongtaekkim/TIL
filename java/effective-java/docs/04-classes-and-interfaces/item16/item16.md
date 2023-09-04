# Item16. Inpublicclasses,useaccessormethods,notpublicfields



이따금 인스턴스 필드들을 모아놓는 일 외에는 아무 목적도 없는 퇴보한 클래스를 작성하려 할 때가 있다.

~~~java
	public class Point {
    public double x;
    public double y;
~~~

- 이런 클래스는 데이터 필드에 직접 접근할 수 있으니 캡슐화의 이점을 제공하지 못한다 (아이템15)





### public class 에서 필드 접근자

- API를 수정하지 않고는 내부표현을 바꿀 수 없고, 불변식을 보장할 수 없으며, 외부에서 필드에 접근할 때 부수작업을 수행할 수도 없다.
- 철저한 객체지향 프로그래머는 이런 클래스를 상당히 싫어해서 필드들을 모두 private로 바꾸고 public 접근자(getter)를 추가한다.
- public 클래스가 필드를 공개하면 이를 사용하는 클라이언트가 생겨날 것이므로 내부 표현방식을 마음대로 **바꿀 수 없게 된다.**

~~~java
class Point {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        // 부가 작업
        return x;
    }
    public double getY() { return y; }

    public void setX(double x) {
        // 부가 작업
        this.x = x;
    }
    public void setY(double y) { this.y = y; }
}
~~~



### package-private, private 중첩클래스 의 필드 접근자

**하지만 package-private 클래스 혹은 private 중첩 클래스라면 데이터 필드를 노출한다 해도 하등의 문제가 없다.**
그 클래스가 표현하려는 추상개념만 올바르게 표현해주면 된다.
이 방식은 클래스 선언 면에서나 이를 사용하는 클라이언트 코드 면에서나 접근자 방식보다 훨씬 깔끔하다.
클라이언트 코드가 이 클래스 내부 표현에 묶이기는 하나, 클라이언트도 어차피 이 클래스를 포함하는 패키지 안에서만 동작하는 코드일 뿐이다.
따라서 패키지 바깥 코드는 전혀 손대지 않고도 데이터 표현 방식을 바꿀 수 있다.
private 중첩 클래스의 경우라면 수정 범위가 더 좁아져서 이 클래스를 포함하는 외부 클래스까지로 제한된다.

- **라고 책에 되어있지만, 그렇다고 하더라도 필드명이 바뀌었을 때 적은 변경이라던가  getter/setter 등을 작성할때 부가작업을 할 수 있다는 이점을 버리면서 까지 할 건 아닌거 같다..**





### 자바 플랫폼 라이브러리에도 public 클래스의 필드를 직접 노출하지 말라는 규칙을 어기는 사례

- 아이템 67에서 설명하듯 내부를 노출한 Dimension 클래스의 심각한 성능문제는 오늘날까지도 해결되지 못했다.

~~~java
// java.awt.Dimension
public class Dimension extends Dimension2D implements java.io.Serializable {
    public int width;
    public int height;
  ...
~~~

~~~java
    public static void main(String[] args) {
        Button button = new Button("hello button");
        button.setBounds(0, 0, 20, 10);
        Dimension size = button.getSize();
        doSomething(size);
    }

    private static void doSomething(Dimension size) {
        Dimension d = new Dimension();
        d.height = size.height;
        d.width = size.width;
        // ... etc ...
    }
    ...
~~~

- public 클래스의 필드가 불변이라면 직접 노출할 때의 단점이 조금은 줄어들지만, 여전히 좋은생각이 아니다.
- API를 변경하지 않고는 표현 방식을 바꿀 수 없고, 필드를 읽을 때 부수작업을 수행할 수 없다는 단점은 여전하다.





-  **불변식은** 보장할 수 있게 된다.
- 예컨대 다음 클래스는 각 인스턴스가 유효한 시간을 표현함을 보장한다.

~~~java
public final class Time {
    private static final int HOURS_PER_DAY    = 24;
    private static final int MINUTES_PER_HOUR = 60;

    public final int hour;
    public final int minute;

    public Time(int hour, int minute) {
        if (hour < 0 || hour >= HOURS_PER_DAY)
            throw new IllegalArgumentException("Hour: " + hour);
        if (minute < 0 || minute >= MINUTES_PER_HOUR)
            throw new IllegalArgumentException("Min: " + minute);
        this.hour = hour;
        this.minute = minute;
    }
  ...
~~~

- 그렇다고 해도 위에서 말한 장점을 버리면서까지 굳이 public을 가져갈 이유가 되는지 모르겠다.



## 정리

Public 클래스는 절대가변필드를 직접 노출해서는 안된다.
불변 필드라면 노출해도 덜 위험하지만 완전히 안심할 수는 없다.
하지만 package-private 클래스나 private 중첩클래스에서는 종종 (불변이든 가변이든) 필드를 노출하는 편이 나을때도 있다.

**-> 라고 쓰여있으나 나는 그냥 private 필드에 접근자 함수를 따로 만들 것 같다.**

















