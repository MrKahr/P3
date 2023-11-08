package com.proj.exception;

public class FailedValidationException extends RuntimeException {

    public FailedValidationException(String msg) {
        super(msg);
    }

    public FailedValidationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public FailedValidationException(Throwable cause) {
        super(cause);
    }
}
