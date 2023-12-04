package com.proj.unitTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

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

    //ValidatePlaySession
    
    @Test
    public void invalidTitle(){
        
        //test if title length is capped by maxlength, assert throws
        Module testModule = new Module("MinesOfPhandelver", "hej", "0-3");
        PlaySession testPlaySession = new PlaySession("012345678901234567890123456789012345678911111111111111", 0, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, 7, testModule);
        Executable e = () -> {testPlaySessionManager.validatePlaySession(testPlaySession, true);};
        Throwable thrown = assertThrows(FailedValidationException.class, e, "title exceeds maximum lenght");
        assertTrue(thrown.getMessage().contains("title exceeds maximum lenght"));
    }

    @Test
    public void invalidMaxNumberOfPlayers(){
        //test if maxNumber is capped by global max, assert throws
        Module testModule = new Module("MinesOfPhandelver", "hej", "0-3");
        PlaySession testPlaySession = new PlaySession("testtitle", 0, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, 20, testModule);
        Executable e = () -> {testPlaySessionManager.validatePlaySession(testPlaySession, true);};
        Throwable thrown = assertThrows(FailedValidationException.class, e, "Maximum players exceeds global maximum");
        assertTrue(thrown.getMessage().contains("Maximum players exceeds global maximum"));
    }

    @Test
    public void invalidCurrentNumberOfPlayers(){
        //test if current number is capped by max, assert throws
        Module testModule = new Module("MinesOfPhandelver", "hej", "0-3");
        PlaySession testPlaySession = new PlaySession("testtitle", 8, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, 7, testModule);
        Executable e = () -> {testPlaySessionManager.validatePlaySession(testPlaySession, true);};
        Throwable thrown = assertThrows(FailedValidationException.class, e, "current number of players exceeds session max");
        assertTrue(thrown.getMessage().contains("current number of players exceeds session max"));
    }

    @Test
    public void validSession(){
        //test assert true
        Module testModule = new Module("MinesOfPhandelver", "hej", "0-3");
        PlaySession testPlaySession = new PlaySession("testtitle", 5, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, 7, testModule);
        assertTrue(testPlaySessionManager.validatePlaySession(testPlaySession, true));
    }

    //addNewPlaySession
    @Test
    public void addInvalidPlaySession(){
        //example invalid currentMax
        Module testModule = new Module("MinesOfPhandelver", "hej", "0-3");
        PlaySession playSession = new PlaySession("testtitle", 8, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, 7, testModule);
        Executable e = () -> {testPlaySessionManager.addNewPlaySession(playSession);};
        Throwable thrown = assertThrows(FailedValidationException.class, e, "Test does not throw as expected");
        assertEquals(thrown.getMessage(), "current number of players exceeds session max");
    }

    @Test
    public void addValidPlaySession(){
        Module testModule = new Module("MinesOfPhandelver", "hej", "0-3");
        PlaySession testPlaySession = new PlaySession("testtitle", 5, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, 7, testModule);
        testPlaySessionManager.addNewPlaySession(testPlaySession);
        
        assertEquals(testPlaySession.getTitle(), testPlaySessionHandler.findById(testPlaySession.getId()).getTitle());
    }

    //updatePlaySession
    @Test // Same test as addNewPlaySession
    public void invalidPlaySessionUpdate(){
        Module testModule = new Module("MinesOfPhandelver", "hej", "0-3");
        PlaySession playSession = new PlaySession("testtitle2", 8, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, 7, testModule);
        Executable e = () -> {testPlaySessionManager.addNewPlaySession(playSession);};
        Throwable thrown = assertThrows(FailedValidationException.class, e, "Session update error");
        assertEquals(thrown.getMessage(), "current number of players exceeds session max");
    }
    @Test
    public void validPlaySessionUpdate(){
        Module testModule = new Module("MinesOfPhandelver", "hej", "0-3");
        PlaySession testPlaySession = new PlaySession("testtitle2", 5, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, 7, testModule);
        testPlaySessionManager.addNewPlaySession(testPlaySession);
        testPlaySessionManager.updatePlaySession(testPlaySession.getId(), "testtitle2", 7, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, testModule);
        PlaySession foundPlaySession = testPlaySessionHandler.findById(testPlaySession.getId());

        assertEquals(testPlaySession.getTitle(), foundPlaySession.getTitle());
        //opdaterer session med id 0 fra tidligere test, og sikrer at det virker ved at sammenligne den opdaterede titel (før testtitle, nu testtitle2)
    }

    //getSessions
    @Test 
    public void getSessionsTest(){//Not to sure how to test this - same, vi hører lige emil imorgen
        Module testModule = new Module("MinesOfPhandelver", "hej", "0-3");
        PlaySession testPlaySession1 = new PlaySession("testtitle1", 5, LocalDateTime.now(), PlaySessionStateEnum.CANCELLED, 7, testModule);
        PlaySession testPlaySession2 = new PlaySession("testtitle2", 5, LocalDateTime.of(2020, 10, 1, 0, 0), PlaySessionStateEnum.CANCELLED, 7, testModule);
        testPlaySessionManager.addNewPlaySession(testPlaySession1);
        testPlaySessionManager.addNewPlaySession(testPlaySession2);

        List<PlaySession> playSessions = testPlaySessionManager.getSessions(LocalDateTime.of(2021, 1, 1, 0, 0, 0), LocalDateTime.of(2024, 1, 1, 0, 0, 0));

        boolean contains = false;
        for(PlaySession playSession : playSessions) {
            if(playSession.getTitle().equals("testtitle1")) {
                contains = true;
            }
            if(playSession.getTitle().equals("testtile2")) {
                fail("PlaySession 2 found, but was not supposed to");
            }
        }

        assertTrue(contains);
    }
    
}
