package com.proj.function;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import com.proj.model.session.PlaySession;
import com.proj.exception.FailedValidationException;
import com.proj.model.events.ModuleSet;
import com.proj.model.session.Module;

/**
 * CalendarManager is responsible for setting the current date, validating the session, 
 */


public class CalendarManager {

    @Autowired
    private PlaySessionRepository playSessionRepository;
    private ModuleRepository moduleRepository;

    // Field
    private LocalDate currentDate;

    // Constructor
    public CalendarManager(LocalDate currentDate){
        this.currentDate = currentDate;
    }


    // Method
    public void setCurrentDate(){
        this.currentDate = LocalDate.now();
    }


    
    public boolean lookupModuleID(int moduleID){
        boolean result = true;
        try {
            moduleRepository.findById(moduleID);
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
        String id = playSession.getId();
        int maxNumberOfPlayers = playSession.getMaxNumberOfPlayers();
        int currentNumberOfPlayers = playSession.getCurrentNumberOfPlayers();
        int state = playSession.getState();
        Module module = playSession.getModule();
        int moduleID = module.getId();
        //check if date is valid - done
        try {
            LocalDateTime date = playSession.getDate();
        } catch (Exception e) {
            System.out.println("Session has invalid date " + e.getMessage());
            return false;
        }
        
        //Validation Logic
        
        try {// Session ID found in database - TBD
            if(state < 0 || state > 4){
                return false;
            }//Title - Title length within maxTitleLength size - Done
            else if(title.length() > maxTitleLength || title.length() <= 0){
                return false;
            }//local max number of players within global max limit - Done
            else if(maxNumberOfPlayers > globalMaxNumberOfPlayers){
                return false;
            }//Current number of players - not higher than maxNumberOfPlayers - Done
            else if(currentNumberOfPlayers > maxNumberOfPlayers){
                return false;
            }//state - Valid State, kan enten laves om til ENUM, state tal indenfor range - WIP
            //Evt kan vi også tjekke om specifikke state conditions er opfyldt her.
            else if(state < 0 || state > 3){
                return false;
            }//modulesetevents - Valid set of module IDs (TBD) - WIP
            else if(state < 0 || state > 5){
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
    public PlaySession lookupPlaySessionID(String id){
        PlaySession result;
        try {
            Optional<PlaySession> optionalPlaySession = playSessionRepository.findById(id);
            if (optionalPlaySession.isPresent()) {
            result = optionalPlaySession.get();
            } else {
            System.out.println("Session not found");
            result = null;
            }
            result = playSessionRepository.findById(id).get();
        } catch (Exception e) {
            System.out.println("Session not found " + e.getMessage());
            result = null;
        }
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
    public PlaySession updatePlaySession(String id, String title, int maxNumberOfPlayers, LocalDateTime date, int state, Module module){
        PlaySession playSessionUpdate;
        playSessionUpdate = lookupPlaySessionID(id);

        playSessionUpdate.setTitle(title);
        playSessionUpdate.setMaxNumberOfPlayers(maxNumberOfPlayers);
        playSessionUpdate.setDate(date);
        playSessionUpdate.setState(state);
        playSessionUpdate.setModule(module);
        
        if (validatePlaySession(playSessionUpdate)){
            return playSessionRepository.save(playSessionUpdate);
        } else {
            throw new FailedValidationException("Session validation error");
        }
    }

    public PlaySession sendSessions(){
        // REST response to GET request for session
        
        return null;
    }
    
    public PlaySession getSessions(LocalDateTime startDate, LocalDateTime endDate){
        //henter sessions i en tidsperiode fra databasen
        return null;
    }

    /**
     * public PlaySession getSessionInfo(){
     *   return null;
     * }
     */

}
