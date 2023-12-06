package com.proj.function;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proj.exception.FailedValidationException;
import com.proj.exception.PlaySessionNotFoundException;
import com.proj.model.session.Module;
import com.proj.model.session.PlaySession;
import com.proj.model.session.PlaySessionStateEnum;
import com.proj.repositoryhandler.ModuledbHandler;
import com.proj.repositoryhandler.PlaySessiondbHandler;

/**
 * PlaySessionManager is responsible for setting the current date, lookupModuleID, validating the session, lookupPlaySessionID,
 * addNewPlaySession, updatePlaySession and getSessions.
 */
@Service
public class PlaySessionManager {

    // Field
    @Autowired
    private PlaySessiondbHandler playSessionHandler;
    @Autowired
    private ModuledbHandler moduledbHandler;
    


    /**
     * 
     * @param playSession object to be saved
     * @throws FailedValidationException
     */
    public void addNewPlaySession(PlaySession playSession) throws FailedValidationException{
        if (validatePlaySession(playSession, true)){
            playSessionHandler.save(playSession);
        } else {
            throw new FailedValidationException("Session add error");
        }
    }



    /**
     * Updates the playSession in the database by resetting the playSession info.
     * @param id                    unique ID of playSession in database
     * @param title                 of playSession
     * @param description           String describing the playsession
     * @param maxNumberOfPlayers    of playSession
     * @param date                  where playsession takes place
     * @param state                 of playSession, as expressed by PlaySessionStateEnum
     * @param module                associated with playsession
     * @return                      saved playSession.
     */
    public void updatePlaySession(Integer id, String title, String description, int maxNumberOfPlayers, LocalDateTime date, PlaySessionStateEnum state, Module module){
        PlaySession playSessionUpdate;
        playSessionUpdate = lookupPlaySessionID(id);

        playSessionUpdate.setTitle(title);
        playSessionUpdate.setDescription(description);
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
     * @param playSession                Object to validate.
     * @param isNewSession               Boolean explaining if playsession is being created or updated
     * @return                           True or false, true if the validation passed and false if it failed to validate. 
     * @throws FailedValidationException with specific error messages if validation fails
     */

    public Boolean validatePlaySession(PlaySession playSession, Boolean isNewSession) throws FailedValidationException{
        //Static Max values
        final int globalMaxNumberOfPlayers = 7;
        final int maxTitleLength = 40;
        String title = playSession.getTitle();
        int maxNumberOfPlayers = playSession.getMaxNumberOfPlayers();
        int currentNumberOfPlayers = playSession.getCurrentNumberOfPlayers();
        String state = playSession.getState();
        //Module module = playSession.getModule();
        //int moduleID = module.getId();//this line is the problem - NullPointerException - kommer stadig ikke ned til notnull7 for some reason
        // tests kører ikke breakpoints iirc, så skal finde ud af hvor nullpointerexceptionen kommer fra
        //Validation Logic - If statment that searches for errors in the playSession info.
        if(title.length() > maxTitleLength){
            throw new FailedValidationException("title exceeds maximum lenght");
        } else if(title.length() <= 0) {
            throw new FailedValidationException("title is empty");
        }//local max number of players within global max limit - Done
        else if(maxNumberOfPlayers > globalMaxNumberOfPlayers){
            throw new FailedValidationException("Maximum players exceeds global maximum");
        }//Current number of players - not higher than maxNumberOfPlayers - Done
        else if(currentNumberOfPlayers > maxNumberOfPlayers){
            throw new FailedValidationException("current number of players exceeds session max");
        }//state - Valid State, kan enten laves om til ENUM, state tal indenfor range - WIP
        //Evt kan vi også tjekke om specifikke state conditions er opfyldt her.
        else if(!state.equals(PlaySessionStateEnum.PLANNED.toString()) && !state.equals(PlaySessionStateEnum.CANCELLED.toString()) && !state.equals(PlaySessionStateEnum.CONCLUDED.toString()) && !state.equals(PlaySessionStateEnum.REWARDSRELEASED.toString())){
            throw new FailedValidationException("Session state invalid");
        }//module - Module ID found in database - Done
        //else if(lookupModuleID(playSession.getModule().getId()) == false){
        //    return false;
        //} 
        else{
            return true;//If validation is passed it will return true.
        }
    }



    /**
     * LookupPlaySession is used by validationPlaySession and updatePlaySession to find the playSession and see if it exists in the database.
     * @param id of playSession that is requested.
     * @return   The playSession id or throw exception.
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
     * @return         This will return true or false, true if found and false if not.
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
     * @param endDate   is the date that the user request the method stops retrieving playSessions.
     * @return          playSessions within the time period.
     */
    public List<PlaySession> getSessions(LocalDateTime startDate, LocalDateTime endDate){
        //henter sessions i en tidsperiode fra databasen
        Iterable<PlaySession> playSessions = playSessionHandler.findByDateBetween(startDate, endDate);
        List<PlaySession> playSessionPeriod = new ArrayList<>();
        playSessions.forEach(playSessionPeriod::add);
        return playSessionPeriod;
    }
}
