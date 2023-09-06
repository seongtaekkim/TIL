package me.staek.chapter04.item18.callback_wrapper_and_self;


/**
 * Callback 객체인 Inner class는 인스턴스생성시 Service를 인자로 받는다.
 * inner가 run 메서드를 호출할 때 service 의 run 함수에 self instance 를 매개변수로 넘겨준다.
 * 이후 service에서 inner의 콜백 메서드를 실행하게 된다..
 *
 * Wrapper 로 inner객체를 감싸서 같은 방식으로 진행한다. 하지만 wrapper 의 콜백이 아닌, inner 의 콜백이 실행된다.
 * -> 자기참조를 인자로 전달하기에 발생하는문제이다. 주의해야 한다.
 */
public class App {
    public static void main(String[] args) {
        Service service = new Service();
        Inner inner = new Inner(service);
        inner.run();
        System.out.println("==========================");
        Wrapper wrapper = new Wrapper(inner);
        wrapper.run();
    }
}
