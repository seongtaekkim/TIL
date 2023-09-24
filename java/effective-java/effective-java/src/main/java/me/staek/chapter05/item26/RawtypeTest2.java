package me.staek.chapter05.item26;

import java.util.ArrayList;
import java.util.List;

/**
 * 로우타입을 사용하게 되면
 * - unsafeAdd 메서드가 raw 타입(List)을 인자로 받으면, rawtype인 list에 입력이 된다.
 * - 그 후 출력 시 String으로 타입 캐스팅하는 시점에 런타임에러가 발생하게 된다.
 *
 * 반면 인자로 매개변수화 타입으로 명시적으로 받게되면,
 * -> List<String> list 라는 (표현력) 과
 * -> String이 아닌 타입 입력 시 컴파일 에러가 발생하여 (안정성) 두가지 장점이 생긴다.
 */
public class RawtypeTest2 {
    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();
        unsafeAdd(strings, Integer.valueOf(42));
        String s = strings.get(0); // 컴파일러가 자동으로 형변환 코드를 넣어준다.
    }

//    private static void unsafeAdd(List<String> list, String o) {
    private static void unsafeAdd(List list, Object o) {
        list.add(o); // 경고 발생함
    }
}
