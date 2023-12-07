package com.proj.controller.advisors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class AuthenticationAdvisor extends ResponseEntityExceptionHandler {
    
    /**
     * 
     * Uses a wildcard, see: https://docs.oracle.com/javase/tutorial/java/generics/wildcards.html 
     * @return
     * 
     */
    @ResponseBody
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentials(HttpServletRequest request, Throwable exception){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>("Wrong username or password", status); // The static way of doing it: ResponseEntity.badRequest().body("Wrong username or password");
    }

    // Broken. TODO: Fix or find another way of dynamically showing HTTP status of server.
    // private HttpStatus getStatus(HttpServletRequest request) {
    //     Integer code = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    //     HttpStatus status = HttpStatus.resolve(code);
    //     return (status != null) ? status : HttpStatus.INTERNAL_SERVER_ERROR;
    // }
}