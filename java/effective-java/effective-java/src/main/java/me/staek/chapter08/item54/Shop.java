package me.staek.chapter08.item54;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 컨테이너 자원 반환 ==> null 혹은 빈 컨테이너
 */
public class Shop {

    private final List<Cheese> chesseInStock = new ArrayList<>();

    /**
     * 컨테이너 자원 상태에 따라 null 혹인 생성자복사 인스턴스 리턴
     * -> 클라이언트 예외처리 필요
     */
    public List<Cheese> getCheeses() {
        return chesseInStock.isEmpty() ? null : new ArrayList<>(chesseInStock);
    }

    /**
     * 빈 컬렉션 반환 (1) - 빈 컬렉션
     */
//    public List<Cheese> getCheeses() {
//        return new ArrayList<>(chesseInStock);
//    }

    /**
     * 빈 컬렉션 반환 (2) - 정팩메
     */
//    public List<Cheese> getCheeses() {
//        return chesseInStock.isEmpty() ? Collections.emptyList() : new ArrayList<>(chesseInStock);
//    }

    /**
     * 빈 컬렉션 반환 (3) - 배열
     */
//    public Cheese[] getCheeses() {
//        return chesseInStock.toArray(new Cheese[0]);
//    }

//    private static final Cheese[] EMPTY_CHEESE_ARRAY = new Cheese[0];
//    public Cheese[] getCheeses() {
//        return chesseInStock.toArray(EMPTY_CHEESE_ARRAY);
//    }
    public static void main(String[] args) {

        Shop shop = new Shop();
        List<Cheese> cheeses = shop.getCheeses();
        if (cheeses != null && cheeses.contains(Cheese.STILTON))
            System.out.println("good");
    }
}
