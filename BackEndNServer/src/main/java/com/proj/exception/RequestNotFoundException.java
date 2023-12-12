package com.proj.exception;

public class RequestNotFoundException extends RuntimeException {  
    public RequestNotFoundException(){}
    
    public RequestNotFoundException(Integer id){
        super("Request with id '" + id + "' does not exist.");
    }
    
    public RequestNotFoundException(Throwable cause) {
        super(cause);
    }
    public RequestNotFoundException(String msg) {
        super(msg);
    }
    public RequestNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}