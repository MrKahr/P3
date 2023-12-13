package com.proj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proj.model.users.RoleType;
import com.proj.controller.security.authentication.UserDAO;

@Controller
public class GenericController {
    @Autowired
    private UserDAO userDAO;

    // Show the front page
    @GetMapping("/")
    public String showHomePage(@CurrentSecurityContext(expression = "authentication") Authentication authentication) {

        System.out.println("Has authority: " + userDAO.checkAuthority(authentication, RoleType.ADMIN));
        return "index";
    }

    @GetMapping("/userhomepage")
    public String showUserHomePage() {
        return "userhomepage";
    }

    
    @GetMapping("/currentsessionuser")
    @ResponseBody 
    String getCurrentUserName(@CurrentSecurityContext(expression = "authentication") Authentication authentication){
      return authentication.getName();
    }

}
