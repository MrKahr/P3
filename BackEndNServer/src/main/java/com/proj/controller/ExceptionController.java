package com.proj.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ExceptionController implements ErrorController {

    /**
     * Controller for handling http errors. These errors are to be displayed to user on the frontend.
     * @param request The servlet request
     * @param cause The exception causing the issue.
     * @return
     * @see https://www.logicbig.com/tutorials/spring-framework/spring-boot/implementing-error-controller.html
     */
    @RequestMapping("/error")
    public ModelAndView handleException(HttpServletRequest request) {
        
        Integer status = (Integer) request.getAttribute("jakarta.servlet.error.status_code"); // Get the Http status code from the request.
        Exception cause = (Exception) request.getAttribute("jakarta.servlet.error.exception"); // Get the exception involved. The exception is wrapped in a jakarta error object.
        String reason;

        // TODO: We might remove the if-statement and keep the else-clause.
        // if(cause != null && cause.getCause() != null && cause.getCause().getMessage() != null) {
        //     reason = cause.getCause().getMessage();
        // }
        // else { // The default error message if none is provided with the throwable.
            reason = "Uh, oh. Something went wrong!\n";

            // The default error message is then customized using this switch.
            switch (status) {
                case 403:
                    reason += "You do not have permission to do that!";
                    break;
                case 404:
                    reason += "The server could not find what you where looking for.";
                    break;  
                default:
                    reason += "Seems like the server exploded.";    
                    break;
            }  
        // }

        ModelAndView model = new ModelAndView("/error/HttpStatusErrorCodes");

        model.addObject("statusCode", status);
        model.addObject("reason", reason);

        return model;
    }
}
