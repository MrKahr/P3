package com.proj.controller.advisors;

import org.springframework.http.HttpStatus;

public class ErrorJSONTemplate {
    // Field
    String title;
    HttpStatus status;
    String message;


    // Constructor
    public ErrorJSONTemplate(String title, HttpStatus status, String message){
        this.title = title;
        this.status = status;
        this.message = message;
    }

}
