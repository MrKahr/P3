package com.proj.controller.navigation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController{
    // profile page
    @GetMapping("/profilepage")
    public String showProfilePage(){
        return "myProfile/profilepage";
    }
    // profile settings page
    @GetMapping("/profilesettingspage")
    public String showProfileSettingsPage(){
        return "myProfile/profilesettingspage";
    }

}
