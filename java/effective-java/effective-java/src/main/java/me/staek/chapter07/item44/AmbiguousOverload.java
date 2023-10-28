package me.staek.chapter07.item44;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Duplicate {
    public static void dup(char c) {

    }
    public static void dup() {

    }
}

/**
 * 호출하는 함수와 매개변수가 모두 다중정의된 메서드라면, 다중정의해소는 제대로 동작하지 않는다.
 * 이런경우, 매개변수를 명시적 형변환 해야 한다.
 */
public class AmbiguousOverload {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        Callable<String> hello = () -> {
            Thread.sleep((2000L));
            return ("hello");
        };

//        executorService.submit(System.out::println);
        executorService.submit((Runnable) Duplicate::dup);
        Future<String> helloFuture = executorService.submit(hello);
        System.out.println(helloFuture.isDone());
        System.out.println("started!");
    }
}
