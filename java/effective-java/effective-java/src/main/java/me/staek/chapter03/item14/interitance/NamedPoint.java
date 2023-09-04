package me.staek.chapter03.item14.interitance;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;


/**
 * TODO 부모클래스에 Comparable가 구현되어 있을 때,
 *      자식클래스에 필드하나가 추가되면(equals 에 추가되는) TreeSet 생성자에 Comparator 구현체를 인자로 넘겨 해결 가능하다.
 */
public class NamedPoint extends Point {

    final private String name;

    public NamedPoint(int x, int y, String name) {
        super(x, y);
        this.name = name;
    }

    @Override
    public String toString() {
        return "NamedPoint{" +
                "name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public static void main(String[] args) {
        NamedPoint p1 = new NamedPoint(1, 0, "keesun");
        NamedPoint p2 = new NamedPoint(1, 0, "whiteship");

        Set<NamedPoint> points = new TreeSet<>(new Comparator<NamedPoint>() {
            @Override
            public int compare(NamedPoint p1, NamedPoint p2) {
                int result = Integer.compare(p1.getX(), p2.getX());
                if (result == 0) {
                    result = Integer.compare(p1.getY(), p2.getY());
                }
                if (result == 0) {
                    result = p1.name.compareTo(p2.name);
                }
                return result;
            }
        });

        points.add(p1);
        points.add(p2);

        System.out.println(points);
    }

}
