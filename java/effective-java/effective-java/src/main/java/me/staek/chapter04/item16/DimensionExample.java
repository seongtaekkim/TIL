package me.staek.chapter04.item16;

import java.awt.*;

/**
 * public 필드가 있는 객체는, 그객체를 다른 쪽에 인자로 넘길때 복사를 해야 한다
 * 그렇지 않으면 내가 생성한 객체의 필드값이 바뀌었는지 알수 없다.
 */
public class DimensionExample {

    public static void main(String[] args) {
        Button button = new Button("hello button");
        button.setBounds(0, 0, 20, 10);

        Dimension size = button.getSize();
        System.out.println(size.height);
        System.out.println(size.width);

        doSomething(size);
    }

    private static void doSomething(Dimension size) {
        Dimension d = new Dimension();
        d.height = size.height;
        d.width = size.width;
        // ... etc ...
    }

}
