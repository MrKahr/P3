package com.proj.unitTest;

import java.time.LocalDateTime;
import com.proj.model.session.PlaySession;
import com.proj.model.session.PlaySessionStateEnum;
import com.proj.model.session.Module;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;



public class playSessionTest {
    // setModule()
    @Test
    public void setNullModule(){
        LocalDateTime localdatetime = LocalDateTime.now();
        PlaySession mySession = new PlaySession("Hello", 2, localdatetime, PlaySessionStateEnum.CANCELLED, 5, null);
        
        Executable e = () -> {mySession.setModule(null);};
        assertThrows(NullPointerException.class, e);
    }

    @Test
    public void setValidModule(){
        LocalDateTime localdatetime = LocalDateTime.now();
        Module mymodule = new Module("dnd1", "this is a session", "1-4");
        PlaySession mySession = new PlaySession("Hi", 2, localdatetime, PlaySessionStateEnum.CANCELLED, 5, null);
        
        mySession.setModule(mymodule);
        Module currentModule = mySession.getModule();
        assertTrue(currentModule.equals(mymodule));
    }
    // addModuleSet()
    @Test
    public void noElementModuleSet(){
        LocalDateTime localdatetime = LocalDateTime.now();
        Module mymodule = new Module("dnd1", "this is a session", "1-4");
        PlaySession mySession = new PlaySession("Howdy", 2, localdatetime, PlaySessionStateEnum.CANCELLED, 5, null);
        mySession.addModuleSet(mymodule);
        assertTrue(mySession.getModuleSetEvents().size() == 1);
    }
    @Test
    public void onePrevElementModuleSet(){
        int prevElements = 1;
        LocalDateTime localdatetime = LocalDateTime.now();
        Module mymodule = new Module("dnd1", "this is a session", "1-4");
        PlaySession mySession = new PlaySession("ello", 2, localdatetime, PlaySessionStateEnum.CANCELLED, 5, null);
        for(int i = 0; i < 2; i++){
            mySession.addModuleSet(mymodule);
        }
        assertTrue(mySession.getModuleSetEvents().size() == prevElements + 1);
    }
    @Test
    public void twoPrevElementModuleSet(){
        int prevElements = 2;
        LocalDateTime localdatetime = LocalDateTime.now();
        Module mymodule = new Module("dnd1", "this is a session", "1-4");
        PlaySession mySession = new PlaySession("bongiuourno", 2, localdatetime, PlaySessionStateEnum.CANCELLED, 5, null);
        for(int i = 0; i < 3; i++){
            mySession.addModuleSet(mymodule);
        }
        assertTrue(mySession.getModuleSetEvents().size() == prevElements + 1);
    }

    // removeModule()
    @Test
    public void removeNullModule(){
        LocalDateTime localdatetime = LocalDateTime.now();
        PlaySession mySession = new PlaySession("Hej", 2, localdatetime, PlaySessionStateEnum.CANCELLED, 5, null);

        Executable e = () -> {mySession.removeModule();};
        assertThrows(NullPointerException.class, e);
    }

    @Test
    public void removeValidModule(){
        LocalDateTime localdatetime = LocalDateTime.now();
        Module mymodule = new Module("dnd1", "this is a session", "1-4");
        PlaySession mySession = new PlaySession("davs", 2, localdatetime, PlaySessionStateEnum.CANCELLED, 5, mymodule);

        assertTrue(!(Objects.isNull(mySession.getModule())));
    }
    
}
