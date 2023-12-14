package me.staek.chapter09.item58.linkedlist_and_iteratable;


/**
 * LinkedList2 는 Iterable 을 확장하여 for-each statement 가 가능하고
 * ListIterator 를 구현하여, 단방향 반복이 가능함.
 */
public class Main {
    public static void main(String[] args) {
        LinkedList2 list = new LinkedList2();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.addLast(4);
        list.addLast(5);

        for (Object o : list) {
            System.out.println(o);
        }

        System.out.println("===============");

        LinkedList2.ListIterator listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            System.out.println(listIterator.next());
        }
    }
}
