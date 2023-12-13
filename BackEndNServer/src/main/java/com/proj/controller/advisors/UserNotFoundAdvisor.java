/**
 * TODO: OUTDATED! DO NOT USE.
 * This is left as a "guide/reference" on how to start implementing advisors.
 * For additional info, see AuthenticationAdvisor.
 * And as always, have a look at the documentaion, for instance: https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#web.servlet.spring-mvc.error-handling
 */




// package com.proj.controller.advisors;

// import org.springframework.http.HttpStatus;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.ResponseBody;
// import org.springframework.web.bind.annotation.ResponseStatus;

// import com.proj.exception.UserNotFoundException;

// @ControllerAdvice
// public class UserNotFoundAdvisor{
//   @ResponseBody
//   @ExceptionHandler(UserNotFoundException.class)
//   @ResponseStatus(HttpStatus.NOT_FOUND)
//   String userNotFoundHandler(UserNotFoundException unfe) {
//     return unfe.getMessage();
//   }
// }