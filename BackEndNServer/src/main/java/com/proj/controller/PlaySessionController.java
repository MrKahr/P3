package com.proj.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.proj.model.session.PlaySession;
import com.proj.model.session.Module;
import com.proj.function.PlaySessionManager;
import com.proj.repositoryhandler.PlaySessionHandler;

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
  
  PlaySessionHandler playSessionHandler;
  PlaySessionManager playSessionManager = new PlaySessionManager(LocalDateTime.now());

  @PostMapping(path = "/NewPlaySession")
  public @ResponseBody String addNewPlaySessionResponse(@RequestParam String title, @RequestParam Integer id, @RequestParam Integer currentNumberOfPlayers, @RequestParam LocalDateTime date, @RequestParam PlaySessionStateEnum state, @RequestParam Integer maxNumberOfPlayers, @RequestParam Module module){
    playSessionManager.addNewPlaySession(title,id,currentNumberOfPlayers, date, state, maxNumberOfPlayers, module);
    return "PlaySession Created";
  }
  
  @PostMapping(path = "/UpdatePlaySession") //responds to put requests with path "/UpdatePlaySession" and request parameters, returns Successful if update is valid
  public @ResponseBody String updatePlaySessionResponse(@RequestParam String title, @RequestParam Integer id, @RequestParam LocalDateTime date, @RequestParam PlaySessionStateEnum state, @RequestParam Integer maxNumberOfPlayers, @RequestParam Module module){
    playSessionManager.updatePlaySession(id, title, maxNumberOfPlayers, date, state, module);
    return "Update Successfull";
  }

  @GetMapping(path="/getAllPlaySessions") //returns all play sessions
  public @ResponseBody List<PlaySession> getAllPlaySessions() {
    Iterable<PlaySession> playSessions = playSessionHandler.findAll();
    List<PlaySession> allPlaySessions = new ArrayList<>();
        playSessions.forEach(allPlaySessions::add);
    return allPlaySessions;
  }
  
  @GetMapping(path="/DateBetween") //returns all play sessions between two dates
  public @ResponseBody List<PlaySession> getPlaySessionsBetween(@RequestParam LocalDateTime startDateTime, @RequestParam LocalDateTime endDateTime) {
      return playSessionManager.getSessions(startDateTime, endDateTime);
  }

}
