package com.proj.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import com.proj.model.session.PlaySession;
import com.proj.model.session.Module;
import com.proj.function.PlaySessionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.proj.model.session.*;

@Controller
@RequestMapping(path = "/P3")
public class PlaySessionController {
  

  @GetMapping(path="/all")
  public @ResponseBody String getAllPlaySessions() {
    // This returns a JSON or XML with the PlaySessions
    return "Fisk";//PlaySessiondbHandler.findAll();
  }
  
  @GetMapping(path="/DateBetween")
  public @ResponseBody String getPlaySessionsBetween(@RequestParam LocalDateTime startDateTime, @RequestParam LocalDateTime endDateTime) {
    PlaySessionManager.getSessions(startDateTime, endDateTime); //TODO Get sessions returnerer list<PlaySession>, convert this to JSON
      return new SomeData();
  }
  
}
