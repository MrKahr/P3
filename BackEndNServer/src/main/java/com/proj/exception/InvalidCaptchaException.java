package com.proj.exception;

public class InvalidCaptchaException extends RuntimeException {
    public InvalidCaptchaException() {

    }
    public InvalidCaptchaException(String msg) {
        super(msg);
    }
    public InvalidCaptchaException(String msg, Throwable cause) {
        super(msg, cause);
    }
    public InvalidCaptchaException(Throwable cause) {
        super(cause);
    }
}