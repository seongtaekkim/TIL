package me.staek.chapter02.item05.factorymethod;


import me.staek.chapter02.item05.Dictionary;
import me.staek.chapter02.item05.MockDictionary;

public class MockDictionaryFactory implements DictionaryFactory {
    @Override
    public Dictionary getDictionary() {
        return new MockDictionary();
    }
}
