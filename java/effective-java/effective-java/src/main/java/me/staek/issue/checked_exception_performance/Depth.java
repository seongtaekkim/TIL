package me.staek.issue.checked_exception_performance;


public class Depth {
    void method1() throws Exception {
        Depth2 d = new Depth2();
        d.method1();
    }
}

class Depth2 {
    void method1() throws Exception {
        Depth3 d = new Depth3();
        d.method1();    }
}

class Depth3 {
    void method1() throws Exception {
        Depth4 d = new Depth4();
        d.method1();    }
}

class Depth4 {
    void method1() throws Exception {
        Depth5 d = new Depth5();
        d.method1();    }
}

class Depth5 {
    void method1() throws Exception {
        throw new Exception();
    }
}