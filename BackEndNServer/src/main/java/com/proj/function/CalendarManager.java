package com.proj.function;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import com.proj.exception.UserNotFoundException;
import com.proj.model.session.PlaySession;
import com.proj.model.session.Module;

/**
 * CalendarManager is responsible for setting the current date, validating the session, 
 */


public class CalendarManager {

    @Autowired
    private CalendarRepository calendarRepository;
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

    public Boolean validateSession(PlaySession playSession){
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
        int moduleIDLookup = -1;
        moduleIDLookup = moduleRepository.findById(moduleID);
        //Validation Logic
        // Session ID - TBD
        try {
        if(1 == 1){
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
        }//datetime - Valid LocalDateTime - WIP
         else if(1==1){
            return false;
        }//state - Valid State, kan enten laves om til ENUM, state tal indenfor range - WIP
        //Evt kan vi også tjekke om specifikke state conditions er opfyldt her.
         else if(state < 0 || state > 3){
            return false;
        }//modulesetevents - Valid set of module IDs (TBD) - WIP
         else if(1==1){
            return false;
        }//module - Valid module ID (TBD) - WIP
         else if(moduleIDResult == -1){
            return false;
         }
            
        } else{
            return true;
        }

        } catch(){
            
        }
        

    }

    public PlaySession sendSessionUpdate(){
        return null;
    }

    public PlaySession sendCalendar(){
        return null;
    }

    public PlaySession getSessions(){
        return null;
    }

    public PlaySession getSessionInfo(){
        return null;
    }

    public PlaySession setSessionState(){
        return null;
    }

    public PlaySession addToCalendar(){
        return null;
    }
}
