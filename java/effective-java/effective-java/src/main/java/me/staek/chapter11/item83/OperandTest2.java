package me.staek.chapter11.item83;

/**
 * 32bit, 64bit 쓰기 차이가 난다면 시간 차이가 날까 ..?
 * 아니다!
 */
public class OperandTest2 {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        long sum =0L;
        long data = 1L;
        for (long i = 0; i<100000000000L ; i++) {
            sum = sum + data;
        }

//        int sum =0;
//        int data = 1;
//        for (long i = 0; i<100000000000L ; i++) {
//            sum = sum + data;
//        }

        long end = System.currentTimeMillis();
        System.out.println(end-start);
    }
}
