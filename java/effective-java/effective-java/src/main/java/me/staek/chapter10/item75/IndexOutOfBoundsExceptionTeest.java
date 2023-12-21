package me.staek.chapter10.item75;

import java.util.Arrays;

class MyArr {
    private int[] arr;
    static MyArr of(int[] arr) {
        MyArr myArr = new MyArr();
        myArr.arr = Arrays.copyOf(arr, arr.length);
        return myArr;
    }

    public int get(int i) {
        if (i > arr.length-1)
            throw new IndexOutOfBoundsException(0, arr.length, i);
        return arr[i];
    }
}

/**
 * Custom IndexOutOfBoundsException example
 */
public class IndexOutOfBoundsExceptionTeest {
    public static void main(String[] args) {
        MyArr arr = MyArr.of(new int[] {1,2,3});
        arr.get(10);
    }
}
