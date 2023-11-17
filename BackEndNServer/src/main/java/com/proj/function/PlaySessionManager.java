package com.proj.function;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.proj.model.session.PlaySession;
import com.proj.model.session.PlaySessionStateEnum;
import com.proj.model.events.ModuleSet;
import com.proj.model.session.Module;
import com.proj.exception.FailedValidationException;
import com.proj.exception.PlaySessionNotFoundException;
import com.proj.repositoryhandler.ModuledbHandler;
import com.proj.repositoryhandler.PlaySessionHandler;

/**
 * CalendarManager is responsible for setting the current date, validating the session,
 */


public class PlaySessionManager {

    @Autowired
    PlaySessionHandler playSessionHandler;
    ModuledbHandler moduledbHandler;

    // Field
    private LocalDateTime currentDate;

    // Constructor
    public PlaySessionManager(LocalDateTime currentDate){
        this.currentDate = currentDate;
    }


    // Method
    public void setCurrentDate(){
        this.currentDate = LocalDateTime.now();
    }


    /**
     * LookModule is used by validationPlaySession to find the module and see if it exists in the database.
     */
    public boolean lookupModuleID(int moduleID){
        boolean result = true;
        try {
            moduledbHandler.findById(moduleID);
        } catch (Exception e) {
            System.out.println("Module not found " + e.getMessage());
            result = false;
        }
        return result;
    }
    
    /**
     * Validates the playSession
     * @param playSession object to validate.
     * @return True or false, true if the validation passed and false if it failed to validate.
     */
    public Boolean validatePlaySession(PlaySession playSession){
        //Static Max values
        final int globalMaxNumberOfPlayers = 7;
        final int maxTitleLength = 40;
        String title = playSession.getTitle();
        Integer id = playSession.getId();
        int maxNumberOfPlayers = playSession.getMaxNumberOfPlayers();
        int currentNumberOfPlayers = playSession.getCurrentNumberOfPlayers();
        PlaySessionStateEnum state = playSession.getState();
        Module module = playSession.getModule();
        int moduleID = module.getId();
        
        //Validation Logic
        
        try {// Session ID found in database - TBD
            lookupPlaySessionID(id); //throws exception if not found
            playSession.getDate(); //throws exception if not found
            //Title - Title length within maxTitleLength size - Done
            if(title.length() > maxTitleLength || title.length() <= 0){
                return false;
            }//local max number of players within global max limit - Done
            else if(maxNumberOfPlayers > globalMaxNumberOfPlayers){
                return false;
            }//Current number of players - not higher than maxNumberOfPlayers - Done
            else if(currentNumberOfPlayers > maxNumberOfPlayers){
                return false;
            }//state - Valid State, kan enten laves om til ENUM, state tal indenfor range - WIP
            //Evt kan vi også tjekke om specifikke state conditions er opfyldt her.
            else if(state != PlaySessionStateEnum.PLANNED && state != PlaySessionStateEnum.CANCELLED && state != PlaySessionStateEnum.CONCLUDED && state != PlaySessionStateEnum.REWARDSRELEASED){
                return false;
            }//module - Module ID found in database - Done
            else if(lookupModuleID(moduleID) == false){
                return false;
            } else{
                return true;//If validation is passed it will return true.
            }
        } catch(Exception e){
            System.out.println("Session validation error " + e.getMessage());
            return false;//Catch exception returns false if the validation failed.
        }

    }

    //sendSessionUpdate - find the playSession in the database for comparison.
    //Optional<PlaySession>

    public PlaySession lookupPlaySessionID(Integer id){
        PlaySession result;
        Object optionalPlaySession = playSessionHandler.findById(id); //ERROR
        if (optionalPlaySession instanceof PlaySession) {
            result = (PlaySession) optionalPlaySession;
        } else {
            throw new PlaySessionNotFoundException("Session not found");
        }
        result = playSessionHandler.findById(id);//ERROR
        return result;
    }

    /**
     * Updates the playSession in the database
     *
     * @param playSessionID       unique ID of playSession in database
     * @param title               of playSession
     * @param maxNumberOfPlayers  of playSession
     * @param date                of playSession
     * @param state               of playsession
     * @param module              of playSession
     * @return saved playSession
     */
    public PlaySession updatePlaySession(int id, String title, int maxNumberOfPlayers, LocalDateTime date, PlaySessionStateEnum state, Module module){
        PlaySession playSessionUpdate;
        playSessionUpdate = lookupPlaySessionID(id);

        playSessionUpdate.setTitle(title);
        playSessionUpdate.setMaxNumberOfPlayers(maxNumberOfPlayers);
        playSessionUpdate.setDate(date);
        playSessionUpdate.setState(state);
        playSessionUpdate.setModule(module);
        
        if (validatePlaySession(playSessionUpdate)){
            playSessionHandler.save(playSessionUpdate);
            return playSessionUpdate;
        } else {
            throw new FailedValidationException("Session validation error");
        }
    }

    /* public List < PlaySession > findAll() {
        return playSessionRepository.f
    } */

    public List<PlaySession> getSessions(LocalDateTime startDate, LocalDateTime endDate){
        //henter sessions i en tidsperiode fra databasen
        Iterable<PlaySession> playSessions = playSessionHandler.findByDateBetween(startDate, endDate);
        List<PlaySession> result = new ArrayList<>();
        playSessions.forEach(result::add);

        return result;
    }

    public void sendSessions(Iterable<PlaySession> sessions){
        // REST response to GET request for session
        
    }

    /**
     * public PlaySession getSessionInfo(){
     *   return null;
     * }
     */

}
