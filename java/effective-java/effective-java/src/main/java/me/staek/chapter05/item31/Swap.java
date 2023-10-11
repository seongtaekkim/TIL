package me.staek.chapter05.item31;


import java.util.Arrays;
import java.util.List;

/**
 * 와일드카드 타입을 실제 타입으로 바꿔주는 private 도우미 메서드
 */
public class Swap {

    /**
     * 굳이 비한정적타입(?) 을 사용하지말고 E를 정의해서 사용해보자.
     */
    public static <E> void swap(List<E> list, int i, int j) {
        list.set(i, list.set(j, list.get(i)));
    }

    /**
     * 비한정적타입(?)으로 인자를 받고 사용하는 건 상관 없으나,
     * 이를 다시 할당할 경우, 받는쪽에서는 똑같이 비한정적타입을 예상하기에 컴파일에러가 발생한다.
     *
     * 굳이 비한정적타입을 사용한다면, 와일드카드를 실제타입으로 바꿔주는 메서드(swapHelper)를 정의해서 사용하면 되지만,
     * 오히려 복잡하니 그냥 E 를 정의해서 사용하는것도 고려해 보자.
     */
//    public static void swap(List<?> list, int i, int j) {
////        list.set(i, list.set(j, list.get(i)));
//        swapHelper(list, i, j);
//    }

    // 와일드카드 타입을 실제 타입으로 바꿔주는 private 도우미 메서드
    private static <E> void swapHelper(List<E> list, int i, int j) {
        list.set(i, list.set(j, list.get(i)));
    }

    public static void main(String[] args) {
        // 첫 번째와 마지막 인수를 스왑한 후 결과 리스트를 출력한다.
        List<String> argList = Arrays.asList(args);
        swap(argList, 0, argList.size() - 1);
        System.out.println(argList);
    }
}
