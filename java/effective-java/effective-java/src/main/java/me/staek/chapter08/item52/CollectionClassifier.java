package me.staek.chapter08.item52;

import java.math.BigInteger;
import java.util.*;

/**
 * 다중정의 메서드는 컴파일타임에 지정된 타입으로 정적으로 지정됨.
 */
public class CollectionClassifier {
    public static String classify(Set<?> s) {
        return "Set";
    }

    public static String classify(List<?> lst) {
        return "List";
    }

    public static String classify(Collection<?> c) {
        return "Unknown Collection";
    }

    public static void main(String[] args) {
        Collection<?>[] collections = {
                new HashSet<String>(),
                new ArrayList<BigInteger>(),
                new HashMap<String, String>().values()
        };


        for (Collection<?> c : collections)
            System.out.println(classify(c));

        /**
         * 다중정의 해결(1)
         * 명시적으로 타입을 지정해야 우리가 원하는 결과를 받을 수 있음.
         */
        @SuppressWarnings("unchecked")
        HashSet<String> collection = (HashSet<String>) collections[0];
        System.out.println(classify(collection));
    }
}
