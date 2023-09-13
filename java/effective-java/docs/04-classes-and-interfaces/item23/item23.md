# 태그달린 클래스보다는 클래스 계층구조를 활용하라



- 두 가지 이상의 의미를 표현할 수 있으며, 그 중 현재 표현하는 의미를 태그 값으로 알려주는 클래스가 있다.

~~~java
class Figure {
    enum Shape { RECTANGLE, CIRCLE, SQUARE };

    // 태그 필드 - 현재 모양을 나타낸다.
    final Shape shape;

    // 다음 필드들은 모양이 사각형(RECTANGLE)일 때만 쓰인다.
    double length;
    double width;

    // 다음 필드는 모양이 원(CIRCLE)일 때만 쓰인다.
    double radius;

    // 원용 생성자
    Figure(double radius) {
        shape = Shape.CIRCLE;
        this.radius = radius;
    }

    // 사각형용 생성자
    Figure(double length, double width) {
        if (this.length == this.width) {
            shape = Shape.SQUARE;
        } else {
            shape = Shape.RECTANGLE;
        }

        this.length = length;
        this.width = width;
    }

    double area() {
        switch(shape) {
            case RECTANGLE, SQUARE:
                return length * width;
            case CIRCLE:
                return Math.PI * (radius * radius);
            default:
                throw new AssertionError(shape);
        }
    }
}
~~~



## 태그클래스 단점  - 장황하고 오류를 내기 쉽고 비효율적이다.
- 열거타입 선언, 태그필드, switch 문 등 쓸데 없는 코드가 많다.
- 여러 구현이 한 클래스에 혼합되어 있어서 가독성이 좋지 않다.
- 다른 의미를 위한 코드도 언제나 함께하니 메모리도 많이 사용한다.
- 필드들을 final로 선언하려면 해당 의미에 쓰이지 않는 필드들까지 생성자에서 초기화해서 써야 한다. (쓰지 않는 필드를 초기화하는 불필요한 코드가 늘어난다.)
- 또다른 의미를 추가하려면 코드를 수정해야 한다.
  - Switch 등의 분기문이 필요하므로, 코드가 추가된다.
- 인스턴스의 타입만으로는 현재 나타내는 의미를 알 길이 전혀 없다.



## 개선: 태그클래스 -> 계층구조 클래스

- 추상클래스를 정의하고 태그 값에 따라 동작이 달라지는 메서드들을 추상메서드로 선언한다.
- 확장한 구현체를 의미별로 하나씩 정의한다. (Circle, Rectangle)

~~~java
abstract class Figure {
    abstract double area();
}

class Circle extends Figure {
    final double radius;

    Circle(double radius) { this.radius = radius; }

    @Override double area() { return Math.PI * (radius * radius); }
}

class Rectangle extends Figure {
    final double length;
    final double width;

    Rectangle(double length, double width) {
        this.length = length;
        this.width  = width;
    }
    @Override double area() { return length * width; }
}

~~~

- 클래스 계층구조는 태그달린 클래스의 단점을 모두 날려버린다.
- 각 의미를 독립된 클래스에 담아 관련없던 데이터 필드를 모두 제거 했다.
- 존재하는 필드는 모두 final 이다.
- 코드를 빼먹어 런타임오류가 발생하는 일이 없다.
- 루트클래스의 코드를 건드리지 않고도 독립적으로 구조를 확장할 수 있다.





### 새로운 의미가 추가될 때클래스 계층구조에서라면 간단하게 추가될 수있다

~~~java
// 정사각형 추가
class Square extends Rectangle {
    Square(double side) {
        super(side, side);
    }
}
~~~





### 주의사항

- 불변객체로 구현했기때문에 문제가 없지만

- setWith, setHiehgt 등이 가능한 구조로 만든다면

- **리스코프 치환원칙 위배가 발생할 수 있다.**

  - ~~~java
    Rectangle rectangle = new Square();
    rectangle.setWidth(10);
    rectangle.setHeight(5);		
    System.out.println(rectangle.getArea());
    // result : 50
    ~~~

    ```java
    Rectangle rectangle = new Rectangle();
    rectangle.setWidth(10);
    rectangle.setHeight(5);
    System.out.println(rectangle.getArea());
    // result: 25
    ```



## 정리

태그달린 클래스를 써야 하는 상황은 거의 없다.
새로운 클래스를 작성하는데 태그 필드가 등장한다면 태그를 없애고 계층구조로 대체하는 방법을 생각해보자.
기존 클래스가 태그 필드를 사용하고 있다면 계층구조로 리팩터링 하는걸 고려하자.





