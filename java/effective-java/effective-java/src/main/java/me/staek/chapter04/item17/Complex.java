package me.staek.chapter04.item17;

/**
 * 불변객체의 장점
 * - 함수형 프로그래밍에 적합하다. (피연산자에 함수를 적용한 결과를 반혼하지만 피연산자가 바뀌지는 않는다.)
 *   ->>> plus 함수가 객체를 생성리턴하는게 아닌, 기존변수에 더하는 식으로 진행된다면, 함수형 프로그래밍이 아니다.
 * - 불변객체는 단순한다.
 *   ->>> 가변객체에 비해 고려해야할 사항이 줄어듬
 * - 불변객체는 근본적으로 스레드 안전하여 따로 동기화할 필요가 없다.
 *   ->>> 여러 스레드에서 하나의 인스턴스 필드를 변경할 때 사용에 대한 우려사항이 없다.
 * - 불변객체는 안심하고 공유할 수 있다. (public static final)
 *   ->>> 스레드간, 함수간 재사용 및 공유가 가능함. final이 레퍼런스룰 참조하더라도 setter가 없으니 안전하다.
 * - 불변객체끼리는 내부데이터를 공유할 수 있다.
 *   ->>> BigIngeterEx.java 참조
 * - 객체를 만들 때 불변객체로 구성하면 이점이 많다.
 * - 실패원자성을 제공한다. (아이템 76, p407)
 *   ->>> 어떤 작업을 진행하다가 예외 등이 발생해 실패해도, 내부에 어떤 값도 변경되지 않음이 보장된다.
 * - 객체생성에 여러단계 필요: 다단계연산
 *   ->>> plus, minus, divideby 를 모두 샐행해야 한다면 현재로서는 인스턴스가 세개 만들어진다.
 *   ->>> 이 세개를 합쳐서 하나의 인스턴스가 나오도록 하면 된다.
 *   ->>> 혹은 가변동반클래스를 이용한다. (string -> StringBuilder)
 */
public final class Complex {
    private final double re;
    private final double im;

    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE  = new Complex(1, 0);
    public static final Complex I    = new Complex(0, 1);

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double realPart()      { return re; }
    public double imaginaryPart() { return im; }

    public Complex plus(Complex c) {
        return new Complex(re + c.re, im + c.im);
    }

    // 코드 17-2 정적 팩터리(private 생성자와 함께 사용해야 한다.) (110-111쪽)
    public static Complex valueOf(double re, double im) {
        return new Complex(re, im);
    }

    public Complex minus(Complex c) {
        return new Complex(re - c.re, im - c.im);
    }

    public Complex times(Complex c) {
        return new Complex(re * c.re - im * c.im,
                re * c.im + im * c.re);
    }

    public Complex dividedBy(Complex c) {
        double tmp = c.re * c.re + c.im * c.im;
        return new Complex((re * c.re + im * c.im) / tmp,
                (im * c.re - re * c.im) / tmp);
    }

    @Override public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Complex))
            return false;
        Complex c = (Complex) o;

        return Double.compare(c.re, re) == 0
                && Double.compare(c.im, im) == 0;
    }
    @Override public int hashCode() {
        return 31 * Double.hashCode(re) + Double.hashCode(im);
    }

    @Override public String toString() {
        return "(" + re + " + " + im + "i)";
    }
}
