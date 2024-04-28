package me.staek.chapter11.item79;

import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * iterator 도중 삭제로직을 다른스레드가 위임받아 실행한다면?
 * - 호출스레드가 락을 점유한상태로 순회중에 added() 메서드를 통해 다른 스레드를 호출하여 락 획득을 시도한다.(대기)
 * - 호출스레드는 실행한 스레드가 종료되기를 기다린다. (호출자 입장에서는 외부함수를 실행한 것임)
 */
public class Test_DeadLock {
    public static void main(String[] args) {
        ObservableSet<Integer> set = new ObservableSet<>(new HashSet<>());

        set.addObserver(new SetObserver<>() {
            public void added(ObservableSet<Integer> s, Integer e) {
                System.out.println(e);
                if (e == 23) {
                    ExecutorService exec = Executors.newSingleThreadExecutor();
                    try {
                        exec.submit(() -> s.removeObserver(this)).get();
                    } catch (ExecutionException | InterruptedException ex) {
                        throw new AssertionError(ex);
                    } finally {
                        exec.shutdown();
                    }
                }
            }
        });

        for (int i = 0; i < 100; i++)
            set.add(i);
    }
}
