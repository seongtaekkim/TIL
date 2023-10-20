package me.staek.chapter06.item39._01_markerannotation;

import me.staek.chapter06.item39.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Test이 작성된 메서드를 실행
 * 1) 메서드의 예외처리 작성 (InvocationTargetException)
 * 2) 1)이외 예외처리 작성 (non static method는 인스턴스가 없으므로 여기에 걸림)
 */
public class RunTests {
    public static void main(String[] args) throws Exception {
        int tests = 0;
        int passed = 0;
        Class<?> testClass = Class.forName("me.staek.chapter06.item39._01_markerannotation.Sample");
        for (Method m : testClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Test.class)) {
                tests++;
                try {
                    m.invoke(null);
                    passed++;
                } catch (InvocationTargetException wrappedExc) {
                    Throwable exc = wrappedExc.getCause();
                    System.out.println(m + " failed: " + exc + " " + wrappedExc.getMessage());
                } catch (Exception exc) {
                    System.out.println("Invalid @Test: " + m + " " + exc.getMessage());
                }
            }
        }
        System.out.printf("Passed: %d, Failed: %d%n", passed, tests - passed);
    }
}
