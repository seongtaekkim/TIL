package me.staek.stream.scenario;

import me.staek.stream.vo.Study;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Stream_Scenario {
    public static void main(String[] args) {
        /**
         * list 데이터 두 가지 정의
         * list 두가지에 대한 list studyWithStaek 를 생성
         */
        List<Study> effectiveStudies = new ArrayList<>();
        effectiveStudies.add(new Study(43, "item43", true));
        effectiveStudies.add(new Study(44, "item44", true));
        effectiveStudies.add(new Study(45, "item45", false));
        effectiveStudies.add(new Study(46, "item46", false));
        effectiveStudies.add(new Study(47, "item47", false));

        List<Study> cloudStudies = new ArrayList<>();
        cloudStudies.add(new Study(1, "container isolation", true));
        cloudStudies.add(new Study(2, "container resource", true));
        cloudStudies.add(new Study(3, "networking", true));
        cloudStudies.add(new Study(4, "overlay filesystem", true));
        cloudStudies.add(new Study(5, "overlay network", false));

        List<List<Study>> studyWithStaek = new ArrayList<>();
        studyWithStaek.add(effectiveStudies);
        studyWithStaek.add(cloudStudies);


        System.out.println("cloudstudy 중 overlay로 시작하는 title 모두 출력");
        cloudStudies.stream().filter(st -> st.getTitle().startsWith("overlay"))
                             .forEach(os -> System.out.println(os.getTitle()));

        System.out.println("cloudstudy 중 종료되지 않은 title 모두 출력");
        // Predicate
//        System.out.println(Predicate.not(Study::isClosed));;
//        System.out.println(Predicate.not((Study t) -> effectiveStudies.get(0).isClosed()));;
        cloudStudies.stream().filter(Predicate.not(Study::isClosed))
                             .forEach(st -> System.out.println(st.getTitle()));

        System.out.println("========================");
        cloudStudies.stream()
                .map(study -> study.getTitle())
                .forEach(x -> System.out.println(x));

        System.out.println("모든 스터디 list를 하나의 리스트로 합친 후 title을 출력");
        studyWithStaek.stream().flatMap(list -> list.stream())
                               .forEach(st -> System.out.println(st.getTitle()));


        System.out.println("cloudstudy 중 over로 시작하는 title이 있는지 여부 출력");
        // anyMatch : 종료연산
        boolean over = cloudStudies.stream().anyMatch(st -> st.getTitle().contains("over"));
        System.out.println(over);

        System.out.println("cloudstudy 중 over로 시작하는 title을 list로 변경 후 출력");
        List<Study> list = cloudStudies.stream().filter(st -> st.getTitle().contains("over"))
                .collect(Collectors.toList());
        list.forEach(st -> System.out.println(st.getTitle()));
    }
}
