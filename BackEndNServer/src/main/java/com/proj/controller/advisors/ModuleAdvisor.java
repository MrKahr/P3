package com.proj.controller.advisors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.proj.exception.FailedValidationException;
import com.proj.exception.NoModuleFoundException;

@ControllerAdvice
public class ModuleAdvisor {
    @ResponseBody
    @ExceptionHandler(FailedValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String moduleValidationFailed(FailedValidationException fve) {
        return fve.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(NoModuleFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String moduleNotFound(NoModuleFoundException nmfe) {
        return nmfe.getMessage();
    }
}