package com.proj.controller.navigation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/admin")
public class AdminController{
    // admin page
    @GetMapping("/adminpage")
    public String showAdminPage(){
        return "admin/adminpage";
    }
}