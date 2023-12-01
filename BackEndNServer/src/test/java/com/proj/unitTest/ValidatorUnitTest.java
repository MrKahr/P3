package com.proj.unitTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.proj.model.users.User;
import com.proj.model.users.BasicUserInfo;
import com.proj.model.users.DM;
import com.proj.model.users.Guest;
import com.proj.model.users.Member;
import com.proj.model.users.Admin;
import com.proj.model.users.SuperAdmin;

import com.proj.validators.UserValidator;

import com.proj.function.RoleAssigner;

import com.proj.exception.FailedValidationException;

public class ValidatorUnitTest {
    User user;
    UserValidator userValidator;

    // Provide a user to test before each test with all possible roles fulfilled
    @BeforeEach
    void init() {
        // Set all users so that they have all possible info to see if filter works
        // correctly
        user = new User(new BasicUserInfo("user1", "233Gel+"));
        user.setId(1);

        RoleAssigner.setRole(user, new Guest("Bard Level 1"));
        RoleAssigner.setRole(user,
                new Member("John Adventureman", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        RoleAssigner.setRole(user, new DM(new ArrayList<String>()));
        RoleAssigner.setRole(user, new Admin(new ArrayList<String>(), new ArrayList<String>()));
        RoleAssigner.setRole(user, new SuperAdmin());
        // create instance of new user validator to use in all validation tests
        userValidator = new UserValidator(user);
    }

    @Test
    public void nullUser() {
        UserValidator currentUserValidator = new UserValidator(null);
        try {
            currentUserValidator.ValidateUserName().ValidatePassword().ValidateEmail();
        } catch (NullPointerException npr) {
            String errormsg = npr.getMessage();
            assertTrue(errormsg.equals("Cannot validate null user"));
        }
    }

    @Test
    public void nullBasicuserInfo() {
        user.setBasicUserInfo(null);
        try {
            userValidator.ValidateUserName().ValidatePassword().ValidateEmail();
        } catch (NullPointerException npr) {
            String errormsg = npr.getMessage();
            assertTrue(errormsg.equals("Cannot validate null basicUserInfo"));
        }
    }

    @Test
    public void nullMemberInfo() {
        user.setMemberInfo(null);
        try {
            userValidator.ValidateUserName().ValidatePassword().ValidateEmail();
        } catch (NullPointerException npr) {
            String errormsg = npr.getMessage();
            assertTrue(errormsg.equals("Cannot validate null memberInfo"));
        }
    }

    @Test
    public void nullUsername() {
        user.getBasicUserInfo().setUserName(null);
        try {
            userValidator.ValidateUserName().ValidatePassword().ValidateEmail();
        } catch (NullPointerException npr) {
            String errormsg = npr.getMessage();
            assertTrue(errormsg.equals("Cannot validate null Username"));
        }
    }

    @Test
    public void nullPassword() {
        user.getBasicUserInfo().setPassword(null);
        try {
            userValidator.ValidateUserName().ValidatePassword().ValidateEmail();
        } catch (NullPointerException npr) {
            String errormsg = npr.getMessage();
            assertTrue(errormsg.equals("Cannot validate null password"));
        }
    }

    public void nullEmail() {
        user.getMemberInfo().setEmail(null);
        try {
            userValidator.ValidateUserName().ValidatePassword().ValidateEmail();
        } catch (NullPointerException npr) {
            String errormsg = npr.getMessage();
            assertTrue(errormsg.equals("Cannot validate null email"));
        }
    }

    @Test
    public void userNameTooShort() {
        user.getBasicUserInfo().setUserName("");
        try {
            userValidator.ValidateUserName().ValidatePassword().ValidateEmail();
        } catch (FailedValidationException fve) {
            String errormsg = fve.getMessage();
            assertTrue(errormsg.equals("Username is not valid"));
        }

    }

    @Test
    public void passwordTooShort() {
        user.getBasicUserInfo().setPassword("");
        try {
            userValidator.ValidateUserName().ValidatePassword().ValidateEmail();
        } catch (FailedValidationException fve) {
            String errormsg = fve.getMessage();
            assertTrue(errormsg.equals("Password is not valid"));
        }
    }

    @Test
    public void emailTooShort() {
        user.getMemberInfo().setEmail("");
        try {
            userValidator.ValidateUserName().ValidatePassword().ValidateEmail();
        } catch (FailedValidationException fve) {
            String errormsg = fve.getMessage();
            assertTrue(errormsg.equals("Email is not valid"));
        }
    }

    //TODO: Check valid password/email/username
}
