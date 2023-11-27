package com.proj.integrationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.proj.repositoryhandler.UserdbHandler;
import com.proj.model.users.BasicUserInfo;
import com.proj.model.users.User;

@SpringBootTest
public class UserdbHandlerTest {
    @Autowired
    private UserdbHandler userdbHandler;

    @Test
    public void createAndRetrieveUser() {
        BasicUserInfo info = new BasicUserInfo("userGuy", "password2");
        User user = new User(info);

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

        for(User user : users) {
            assertTrue(userdbHandler.existsById(user.getId()));
        }
        userdbHandler.deleteAll(users);
    }
}
