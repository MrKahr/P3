package com.proj.controller.navigation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.proj.function.UserManager;


@Controller
@RequestMapping("/profile")
public class ProfileController{
  @Autowired
  private UserManager userManager;

    // profile page
    @GetMapping("/{username}")
    public String showProfilePage(@PathVariable String username){
        checkAccount(username);
        return "profile/profilepage";
    }
    // profile settings page
    @GetMapping("/{username}/settings")
    public String showProfileSettingsPage(@PathVariable String username){  
        checkAccount(username);
        return "profile/profilesettingspage";
    }

    public void checkAccount(String username){
        try {
            userManager.lookupAccount(username);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "That user does not exist");
        }
    }



}
