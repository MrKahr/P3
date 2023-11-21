// Handles http requests for users from front end

// Frontend -> UserController -> Backend

// The call hierarchy for the database connection is: Controller -> Manager -> Handler

// Help Repository setup -> https://www.geeksforgeeks.org/spring-boot-crudrepository-with-example/
// Help JPARepositories annotation -> https://stackoverflow.com/questions/27856266/how-to-make-instance-of-crudrepository-interface-during-testing-in-spring 

package com.proj.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proj.model.users.*;
import com.proj.model.session.PlaySession;
import com.proj.model.session.Module;
import com.proj.repositoryhandler.UserdbHandler;
import com.proj.exception.IllegalUserOperationException;
import com.proj.function.RoleAssigner;
import com.proj.function.UserManager;

/**
 * This controller handles sending HTTP requests from frontend to the server for
 * various operations on users.
 * Operations include getting a user, modifying a user, deleting a user,
 * creating a user or banning a user.
 */

// Getting a user
// TODO: Get single user from db - Profilepages(self + other)
// TODO: Get multiple users from db - Adminpage
// TODO: Get all users from db - Adminpage

// Modifying a user
// TODO: Modify single user - Profile, Adminpage
// TODO: Modify multiple users in DB - Adminpage

// Creating a user
// TODO: Add users to DB - SignupPage
// TODO: Add multiple users to DB - MAYBE NOT NEEDED

// Deleting a user
// TODO: Delete user in DB - Profile, Adminpage
// TODO: Delete mulitiple users in DB - MAYBE NOT NEEDED

// Banning a user
// TODO: Ban single user - Adminpage
// TODO: Ban multiple users - MAYBE NOT NEEDED

@Controller
public class UserController {
  @Autowired
  UserdbHandler userdbHandler;

  // TODO: FOR TESTING PURPOSES!!!!!
  ArrayList<Integer> ids = new ArrayList<Integer>();

  /**
   * Generates users for testing - Delete if found after completing UserController
   * TODO: delete after compleing UserController
   * 
   * @param number
   * @return
   */
  @RequestMapping(path = "/user/create/{number}")
  @ResponseBody
  Object saveUsers(@PathVariable Integer number) {
    UserManager userManager = new UserManager(0);
    ArrayList<User> sanitizedUsers = new ArrayList<User>();
    try {
      for (int i = 0; i < number; i++) {
        User user = new User(new BasicUserInfo("name" + i, "password" + i));
        if(i >= 1){
        RoleAssigner.setRole(user, new Guest("Level 1 bard" + i));
        }
        if(i >= 2){
        RoleAssigner.setRole(user, new Member("Fisk", "Randomstuff", "Table", "Stringwauy", "NoEmail"));
        }
        if(i >= 3){
        RoleAssigner.setRole(user, new DM(new ArrayList<String>()));
        }
        if(i >= 4){
        RoleAssigner.setRole(user, new Admin(new ArrayList<String>(), new ArrayList<String>()));
        }
        if(i == 5){
          RoleAssigner.setRole(user, new SuperAdmin());
        }
        userdbHandler.save(user);
        ids.add(user.getId());
      }
      for(User user : userdbHandler.findAllById(ids)){
        sanitizedUsers.add(userManager.sanitizeDBLookup(user));
      }
      return sanitizedUsers;
    } catch (Exception e) {
      e.printStackTrace();
      return sanitizedUsers; 
    }
  }

  /**
   * Page showing a specific user's profile. Must check access level (Member+)
   * TODO: DO NOT PASS STRINGS TO IDENTIFY ACCESS LEVEL. THIS MUST BE HASHED
   * DO NOT MAP USER ID DIRECTLY TO GET
   */
  @GetMapping(path = "/user/{id}")
  @ResponseBody
  Object profile(@PathVariable Integer id, @RequestParam Integer accessingUserID) {
    User accessingUser = userdbHandler.findById(accessingUserID);
    User user = null;
    // Finding user themselves
    if (accessingUserID == id) {
      user = userdbHandler.findById(id);
      // Sanitize/remove sensitive data from database (e.g. password, real name etc)
    } else {
      // Sanitize/remove sensitive data from database (e.g. password, real name etc)
    }
    return user;
  } 


  /**
   * Admin page for managing users. Must check access level. (Admin+)
   * TODO: Check access level
   * TODO: Make function flexible enough to return a couple of users
   */
  @GetMapping(path = "/users")
  @ResponseBody
  Object getSomeUsers(@PathVariable Integer start, Integer end) {

    if (start > end) {
      start = 0;
    }
    if (end > ids.size()) {
      end = ids.size();
    }
    try {

      return userdbHandler.findAllById(ids);
    } catch (Exception e) {
      e.printStackTrace();
      return "Could not retrieve users. Failed with: " + e.getMessage();
    }
  }

  @GetMapping(path = "/user/all")
  @ResponseBody
  Object getAllUsers() {
    return userdbHandler.findAll();
  }
}
