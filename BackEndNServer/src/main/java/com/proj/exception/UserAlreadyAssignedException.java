package com.proj.exception;

public class UserAlreadyAssignedException extends RuntimeException {  
    public UserAlreadyAssignedException(){}
    
    public UserAlreadyAssignedException(Integer id){
        super("Request with id '" + id + "' does not exist.");
    }
    
    public UserAlreadyAssignedException(Throwable cause) {
        super(cause);
    }
    public UserAlreadyAssignedException(String msg) {
        super(msg);
    }
    public UserAlreadyAssignedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}