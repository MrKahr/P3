package com.proj.model.events;

public abstract class Request {
    //Field
    protected int requestingId; //the ID of the user that made the request
    protected RequestType requestType;

    //Method
    public int getId(){
        return requestingId;
    }

    public RequestType getRequestType(){
        return requestType;
    } 
}