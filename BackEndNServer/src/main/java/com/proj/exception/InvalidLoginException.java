package com.proj.exception;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException() {

    }
    public InvalidLoginException(String msg) {
        super(msg);
    }
    public InvalidLoginException(String msg, Throwable cause) {
        super(msg, cause);
    }
    public InvalidLoginException(Throwable cause) {
        super(cause);
    }
}