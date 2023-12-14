// Handles http requests for users from front end

// Frontend -> UserController -> Backend

// The call hierarchy for the database connection is: Controller -> Manager -> Handler

// Help Repository setup -> https://www.geeksforgeeks.org/spring-boot-crudrepository-with-example/
// Help JPARepositories annotation -> https://stackoverflow.com/questions/27856266/how-to-make-instance-of-crudrepository-interface-during-testing-in-spring 

package com.proj.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proj.model.users.*;
import com.proj.exception.UserNotFoundException;
import com.proj.function.RoleAssigner;
import com.proj.function.UserManager;

  // DONE: Get single user from db - Profilepages(self + other)
  // TODO: Modify single user - Profile, Adminpage
  // TODO: Add users to DB - SignupPage
  // TODO: Deactivate account

@Controller
@RequestMapping(path = "/user")
public class UserController {
  @Autowired
  private UserManager userManager;

  // TODO: FOR TESTING PURPOSES!!!!!
  ArrayList<Integer> ids = new ArrayList<Integer>();

  /**
   * Generates users for testing - Delete if found after completing UserController
   * TODO: delete after compleing UserController
   * 
   * @param number
   * @return
   */
  @RequestMapping(path = "/create/{number}")
  @ResponseBody
  Object saveUsers(@PathVariable Integer number) {
    ArrayList<User> sanitizedUsers = new ArrayList<User>();
    try {
      for (int i = 0; i < number; i++) {
        User user = new User(new BasicUserInfo("n" + i, "p" + i));
        if (i >= 1) {
          RoleAssigner.setRole(user, new Guest("Level 1 bard" + i));
        }
        if (i >= 2) {
          RoleAssigner.setRole(user, new Member("Fisk", "Randomstuff", "Table", "Stringwauy", "NoEmail"));
        }
        if (i >= 3) {
          RoleAssigner.setRole(user, new DM(new ArrayList<String>()));
        }
        if (i >= 4) {
          RoleAssigner.setRole(user, new Admin(new ArrayList<String>(), new ArrayList<String>()));
        }
        if (i == 5) {
          RoleAssigner.setRole(user, new SuperAdmin());
        }
        userManager.getUserdbHandler().save(user);
        ids.add(user.getId());
      }
      for (User user : userManager.getUserdbHandler().findAllById(ids)) {
        sanitizedUsers.add(userManager.sanitizeDBLookup(user, user));
      }

      return sanitizedUsers;
    } catch (Exception e) {
      e.printStackTrace();
      return sanitizedUsers;
    }
  }



  /**
   * Get a user from database
   * @param username - the name of the user looked up on the database
   * @param requestingUsername - the username of the person requesting the page
   * @return a user object that is sanitized 
   */
  @GetMapping(path = "/{username}")
  @ResponseBody Object profile(@PathVariable String username, @RequestParam String requestingUsername) {
    // Find and sanitize users
      User user = userManager.lookupAccount(username);
      User requestingUser = userManager.lookupAccount(requestingUsername);
      User sanitizedUser = userManager.sanitizeDBLookup(user, requestingUser);
    // Note exceptions controlled by advisors
    return sanitizedUser;
  }

  // TODO: Modify single user - Profile, Adminpage
  // TODO: Add users to DB - SignupPage
  // TODO: Deactivate account
 
  // @PutMapping(path = "/{username}/deactivate")
  // @ResponseBody
  // Object getSomeUsers(@PathVariable String username, @RequestParam String requestingUsername) {
  //   User user = userManager.lookupAccount(username);
  //   // YOU MAY ONLY DEACTIVE YOURSELF
  //   if (start > end) {
  //     start = 0;
  //   }
  //   if (end > ids.size()) {
  //     end = ids.size();
  //   }
  //   try {

  //     return userManager.getUserdbHandler().findAllById(ids);
  //   } catch (Exception e) {
  //     e.printStackTrace();
  //     return "Could not retrieve users. Failed with: " + e.getMessage();
  //   }
  // }

  @GetMapping(path = "/user/all")
  @ResponseBody
  Object getAllUsers() {
    return userManager.getUserdbHandler().findAll();
  }
}
