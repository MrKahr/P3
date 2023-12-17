package com.proj.controller.advisors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.proj.exception.FailedValidationException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class FailedValidationAdvisor {
    
    /**
     * This advisor is used when a FailedValidationException is encountered, i.e. when a user supplied an invalid password during signup.
     * Uses a wildcard, see: https://docs.oracle.com/javase/tutorial/java/generics/wildcards.html 
     * @return
     * @see https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#web.servlet.spring-mvc.error-handling
     */
    @ExceptionHandler(FailedValidationException.class)
    public ResponseEntity<?> failedValidation(HttpServletRequest request, Throwable exception){

        HttpStatus status = HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>("Falied validation: " + exception.getMessage(), status);
    }
}
