package com.proj.unitTest;

import java.time.LocalDateTime;
import com.proj.model.session.PlaySession;
import com.proj.model.session.PlaySessionStateEnum;
import com.proj.model.session.Reward;
import com.proj.exception.AddRewardsFailedException;
import com.proj.exception.PlaySessionFullException;
import com.proj.model.session.Module;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.function.Executable;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class playSessionTest {
    // setModule()

    @Test
    public void setValidModule() {
        LocalDateTime localdatetime = LocalDateTime.now();
        Module mymodule = new Module("dnd1", "this is a session", "1-4");
        PlaySession mySession = new PlaySession("Hi", "desc", "MrDM", 2, localdatetime, PlaySessionStateEnum.CANCELLED,
                5, null);

        mySession.setModule(mymodule);
        Module currentModule = mySession.getModule();
        assertTrue(currentModule.equals(mymodule));
    }

    // addModuleSet()
    @Test
    public void noElementModuleSet() {
        LocalDateTime localdatetime = LocalDateTime.now();
        Module mymodule = new Module("dnd1", "this is a session", "1-4");
        PlaySession mySession = new PlaySession("Howdy", "desc", "MrDM", 2, localdatetime,
                PlaySessionStateEnum.CANCELLED, 5, null);
        mySession.addModuleSet(mymodule);
        assertTrue(mySession.getModuleSetEvents().size() == 1);
    }

    @Test
    public void onePrevElementModuleSet() {
        int prevElements = 1;
        LocalDateTime localdatetime = LocalDateTime.now();
        Module mymodule = new Module("dnd1", "this is a session", "1-4");
        PlaySession mySession = new PlaySession("ello", "desc", "MrDM", 2, localdatetime,
                PlaySessionStateEnum.CANCELLED, 5, null);
        for (int i = 0; i < 2; i++) {
            mySession.addModuleSet(mymodule);
        }
        assertTrue(mySession.getModuleSetEvents().size() == prevElements + 1);
    }

    @Test
    public void twoPrevElementModuleSet() {
        int prevElements = 2;
        LocalDateTime localdatetime = LocalDateTime.now();
        Module mymodule = new Module("dnd1", "this is a session", "1-4");
        PlaySession mySession = new PlaySession("bongiuourno", "desc", "MrDM", 2, localdatetime,
                PlaySessionStateEnum.CANCELLED, 5, null);
        for (int i = 0; i < 3; i++) {
            mySession.addModuleSet(mymodule);
        }
        assertTrue(mySession.getModuleSetEvents().size() == prevElements + 1);
    }

    // removeModule()
    @Test
    public void removeNullModule() {
        LocalDateTime localdatetime = LocalDateTime.now();
        PlaySession mySession = new PlaySession("Hej", "desc", "MrDM", 2, localdatetime, PlaySessionStateEnum.CANCELLED,
                5, null);

        Executable e = () -> {
            mySession.removeModule();
        };
        assertThrows(NullPointerException.class, e);
    }

    @Test
    public void removeValidModule() {
        LocalDateTime localdatetime = LocalDateTime.now();
        Module mymodule = new Module("dnd1", "this is a session", "1-4");
        PlaySession mySession = new PlaySession("davs", "desc", "MrDM", 2, localdatetime,
                PlaySessionStateEnum.CANCELLED, 5, mymodule);

        assertTrue(!(Objects.isNull(mySession.getModule())));
    }

    @Test
    public void assignTooManyUsers() {
        Module module = new Module("moduleName", "description", "02-03");
        LocalDateTime time = LocalDateTime.of(2024, 1, 1, 0, 0, 0, 0);
        PlaySessionStateEnum state = PlaySessionStateEnum.PLANNED;
        PlaySession testPlaySession = new PlaySession("title", "desc", "MrDM", 0, time, state, 2, module);
        testPlaySession.assignUser("Guy the First");
        testPlaySession.assignUser("Guy the Second");
        Executable e = () -> {
            testPlaySession.assignUser("Guy Throwington");
        };
        assertThrows(PlaySessionFullException.class, e);
    }

    @Test
    public void unassignTooManyUsers() {
        Module module = new Module("moduleName", "description", "02-03");
        LocalDateTime time = LocalDateTime.of(2024, 1, 1, 0, 0, 0, 0);
        PlaySessionStateEnum state = PlaySessionStateEnum.PLANNED;
        PlaySession testPlaySession = new PlaySession("title", "desc", "MrDM", 0, time, state, 2, module);
        testPlaySession.assignUser("Guy the Only");
        testPlaySession.unassignUser("Guy the Only");
        Executable e = () -> {
            testPlaySession.unassignUser("Guy the only");
        };
        assertThrows(NoSuchElementException.class, e);
        
    }

    @Test
    public void changeDescription() {
        Module module = new Module("moduleName", "description", "02-03");
        LocalDateTime time = LocalDateTime.of(2024, 1, 1, 0, 0, 0, 0);
        PlaySessionStateEnum state = PlaySessionStateEnum.PLANNED;
        PlaySession testPlaySession = new PlaySession("title", "desc", "MrDM", 0, time, state, 2, module);
        String newDesc = "New description";
        testPlaySession.setDescription(newDesc);

        assertEquals(testPlaySession.getDescription(), newDesc);
        assertEquals(testPlaySession.getDescriptionChanges().size(), 1);
    }

    @Test
    public void addRewards() {
        Module module = new Module("moduleName", "description", "02-03");
        LocalDateTime time = LocalDateTime.of(2024, 1, 1, 0, 0, 0, 0);
        PlaySessionStateEnum state = PlaySessionStateEnum.CONCLUDED;
        PlaySession testPlaySession = new PlaySession("title", "desc", "MrDM", 0, time, state, 2, module);

        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new Reward("Legendary thingy", 1, "It is legendary."));
        rewards.add(new Reward("Gold", 100, "It is gold"));
        testPlaySession.setRewards(rewards);

        assertEquals(testPlaySession.getRewards().get(0).getName(), "Legendary thingy");
        assertEquals(testPlaySession.getRewardsGiven().getResponsibleDm(), "MrDM");
    }

    @Test
    public void addRewardsNotConcluded() {
        Module module = new Module("moduleName", "description", "02-03");
        LocalDateTime time = LocalDateTime.of(2024, 1, 1, 0, 0, 0, 0);
        PlaySessionStateEnum state = PlaySessionStateEnum.PLANNED;
        PlaySession testPlaySession = new PlaySession("title", "desc", "MrDM", 0, time, state, 2, module);

        ArrayList<Reward> rewards = new ArrayList<Reward>();
        rewards.add(new Reward("Legendary thingy", 1, "It is legendary."));
        rewards.add(new Reward("Gold", 100, "It is gold"));
        Executable e = () -> {
            testPlaySession.setRewards(rewards);
        };
        assertThrows(AddRewardsFailedException.class, e);
    }
}
