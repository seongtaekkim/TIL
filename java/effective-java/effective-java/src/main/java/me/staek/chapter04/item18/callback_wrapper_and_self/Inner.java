package me.staek.chapter04.item18.callback_wrapper_and_self;

class Inner implements Callback {

    private final Service service;

    public Inner(Service service) {
        this.service = service;
    }

    @Override
    public void call() {
        System.out.println("call inner");
    }

    @Override
    public void run() {
        this.service.run(this);
    }
}
