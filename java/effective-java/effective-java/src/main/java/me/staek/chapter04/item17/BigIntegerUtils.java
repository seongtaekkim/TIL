package me.staek.chapter04.item17;


import java.math.BigInteger;

class ChildBigInteger extends BigInteger {

    public ChildBigInteger(String val) {
        super(val);
    }
}

/**
 * TODO 부모클래스는 불변객체인데 자식클래스가 생성된다면, 객체를 인자로 전달해야 할 상황에서
 *      가변객체일 경우를 대비해 부모클래스 타입이 아니라면 복사한 객체를 인자로 전달하도록 해야 한다.
 */
public class BigIntegerUtils {

    public static BigInteger safeInstance(BigInteger val) {
        System.out.println("BigIngeger??: " + (val.getClass() == BigInteger.class));
        return val.getClass() == BigInteger.class ? val : new BigInteger(val.toByteArray());
    }

    public static void main(String[] args) {

        BigInteger bi = new BigInteger("10");
        System.out.println(bi);
        BigInteger bigInteger = safeInstance(bi);
        System.out.println(bigInteger);

        ChildBigInteger cbi = new ChildBigInteger("10");
        System.out.println(cbi);
        BigInteger bigInteger1 = safeInstance(cbi);
        System.out.println(cbi);

    }
}
