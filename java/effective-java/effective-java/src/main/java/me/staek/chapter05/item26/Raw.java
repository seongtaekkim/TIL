package me.staek.chapter05.item26;

import java.util.ArrayList;
import java.util.List;

/**
 * 런타임에 실패
 * - unsafeAdd 메서드가 raw 타입(List)을 사용
 */
public class Raw {
    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();
        unsafeAdd(strings, Integer.valueOf(42));
        String s = strings.get(0); // 컴파일러가 자동으로 형변환 코드를 넣어준다.
    }

    /**
     * Object type이 String type인 List에 추가되어 암시적 형변환 시점에 런타임에러가 발생가능하다.
     */
    private static void unsafeAdd(List list, Object o) {
        list.add(o);
    }
}
