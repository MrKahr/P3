package com.proj.integrationTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.proj.exception.UserNotFoundException;
import com.proj.function.UserManager;
import com.proj.model.users.User;

@SpringBootTest
public class CleanupTest {
    //Field
    @Autowired
    UserManager userManager;
    
    //Method
    @Test
    public void deleteExpiredUser() throws InterruptedException{
        User user = new User("UserWho'sGottaGo", "Password123");
        user.getBasicUserInfo().setDeletionDate(LocalDateTime.now().minusMinutes(1));   //tell the system that the user should have been deleted one minute ago
        userManager.getUserdbHandler().save(user);  //save the user in the database
        
        Thread.sleep(2000); //assuming the cron expression in cleanupDeletedUsers fires every second, the user should be gone after this delay
        
        Executable e = () -> {userManager.lookupAccount(user.getBasicUserInfo().getUserName());};
        assertThrows(UserNotFoundException.class, e);
    }

    @Test
    public void deleteManyUsers() throws InterruptedException {
        //create and save some expired users. Validation doesn't matter
        User user1 = new User("personperson","1234");
        User user2 = new User("hugh mann","4321");
        User user3 = new User("ererererere","1243");
        User user4 = new User("aaaaaaaaaaaa","1432");
        User user5 = new User("idontwanttotestanymore","1111");
        user1.getBasicUserInfo().setDeletionDate(LocalDateTime.now().minusMinutes(1));
        user2.getBasicUserInfo().setDeletionDate(LocalDateTime.now().minusMinutes(1));
        user3.getBasicUserInfo().setDeletionDate(LocalDateTime.now().minusMinutes(1));
        user4.getBasicUserInfo().setDeletionDate(LocalDateTime.now().minusMinutes(1));
        user5.getBasicUserInfo().setDeletionDate(LocalDateTime.now().minusMinutes(1));
        userManager.getUserdbHandler().save(user1);
        userManager.getUserdbHandler().save(user2);
        userManager.getUserdbHandler().save(user3);
        userManager.getUserdbHandler().save(user4);
        userManager.getUserdbHandler().save(user5);
        Thread.sleep(2000); //assuming the cron expression in cleanupDeletedUsers fires every second, the user should be gone after this delay
        Executable e1 = () -> {userManager.lookupAccount(user1.getBasicUserInfo().getUserName());};
        Executable e2 = () -> {userManager.lookupAccount(user2.getBasicUserInfo().getUserName());};
        Executable e3 = () -> {userManager.lookupAccount(user3.getBasicUserInfo().getUserName());};
        Executable e4 = () -> {userManager.lookupAccount(user4.getBasicUserInfo().getUserName());};
        Executable e5 = () -> {userManager.lookupAccount(user5.getBasicUserInfo().getUserName());};
        assertThrows(UserNotFoundException.class, e1);
        assertThrows(UserNotFoundException.class, e2);
        assertThrows(UserNotFoundException.class, e3);
        assertThrows(UserNotFoundException.class, e4);
        assertThrows(UserNotFoundException.class, e5);
    }
}