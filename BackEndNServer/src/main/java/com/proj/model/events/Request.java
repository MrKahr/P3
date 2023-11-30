package com.proj.model.events;

public abstract class Request {
    //Method
    public abstract int getUserId();

    public abstract RequestType getRequestType();
}