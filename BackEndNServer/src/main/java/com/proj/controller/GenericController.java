package com.proj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GenericController {
    
    // Show the front page
    @GetMapping("/")
    public String showHomePage(){
        return "index";
    }
    // Show the front page
    @GetMapping("/userhomepage")
    public String showUserHomePage(){
        return "userhomepage";
    }
}
