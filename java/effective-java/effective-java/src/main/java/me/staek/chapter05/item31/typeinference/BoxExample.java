package me.staek.chapter05.item31.typeinference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 여러가지 추론타입에대한 예제
 */
public class BoxExample {

    private static <U> void addBox(U u, List<Box<U>> boxes) {
        Box<U> box = new Box<>();
        box.set(u);
        boxes.add(box);
    }

    private static <U> void outputBoxes(List<Box<U>> boxes) {
        int counter = 0;
        for (Box<U> box: boxes) {
            U boxContents = box.get();
            System.out.println("Box #" + counter + " contains [" +
                    boxContents.toString() + "]");
            counter++;
        }
    }

    private static void processStringList(List<String> stringList) {

    }


    public static void main(String[] args) {

        /**
         * 제네릭 메서드 인수에 대한 명시적타입인수
         */
        ArrayList<Box<Integer>> listOfIntegerBoxes = new ArrayList<>();
        BoxExample.<Integer>addBox(10, listOfIntegerBoxes); // 입력되는 10을 보고 리턴 타입을 추론하는 것임.
        BoxExample.addBox(20, listOfIntegerBoxes);
        BoxExample.addBox(30, listOfIntegerBoxes);
        BoxExample.outputBoxes(listOfIntegerBoxes);



        /**
         *  Target Type
         *  메서드 리턴타입이 제네릭인데, 명시적 형변환 없이 타입추론이 가능.
         */
        List<String> stringlist = Collections.emptyList();
        List<Integer> integerlist = Collections.<Integer>emptyList();


        /**
         * Target Type
         * 메서드 인자 타입추론
         */
        BoxExample.processStringList(Collections.<String>emptyList());
        BoxExample.processStringList(Collections.emptyList());
    }
}
