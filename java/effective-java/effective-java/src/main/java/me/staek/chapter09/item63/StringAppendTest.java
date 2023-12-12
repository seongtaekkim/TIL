package me.staek.chapter09.item63;

public class StringAppendTest {
    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        String ret = "apple";
        for (int i=0 ; i<100000; i++) {
            ret = new String(ret + "apple");
            ret += "apple";
            ret = new StringBuilder(ret).append("LOVE").toString(); // java8 에서는 + 코드가 실제 이처럼 동작한다.
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);
    }
}
