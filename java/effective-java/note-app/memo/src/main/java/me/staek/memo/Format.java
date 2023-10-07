package me.staek.memo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Format implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Format> resource = new ArrayList<>();
    public Format() {

    }
    public void put(Format format) {
        resource.add(format);
    }
    public Format get(int idx) {
        return resource.get(idx);
    }
    public int size() {
        return resource.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Format format = (Format) o;
        return fontSize == format.fontSize && fontStyle == format.fontStyle && Objects.equals(fontName, format.fontName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fontSize, fontName, fontStyle);
    }

    @Override
    public String toString() {
        return "Format{" +
                "fontSize=" + fontSize +
                ", fontName='" + fontName + '\'' +
                ", fontStyle=" + fontStyle +
                '}';
    }

    private int fontSize;
    private String fontName;
    private int fontStyle;

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public int getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }
}
