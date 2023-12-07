package com.proj.integrationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.proj.repositoryhandler.UserdbHandler;

import com.proj.function.RoleAssigner;

import com.proj.model.users.*;

@SpringBootTest
public class UserdbHandlerTest {
    private User user;
    private User requestingUser;

    @Autowired
    private UserdbHandler userdbHandler;

    @BeforeEach
    public void init() {
        BasicUserInfo info = new BasicUserInfo("userGuy", "password2");
        user = new User(info);
    }

    @Test
    public void createAndRetrieveUser() {
        userdbHandler.save(user);
        User foundUser = userdbHandler.findById(user.getId());

        assertEquals(user.getBasicUserInfo().getUserName(), foundUser.getBasicUserInfo().getUserName());
        userdbHandler.delete(user);
    }

    @Test
    public void createAndRetrieveMultipleUsers() {
        ArrayList<User> users = new ArrayList<User>();
        for (int i = 0; i < 5; i++) {
            BasicUserInfo info = new BasicUserInfo("UserWithANumber" + i + 1, "password" + i + 1);
            users.add(new User(info));
        }
        userdbHandler.saveAll(users);

        for (User user : users) {
            assertTrue(userdbHandler.existsById(user.getId()));
        }
        userdbHandler.deleteAll(users);
    }

    @Test
    public void equivalenceTestByID() {
        user.setId(1);
        RoleAssigner.setRole(user, new Guest("Bard Level 1"));
        RoleAssigner.setRole(user,
                new Member("John Adventureman", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        RoleAssigner.setRole(user, new DM(new ArrayList<String>()));
        RoleAssigner.setRole(user, new Admin(new ArrayList<String>(), new ArrayList<String>()));
        RoleAssigner.setRole(user, new SuperAdmin());

        // Requesting user is always different from user unless specified by test
        requestingUser = new User(new BasicUserInfo("user2", "4321"));
        requestingUser.setId(2);
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
