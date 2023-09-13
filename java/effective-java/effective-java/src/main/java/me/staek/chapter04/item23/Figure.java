package me.staek.chapter04.item23;

/**
 * 클래스 계층구조는 태그달린 클래스의 단점을 모두 날려버린다.
 * 각 의미를 독립된 클래스에 담아 관련없던 데이터 필드를 모두 제거 했다.
 * 존재하는 필드는 모두 final 이다.
 * 코드를 빼먹어 런타임오류가 발생하는 일이 없다.
 * 루트클래스의 코드를 건드리지 않고도 독립적으로 구조를 확장할 수 있다.
 */
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

class Square extends Rectangle {
    Square(double side) {
        super(side, side);
    }
}