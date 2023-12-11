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

    // Role testing
    @GetMapping("/fisk")
    public String showFisk(){
        return "myEvents/eventlistpage";
    }
    // Role testing
    @GetMapping("/userhomepage")
    public String showFisk2(){
        return "userhomepage";
    }
    
}
