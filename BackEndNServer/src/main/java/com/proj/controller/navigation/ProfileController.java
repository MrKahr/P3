package com.proj.controller.navigation;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProfileController{
    // profile page
    @GetMapping("/profile/{username}")
    public String showProfilePage(){
        return "myProfile/profilepage";
    }
    // profile settings page
    @GetMapping("/profilesettingspage")
    public String showProfileSettingsPage(){
        return "myProfile/profilesettingspage";
    }

}
