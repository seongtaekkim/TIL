package me.staek.memo.code;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Program {
    public static final String APP_NAME = "note";
    public static final int WITDH = 500;
    public static final int HEIGHT = 500;

    public static final int DEFAULT_FONT_SIZE = 12;
    public static final String DEFAULT_FONT_NAME = Menu.ARIAL.value();
    public static final int DEFAULT_FONT_STYLE = Font.PLAIN;
    public static final Map<String, Integer> myMap;
    static {
        Map<String, Integer> aMap = new HashMap<>() ;
        aMap.put(Menu.SIZE8.value(), 8);
        aMap.put(Menu.SIZE12.value(), 12);
        aMap.put(Menu.SIZE16.value(), 16);
        aMap.put(Menu.SIZE24.value(), 24);
        myMap = Collections.unmodifiableMap(aMap);
    }
}
