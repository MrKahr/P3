package com.proj.controller;

import java.time.LocalDateTime;
import java.util.List;
import com.proj.model.session.PlaySession;
import com.proj.model.session.Module;
import com.proj.exception.NoModuleFoundException;
import com.proj.function.ModuleManager;
import com.proj.function.PlaySessionManager;
import com.proj.repositoryhandler.PlaySessiondbHandler;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proj.model.session.*;

@Controller
public class PlaySessionController {

  @Autowired
  private PlaySessiondbHandler playSessiondbHandler;
  @Autowired
  private PlaySessionManager playSessionManager;
  @Autowired
  private ModuleManager moduleManager;

  @PostMapping(path = "/newplaysession")
  public @ResponseBody String addNewPlaySessionResponse(@RequestParam String title,
      @RequestParam String description, @RequestParam String dm, @RequestParam Integer currentNumberOfPlayers,
      @RequestParam LocalDateTime date,
      @RequestParam Integer maxNumberOfPlayers, @RequestParam Integer moduleID) {
    Module module;
    try {
      module = moduleManager.getModuledbHandler().findById(moduleID);
    } catch (NoModuleFoundException nmfe) {
      module = null;
    }

    PlaySession playSession = new PlaySession(title, description, dm, currentNumberOfPlayers, date,
        PlaySessionStateEnum.PLANNED,
        maxNumberOfPlayers, module);
    playSessionManager.addNewPlaySession(playSession);
    return "PlaySession Created with ID: " + playSession.getId();
  }

  @PostMapping(path = "/updateplaysession") // responds to put requests with path "/UpdatePlaySession" and request
                                            // parameters, returns Successful if update is valid
  public @ResponseBody String updatePlaySessionResponse(@RequestParam Integer id, @RequestParam String title,
      @RequestParam String description,
      @RequestParam LocalDateTime date, @RequestParam String stateString,
      @RequestParam Integer maxNumberOfPlayers, Integer moduleID) {
    Module module;
    try {
      module = moduleManager.getModuledbHandler().findById(moduleID);
    } catch (NoModuleFoundException nmfe) {
      module = null;
    }
    PlaySessionStateEnum state = PlaySessionStateEnum.valueOf(stateString);
    playSessionManager.updatePlaySession(id, title, description, maxNumberOfPlayers, date, state, module);
    return "Update Successfull";
  }

  
   @GetMapping(path="/getAllPlaySessions") //returns all play sessions
   public @ResponseBody List<PlaySession> getAllPlaySessions() {
   Iterable<PlaySession> playSessions = playSessiondbHandler.findAll();
   List<PlaySession> allPlaySessions = new ArrayList<>();
   playSessions.forEach(allPlaySessions::add);
   return allPlaySessions;
   }
   

  @GetMapping(path = "/datebetween") // returns all play sessions between two dates
  public @ResponseBody ArrayList<PlaySession> getPlaySessionsBetween(@RequestParam LocalDateTime startDateTime,
      @RequestParam LocalDateTime endDateTime) {
    Iterable<PlaySession> playSessions = playSessiondbHandler.findByDateBetween(startDateTime, endDateTime);
    // We don't want rewards publicly accessible
    ArrayList<PlaySession> testList = new ArrayList<PlaySession>() {
      
    };
    for(PlaySession playSession : playSessions) {
      playSession.setRewards(null);
      testList.add(playSession);
    }
    return testList;
  }

  @GetMapping(path = "/play_session") //TODO: return specific play session with or without rewards, depending on access level
  public @ResponseBody PlaySession getPlaySession(@RequestParam int id) {
    return playSessiondbHandler.findById(id);
  }
}
