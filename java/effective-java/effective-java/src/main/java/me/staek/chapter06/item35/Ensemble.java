package me.staek.chapter06.item35;

/**
 * original을 사용하지 말고,
 * 열거타입상수 정의 시 임의 값을 저장하여 사용하는 방식이 좋다.
 */
public enum Ensemble {
    SOLO(1), DUET(2), TRIO(3), QUARTET(4), QUINTET(5),
    SEXTET(6), SEPTET(7), OCTET(8), DOUBLE_QUARTET(8),
    NONET(9), DECTET(10), TRIPLE_QUARTET(12);

    private final int numberOfMusicians;
    Ensemble(int size) { this.numberOfMusicians = size; }
    public int numberOfMusicians() { return numberOfMusicians; }

    public static void main(String[] args) {
        System.out.println(Ensemble.SOLO.numberOfMusicians());
    }
}
