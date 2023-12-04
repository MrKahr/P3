package com.proj.unitTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import com.proj.exception.FailedValidationException;
import com.proj.function.PlaySessionManager;
import com.proj.model.session.Module;
import com.proj.model.session.PlaySession;
import com.proj.model.session.PlaySessionStateEnum;
import com.proj.repositoryhandler.PlaySessionHandler;

@SpringBootTest
@ComponentScan
public class playSessionManagerTest {
    //lookupPlaySessionID - param int id - need DB connection
    @Autowired
    PlaySessionHandler testPlaySessionHandler;
    @Autowired
    PlaySessionManager testPlaySessionManager;
    
    @Test
    public void invalidTitle(){
        
        //test if title length is capped by maxlength, assert throws
        Module testModule = new Module("MinesOfPhandelver", "hej", "0-3");
        PlaySession testPlaySession = new PlaySession("012345678901234567890123456789012345678911111111111111", 0, 0, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, 7, testModule);
        Executable e = () -> {testPlaySessionManager.validatePlaySession(testPlaySession, true);};
        Throwable thrown = assertThrows(FailedValidationException.class, e, "title exceeds maximum lenght");
        assertTrue(thrown.getMessage().contains("title exceeds maximum lenght"));
    }

    @Test
    public void invalidMaxNumberOfPlayers(){
        //test if maxNumber is capped by global max, assert throws
        Module testModule = new Module("MinesOfPhandelver", "hej", "0-3");
        PlaySession testPlaySession = new PlaySession("testtitle", 0, 0, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, 20, testModule);
        Executable e = () -> {testPlaySessionManager.validatePlaySession(testPlaySession, true);};
        Throwable thrown = assertThrows(FailedValidationException.class, e, "Maximum players exceeds global maximum");
        assertTrue(thrown.getMessage().contains("Maximum players exceeds global maximum"));
    }

    @Test
    public void invalidCurrentNumberOfPlayers(){
        //test if current number is capped by max, assert throws
        Module testModule = new Module("MinesOfPhandelver", "hej", "0-3");
        PlaySession testPlaySession = new PlaySession("testtitle", 0, 8, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, 7, testModule);
        Executable e = () -> {testPlaySessionManager.validatePlaySession(testPlaySession, true);};
        Throwable thrown = assertThrows(FailedValidationException.class, e, "current number of players exceeds session max");
        assertTrue(thrown.getMessage().contains("current number of players exceeds session max"));
    }

    @Test
    public void validSession(){
        //test assert true
        Module testModule = new Module("MinesOfPhandelver", "hej", "0-3");
        PlaySession testPlaySession = new PlaySession("testtitle", 0, 5, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, 7, testModule);
        assertTrue(testPlaySessionManager.validatePlaySession(testPlaySession, true));
    }

    //addNewPlaySession
    @Test
    public void addInvalidPlaySession(){
        //example invalid currentMax
        Module testModule = new Module("MinesOfPhandelver", "hej", "0-3");
        Executable e = () -> {testPlaySessionManager.addNewPlaySession("testtitle", 0, 0, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, 7, testModule);};
        Throwable thrown = assertThrows(FailedValidationException.class, e, "Session add error");
        assertTrue(thrown.getMessage().contains("Session add error"));
    }

    @Test
    public void addValidPlaySession(){
        Module testModule = new Module("MinesOfPhandelver", "hej", "0-3");
        PlaySession testPlaySession = new PlaySession("testtitle", 0, 5, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, 7, testModule);
        testPlaySessionManager.addNewPlaySession("testtitle", 0, 0, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, 7, testModule);
        //assertEquals(testPlaySession, testPlaySessionHandler.findById(0));
    }

    //updatePlaySession
    @Test // Same test as addNewPlaySession
    public void invalidPlaySessionUpdate(){
        Module testModule = new Module("MinesOfPhandelver", "hej", "0-3");
        Executable e = () -> {testPlaySessionManager.addNewPlaySession("testtitle2", 0, 0, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, 7, testModule);};
        Throwable thrown = assertThrows(FailedValidationException.class, e, "Session update error");
        assertTrue(thrown.getMessage().contains("Session update error"));
    }
    @Test
    public void validPlaySessionUpdate(){
        Module testModule = new Module("MinesOfPhandelver", "hej", "0-3");
        PlaySession testPlaySession = new PlaySession("testtitle2", 0, 5, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, 7, testModule);
        testPlaySessionManager.updatePlaySession(0, "testtitle2", 7, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, testModule);
        //assertEquals(testPlaySession.getTitle(), testPlaySessionHandler.findById(0).getTitle());
        //opdaterer session med id 0 fra tidligere test, og sikrer at det virker ved at sammenligne den opdaterede titel (før testtitle, nu testtitle2)
    }

    //getSessions
    @Test 
    public void getSessionsTest(){//Not to sure how to test this - same, vi hører lige emil imorgen
        
    }
    
    @Test
    public void validSessionID(){
        //not sure how to test, returns playsession object from id
        
    }

    /* @Test
    public void invalidSessionID(){
        //assert throws with invalid ID
        PlaySessionHandler testPlaySessionHandler = new PlaySessionHandler();
        Executable e = () -> {testPlaySessionHandler.findById(100);};
        Throwable thrown = assertThrows(PlaySessionNotFoundException.class, e, "Session not found");
        assertTrue(thrown.getMessage().contains("Session not found"));
        
    } */
    //lookupModuleID - param int id - need DB connection

    @Test
    public void validModuleID(){
        //assert true
        
    }

    /* @Test
    public void invalidModuleID(){
        //assert throws with invalid ID
        ModuledbHandler testModuledbHandler = new ModuledbHandler();
        Executable e = () -> {testModuledbHandler.findById(100);};
        Throwable thrown = assertThrows(PlaySessionNotFoundException.class, e, "Session not found");
        assertTrue(thrown.getMessage().contains("module not found"));
        //TODO check actual error type and message
        
    } */
    
    //validatePlaySession

    @Test
    public void getDateTest(){
        //confused myself here, rn this tests if date is not null, should test that correct error is thrown in validation if date is null. Maybe. I think. Brain go brr.
        Module testModule = new Module("MinesOfPhandelver", "hej", "0-3");
        PlaySession testPlaySession = new PlaySession("testtitle", 0, 0, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, 7, testModule);
        assertNotNull(testPlaySession.getDate());
    }
    
}
