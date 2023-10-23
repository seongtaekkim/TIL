package me.staek.chapter06.item38;

import java.util.EnumSet;
import java.util.Set;

/**
 * EnumSet에서 확장된 Enum 사용 예제, 사용불가능 예제
 */
public class EnumSetMapTest {
    public static void main(String[] args) {
        Set<BasicOperation> s = EnumSet.of(BasicOperation.PLUS, BasicOperation.MINUS); // 가능
//        Set<Operation> s2 = EnumSet.of(BasicOperation.PLUS, BasicOperation.MINUS); // 불가능
//        Set<BasicOperation> s3 = EnumSet.of(BasicOperation.PLUS, ExtendedOperation.EXP); // 불가능
    }
}
