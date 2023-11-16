package com.proj.unitTest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.proj.model.users.*;
import com.proj.function.BanHandler;

import java.time.LocalDateTime;
import java.time.Duration;

public class banTest {
    //LocalDateTime counts nanoseconds, so to avoid false negatives,
    //we check if a time is within 1 minute of another to compare them
    @Test
    public void banConstructingLocalDateTime(){
        Ban banWithLocalDateTime = new Ban("You stepped on my toes", LocalDateTime.now().plusDays(2)); //a ban until two days from now
        assertTrue(banWithLocalDateTime.getReason().contains("You stepped on my toes"));    //has the reason been set?
        LocalDateTime startDate = banWithLocalDateTime.getStartDate();      //is the start date correct?
        assertTrue(startDate.isBefore(LocalDateTime.now().plusMinutes(1)) && startDate.isAfter(LocalDateTime.now().minusMinutes(1)));
        LocalDateTime endDate = banWithLocalDateTime.getEndDate();          //is the end date correct?
        assertTrue(endDate.isBefore(LocalDateTime.now().plusDays(2).plusMinutes(1)) && endDate.isAfter(LocalDateTime.now().plusDays(2).minusMinutes(1)));
    }

    @Test
    public void banConstructingDuration(){
        Ban banWithDuration = new Ban("You were complicit in stepping on my toes", Duration.ofDays(2));//a two day ban    
        assertTrue(banWithDuration.getReason().contains("You were complicit in stepping on my toes"));
        LocalDateTime startDate = banWithDuration.getStartDate();
        assertTrue(startDate.isBefore(LocalDateTime.now().plusMinutes(1)) && startDate.isAfter(LocalDateTime.now().minusMinutes(1)));
        LocalDateTime endDate = banWithDuration.getEndDate();
        assertTrue(endDate.isBefore(LocalDateTime.now().plus(Duration.ofDays(2)).plusMinutes(1)) && endDate.isAfter(LocalDateTime.now().plus(Duration.ofDays(2)).minusMinutes(1)));

    }

    @Test
    public void banConstructingPermanent(){
        Ban banPermanent = new Ban("You stepped on my toes again");
        assertTrue(banPermanent.getReason().contains("You stepped on my toes again"));
        LocalDateTime startDate = banPermanent.getStartDate();
        assertTrue(startDate.isBefore(LocalDateTime.now().plusMinutes(1)) && startDate.isAfter(LocalDateTime.now().minusMinutes(1)));
        assertNull(banPermanent.getEndDate());  //no end date specified, so it should be null
    }

    @Test
    public void banningPermanently(){
        User user = new User(new BasicUserInfo("user_person", "1234"));
        BanHandler.banUser(user);   //banning them permanently without a reason
        Ban ban = user.getBasicUserInfo().getActiveBan();
        assertNull(ban.getEndDate());                    //there shouldn't be an expiry date
        assertTrue(ban.getReason().equals("")); //the reason should be an empty string
        LocalDateTime startDate = ban.getStartDate();    //checking the start date to make sure banUser sets it properly
        assertTrue(startDate.isBefore(LocalDateTime.now().plusMinutes(1)) && startDate.isAfter(LocalDateTime.now().minusMinutes(1)));
    }

    @Test
    public void banningPermanentlyWithReason(){
        User user = new User(new BasicUserInfo("user_person", "1234"));
        BanHandler.banUser(user, "This is a test"); //banning them permanently with a reason
        Ban ban = user.getBasicUserInfo().getActiveBan();
        assertNull(ban.getEndDate());                               //there shouldn't be an expiry date
        assertTrue(ban.getReason().contains("This is a test"));   //there should be a reason this time
        LocalDateTime startDate = ban.getStartDate();               //checking the start date to make sure banUser sets it properly
        assertTrue(startDate.isBefore(LocalDateTime.now().plusMinutes(1)) && startDate.isAfter(LocalDateTime.now().minusMinutes(1)));
    }

    @Test
    public void banningWithLocalDateTime(){
        User user = new User(new BasicUserInfo("user_person", "1234"));
        BanHandler.banUser(user, LocalDateTime.now().plusDays(2));   //banning them for 2 days without a reason
        Ban ban = user.getBasicUserInfo().getActiveBan();
        LocalDateTime endDate = ban.getEndDate();        //is the end date correct?
        assertTrue(endDate.isBefore(LocalDateTime.now().plusDays(2).plusMinutes(1)) && endDate.isAfter(LocalDateTime.now().plusDays(2).minusMinutes(1)));
        assertTrue(ban.getReason().equals("")); //the reason should be an empty string
        LocalDateTime startDate = ban.getStartDate();    //checking the start date to make sure banUser sets it properly
        assertTrue(startDate.isBefore(LocalDateTime.now().plusMinutes(1)) && startDate.isAfter(LocalDateTime.now().minusMinutes(1)));
    }

    @Test 
    public void banningWithLocalDateTimeWithReason(){
        User user = new User(new BasicUserInfo("user_person", "1234"));
        BanHandler.banUser(user, "This is a test", LocalDateTime.now().plusDays(2)); //banning them for 2 days with a reason
        Ban ban = user.getBasicUserInfo().getActiveBan();
        LocalDateTime endDate = ban.getEndDate();                 //is the end date correct?
        assertTrue(endDate.isBefore(LocalDateTime.now().plusDays(2).plusMinutes(1)) && endDate.isAfter(LocalDateTime.now().plusDays(2).minusMinutes(1)));
        assertTrue(ban.getReason().contains("This is a test")); //there should be a reason this time
        LocalDateTime startDate = ban.getStartDate();             //checking the start date to make sure banUser sets it properly
        assertTrue(startDate.isBefore(LocalDateTime.now().plusMinutes(1)) && startDate.isAfter(LocalDateTime.now().minusMinutes(1)));
    }

    @Test
    public void banningWithDuration(){
        User user = new User(new BasicUserInfo("user_person", "1234"));
        BanHandler.banUser(user, Duration.ofDays(2));   //banning them for 2 days without a reason
        Ban ban = user.getBasicUserInfo().getActiveBan();
        LocalDateTime endDate = ban.getEndDate();        //is the end date correct?
        assertTrue(endDate.isBefore(LocalDateTime.now().plus(Duration.ofDays(2)).plusMinutes(1)) && endDate.isAfter(LocalDateTime.now().plusDays(2).minusMinutes(1)));
        assertTrue(ban.getReason().equals("")); //the reason should be an empty string
        LocalDateTime startDate = ban.getStartDate();    //checking the start date to make sure banUser sets it properly
        assertTrue(startDate.isBefore(LocalDateTime.now().plusMinutes(1)) && startDate.isAfter(LocalDateTime.now().minusMinutes(1)));
    }
    
    @Test
    public void banningWithDurationWithReason(){
        User user = new User(new BasicUserInfo("user_person", "1234"));
        BanHandler.banUser(user, "This is a test", Duration.ofDays(2)); //banning them for 2 days with a reason
        Ban ban = user.getBasicUserInfo().getActiveBan();
        LocalDateTime endDate = ban.getEndDate();                 //is the end date correct?
        assertTrue(endDate.isBefore(LocalDateTime.now().plus(Duration.ofDays(2)).plusMinutes(1)) && endDate.isAfter(LocalDateTime.now().plusDays(2).minusMinutes(1)));
        assertTrue(ban.getReason().contains("This is a test")); //there should be a reason this time
        LocalDateTime startDate = ban.getStartDate();             //checking the start date to make sure banUser sets it properly
        assertTrue(startDate.isBefore(LocalDateTime.now().plusMinutes(1)) && startDate.isAfter(LocalDateTime.now().minusMinutes(1)));
    }

    @Test
    public void updatingBans(){
        User user = new User(new BasicUserInfo("user_person", "1234"));
        Ban ban = new Ban("This ban will expire later", LocalDateTime.now().plusHours(1));
        user.getBasicUserInfo().setActiveBan(ban);  //giving the user a ban
        BanHandler.updateBanStatus(user);   //the ban has not expired, so this should do nothing
        assertTrue(user.getBasicUserInfo().getActiveBan().equals(ban));
        assertFalse(user.getBasicUserInfo().getExpiredBans().contains(ban));

        user.getBasicUserInfo().getActiveBan().setEndDate(LocalDateTime.now().minusHours(1));  //making the ban expired

        BanHandler.updateBanStatus(user);   //this should remove the active ban and put it in their list of expired bans
        assertNull(user.getBasicUserInfo().getActiveBan()); //is the ban no longer active?
        assertTrue(user.getBasicUserInfo().getExpiredBans().contains(ban)); //is the ban in the list of expired ones?
    }

    @Test
    public void updatingAndCheckingBans(){
        User user = new User(new BasicUserInfo("user_person", "1234"));
        Ban ban = new Ban("This ban will expire later", LocalDateTime.now().plusHours(1));
        user.getBasicUserInfo().setActiveBan(ban);  //giving the user a ban
        assertTrue(BanHandler.updateAndCheckBanStatus(user));   //the ban has not expired, so this should return true and not move the ban anywhere
        assertTrue(user.getBasicUserInfo().getActiveBan().equals(ban));
        assertFalse(user.getBasicUserInfo().getExpiredBans().contains(ban));

        user.getBasicUserInfo().getActiveBan().setEndDate(LocalDateTime.now().minusHours(1));  //making the ban expired

        assertFalse(BanHandler.updateAndCheckBanStatus(user));   //this should remove the active ban and put it in their list of expired bans AND return false
        assertNull(user.getBasicUserInfo().getActiveBan()); //is the ban no longer active?
        assertTrue(user.getBasicUserInfo().getExpiredBans().contains(ban)); //is the ban in the list of expired ones?
    }

    @Test
    public void unbanUser(){
        User user = new User(new BasicUserInfo("user_person", "1234"));
        BanHandler.banUser(user);   //ban the user
        Ban ban = user.getBasicUserInfo().getActiveBan();   //grab the ban we made for later
        BanHandler.unbanUser(user); //end the ban
        assertNull(user.getBasicUserInfo().getActiveBan()); //there should be no active ban
        assertTrue(user.getBasicUserInfo().getExpiredBans().contains(ban)); //the ban should be in here
        Throwable thrown = assertThrows(
           IllegalArgumentException.class,
           () -> BanHandler.unbanUser(user),
           "Expected unbanUser() to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Given user is not banned!"));
    }

    @Test
    public void changeBanReason(){
        User user = new User(new BasicUserInfo("user_person", "1234"));
        Throwable thrown = assertThrows(
           IllegalArgumentException.class,
           () -> BanHandler.changeBanReason(user, "Some reason"),
           "Expected changeBanReason() to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Given user is not banned!"));
        BanHandler.banUser(user);   //ban the user
        BanHandler.changeBanReason(user, "Here's a new ban reason");
        assertTrue(user.getBasicUserInfo().getActiveBan().getReason().contains("Here's a new ban reason"));
    }
}