package com.proj.exception;

public class FailedSanitizationException extends RuntimeException{
    public FailedSanitizationException(){}

    public FailedSanitizationException(String msg) {
        super(msg);
    }

    public FailedSanitizationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public FailedSanitizationException(Throwable cause) {
        super(cause);
    }
}
