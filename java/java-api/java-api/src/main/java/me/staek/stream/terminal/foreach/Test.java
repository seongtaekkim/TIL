package me.staek.stream.terminal.foreach;

import java.util.stream.IntStream;

/**
 * 이펙티브 자바 아이템 46
 * forEach 연산은 최종 연산 중 기능이 가장 적고 가장 ‘덜’ 스트림답기 때문에,
 * forEach 연산은 스트림 계산 결과를 보고할 때(주로 print 기능)만 사용하고 계산하는 데는 쓰지 말자
 */
public class Test {
    public static void main(String[] args) {


          //for-loop로 짠 경우
//        for (int i = 0; i < 100; i++) {
//            if (i > 50) {
//                break;
//                //50번 돌고 반복을 종료한다.
//            }
//            System.out.println(i);
//        }

//        IntStream.range(1, 100).forEach(i -> {
//            if (i > 10)
//                return;
//            System.out.println(i);
//        });


        IntStream.range(1, 100)
                .filter(i -> i <= 10)
                .forEach(System.out::println);
    }
}
