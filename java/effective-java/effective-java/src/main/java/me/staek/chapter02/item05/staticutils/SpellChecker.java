package me.staek.chapter02.item05.staticutils;


import me.staek.chapter02.item05.DefaultDictionary;
import me.staek.chapter02.item05.Dictionary;

import java.util.List;

/**
 * 유틸리티 클래스로 생성
 */
public class SpellChecker {

    private static final Dictionary dictionary = new DefaultDictionary();

    private SpellChecker() {}

    public static boolean isValid(String word) {
        // TODO 여기 SpellChecker 코드
        return dictionary.contains(word);
    }

    public static List<String> suggestions(String typo) {
        // TODO 여기 SpellChecker 코드
        return dictionary.closeWordsTo(typo);
    }
}
