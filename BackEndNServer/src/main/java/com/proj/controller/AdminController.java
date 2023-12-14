package com.proj.controller;
/* 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;

import com.proj.controller.security.authentication.UserDAO;
import com.proj.model.users.RoleType;

@Controller
public class AdminController {
    // Added to enable authetication of users
    @Autowired
    private UserDAO userDAO;

    @CurrentSecurityContext(expression = "authentication") Authentication authentication)
    {

        // Find and sanitize users
        System.out.println("Has authority: " + userDAO.checkAuthority(authentication, RoleType.ADMIN));
    }
}
 */
/**
 * This controller handles sending HTTP requests from frontend to the server for
 * various operations on users.
 * Operations include getting a user, modifying a user, deleting a user,
 * creating a user or banning a user.
 */

// Getting a user

// TODO: Get multiple users from db - Adminpage
// TODO: Get all users from db - Adminpage

// Modifying a user
// TODO: Modify single user - Profile, Adminpage
// TODO: Modify multiple users in DB - Adminpage

// Creating a user
// TODO: Add users to DB - SignupPage

// Deleting a user
// TODO: Delete user in DB - Profile, Adminpage

// Banning a user
// TODO: Ban single user - Adminpage
// TODO: Ban multiple users - MAYBE NOT NEEDED
