package me.staek.chapter05.item27;

import java.util.HashSet;
import java.util.Set;

/**
 * intellij ->> ADD VM Option: -Xlint:unchecked
 * console  ->> javac XlintTest.java -Xlint:unchecked
 *
 * 위 옵션으로 컴파일을 하면 워닝이 출려된다고 하는데 나는 안됨.. 왜지? (jvm11, 17)
 * -> names.add("aeg"); 이렇게 제네릭을 사용하니까 경고가 잘 뜬다.
 *
 */
public class XlintTest {

    public static void main(String[] args) {
        Set names = new HashSet();
        names.add("aeg");
        Set<String> strings = new HashSet<>();
    }
}
