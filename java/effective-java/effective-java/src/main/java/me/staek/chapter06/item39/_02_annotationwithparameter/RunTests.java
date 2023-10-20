package me.staek.chapter06.item39._02_annotationwithparameter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @ExceptionTest이 작성된 메서드를 실행
 * 1) 메서드의 예외 발생(InvocationTargetException)에 대한 타입이 예상한 타입인지 체크
 * 2) 1)이외 예외처리 작성 (non static method는 인스턴스가 없으므로 여기에 걸림)
 */
public class RunTests {
    public static void main(String[] args) throws Exception {
        int tests = 0;
        int passed = 0;
        Class<?> testClass = Class.forName("me.staek.chapter06.item39._02_annotationwithparameter.Sample2");
        for (Method m : testClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(ExceptionTest.class)) {
                tests++;
                try {
                    m.invoke(null);
                    System.out.printf("Test %s failed: no exception%n", m);
                } catch (InvocationTargetException wrappedEx) {
                    Throwable exc = wrappedEx.getCause();
                    Class<? extends Throwable> excType =
                            m.getAnnotation(ExceptionTest.class).value();
                    if (excType.isInstance(exc)) {
                        passed++;
                    } else {
                        System.out.printf(
                                "Test %s failed: expected %s, got %s%n",
                                m, excType.getName(), exc);
                    }
                } catch (Exception exc) {
                    System.out.println("Invalid @ExceptionTest: " + m);
                }
            }
        }

        System.out.printf("Passed: %d, Failed: %d%n", passed, tests - passed);
    }
}
