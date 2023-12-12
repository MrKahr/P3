package com.proj.controller.navigation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController{
    // admin page
    @GetMapping("/adminpage")
    public String showAdminPage(){
        return "admin/adminpage";
    }
}