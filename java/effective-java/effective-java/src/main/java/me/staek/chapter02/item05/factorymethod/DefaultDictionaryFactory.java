package me.staek.chapter02.item05.factorymethod;


import me.staek.chapter02.item05.DefaultDictionary;
import me.staek.chapter02.item05.Dictionary;

public class DefaultDictionaryFactory implements DictionaryFactory {
    @Override
    public Dictionary getDictionary() {
        return new DefaultDictionary();
    }
}
