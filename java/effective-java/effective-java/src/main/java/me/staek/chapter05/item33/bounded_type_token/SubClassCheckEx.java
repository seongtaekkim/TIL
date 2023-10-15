package me.staek.chapter05.item33.bounded_type_token;

/**
 * asSubclass, isAssignableFrom 예제
 */
public class SubClassCheckEx {

    static class Super<T> {
        T value;
    }

    static class Sub extends Super<String> {

    }

    public static void main(String[] args) throws NoSuchFieldException {


        Sub sub = new Sub();
        Super super1 = new Super();
        String s = new String("a");

        /**
         * 호출자가 인자에 할당될 수 있는가, subclass인가
         */
//        Class<? extends Super> subclass = Sub.class.asSubclass(Super.class);
        Class<? extends Super> subclass = sub.getClass().asSubclass(super1.getClass());
//        Class<? extends String> subclass = Sub.class.asSubclass(String.class); // ClassCastException
        System.out.println(subclass);
        /**
         *  인자가 호출자에 할당될 수 있는가를 체크
         */
        System.out.println(super1.getClass().isAssignableFrom(sub.getClass())); // sub는 super에 할당될 수 있다/
        System.out.println(sub.getClass().isAssignableFrom(super1.getClass())); // super는 sub에 할당될 수 없다.
        System.out.println(super1.getClass().isAssignableFrom(s.getClass())); // string은 super1에 할당될 수 없다.
    }
}
