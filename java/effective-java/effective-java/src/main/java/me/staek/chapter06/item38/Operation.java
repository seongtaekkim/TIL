package me.staek.chapter06.item38;

/**
 * item34에서 사용한 abstract apply()를 확장을 위해 interface로 추출.
 */
public interface Operation {
    default double apply(double x, double y) {
        return x * y;
    }
}
