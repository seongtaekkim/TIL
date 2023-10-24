package me.staek.chapter02.item05.singleton;


import me.staek.chapter02.item05.DefaultDictionary;
import me.staek.chapter02.item05.Dictionary;

import java.util.List;

/**
 * 싱글톤으로 인터페이스 생성
 * - api 변경 용이
 */
public class SpellChecker {

    private final Dictionary dictionary = new DefaultDictionary();

    private SpellChecker() {}

    public static final SpellChecker INSTANCE = new SpellChecker();

    public boolean isValid(String word) {
        // TODO 여기 SpellChecker 코드
        return dictionary.contains(word);
    }

    public List<String> suggestions(String typo) {
        // TODO 여기 SpellChecker 코드
        return dictionary.closeWordsTo(typo);
    }
}
