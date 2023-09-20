package me.staek.chapter05.item28;

import java.util.ArrayList;
import java.util.List;


/**
 * 제네릭은 런타임에 타입이 소거된다.
 * - 실제로는
 *
 *    LINENUMBER 10 L1
 *     ALOAD 1
 *     LDC "effective-master"
 *     INVOKEINTERFACE java/util/List.add (Ljava/lang/Object;)Z (itf)
 *     POP
 *    L2
 *     LINENUMBER 11 L2
 *     ALOAD 1
 *     ICONST_0
 *     INVOKEINTERFACE java/util/List.get (I)Ljava/lang/Object; (itf)
 *     CHECKCAST java/lang/String
 *     ASTORE 2
 *
 */
public class GenericWithErasure {
    public static void main(String[] args) {
        List<String> studyName = new ArrayList<>();
        studyName.add("effective-master");
        String name = studyName.get(0);
        System.out.println(name);

        // 실제로는 아래처럼 동작한다고 보면 된다.
        // javac -Xlint:unchecked MyGeneric.java // warning 발생
        List studyNameRuntime = new ArrayList<>();
        studyNameRuntime.add("effective-master");
        Object o = studyNameRuntime.get(0);
        name = (String)o;
        System.out.println(name);
    }

}
