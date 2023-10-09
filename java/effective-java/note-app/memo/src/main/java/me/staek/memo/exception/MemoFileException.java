package me.staek.memo.exception;

public class MemoFileException extends Exception {
    public static final MemoFileException MEMO_FILE_EXCEPTION = new MemoFileException();

    public MemoFileException() {
        System.exit(2);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
