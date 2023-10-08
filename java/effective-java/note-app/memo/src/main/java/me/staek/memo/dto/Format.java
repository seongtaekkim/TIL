package me.staek.memo.dto;

import java.awt.*;
import java.io.Serializable;

public class Format implements Serializable {

    private static final long serialVersionUID = 1L;
    public Format() {
        wrap = WordWrap.WORD_WRAP_INIT;
        font = MyFont.INIT_FONT;
    }


    private WordWrap wrap;

    private Font font;

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public WordWrap getWrap() {
        return wrap;
    }

    private Object readResolve() {
        return this;
    }
}
