package me.staek.chapter05.item26;

/**
 * generic 특징
 * - 컴파일 시 타입 소거됨을 확인
 */
public class Box<E> {

    private E item;

    private void add(E e) {
        this.item = e;
    }

    private E get() {
        return this.item;
    }

    private static void printBox(Box<?> box) {
        System.out.println(box.get());
    }

    public static void main(String[] args) {
        Box<Integer> box = new Box<>();
        box.add(10);
        /**
         *     INVOKEVIRTUAL me/staek/chapter05/item26/Box.get ()Ljava/lang/Object;
         *     CHECKCAST java/lang/Integer
         */
        System.out.println(box.get() * 100);
        printBox(box);
    }

}
