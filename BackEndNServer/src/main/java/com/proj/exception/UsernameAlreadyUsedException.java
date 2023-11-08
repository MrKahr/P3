package com.proj.exception;

public class UsernameAlreadyUsedException extends RuntimeException {
    public UsernameAlreadyUsedException(String msg) {
        super(msg);
    }

    public UsernameAlreadyUsedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UsernameAlreadyUsedException(Throwable cause) {
        super(cause);
    }   
}
