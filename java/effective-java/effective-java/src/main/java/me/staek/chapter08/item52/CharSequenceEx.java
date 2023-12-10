package me.staek.chapter08.item52;


/**
 * java에서 다중정의 혼동을 최소화 하기위해
 * 위임 방식을 선택하였다.
 *
 * // String.java
 *     public boolean contentEquals(StringBuffer sb) {
 *         return contentEquals((CharSequence)sb);
 *     }
 */
public class CharSequenceEx {
    public static void main(String[] args) {
        String test = new String("test");
        StringBuffer sb = new StringBuffer();
        StringBuilder sb2 = new StringBuilder();
        sb.append("seongtki");
        sb2.append("test");
        System.out.println(test.contentEquals(sb));
        System.out.println(test.contentEquals(sb2));

        /**
         * 책에서는 valueOf가 다중정의 문제라고 하는데,,, 이정도가 왜 문제인지 이해가 잘 안간다.
         */
        char[] charArray = sb2.toString().toCharArray();
        System.out.println(String.valueOf(sb2));;
        System.out.println(String.valueOf(charArray));;

    }
}
