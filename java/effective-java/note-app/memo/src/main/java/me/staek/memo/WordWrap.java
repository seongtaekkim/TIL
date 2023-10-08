package me.staek.memo;

import java.io.Serializable;

public class WordWrap implements Serializable {
    private static final long serialVersionUID = 2L;

    private boolean onoffWrap = false;

    public static final WordWrap WORD_WRAP_INIT = new WordWrap(false);

    public WordWrap(boolean onoffWrap) {
        this.onoffWrap = onoffWrap;
    }

    public boolean getOnoffWrap() {
        return onoffWrap;
    }

    public void setOnoffWrap(boolean onoffWrap) {
        this.onoffWrap = onoffWrap;
    }

    private Object readResolve() {
        return this;
    }
}
