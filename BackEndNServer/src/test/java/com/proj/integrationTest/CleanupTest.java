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
/*     @Test
    public void deleteExpiredUser() throws InterruptedException{
        User user = new User("UserWho'sGottaGo", "Password123");
        user.getBasicUserInfo().setDeletionDate(LocalDateTime.now().minusMinutes(1));   //tell the system that the user should have been deleted one minute ago
        userManager.getUserdbHandler().save(user);  //save the user in the database
        
        Thread.sleep(2000); //assuming the cron expression in cleanupDeletedUsers fires every second, the user should be gone after this delay
        
        Executable e = () -> {userManager.lookupAccount(user.getBasicUserInfo().getUserName());};
        assertThrows(UserNotFoundException.class, e);
    } */

}