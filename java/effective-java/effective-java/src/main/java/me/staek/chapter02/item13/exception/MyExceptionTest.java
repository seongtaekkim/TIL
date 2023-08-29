package me.staek.chapter02.item13.exception;

class MyException extends Exception { }

class MyRuntime extends RuntimeException { }

class MyError extends Error {}
/**
 * TODO checkedException, uncatchedException example
 */
public class MyExceptionTest {
    /**
     *
     * @param name
     * @throws MyException
     */
    public void hello(String name) throws MyException {
        if (name.equals("푸틴")) {
            throw new MyException();
        }
        System.out.println("hello");
    }

    /**
     * TODO RuntimeException은 throw new 해도 호출자에서 catch하지 않아도 된다.
     *      해당 스레드는 바로 종료될 것이다.
     * @param name
     * @throws MyRuntime
     */
    public void runtimeHello(String name) throws MyRuntime {
//        throw new MyRuntime();

    }

    /**
     * TODO Error class도 unchecked exception 이다.
     *      https://docs.oracle.com/javase/tutorial/essential/exceptions/runtime.html
     * @param name
     * @throws Error
     */
    public void errorHello(String name) {
        throw new MyError();
    }

    public static void main(String[] args) {
        MyExceptionTest myApp = new MyExceptionTest();
        try {
            myApp.hello("푸틴");
        } catch (MyException e) {
            e.printStackTrace();
        }

        myApp.runtimeHello("runtime error");
//        myApp.errorHello("error");
        System.out.println("program exit");
    }
}
