package me.staek.chapter09.item58;

import java.util.*;

/**
 * 중첩반복 예제(1)
 * 중첩반복의 경우, for문을 잘못 사용하면 런타임에러가 날 수 있다.
 * 이 경우, for-each 를 사용하면 매우 간단하면서 에러가 안나는 방식으로 코드가 가능하다.
 */
public class Card {
    private final Suit suit;
    private final Rank rank;

    enum Suit { CLUB, DIAMOND, HEART, SPADE }
    enum Rank { ACE, DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT,
        NINE, TEN, JACK, QUEEN, KING }

    static Collection<Suit> suits = Arrays.asList(Suit.values());
    static Collection<Rank> ranks = Arrays.asList(Rank.values());

    Card(Suit suit, Rank rank ) {
        this.suit = suit;
        this.rank = rank;
    }

    public static void main(String[] args) {
        List<Card> deck = new ArrayList<>();
        
        for (Iterator<Suit> i = suits.iterator(); i.hasNext(); )
            for (Iterator<Rank> j = ranks.iterator(); j.hasNext(); )
                deck.add(new Card(i.next(), j.next()));

//        for (Suit suit : suits)
//            for (Rank rank : ranks)
//                deck.add(new Card(suit, rank));
    }
}
