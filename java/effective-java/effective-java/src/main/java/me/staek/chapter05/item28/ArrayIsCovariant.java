package me.staek.chapter05.item28;

import java.util.ArrayList;
import java.util.List;

public class ArrayIsCovariant {

    public static void main(String[] args) {
        // 공변
        Object[] anything = new String[10];
//        anything[0] = 1;

        // 불공변
        List<String> names = new ArrayList<>();
//        List<Object> objects = names;


//        // 제네릭과 배열을 같이 사용할 수 있다면...
//        List<String>[] stringLists = new ArrayList<String>[1];
//        List<Integer> intList = List.of(42);
//        Object[] objects = stringLists;
//        objects[0] = intList;
//        String s = stringLists[0].get(0);
//        System.out.println(s);
    }
}
