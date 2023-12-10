package me.staek.chapter08.item52;

import java.math.BigInteger;
import java.util.*;

/**
 * 다중정의 해결(2)
 * - instanceof 로 런타임에 타입을 파악한다.
 */
public class FixedCollectionClassifier {
    public static String classify(Collection<?> c) {
        return c instanceof Set  ? "Set" :
                c instanceof List ? "List" : "Unknown Collection";
    }

    public static void main(String[] args) {
        Collection<?>[] collections = {
                new HashSet<String>(),
                new ArrayList<BigInteger>(),
                new HashMap<String, String>().values()
        };

        for (Collection<?> c : collections)
            System.out.println(classify(c));
    }
}
