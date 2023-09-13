package me.staek.chapter04.item23;

/**
 * 태크클래스 구현
 * - 한 클래스에 여러 관점의 기능이 뒤섞여 있어 객체지향적이지 않다.
 * - 열거타입 선언, 태그필드, switch 문 등 쓸데 없는 코드가 많다.
 * - 여러 구현이 한 클래스에 혼합되어 있어서 가독성이 좋지 않다.
 * - 다른 의미를 위한 코드도 언제나 함께하니 메모리도 많이 사용한다.
 * - 필드들을 final로 선언하려면 해당 의미에 쓰이지 않는 필드들까지 생성자에서 초기화해서 써야 한다. (쓰지 않는 필드를 초기화하는 불필요한 코드가 늘어난다.)
 * - 또다른 의미를 추가하려면 코드를 수정해야 한다.
 * - Switch 등의 분기문이 필요하므로, 코드가 추가된다.
 * - 인스턴스의 타입만으로는 현재 나타내는 의미를 알 길이 전혀 없다.
 */
class TagedFigure {
    enum Shape { RECTANGLE, CIRCLE, SQUARE };

    // 태그 필드 - 현재 모양을 나타낸다.
    final Shape shape;

    // 다음 필드들은 모양이 사각형(RECTANGLE)일 때만 쓰인다.
    double length;
    double width;

    // 다음 필드는 모양이 원(CIRCLE)일 때만 쓰인다.
    double radius;

    // circle 용 생성자
    TagedFigure(double radius) {
        shape = Shape.CIRCLE;
        this.radius = radius;
    }

    // 사각형용 생성자
    TagedFigure(double length, double width) {
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
