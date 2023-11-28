package com.proj.unitTest;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.proj.function.RoleAssigner;
import com.proj.model.users.User;
import com.proj.model.users.BasicUserInfo;
import com.proj.model.users.DM;
import com.proj.model.users.Guest;
import com.proj.model.users.Member;
import com.proj.model.users.Admin;
import com.proj.model.users.SuperAdmin;
import com.proj.validators.Validatable;
import com.proj.validators.UsernameValidator;
import com.proj.validators.PasswordValidator;

public class ValidatorUnitTest {
    User user;
    Validatable usernameValidator = new UsernameValidator();
    Validatable passwordValidator = new PasswordValidator();

    // Provide a user to test before each test with all possible roles fulfilled
    @BeforeEach
    void init() {
        // Set all users so that they have all possible info to see if filter works
        // correctly
        user = new User(new BasicUserInfo("user1", "1234"));
        user.setId(1);

        RoleAssigner.setRole(user, new Guest("Bard Level 1"));
        RoleAssigner.setRole(user,
                new Member("John Adventureman", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        RoleAssigner.setRole(user, new DM(new ArrayList<String>()));
        RoleAssigner.setRole(user, new Admin(new ArrayList<String>(), new ArrayList<String>()));
        RoleAssigner.setRole(user, new SuperAdmin());
    }

    @Test
    public void userNameTooShort() {
        user.getBasicUserInfo().setUserName("hello123");
        Validatable rootValidatable = passwordValidator;
        rootValidatable.nextValidator(usernameValidator);

        assertFalse(rootValidatable.HandleString(user.getBasicUserInfo().getUserName()));

    }
}
