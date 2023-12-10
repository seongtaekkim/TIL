package me.staek.chapter08.item50;

import java.util.Date;

public class Attacks {
    public static void main(String[] args) {
        // 공격 - 생성자
        Date start = new Date();
        Date end = new Date();
        Period p = new Period(start, end);
        end.setYear(78);
        System.out.println(p);

        // 공격 - 매개변수
        start = new Date();
        end = new Date();
        p = new Period(start, end);
        p.end().setYear(78);
        System.out.println(p);
    }
}
