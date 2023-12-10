package me.staek.chapter08.item52;

import java.util.List;

/**
 * 재정의한 메서드는, 런타임에 실제 클래스에 대한 정보에 의해 호출됨.
 */
public class Overriding {
    public static void main(String[] args) {
        List<Wine> wineList = List.of(
                new Wine(), new SparklingWine(), new Champagne());

        for (Wine wine : wineList)
            System.out.println(wine.name());
    }
}
