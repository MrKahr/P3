package com.proj.integrationTest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.proj.model.users.BasicUserInfo;
import com.proj.model.users.User;
import com.proj.exception.FailedValidationException;
import com.proj.function.UserManager;

@SpringBootTest
public class UserManagerIntegrationTest {
    @Autowired
    UserManager userManager;
    BasicUserInfo basicUserInfo;
    User user;

    @BeforeEach
    public void init() {
        // Initialize a single user before each test that has a valid username and a
        // password
        BasicUserInfo buinfo = new BasicUserInfo("user", "fefoeefwe23-A");
        user = new User(buinfo);
    }
}
