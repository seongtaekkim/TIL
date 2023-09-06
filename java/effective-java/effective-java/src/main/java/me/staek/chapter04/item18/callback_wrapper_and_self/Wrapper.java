package me.staek.chapter04.item18.callback_wrapper_and_self;

public class Wrapper implements Callback {

    private final Inner inner;

    public Wrapper(Inner inner) {
        this.inner = inner;
    }

    @Override
    public void call() {
        this.inner.call();
        System.out.println("call wrapper");
    }

    @Override
    public void run() {
        this.inner.run();
    }
}
