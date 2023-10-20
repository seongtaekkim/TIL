package me.staek.chapter06.item39._04_repeatableannotation;

import java.lang.reflect.Method;

/**
 * @ExceptionTest이 작성된 메서드를 실행
 * 1) 메서드의 예외 발생(InvocationTargetException)에 대한 타입이 예상한 타입인지 체크
 *     --> getAnnotationsByType 메서드를 이용하여 모든 @ExceptionTest 를 가져와 인스턴스를 체크함.
 * 2) 1)이외 예외처리 작성 (non static method는 인스턴스가 없으므로 여기에 걸림)
 */
public class RunTests {
    public static void main(String[] args) throws Exception {
        int tests = 0;
        int passed = 0;
        Class testClass = Class.forName("me.staek.chapter06.item39._04_repeatableannotation.Sample4");
        for (Method m : testClass.getDeclaredMethods()) {

            if (m.isAnnotationPresent(ExceptionTest.class)
                    || m.isAnnotationPresent(ExceptionTestContainer.class)) {
                tests++;
                try {
                    m.invoke(null);
                    System.out.printf("Test %s failed: no exception%n", m);
                } catch (Throwable wrappedExc) {
                    Throwable exc = wrappedExc.getCause();
                    int oldPassed = passed;
                    ExceptionTest[] excTests =
                            m.getAnnotationsByType(ExceptionTest.class);
                    for (ExceptionTest excTest : excTests) {
                        if (excTest.value().isInstance(exc)) {
                            passed++;
                            break;
                        }
                    }
                    if (passed == oldPassed)
                        System.out.printf("Test %s failed: %s %n", m, exc);
                }
            }
        }
        System.out.printf("Passed: %d, Failed: %d%n", passed, tests - passed);
    }
}
