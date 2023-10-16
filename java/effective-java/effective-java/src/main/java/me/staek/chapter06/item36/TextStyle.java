package me.staek.chapter06.item36;

import java.util.*;

public class TextStyle {
    public enum Style {BOLD, ITALIC, UNDERLINE, STRIKETHROUGH}

    // Any Set could be passed in, but EnumSet is clearly best
    public void applyStyles(Set<Style> styles) {
        System.out.printf("Applying styles %s to text%n",
                Objects.requireNonNull(styles));
    }

    // Sample use
    public static void main(String[] args) {
        TextStyle text = new TextStyle();
        text.applyStyles(EnumSet.of(Style.BOLD, Style.ITALIC));


//        EnumSet<Style> styles = EnumSet.allOf(Style.class);
//        EnumSet<Style> styles = EnumSet.noneOf(Style.class);
        EnumSet<Style> styles = EnumSet.of(Style.BOLD, Style.UNDERLINE);
//        EnumSet<Style> styles = EnumSet.range(Style.ITALIC, Style.STRIKETHROUGH);

        styles.add(Style.STRIKETHROUGH); // insert elements to enum set

        System.out.println(styles);
        Iterator<Style> it = styles.iterator(); // iterator
        while (it.hasNext())
            System.out.print(it.next() + ", ");
        System.out.println();

        styles.remove(Style.BOLD);
        System.out.println(styles);

        /**
         * immutable EnumSet
         * type : java.util.Collections$UnmodifiableCollection
         */
        Collection<Style> immutableStyles = Collections.unmodifiableCollection(styles);
        System.out.println(immutableStyles.getClass().getName());;
        immutableStyles.add(Style.BOLD); // UnsupportedOperationException

    }
}
