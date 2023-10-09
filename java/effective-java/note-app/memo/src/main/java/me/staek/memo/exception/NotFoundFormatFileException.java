package me.staek.memo.exception;

public class NotFoundFormatFileException extends Exception {
    public static final NotFoundFormatFileException NOT_FOUND_FORMAT_FILE_EXCEPTION = new NotFoundFormatFileException();

    public NotFoundFormatFileException() {
        System.exit(2);
    }
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
