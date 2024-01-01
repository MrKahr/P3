package com.proj.controller.api;

import java.time.LocalDateTime;
import java.util.List;

import com.proj.model.session.Module;
import com.proj.model.users.RoleType;
import com.proj.controller.security.authentication.UserDAO;
import com.proj.exception.NoModuleFoundException;
import com.proj.function.ModuleManager;
import com.proj.function.PlaySessionManager;
import com.proj.repositoryhandler.PlaySessiondbHandler;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proj.model.session.*;

@Controller
@RequestMapping (path = "/api/playsession")
public class PlaySessionController {

  @Autowired
  private PlaySessiondbHandler playSessiondbHandler;
  @Autowired
  private PlaySessionManager playSessionManager;
  @Autowired
  private ModuleManager moduleManager;
  @Autowired
  private UserDAO userDAO;

  // TODO: Atm. everyone can create a playsession. Make sure this does NOT continue!
  @PostMapping(path = "/newplaysession")
  public @ResponseBody String addNewPlaySessionResponse(@RequestParam String title,
      @RequestParam String description, @RequestParam String dm, @RequestParam String location,
      @RequestParam LocalDateTime date, @RequestParam Integer maxNumberOfPlayers, @RequestParam Integer moduleID,
      @RequestParam String requiredRole) {
      
      Module module = null;
      RoleType requiredRoleType;
      Integer currentNumberOfPlayers = 0;

      // try {
      //   module = moduleManager.getModuledbHandler().findById(moduleID);
      // } catch (NoModuleFoundException nmfe) {
      //   module = null;
      // }

      if(requiredRole.equals("Member")){
        requiredRoleType = RoleType.MEMBER;
      }
      else {
        requiredRoleType = null;
      }

      PlaySession playSession = new PlaySession(title, description, dm, currentNumberOfPlayers, date,
                                                PlaySessionStateEnum.PLANNED, maxNumberOfPlayers, module, location, requiredRoleType);
      playSessionManager.addNewPlaySession(playSession);
      return "PlaySession Created with ID: " + playSession.getId();
  }

  // TODO: Atm. everyone can update a playsession. Make sure this does NOT continue!
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

   @GetMapping(path="/getallplaysessions") //returns all play sessions
   public @ResponseBody List<PlaySession> getAllPlaySessions(@CurrentSecurityContext(expression = "authentication") Authentication authentication) {
      
      Iterable<PlaySession> playSessions = playSessiondbHandler.findAll();
      return processPlaySessions(playSessions, authentication);
    }

  @GetMapping(path = "/datebetween") // returns all play sessions between two dates
  public @ResponseBody ArrayList<PlaySession> getPlaySessionsBetween(@RequestParam LocalDateTime startDateTime, @RequestParam LocalDateTime endDateTime,
   @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
      
      Iterable<PlaySession> playSessions = playSessiondbHandler.findByDateBetween(startDateTime, endDateTime);
      return processPlaySessions(playSessions, authentication);
  }

  /**
   * Ensures that the user can only get playsessions they have the required role for.
   * @param playSessionList The list of playsessions to process.
   * @param authentication The user's authentication object.
   * @return
   */
  private ArrayList<PlaySession> processPlaySessions(Iterable<PlaySession> playSessionList, Authentication authentication){
    ArrayList<PlaySession> playSessions = new ArrayList<PlaySession>();
    
    for(PlaySession playSession : playSessionList) {
        if(playSession.getRequiredRole() != null){
          if(!userDAO.checkAuthority(authentication, playSession.getRequiredRole())){
            continue;
          }
        }
        playSession.setRewards(null); // We don't want rewards publicly accessible
        playSessions.add(playSession);
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
  public @ResponseBody String assignUser(@RequestParam String username, @RequestParam Integer playSessionID) {
    PlaySession playSession = playSessiondbHandler.findById(playSessionID);
    playSessionManager.assignUserToPlaySession(playSession, username);
    return username + " assigned successfully.";
  }

  @PostMapping("/play_session/unassign")
  public @ResponseBody String unassignUser(@RequestParam String username, @RequestParam Integer playSessionID,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    PlaySession playSession = playSessiondbHandler.findById(playSessionID);
    if (authentication.getName().equals(playSession.getDm()) || authentication.getName().equals(username)) { // Can only unassign yourself, unless you're the DM
      playSessionManager.unassignUserFromPlaySession(playSession, username);
    }
    
    return username + " removed from session.";
  }
}
