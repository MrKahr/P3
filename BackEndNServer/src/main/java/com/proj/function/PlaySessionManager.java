package com.proj.function;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.proj.exception.FailedValidationException;
import com.proj.exception.PlaySessionNotFoundException;
import com.proj.model.session.Module;
import com.proj.model.session.PlaySession;
import com.proj.model.session.PlaySessionStateEnum;
import com.proj.repositoryhandler.ModuledbHandler;
import com.proj.repositoryhandler.PlaySessionHandler;

/**
 * PlaySessionManager is responsible for setting the current date, lookupModuleID, validating the session, lookupPlaySessionID,
 * addNewPlaySession, updatePlaySession and getSessions.
 */
public class PlaySessionManager {

/*     @Autowired */
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
     * addNewPlaySession
     * @param id
     * @param title
     * @param maxNumberOfPlayers
     * @param date
     * @param state
     * @param module
     */
    public void addNewPlaySession(String title,int id, int currentNumberOfPlayers, LocalDateTime date, PlaySessionStateEnum state, int maxNumberOfPlayers, Module module){
        PlaySession newPlaySession = new PlaySession(title,id,currentNumberOfPlayers, date, state, maxNumberOfPlayers, module);
        if (validatePlaySession(newPlaySession, true)){
            playSessionHandler.save(newPlaySession);
        } else {
            throw new FailedValidationException("Session add error");
        }
    }



    /**
     * Updates the playSession in the database by resetting the playSession info.
     * @param id unique ID of playSession in database
     * @param title of playSession
     * @param maxNumberOfPlayers of playSession
     * @param date of playSession
     * @param state of playSession
     * @param module of playSession
     * @return saved playSession.
     */
    public void updatePlaySession(int id, String title, int maxNumberOfPlayers, LocalDateTime date, PlaySessionStateEnum state, Module module){
        PlaySession playSessionUpdate;
        playSessionUpdate = lookupPlaySessionID(id);

        playSessionUpdate.setTitle(title);
        playSessionUpdate.setMaxNumberOfPlayers(maxNumberOfPlayers);
        playSessionUpdate.setDate(date);
        playSessionUpdate.setState(state);
        playSessionUpdate.setModule(module);
        
        if (validatePlaySession(playSessionUpdate, false)){
            playSessionHandler.save(playSessionUpdate);
        } else {
            throw new FailedValidationException("Session update error");
        }
    }



    /**
     * Validates the playSession with If statement.
     * @param playSession object to validate.
     * @return True or false, true if the validation passed and false if it failed to validate. 
     * throws ValidationFailedException with specific error messages if validation fails
     */
    public Boolean validatePlaySession(PlaySession playSession, Boolean isNewSession){
        //Static Max values
        final int globalMaxNumberOfPlayers = 7;
        final int maxTitleLength = 40;
        String title = playSession.getTitle();
        Integer id = playSession.getId();
        int maxNumberOfPlayers = playSession.getMaxNumberOfPlayers();
        int currentNumberOfPlayers = playSession.getCurrentNumberOfPlayers();
        PlaySessionStateEnum state = playSession.getState();
        //Module module = playSession.getModule();
        //int moduleID = module.getId();//this line is the problem - NullPointerException - kommer stadig ikke ned til notnull7 for some reason
        // tests kører ikke breakpoints iirc, så skal finde ud af hvor nullpointerexceptionen kommer fra
        //Validation Logic - If statment that searches for errors in the playSession info.
        if(title.length() > maxTitleLength || title.length() <= 0){
            throw new FailedValidationException("title exceeds maximum lenght");
        }//local max number of players within global max limit - Done
        else if(maxNumberOfPlayers > globalMaxNumberOfPlayers){
            throw new FailedValidationException("Maximum players exceeds global maximum");
        }//Current number of players - not higher than maxNumberOfPlayers - Done
        else if(currentNumberOfPlayers > maxNumberOfPlayers){
            throw new FailedValidationException("current number of players exceeds session max");
        }//state - Valid State, kan enten laves om til ENUM, state tal indenfor range - WIP
        //Evt kan vi også tjekke om specifikke state conditions er opfyldt her.
        else if(state != PlaySessionStateEnum.PLANNED && state != PlaySessionStateEnum.CANCELLED && state != PlaySessionStateEnum.CONCLUDED && state != PlaySessionStateEnum.REWARDSRELEASED){
            throw new FailedValidationException("Session state invalid");
        }else if (!isNewSession) {
            lookupPlaySessionID(id); //throws exception if not found
            playSession.getDate(); //throws exception if not found
            return false;
        }//module - Module ID found in database - Done
        //else if(lookupModuleID(moduleID) == false){
        //    return false;
        //} 
        else{
            return true;//If validation is passed it will return true.
        }
        //Same code but with try catch, this interferes with testing for now as exceptions are caught before test can recognize assertthrows
        /* try {// Session ID found in database - TBD
            //Title - Title length within maxTitleLength size - Done
            if(title.length() > maxTitleLength || title.length() <= 0){
                throw new FailedValidationException("title exceeds maximum lenght");
            }//local max number of players within global max limit - Done
            else if(maxNumberOfPlayers > globalMaxNumberOfPlayers){
                throw new FailedValidationException("Maximum players exceeds global maximum");
            }//Current number of players - not higher than maxNumberOfPlayers - Done
            else if(currentNumberOfPlayers > maxNumberOfPlayers){
                throw new FailedValidationException("current number of players exceeds session max");
            }//state - Valid State, kan enten laves om til ENUM, state tal indenfor range - WIP
            //Evt kan vi også tjekke om specifikke state conditions er opfyldt her.
            else if(state != PlaySessionStateEnum.PLANNED && state != PlaySessionStateEnum.CANCELLED && state != PlaySessionStateEnum.CONCLUDED && state != PlaySessionStateEnum.REWARDSRELEASED){
                throw new FailedValidationException("Session state invalid");
            }else if (!isNewSession) {
                lookupPlaySessionID(id); //throws exception if not found
                playSession.getDate(); //throws exception if not found
                return false;
            }//module - Module ID found in database - Done
            //else if(lookupModuleID(moduleID) == false){
            //    return false;
            //} 
            else{
                return true;//If validation is passed it will return true.
            }
        } catch(Exception e){
            System.out.println("Session validation error " + e.getMessage());
            return false;//Catch exception returns false if the validation failed.
        } */
    }



    /**
     * LookupPlaySession is used by validationPlaySession and updatePlaySession to find the playSession and see if it exists in the database.
     * @param id of playSession that is requested.
     * @return The playSession id or throw exception.
     */
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
     * LookModule is used by validationPlaySession to find the module and see if it exists in the database.
     * @param moduleID is the id for the playSession that is requested.
     * @return This will return true or false, true if found and false if not.
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
     * getSessions retrieve playSessions from the database within the time period requested the default time period is a month.
     * @param startDate is the date that the user reqests the method starts retrieving playSessions.
     * @param endDate is the date that the user request the method stops retrieving playSessions.
     * @return playSessions within the time period.
     */
    public List<PlaySession> getSessions(LocalDateTime startDate, LocalDateTime endDate){
        //henter sessions i en tidsperiode fra databasen
        Iterable<PlaySession> playSessions = playSessionHandler.findByDateBetween(startDate, endDate);
        List<PlaySession> playSessionPeriod = new ArrayList<>();
        playSessions.forEach(playSessionPeriod::add);
        return playSessionPeriod;
    }
}
