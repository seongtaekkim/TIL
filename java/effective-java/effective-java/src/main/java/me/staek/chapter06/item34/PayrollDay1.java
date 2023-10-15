package me.staek.chapter06.item34;

/**
 * 열거타입상수간 코드공유
 * - 콜라이언트에서 인자에대한 요청을 했을 때,
 * - 주중, 주말에 따라 로직이 분기된다.
 * - 열거타입상수가 추가되면 유지관리상 문제가 발생할 수 있다.
 *     1) 분기문에 로직추가 => Operation1 과 같은 문제
 *     2) 주중, 주말 열거타입상수를 선언하여 분기처리 => 조금더 간단하지만 결국 => Operation1 과 같은 문제 (열거타입상수간 코드공유)
 */
enum PayrollDay1 {
    MONDAY, TUESDAY, WEDNESDAY,
    THURSDAY, FRIDAY,
    SATURDAY, SUNDAY;

    private static final int MINS_PER_SHIFT = 8 * 60;

    int pay(int minsWorked, int payRate) {
        int basePay = minsWorked * payRate;

        int overtimePay;
        switch(this) {
            case SATURDAY :
            case SUNDAY :
                overtimePay = basePay / 2;
                break ;
            default :
                overtimePay = minsWorked <= MINS_PER_SHIFT ?
                        0 : (minsWorked - MINS_PER_SHIFT) * payRate / 2;
        }
        return basePay + overtimePay;
    }

    public static void main(String[] args) {
        for (PayrollDay1 day : values()) {
            System.out.printf("%-10s%d%n", day, day.pay(8 * 60, 1));
        }
    }
}
