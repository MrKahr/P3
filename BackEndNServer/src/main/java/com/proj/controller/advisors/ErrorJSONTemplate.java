package com.proj.controller.advisors;

import org.springframework.http.HttpStatus;

/**
 * Custon Object template for sending to frontend.
 * <p>
 * Experimental. Used in AuthenticationAdvisor.
 */
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
