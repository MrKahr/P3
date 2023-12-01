package com.proj.integrationTest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.same;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.proj.model.users.BasicUserInfo;
import com.proj.model.users.User;
import com.proj.function.UserManager;

@SpringBootTest
public class UserManagerIntegrationTest {
    @Autowired
    UserManager userManager;
    BasicUserInfo basicUserInfo;
    User user; 

    @BeforeEach
    public void init() {
        // Initialize a single user before each test that has a valid username and a password
        BasicUserInfo buinfo = new BasicUserInfo("user" , "fefoeefwe23-A");
        user = new User(buinfo);
    }

    @Test
    public void createValidGuestAccount() {
        String creationResult = userManager.createAccount(user.getBasicUserInfo().getUserName(), user.getBasicUserInfo().getPassword());
        assertTrue(creationResult.equals("User creation successful"));
    }

    @Test
    public void createInvalidUsernameGuestAccount() {
        user.getBasicUserInfo().setUserName("");
        String creationResult = userManager.createAccount(user.getBasicUserInfo().getUserName(),
                user.getBasicUserInfo().getPassword());
        assertTrue(creationResult.equals("Cannot create user because: Username is not valid"));
    }

    @Test
    public void createInvalidPasswordGuestAccount() {
        user.getBasicUserInfo().setUserName("user1");
        user.getBasicUserInfo().setPassword("");
        String creationResult = userManager.createAccount(user.getBasicUserInfo().getUserName(),
                user.getBasicUserInfo().getPassword());
        System.out.println(creationResult);
        assertTrue(creationResult.equals("Cannot create user because: Password is not valid"));
    }
    @Test 
    public void upgradeValidMember(){

    }
    
    @Test
    // Test whether accessingUser is the same as user when accessing elements in db
    public void checkUserNotInDB() {
        boolean userInDb =  userManager.userExistsInDatabase("userxxx39");
        Executable e = () -> {
            // Check username that is not in db
            userManager.userExistsInDatabase("userxxx39");
        };
        assertDoesNotThrow(e);
        assertTrue(!userInDb);
    }
}
