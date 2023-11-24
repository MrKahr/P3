package com.proj.unitTest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertFalse;

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

    @Test   //we'll be using RoleAssigner for these next tests
    public void checkForDependencies(){ 
        User user = new User("user", "1234");   //this user is not an admin or a member

        SuperAdmin superAdmin = new SuperAdmin(); //this role has both admin and member as dependencies
        assertFalse(RoleAssigner.dependenciesFulfilled(user, superAdmin));

        user.setGuestInfo(new Guest(""));  //adding the dependency for the member role
        Member member = new Member("John Doe", "12345678", "0000", "City Town Street 1", "RealMail@mailService.com");
        assertTrue(RoleAssigner.dependenciesFulfilled(user, member));   //dependencies for member should be fulfilled now

        user.setMemberInfo(member); //adding member
        assertFalse(RoleAssigner.dependenciesFulfilled(user, superAdmin));    //still missing a dependency for superAdmin

        user.setAdminInfo(new Admin(null, null));  //adding admin
        assertTrue(RoleAssigner.dependenciesFulfilled(user, superAdmin));   //dependencies should be fulfilled now
        
        //defining a new role class
        class BadRole extends Role {

            @Override
            public RoleType getRoleType() {
                return RoleType.BADTYPE;
            }

            @Override
            public RoleType[] getRoleDependencies() {
                RoleType[] types = {RoleType.BADTYPE};   //this type is no accounted for in getRoleByType
                return types;
            }
        }

        BadRole badRole = new BadRole();

        Executable e = () -> {RoleAssigner.dependenciesFulfilled(user, badRole);};
        Throwable thrown = assertThrows(IllegalArgumentException.class, e, "Expected getRoleByType() to throw, but it didn't");
        assertTrue(thrown.getMessage().contains("roleType not recognized!"));
    }

    @Test
    public void setNewRole(){
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
    public void setReplacementRole(){
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

        //defining a new role class with a type that's not accounted for
        class BadRole extends Role {

            @Override
            public RoleType getRoleType() {
                return RoleType.BADTYPE;    //type returned is not accounted for in setRole
            }

            @Override
            public RoleType[] getRoleDependencies() {
                RoleType[] types = {};
                return types;
            }
        }

        BadRole badRole = new BadRole();

        Executable e = () -> {RoleAssigner.setRole(user, badRole);};
        Throwable thrown = assertThrows(IllegalArgumentException.class, e, "Expected setRole() to throw, but it didn't");
        assertTrue(thrown.getMessage().contains("newRoleObject returns unknown role type"));    //we want this exact error message
    }

    @Test
    public void setRoleMissingDependency(){
        User user = new User("user", "1234");
        Member member = new Member("John Doe", "12345678", "0000", "City Town Street 1", "RealMail@mailService.com");
        Executable e = () -> {RoleAssigner.setRole(user, member);};
        Throwable thrown = assertThrows(IllegalArgumentException.class, e, "Expected setRole() to throw, but it didn't");
        assertTrue(thrown.getMessage().contains("newRoleObject is missing dependencies!"));
    }
}