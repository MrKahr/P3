package com.proj.controller.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignupController {
    // signup
    @GetMapping("/signup")
    public String showSignupPage(){
        return "signuppage";
    }
}
