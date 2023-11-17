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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proj.model.users.*;
import com.proj.repositoryhandler.UserdbHandler;
import com.proj.model.session.PlaySession;
import com.proj.model.session.Module;

/**
 * This controller handles sending HTTP requests from frontend to the server for various operations on users. 
 * Operations include getting a user, modifying a user, deleting a user, creating a user or banning a user. 
 */

   // Getting a user
  //TODO: Get single user from db - Profilepage + Adminpage 
  //TODO: Get multiple users from db - Adminpage
  //TODO: Get all users from db - Adminpage 
  
  // Modifying a user
  // TODO: Modify single user - Profile, Adminpage
  // TODO: Modify multiple users in DB - Adminpage
  
  // Creating a user 
  //TODO: Add users to DB - SignupPage
  //TODO: Add multiple users to DB - MAYBE NOT NEEDED 

  // Deleting a user 
  //TODO: Delete user in DB - Profile, Adminpage
  //TODO: Delete mulitiple users in DB - MAYBE NOT NEEDED

  // Banning a user
  //TODO: Ban single user - Adminpage 
  //TODO: Ban multiple users - MAYBE NOT NEEDED 

  
@Controller 
public class UserController {
  @Autowired 
  UserdbHandler userdbHandler;
 
  /**
   * Admin page for managing users. Must check access level. (Admin+)
   */
  @RequestMapping(path = "/user")
  @ResponseBody Object adminPage(){
    try{
        User user = new User(new BasicUserInfo("name", "password")); 
        userdbHandler.save(user);
        return "Save succesful. Id: '" + user.getId() + "'";
    } catch(Exception e){
        e.printStackTrace();
        return "Save failed with: " + e.getMessage();
    }
}

  /**
   * Page showing a specific user's profile. Must check access level (Member+)
   */
  @GetMapping(path = "/user/{id}")
  public @ResponseBody Object profile(@PathVariable Integer id){
    User user = null;
    try {
      user = userdbHandler.findById(id);
      return user;
    } catch (Exception e) {
        System.out.println("Finding user failed with: " + e.getMessage());
        e.printStackTrace();
        return user;
    }
  }

  /**
   * Page showing the "Sign Up" interface. Must check if user is already logged in (and redirect to frontpage or something)
   */
  //@RequestMapping(path = "/signup")
  //public @ResponseBody 
  //TODO: Consider enums (toString) status strings as response 

  /**
   * Login page requests sent to server. 
   */
  @GetMapping(path = "/login")
  public @ResponseBody Object login(String s){return new User(new BasicUserInfo("Fisk", "Pass"));}

  //@GetMapping(path = "/login")
  //public @ResponseBody String login(User f){return "User";}

  
  @PostMapping(path="/add") // Map ONLY POST Requests
  public @ResponseBody Object addNewUser (@RequestParam String name
      , @RequestParam String password) {
    // @ResponseBody means the returned String is the response, not a view name
    // @RequestParam means it is a parameter from the GET or POST request

    User user = new User(new BasicUserInfo(name, password)); 
    //userdbHandler.save(user);

    return "Director Saved Succesful";
  }

  @GetMapping(path="/all")
  public @ResponseBody Object getAllUsers() {
    // This returns a JSON or XML with the users
    return "Fisk";//userdbHandler.findAll();
  }

    @GetMapping(path = "/publicCalendar")
    public @ResponseBody PlaySession ShowCalendar() {

        ArrayList<PlaySession> playSessionList = new ArrayList<PlaySession>();
        LocalDateTime date = LocalDateTime.of(2023, 12, 12, 10, 10, 0);
        Module module = new Module("Module Name", "Module Description", "2-10");
        PlaySession playSession = new PlaySession("Name1", "2", 2, date, "Not yet", 6, module);
        playSessionList.add(playSession);
        playSession = new PlaySession("Name2", "3", 2, date, "Not yet", 6, module);
        playSessionList.add(playSession);

        return playSessionList.get(0);
    }
}