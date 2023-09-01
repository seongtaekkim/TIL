package me.staek.issue.checked_exception_performance;

public class CustemException extends Exception {
    public static final CustemException CUSTOM_EXCEPTION = new CustemException();


    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
