package com.proj.controller.navigation;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/profile")
public class ProfileController{
    // profile page
    @GetMapping("/{username}")
    public String showProfilePage(){
        
        // We should check if username exists here. If not, throw error to apropriate advisor
        
        return "profile/profilepage";
    }
    // profile settings page
    @GetMapping("/{username}/settings")
    public String showProfileSettingsPage(){
        
        // We should check if username exists here. If not, throw error to apropriate advisor
        
        return "profile/profilesettingspage";
    }
}
