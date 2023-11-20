package com.proj.unitTest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.proj.model.users.*;
import com.proj.model.events.RoleChanged;
import com.proj.function.RoleAssigner;

public class roleAssignmentTest {
    @Test
    public void constructUserObject(){
        BasicUserInfo info = new BasicUserInfo("user1", "1234");
        User user = new User(info);
        assertNotNull(user);                    //is the user object we just made null?
        assertNotNull(user.getBasicUserInfo()); //is the basic user info present?
    }

    @Test
    public void assignNewRole(){    //we'll be using RoleAssigner for these next tests
        BasicUserInfo info = new BasicUserInfo("user1", "1234");
        User user = new User(info);
        Guest guestObject = new Guest("Some information about the user's character");

        RoleChanged assignment = RoleAssigner.setRole(user, guestObject);   //assign the guest object to the user object

        assertNotNull(assignment);  //did we get an RoleChanged object? And are its fields filled correctly?
        assertNotNull(assignment.getDate());
        assertNotNull(assignment.getNewRole());
        assertNull(assignment.getPreviousRole());   //we're not replacing anything so that should show here as well
    }

    @Test
    public void replaceRole(){
        BasicUserInfo info = new BasicUserInfo("user1", "1234");
        User user = new User(info);
        Guest guestObject = new Guest("Some information about the user's character");

        RoleAssigner.setRole(user, guestObject);    //doing the same thing as we did in the last test

        Guest replacementGuestObject = new Guest("Some different information about the user's character");

        RoleChanged assignment = RoleAssigner.setRole(user, replacementGuestObject);

        assertNotNull(assignment);  //did we get an RoleChanged object? And are its fields filled correctly?
        assertNotNull(assignment.getDate());
        assertNotNull(assignment.getNewRole());
        assertNotNull(assignment.getPreviousRole());   //nothing should be null this time since we're replacing a role
    }

    @Test
    public void setBadRoleType(){
        BasicUserInfo info = new BasicUserInfo("user1", "1234");
        User user = new User(info);

        //defining a new role class
        class BadRole extends Role {

            @Override
            public RoleType getRoleType() {
                return RoleType.NOTYPE;    //type returned is not accounted for in RoleAssigner
            }

            @Override
            public RoleType[] getRoleDependencies() {
                RoleType[] types = {};
                return types;
            }
        }

        BadRole badRole = new BadRole();

        Throwable thrown = assertThrows(
           IllegalArgumentException.class,
           () -> RoleAssigner.setRole(user, badRole),
           "Expected setRole() to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("newRoleObject returns unknown role type"));    //we want this exact error message
    }
}