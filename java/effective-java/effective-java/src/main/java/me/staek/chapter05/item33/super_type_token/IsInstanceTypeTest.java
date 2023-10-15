package me.staek.chapter05.item33.super_type_token;

public class IsInstanceTypeTest {

    static class Super {

    }
    static class Sub extends  Super {

    }

    public static void main(String[] args) {
        Super a = new Super();
        Super b = new Sub();

        System.out.println(a.getClass().isInstance(b)); // Sub는 Super 타입이므로 true
        System.out.println(b.getClass().isInstance(a)); // Super는 Sub타입이 될 수 없으므로 false

        System.out.println(a instanceof Super);
        System.out.println(a instanceof Sub);
        System.out.println(b instanceof Super);
        System.out.println(b instanceof Sub);
    }
}
