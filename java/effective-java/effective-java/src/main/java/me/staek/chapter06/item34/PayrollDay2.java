package me.staek.chapter06.item34;

import static me.staek.chapter06.item34.PayrollDay2.PayType.WEEKDAY;
import static me.staek.chapter06.item34.PayrollDay2.PayType.WEEKEND;

/**
 * 열거타입상수간 코드공유 : The strategy enum pattern
 *
 * PayrollDay2 의 상수를 정의할 때 생성자를
 * - 계산을 담당하는 내부 enum class 상수로 지정한다.
 * - 상수 정의 시 계산을 담당하는 "전략"을 무조건 선택해야 하므로 안전하다. (두개의 열거타입상수간 관계를 강제함)
 */
enum PayrollDay2 {
    MONDAY(WEEKDAY), TUESDAY(WEEKDAY), WEDNESDAY(WEEKDAY),
    THURSDAY(WEEKDAY), FRIDAY(WEEKDAY),
    SATURDAY(WEEKEND), SUNDAY(WEEKEND);

    private final PayType payType;

    PayrollDay2(PayType payType) { this.payType = payType; }

    int pay(int minutesWorked, int payRate) {
        return payType.pay(minutesWorked, payRate);
    }

    // The strategy enum type
    enum PayType {
        WEEKDAY {
            int overtimePay(int minsWorked, int payRate) {
                return minsWorked <= MINS_PER_SHIFT ? 0 :
                        (minsWorked - MINS_PER_SHIFT) * payRate / 2;
            }
        },
        WEEKEND {
            int overtimePay(int minsWorked, int payRate) {
                return minsWorked * payRate / 2;
            }
        };

        abstract int overtimePay(int mins, int payRate);
        private static final int MINS_PER_SHIFT = 8 * 60;

        int pay(int minsWorked, int payRate) {
            int basePay = minsWorked * payRate;
            return basePay + overtimePay(minsWorked, payRate);
        }
    }

    public static void main(String[] args) {
        for (PayrollDay2 day : values())
            System.out.printf("%-10s%d%n", day, day.pay(8 * 60, 1));
    }
}
