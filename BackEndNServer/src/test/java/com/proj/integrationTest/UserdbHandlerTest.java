package com.proj.integrationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.proj.repositoryhandler.UserdbHandler;
import com.proj.function.UserManager;

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
    @Order(97)
    public void createAndRetrieveUser() {
        userdbHandler.save(user);
        User foundUser = userdbHandler.findById(user.getId());

        assertEquals(user.getBasicUserInfo().getUserName(), foundUser.getBasicUserInfo().getUserName());

        userdbHandler.delete(user); //Cleanup
    }

    @Test
    @Order(98)
    public void createAndRetrieveMultipleUsers() {
        ArrayList<User> users = new ArrayList<User>();
        for (int i = 0; i < 5; i++) {
            BasicUserInfo info = new BasicUserInfo("UserWithANumber" + i + 1, "password" + i + 1);
            users.add(new User(info));
        }
        userdbHandler.saveAll(users);

        for (User user : users) {
            assertTrue(userdbHandler.existsById(user.getId()));
            userdbHandler.delete(user); //Cleanup
        }
    }

    @Test
    @Order(99)
    public void equivalenceTestByID() {
        User newUser = new User("Bob", "hellLo23+232");
        userdbHandler.save(newUser);

        // Retrieve the same element database and check whether equals returns true
        User accessingUser = userdbHandler.findById(newUser.getId());
        User userLookedUp = userdbHandler.findById(newUser.getId());

        // Check whether system considers them the same
        assertTrue(accessingUser.equals(userLookedUp));

        userdbHandler.delete(newUser);    //Cleanup
    }
}
