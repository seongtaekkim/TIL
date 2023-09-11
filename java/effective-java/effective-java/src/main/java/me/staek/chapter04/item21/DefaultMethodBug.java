package me.staek.chapter04.item21;


interface Mark {
    default void show() {
        System.out.println("interface show");
    }
}

class Super {
    private void show() {
        System.out.println("Super show");
    }
}

class Sub extends  Super implements Mark {

}

/**
 * *** tried to access private method 에러가 발생한다. ***
 *
 * interface, class 두 리소스가 경합할 때 class쪽을 따른다.
 * 그런데 접근자가 private 이면 interface를 따를거라 생각하지만
 * private 을 호출시도해서 런타임 에러가 발생해버리는 버그에 가까운 일이 발생한다..
 */
public class DefaultMethodBug {
    public static void main(String[] args) {
        Sub sub = new Sub();
        sub.show();
    }
}
