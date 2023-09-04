package me.staek.chapter03.item14.composition;

/**
 * TODO equals 와 마찬가지로, 자식클래스에 필드하나가 추가되면(equals 에 추가되는) composition 이 편리하다.
 */
public class NamedPoint implements Comparable<NamedPoint> {

    private final Point point;
    private final String name;

    public NamedPoint(Point point, String name) {
        this.point = point;
        this.name = name;
    }

    public Point getPoint() {
        return this.point;
    }

    @Override
    public int compareTo(NamedPoint namedPoint) {
        int result = this.point.compareTo(namedPoint.point);
        if (result == 0) {
            result = this.name.compareTo(namedPoint.name);
        }
        return result;
    }
}
