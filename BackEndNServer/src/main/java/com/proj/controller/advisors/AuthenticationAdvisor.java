package com.proj.controller.advisors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.RequestDispatcher;
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
    public ModelAndView badCredentials(HttpServletRequest request, Throwable exception){
        
        // This sends a new login page with modified object. (Current implementation does not work as intented, more research required.)
        // This approach is perhaps more advanced. Exposes only the necessary HTML to frontend.
        ModelAndView model = new ModelAndView("authentication/loginPage");
        model.addObject("error", true);

        return model;


        // Send a custom JSON object. Must be parsed on frontend by JS.
        // This approach is the manual way. Exposes all HTML to frontend.

        //HttpStatus status = getStatus(request);
        //return new ResponseEntity<>(new ErrorJSONTemplate("Wrong username or password", status, exception.getMessage()), status);


    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer code = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus status = HttpStatus.resolve(code);
        return (status != null) ? status : HttpStatus.INTERNAL_SERVER_ERROR;
    }
}


    // /**
    //  * 
    //  * Uses a wildcard, see: https://docs.oracle.com/javase/tutorial/java/generics/wildcards.html 
    //  * @return
    //  * 
    //  */
    // @ResponseBody
    // @ExceptionHandler(BadCredentialsException.class)
    // public ResponseEntity<?> badCredentials(HttpServletRequest request, Throwable exception, ModelAndView model){
        
        

    //     HttpStatus status = getStatus(request);
    //     return new ResponseEntity<>(new ErrorJSONTemplate("Wrong username or password", status, exception.getMessage()), status);


    // }

    // private HttpStatus getStatus(HttpServletRequest request) {
    //     Integer code = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    //     HttpStatus status = HttpStatus.resolve(code);
    //     return (status != null) ? status : HttpStatus.INTERNAL_SERVER_ERROR;
    // }