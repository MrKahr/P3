// Handles http requests for users from front end

// Frontend -> UserController -> Backend

// Help Repository setup -> https://www.geeksforgeeks.org/spring-boot-crudrepository-with-example/
// Help JPARepositories annotation -> https://stackoverflow.com/questions/27856266/how-to-make-instance-of-crudrepository-interface-during-testing-in-spring 

package com.proj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proj.model.users.*;

// The call hierarchy for the database connection is: Controller -> Manager -> Handler

@Controller 
@RequestMapping(path = "/user")

public class UserController {
  
  //TODO: Get single user 
  //TODO: Get multiple users 
  //TODO: Get all users 
  //TODO: Add users to DB 
  //TODO: Add multiple users to DB 
  //TODO: Delete users in DB 
  //TODO: Delete mulitiple users in DB 

  @GetMapping(path = "/get")
  public @ResponseBody String hello(){

    User user = new User(new BasicUserInfo("name", "password")); 
    //userdbHandler.save(user);

  return "Fisk";
    //return userdbHandler.findAll();
  
}

  @PostMapping(path="/add") // Map ONLY POST Requests
  public @ResponseBody String addNewUser (@RequestParam String name
      , @RequestParam String password) {
    // @ResponseBody means the returned String is the response, not a view name
    // @RequestParam means it is a parameter from the GET or POST request

    User user = new User(new BasicUserInfo(name, password)); 
    //userdbHandler.save(user);

    return "Director Saved Succesful";
  }

  @GetMapping(path="/all")
  public @ResponseBody String getAllUsers() {
    // This returns a JSON or XML with the users
    return "Fisk";//userdbHandler.findAll();
  }
}