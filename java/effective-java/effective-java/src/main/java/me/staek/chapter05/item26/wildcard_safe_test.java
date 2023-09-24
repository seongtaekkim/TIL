package me.staek.chapter05.item26;

import java.util.HashSet;
import java.util.Set;

/**
 * rawtype vs 한정적 와일드카드
 * - 둘 다 모든 타입에 대한 입력을 받을 수 있다.
 * - rawtype은 add로 모든 타입을 추가할 수 있다. (안정적이지 않음)
 * - 한정적 와일드카드는 add 가 불가하다 (null만 가능) -> 안전함
 *
 */
public class wildcard_safe_test {

    static int numElementsInCommon(Set<?> s1, Set<?> s2) {
//    static int numElementsInCommon(Set s1, Set s2) {
        int result = 0;
        for (Object o1 : s1) {
            if (s2.contains(o1)) {
                result++;
            }
        }

        System.out.println("-----");
        return result;
    }

    public static void main(String[] args) {
        // 함수 테스트
        System.out.println(wildcard_safe_test.numElementsInCommon(Set.of(1, 2, 3), Set.of(1, 2)));


        // same area test
        Set<?> set = new HashSet<>();
        set.add(null);
//        set.add("1"); // compile error
        set.stream().forEach(System.out::println);

        Set rawSet = new HashSet<>();
        rawSet.add(null);
        rawSet.add("test");
        rawSet.add(22222); // potential runtime error
        for (Object o : rawSet){
//            System.out.println((String)o);
            System.out.println(o);
        }
    }
}
