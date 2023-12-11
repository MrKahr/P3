package com.proj.exception;

public class PlaySessionFullException extends RuntimeException {

    public PlaySessionFullException(String msg) {
        super(msg);
    }

    public PlaySessionFullException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PlaySessionFullException(Throwable cause) {
        super(cause);
    }
}