package me.staek.chapter09.item58;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;

/**
 * 중첩반복 예제(2)
 *
 * 코드가 잘못되었지만 운이좋게 런타임에러가 안날 수 있다.
 * 이 경우는 발견이 안되어 더 심각한 문제가 있다.
 */
public class DiceRolls {
    enum Face { ONE, TWO, THREE, FOUR, FIVE, SIX }

    public static void main(String[] args) {
        Collection<Face> faces = EnumSet.allOf(Face.class);

        for (Iterator<Face> i = faces.iterator(); i.hasNext(); )
            for (Iterator<Face> j = faces.iterator(); j.hasNext(); )
                System.out.println(i.next() + " " + j.next());

        System.out.println("***************************");

        for (Face f1 : faces)
            for (Face f2 : faces)
                System.out.println(f1 + " " + f2);
    }
}
