// package com.proj.controller;

// import java.util.Map;

// import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
// import org.springframework.http.HttpStatus;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.servlet.ModelAndView;
// import org.springframework.web.servlet.NoHandlerFoundException;

// import jakarta.servlet.http.HttpServletRequest;

/**
 * Controller for handling http errors.
 * <p>
 * Work In Progess
 * @see https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-servlet/exceptionhandlers.html
 * @see https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-advice.html
 * @see https://docs.spring.io/spring-framework/docs/6.1.1/javadoc-api/org/springframework/web/bind/annotation/ControllerAdvice.html
 * @see https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#web.servlet.spring-mvc.error-handling
 */
// @Controller
// public class ErrorController {

//     @RequestMapping("/error")
//     public ModelAndView fisk(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
//         // Use the request or status to optionally return a ModelAndView
//         if (status == HttpStatus.NOT_FOUND) {
//             // We could add custom model values here
//             return new ModelAndView("/error/404");
//         }
//         return null;
//     }

// }
