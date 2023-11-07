package com.proj.exception;

public class NoModuleFoundException extends RuntimeException {

    public NoModuleFoundException(String msg) {
        super(msg);
    }

    public NoModuleFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public NoModuleFoundException(Throwable cause) {
        super(cause);
    }
}
