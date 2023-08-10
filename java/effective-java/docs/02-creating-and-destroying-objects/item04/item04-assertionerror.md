## Assertionerror



### AssertionError

~~~java
throw new AssertionError();

public class Error extends Throwable {}

public class Throwable implements Serializable {}

~~~



~~~java
// Throwable
public synchronized Throwable fillInStackTrace() {
      if (stackTrace != null ||
          backtrace != null /* Out of protocol state */ ) {
          fillInStackTrace(0);
          stackTrace = UNASSIGNED_STACK;
      }
      return this;
}
~~~



~~~java
// ThreadGroup.class
public void uncaughtException(Thread t, Throwable e) {
      if (parent != null) {
          parent.uncaughtException(t, e);
      } else {
          Thread.UncaughtExceptionHandler ueh =
              Thread.getDefaultUncaughtExceptionHandler();
          if (ueh != null) {
              ueh.uncaughtException(t, e);
          } else if (!(e instanceof ThreadDeath)) {
              System.err.print("Exception in thread \""
                               + t.getName() + "\" ");
              e.printStackTrace(System.err);
          }
      }
  }
~~~



~~~java
// Throwable.class
private void printStackTrace(PrintStreamOrWriter s) {
    // Guard against malicious overrides of Throwable.equals by
    // using a Set with identity equality semantics.
    Set<Throwable> dejaVu = Collections.newSetFromMap(new IdentityHashMap<>());
    dejaVu.add(this);

    synchronized (s.lock()) {
        // Print our stack trace
        s.println(this);
        StackTraceElement[] trace = getOurStackTrace();
        for (StackTraceElement traceElement : trace)
            s.println("\tat " + traceElement);

        // Print suppressed exceptions, if any
        for (Throwable se : getSuppressed())
            se.printEnclosedStackTrace(s, trace, SUPPRESSED_CAPTION, "\t", dejaVu);

        // Print cause, if any
        Throwable ourCause = getCause();
        if (ourCause != null)
            ourCause.printEnclosedStackTrace(s, trace, CAUSE_CAPTION, "", dejaVu);
    }
}
~~~





### Uncaught Exception Handler

- main thread에서 uncatched Exception 을 잡아 에러메세지를 출력한다.
- setDefaultUncaughtExceptionHandler 메소드를 사용해서 어느 부분에서 처리하는지 파악할 수 있다.

~~~java
// UtilityClass.java
public static void main(String[] args) {
        String hello = UtilityClass.hello();
        System.out.println("1");
        Thread.setDefaultUncaughtExceptionHandler(new CustomUncaughtExceptionHandler());

        UtilityClass utilityClass = new UtilityClass();
        utilityClass.hello();
        System.out.println("2");
    }
~~~



~~~java
// CustomUncaughtExceptionHandler.java
public class CustomUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        StackTraceElement[] stackTraceElements = t.getStackTrace();
        StackTraceElement[] exceptionStackTraceElements = e.getStackTrace();

        System.out.println("Thread Name : " + t.getName());
        System.out.println("=========================================");
        for(StackTraceElement element : stackTraceElements) {
            System.out.println("class > method : " + element.getClassName() + " " + element.getMethodName() + "(" + element.getLineNumber() + ")");
        }

        System.out.println("=========================================");
        System.out.println("Exception Message : " + e.getMessage());
        System.out.println("Exception Localized Message : " + e.getLocalizedMessage());
        System.out.println("Exception Cause : " + e.getCause());

        System.out.println("=========================================");
        for(StackTraceElement element : exceptionStackTraceElements) {
            System.out.println("exception class > method : " + element.getClassName() + " " + element.getMethodName() + "(" + element.getLineNumber() + ")");
        }
        System.out.println("=========================================");
    }
}

~~~





