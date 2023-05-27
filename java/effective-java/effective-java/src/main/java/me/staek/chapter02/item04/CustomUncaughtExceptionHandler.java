package me.staek.chapter02.item04;

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

        System.out.println("=========================================" + exceptionStackTraceElements.length);

        for(StackTraceElement element : exceptionStackTraceElements) {
            System.out.println("exception class > method : " + element.getClassName() + " " + element.getMethodName() + "(" + element.getLineNumber() + ")");
        }
        System.out.println("=========================================");
    }
}
