package com.proj.integrationTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.proj.model.users.BasicUserInfo;
import com.proj.repositoryhandler.UserdbHandler;
import com.proj.model.users.User;

@SpringBootTest
public class UserManagerIntegrationTest {

    @Autowired
    private UserdbHandler userdbHandler;

    // Test whether accessingUser is the same as user when accessing elements in db
    @Test
    public void equivalenceTest(){
        BasicUserInfo buinfo = new BasicUserInfo("user1", "1234");
        User user = new User(buinfo);
        userdbHandler.save(user);
        
        // Retrieve the same element database and check whether equals returns true
        User accessingUser = userdbHandler.findById(user.getId());
        User userLookedUp = userdbHandler.findById(user.getId());

        // Check whether system returns a userwith basic info
        assertTrue(accessingUser.getBasicUserInfo() != null);
        assertTrue(userLookedUp.getBasicUserInfo() != null);
        
        // Check whether system considers them the same 
        assertTrue(accessingUser.equals(userLookedUp));
    }
}
