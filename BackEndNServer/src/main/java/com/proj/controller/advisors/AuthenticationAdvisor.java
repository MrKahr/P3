package com.proj.controller.advisors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class AuthenticationAdvisor extends ResponseEntityExceptionHandler {
    
    /**
     * This advisor is used when a BadCredentialsException is encountered, i.e. when a user supplied a wrong username/password.
     * Uses a wildcard, see: https://docs.oracle.com/javase/tutorial/java/generics/wildcards.html 
     * @return
     * 
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentials(HttpServletRequest request, Throwable exception){
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // ResponseEntities can be constructed either statically or using their constructor.
        //      The static way: ResponseEntity.badRequest().body("Wrong username or password");
        // Either way is valid. This method uses their constructor for no particular reason.

        return new ResponseEntity<>("Wrong username or password", status); 
    }
}