package me.staek.chapter04.item18.callback_wrapper_and_self;

public class Service {
    public void run(Callback callback) {
        System.out.println("call service");
        callback.call();
    }
}
