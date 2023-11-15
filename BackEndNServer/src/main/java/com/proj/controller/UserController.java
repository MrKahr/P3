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

import com.proj.director.UserManager;
import com.proj.model.users.*;


@Controller 
@RequestMapping(path = "/P3")

public class UserController {
    UserManager userManager = new UserManager();

  @PostMapping(path="/add") // Map ONLY POST Requests
  public @ResponseBody String addNewUser (@RequestParam String name
      , @RequestParam String password) {
    // @ResponseBody means the returned String is the response, not a view name
    // @RequestParam means it is a parameter from the GET or POST request

    User user = new Guest(name, password); 
    userManager.save(user);

    return "Director Saved Succesful";
  }

  @GetMapping(path="/all")
  public @ResponseBody Iterable<User> getAllUsers() {
    // This returns a JSON or XML with the users
    return userManager.findAll();
  }
}