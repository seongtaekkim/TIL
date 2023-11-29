package me.staek.stream.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 스트림은 데이터를 보관하지 않는다.
 *
 * 중개형 오퍼레이션은 기본적으로 터미널오퍼레이터 오기 전까지 실행되지 않는다.
 * 중개형 오퍼레이터는 여러개가 올 수 있다. 안올수도 있다. (레이지하게 처리됨.)
 * 종료향 오퍼레이터는 반드시 하나 있어야한다. 이게 실행되기 전까지 중개형 오퍼레이터는 무의미 하다.
 * Functional in nature : 근본적으로 펑셔널 하다.
 *
 * 병렬처리 : parallelStream() 내부에 스플레이터가 알아서 해줌
 * 무조건 빠른게 아니다.
 * 그럼 요즘 리액티브 스트림이 등장할 이유가 없다.
 * 스레드 만들고 관리하는 비용이 있다.
 * 데이터가 매우 클 경우에 쓸만할 것이다.
 *
 * 파이프라인 : 오퍼레이션들의 집합
 *
 */
public class Stream_Ex1 {
    public static void main(String[] args) {

        List<String> names = new ArrayList<>();
        names.add("seongtki");
        names.add("jseo");
        names.add("san");
        names.add("hannkim");
        names.add("samin");

        /**
         * 주의사항
         * 종료연산에 중개연산이 처리되기 때문에 중개연산에서 print는 의미가 없다.
         */
        names.stream().map((s) -> {
            System.out.println(s);
            return s.toUpperCase();
        });
        System.out.println("---------------------");

        List<String> collect = names.stream().map((s) -> {
            System.out.println(s);
            return s.toUpperCase();
        }).collect(Collectors.toList()); // Stream default method가 추가되어  .collect(Collectors.toList()) => .toList(); 형태가 가능함.
        collect.forEach(System.out::println);

        System.out.println("---------------------");

        List<String> collect1 = names.parallelStream().map((s) -> {
            System.out.println(s + " " + Thread.currentThread().getName());
            return s.toUpperCase();
        }).toList();

        System.out.println("---------------------");
        collect1.forEach(System.out::println);
//        names.forEach(System.out::println);


    }

}
