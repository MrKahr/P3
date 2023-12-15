package com.proj.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.proj.model.session.Module;
import com.proj.model.users.RoleType;
import com.proj.model.users.User;
import com.proj.exception.IllegalUserOperationException;
import com.proj.exception.NoModuleFoundException;
import com.proj.function.ModuleManager;
import com.proj.function.PlaySessionManager;
import com.proj.function.UserManager;
import com.proj.repositoryhandler.PlaySessiondbHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proj.model.session.*;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class PlaySessionController {

  @Autowired
  private PlaySessiondbHandler playSessiondbHandler;
  @Autowired
  private PlaySessionManager playSessionManager;
  @Autowired
  private ModuleManager moduleManager;
  @Autowired
  private UserManager userManager;

  // TODO: check credentials before creating playsession
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

  // TODO: check credentials before updating playsession
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

  /*
   * @GetMapping(path="/getAllPlaySessions") //returns all play sessions
   * public @ResponseBody List<PlaySession> getAllPlaySessions() {
   * Iterable<PlaySession> playSessions = playSessionHandler.findAll();
   * List<PlaySession> allPlaySessions = new ArrayList<>();
   * playSessions.forEach(allPlaySessions::add);
   * return allPlaySessions;
   * }
   */

  @GetMapping(path = "/datebetween") // returns all play sessions between two dates
  public @ResponseBody List<PlaySession> getPlaySessionsBetween(@RequestParam LocalDateTime startDateTime,
      @RequestParam LocalDateTime endDateTime) {
    List<PlaySession> playSessions = playSessionManager.getSessions(startDateTime, endDateTime);
    // We don't want rewards publicly accessible
    for (PlaySession playSession : playSessions) {
      playSession.setRewards(null);
    }
    return playSessions;
  }

  @GetMapping(path = "/play_session/{id}")
  public @ResponseBody PlaySession getPlaySession(@PathVariable Integer id,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    PlaySession playSession = playSessiondbHandler.findById(id);
    if (!playSession.getDm().equals(authentication.getName())
        || !playSession.getUsers().contains(authentication.getName())) {
      // Hide rewards if user is not part of the session
      playSession.setRewards(null);
    }
    return playSession;
  }

  @PostMapping("/play_session/assign")
  public String assignUser(@RequestParam String username, @RequestParam Integer playSessionID) {
    PlaySession playSession = playSessiondbHandler.findById(playSessionID);
    playSession.assignUser(username);
    playSessiondbHandler.save(playSession);

    return username + " added successfully.";
  }

  @PostMapping("/play_session/unassign")
  public String unassignUser(@RequestParam String username, @RequestParam Integer playSessionID,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    PlaySession playSession = playSessiondbHandler.findById(playSessionID);
    // Can only unassign yourself, unless you're the DM
    if (authentication.getName().equals(playSession.getDm()) || authentication.getName().equals(username)) {
      playSession.unassignUser(username);
      playSessiondbHandler.save(playSession);
    }

    return username + " removed from session.";
  }

  @PostMapping("/play_session/add_rewards")
  public String addRewards(@RequestBody ArrayList<Reward> rewards, @RequestParam Integer playSessionID,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    PlaySession playSession = playSessiondbHandler.findById(playSessionID);
    User user = userManager.lookupAccount(authentication.getName());

    // User must be the DM or an admin to set rewards
    if (authentication.getName().equals(playSession.getDm()) || user.getAllRoles().containsKey(RoleType.ADMIN)) {
      playSession.setRewards(rewards);
      playSessiondbHandler.save(playSession);
      return "Rewards added successfully";
    } else {
      throw new IllegalUserOperationException("User does not have the credentials to set rewards");
    }
  }
}
