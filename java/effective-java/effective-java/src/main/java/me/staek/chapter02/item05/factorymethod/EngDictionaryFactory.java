package me.staek.chapter02.item05.factorymethod;


import me.staek.chapter02.item05.Dictionary;
import me.staek.chapter02.item05.EngDictionary;

public class EngDictionaryFactory implements DictionaryFactory {
    @Override
    public Dictionary getDictionary() {

        Boolean b = new Boolean(false);
        return new EngDictionary();
    }
}
