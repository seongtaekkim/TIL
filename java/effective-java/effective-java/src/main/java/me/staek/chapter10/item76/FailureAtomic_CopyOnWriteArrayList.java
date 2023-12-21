package me.staek.chapter10.item76;


import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CopyOnWriteArrayList 는 sort 할 때 복사본으로 진행함.
 */
public class FailureAtomic_CopyOnWriteArrayList {
    public static void main(String[] args) {
        
        CopyOnWriteArrayList<PhoneNumber> list = new CopyOnWriteArrayList<PhoneNumber>();
        list.add(new PhoneNumber(1,1,1));
        list.add(new PhoneNumber(2,2,2));
        list.add(new PhoneNumber(6,6,6));

        list.sort(new Comparator<PhoneNumber>() {
            @Override
            public int compare(PhoneNumber o1, PhoneNumber o2) {
                return o1.getAreaCode() > o2.getAreaCode() ? 1 : -1;
            }
        });

        list.forEach(System.out::println);
    }
}
