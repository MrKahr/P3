package com.proj.exception;

public class UserNotFoundException extends RuntimeException {  
    public UserNotFoundException(){}
    
    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
    public UserNotFoundException(String msg) {
        super(msg);
    }
    public UserNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
