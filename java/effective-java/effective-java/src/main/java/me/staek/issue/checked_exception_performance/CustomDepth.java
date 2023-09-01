package me.staek.issue.checked_exception_performance;

public class CustomDepth {
    void method1() throws CustemException {
        CustomDepth2 d = new CustomDepth2();
        d.method1();
    }
}

class CustomDepth2 {
    void method1() throws CustemException {
        CustomDepth3 d = new CustomDepth3();
        d.method1();
    }
}
class CustomDepth3 {
    void method1() throws CustemException {
        CustomDepth4 d = new CustomDepth4();
        d.method1();
    }
}
class CustomDepth4 {
    void method1() throws CustemException {
        CustomDepth5 d = new CustomDepth5();
        d.method1();
    }
}
class CustomDepth5 {
    void method1() throws CustemException {

//        throw new CustemException();
        throw CustemException.CUSTOM_EXCEPTION;
    }
}
