package me.staek.chapter04.item17;

import java.awt.*;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO BigInteger가 관리하는 int[]은 새로운 객체가 생성되거나 같은 불변객체에 공유되도 안전하다.
 */
public class BigIntegerEx {

    public static void main(String[] args) {
        BigInteger ten = BigInteger.TEN; // static final 객체를 제공한다.
        System.out.println(ten);
        BigInteger minusTen = ten.negate(); // int 배열을 새로운생성자로 넘겨서 같은 주소를 가지지만,
                                            // 어챠피 불변이기에 문제가 없다.
        System.out.println(ten);


        final Set<Point> points = new HashSet<>();
        Point firstPoint = new Point(1, 2); // 값이 바뀔 수 있기에 Set은 불변이 아니게 되었다.
        points.add(firstPoint);
    }
}
