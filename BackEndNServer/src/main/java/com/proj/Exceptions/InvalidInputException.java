package com.proj.Exceptions;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException() {
    }

    public InvalidInputException(String msg) {
        super(msg);
    }

    public InvalidInputException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidInputException(Throwable cause) {
        super(cause);
    }
}
