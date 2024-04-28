package me.staek.chapter11.item83;

/**
 * javac -g Test.java
 * javap -c -l Test (c: 바이트코드, l: local variable table)
 *
 * local variable table은 슬롯단위의 배열로 되어 있다 접근은 인덱스단위로 하며,
 * 32비트로 되어 있다.
 * double, long은 연속된 슬롯을 차지하며, 접근 시 첫번째 인덱스를 참조함.
 */
public class OperandTest1 {
    public static void main(String[] args) {
//        int a = 1;
//        int b = 2;
//        int c;

//        c = a + b;

        double a = 1.0;
        double b = 10.0;
        double c;
        c = a + b;
    }
}
