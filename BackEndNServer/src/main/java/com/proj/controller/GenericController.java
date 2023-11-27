package com.proj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GenericController {
    
    // Show the front page
    @GetMapping("/")
    public @ResponseBody String showHomePage(){
        return "index";
    }

    // Role testing
    @GetMapping("/fisk")
    public @ResponseBody String showFisk(){
        return "restDemo";
    }

}
