package me.staek.chapter04.item16;

/**
 * 접근자, 변경자를 통해 값에 접근하는게 좋다.
 * 1. 필드 명이 변경되었을 대 해야할 작업이 적다.
 * 2. 메서드에 부가작업을 할 수 있다.
 */
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
