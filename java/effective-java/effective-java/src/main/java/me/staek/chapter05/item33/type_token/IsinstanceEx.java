package me.staek.chapter05.item33.type_token;


/**
 * https://www.tutorialspoint.com/java/lang/class_isinstance.htm
 * https://www.baeldung.com/java-instanceof
 *
 * isInstance : 런타임에 인스턴스체크
 * instanceof : 컴파일타임에 인스턴스 체크
 */
public class IsinstanceEx {

    public static void main(String[] args) throws ClassNotFoundException {

        /**
         * isInstance() : 런타임 타임에 체크
         */
        Class<Long> cls = Long.class;
        Long l = new Long(86576);
        Double d = new Double(3.5);

        // checking for Long instance
        boolean retval = cls.isInstance(l);
        System.out.println(l + " is Long ? " + retval);

        // checking for Long instance
        retval = cls.isInstance(d);
        System.out.println(d + " is Long ? " + retval);


        /**
         * instanceof : 컴파일타임에 체크
         */
//        if (cls instanceof Long) {
//
//        }
    }
}
