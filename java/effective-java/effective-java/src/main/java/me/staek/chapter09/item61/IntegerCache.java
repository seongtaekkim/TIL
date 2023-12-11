package me.staek.chapter09.item61;

/**
 * VM option : -XX:AutoBoxCacheMax=size
 * 기본캐싱범위 : -128 ~ 127
 *
 * Integer는 VM option 으로 컨트롤 되지만
 * Long 은 안됨
 */
public class IntegerCache {
    public static void main(String[] args) {

        /**
         * 객체 itentity 비교
         */
        Integer a0 = new Integer(127);
        Integer b0 = new Integer(127);
        System.out.println(a0 == b0);

        /**
         *  캐싱정보가 불려와
         *  객체 identity가 같다.
         */
        Integer b = Integer.valueOf(127);
        Integer a = Integer.valueOf(127);
        System.out.println(a == b);


        /**
         * 캐싱이 없어서 인스턴스가 생성되어 비교됨
         */
        Integer a2 = Integer.valueOf(128);
        Integer b2 = Integer.valueOf(128);
        System.out.println(a2 == b2);
    }
}
