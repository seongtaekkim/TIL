package me.staek.chapter04.item16;

/**
 * 어차피 불변객체이기 때문에 public 도 상관 없다고 한다.
 * 하지만 method 의 후처리 작업이나, 필드이름 변경시 side effect 를 고려하면 좋은방법은 아닌거 같다
 */
public final class Time {
    private static final int HOURS_PER_DAY    = 24;
    private static final int MINUTES_PER_HOUR = 60;

    public final int hour;
    public final int minute;

    public Time(int hour, int minute) {
        if (hour < 0 || hour >= HOURS_PER_DAY)
            throw new IllegalArgumentException("Hour: " + hour);
        if (minute < 0 || minute >= MINUTES_PER_HOUR)
            throw new IllegalArgumentException("Min: " + minute);
        this.hour = hour;
        this.minute = minute;
    }
    // ....
}
