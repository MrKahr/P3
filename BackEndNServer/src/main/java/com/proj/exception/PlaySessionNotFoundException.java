package com.proj.exception;

public class PlaySessionNotFoundException extends RuntimeException{
    public PlaySessionNotFoundException(){}

    public PlaySessionNotFoundException(Throwable cause){
        super(cause);
    }

    public PlaySessionNotFoundException(String msg){
        super(msg);
    }

    public PlaySessionNotFoundException(String msg, Throwable cause){
        super(msg, cause);
    }
}
