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

    /* Reference implementation of Manage Access Level
     * ===============================================
    @Autowired
    private UserDAO userDAO;
    @GetMapping("/")
    public String showHomePage(@CurrentSecurityContext(expression = "authentication") Authentication authentication){

        System.out.println("Has authority: " + userDAO.checkAuthority(authentication, RoleType.ADMIN));
        
        return "index";
    }
     * =============================================== */
}
