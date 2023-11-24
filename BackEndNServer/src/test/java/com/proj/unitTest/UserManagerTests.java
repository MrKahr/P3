package com.proj.unitTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.proj.function.UserManager;
import com.proj.exception.*;
import com.proj.model.users.*;
import com.proj.repositories.UserRepository;
import com.proj.repositoryhandler.UserdbHandler;

// THIS IS NOT FOR USE - ONLY REFERENCE

//@EnableJpaRepositories(basePackages={"com.proj.function.UserRepository"})
public class UserManagerTests {
    // @Autowired
    // private UserRepository testRepository;

    // UserManager userManager = new UserManager(0);
    // User user1 = new Guest("Fisk", "FiskPassword");
    // User user2 = new Guest("Aborre", "AborrePassword");

    // @Test
    public void dbSaveUser() {
        // Executable e = () -> {testRepository.save(user1);};
        // assertDoesNotThrow(e); // Specifically IllegalArgumentException
    }

    public void LookupAccountUserNotFound() {
        // Executable e = () ->
        // {accountManager.accountExists("ynohijrucelpvdcqkpjxpng");}; // This shouldn't
        // exist
        // assertThrows(UserNotFoundException.class, e);
    }

    public void LookupAccountFoundUser() {
        // Executable e = () -> {accountManager.accountExists("");};
    }

    @Test
    public void sanitizeLookupSuperAdmin() {

        // User has all possible info to see if filter works correctly
        BasicUserInfo buinfo = new BasicUserInfo("user1", "1234");
        User user = new User(buinfo);
        user.setGuestInfo(new Guest("Bard Level 1"));
        user.setMemberInfo(
                new Member("John Adventureman", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        user.setDmInfo(new DM(new ArrayList<String>()));
        user.setAdminInfo(new Admin(new ArrayList<String>(), new ArrayList<String>()));
        user.setSuperAdminInfo(new SuperAdmin());

        // New admin accesses user
        User requestingUser = new User();
        requestingUser.setSuperAdminInfo(new SuperAdmin());

        // userDB handler sanitizes input
        UserManager userManager = new UserManager(1);

        // Sanitized user
        User sanitizedUser = userManager.sanitizeDBLookup(user, requestingUser);
        assertTrue(sanitizedUser.getBasicUserInfo().getPassword().equals(""));
        assertTrue(sanitizedUser.getSuperAdminInfo() != null);
        assertTrue(sanitizedUser.getMemberInfo() != null);
    }

    @Test
    void SanitizeLookupAdmin() {

        // User has all possible info to see if filter works correctly
        BasicUserInfo buinfo = new BasicUserInfo("user1", "1234");
        User user = new User(buinfo);
        user.setGuestInfo(new Guest("Bard Level 1"));
        user.setMemberInfo(
                new Member("John Adventureman", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        user.setDmInfo(new DM(new ArrayList<String>()));
        user.setAdminInfo(new Admin(new ArrayList<String>(), new ArrayList<String>()));
        user.setSuperAdminInfo(new SuperAdmin());

        // New admin accesses user
        User requestingUser = new User();
        requestingUser.setAdminInfo(new Admin());

        // userDB handler sanitizes input
        UserManager userManager = new UserManager(1);

        // Sanitized user
        User sanitizedUser = userManager.sanitizeDBLookup(user, requestingUser);
        assertTrue(sanitizedUser.getBasicUserInfo().getPassword().equals(""));
        assertTrue(sanitizedUser.getSuperAdminInfo() == null);
        assertTrue(sanitizedUser.getMemberInfo() == null);
        assertTrue(sanitizedUser.getAdminInfo() != null);
    }

    @Test
    void SanitizeLookupDM() {
        // User has all possible info to see if filter works correctly
        BasicUserInfo buinfo = new BasicUserInfo("user1", "1234");
        User user = new User(buinfo);
        user.setGuestInfo(new Guest("Bard Level 1"));
        user.setMemberInfo(
                new Member("John Adventureman", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        user.setDmInfo(new DM(new ArrayList<String>()));
        user.setAdminInfo(new Admin(new ArrayList<String>(), new ArrayList<String>()));
        user.setSuperAdminInfo(new SuperAdmin());

        // New admin accesses user
        User requestingUser = new User();
        requestingUser.setDmInfo(new DM());

        // userDB handler sanitizes input
        UserManager userManager = new UserManager(1);

        // Sanitized user
        User sanitizedUser = userManager.sanitizeDBLookup(user, requestingUser);
        assertTrue(sanitizedUser.getBasicUserInfo().getPassword().equals(""));
        assertTrue(sanitizedUser.getSuperAdminInfo() == null);
        assertTrue(sanitizedUser.getMemberInfo() == null);
        assertTrue(sanitizedUser.getAdminInfo() == null);
        assertTrue(sanitizedUser.getDmInfo() != null);
    }

    @Test
    void SanitizeLookMember() {
        // User has all possible info to see if filter works correctly
        BasicUserInfo buinfo = new BasicUserInfo("user1", "1234");
        User user = new User(buinfo);
        user.setGuestInfo(new Guest("Bard Level 1"));
        user.setMemberInfo(
                new Member("John Adventureman", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        user.setDmInfo(new DM(new ArrayList<String>()));
        user.setAdminInfo(new Admin(new ArrayList<String>(), new ArrayList<String>()));
        user.setSuperAdminInfo(new SuperAdmin());

        // New admin accesses user
        User requestingUser = new User();
        requestingUser.setGuestInfo(new Guest("Level 1 Barbarian"));;;

        // userDB handler sanitizes input
        UserManager userManager = new UserManager(1);

        // Sanitized user
        User sanitizedUser = userManager.sanitizeDBLookup(user, requestingUser);
        assertTrue(sanitizedUser.getBasicUserInfo().getPassword().equals(""));
        assertTrue(sanitizedUser.getSuperAdminInfo() == null);
        assertTrue(sanitizedUser.getMemberInfo() == null);
        assertTrue(sanitizedUser.getAdminInfo() == null);
        assertTrue(sanitizedUser.getDmInfo() == null);
        assertTrue(sanitizedUser.getBasicUserInfo() != null);
    }

    @Test
    void SanitizeLookupGuest(){
        // User has all possible info to see if filter works correctly
        BasicUserInfo buinfo = new BasicUserInfo("user1", "1234");
        User user = new User(buinfo);
        user.setGuestInfo(new Guest("Bard Level 1"));
        user.setMemberInfo(
                new Member("John Adventureman", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        user.setDmInfo(new DM(new ArrayList<String>()));
        user.setAdminInfo(new Admin(new ArrayList<String>(), new ArrayList<String>()));
        user.setSuperAdminInfo(new SuperAdmin());

        // New admin accesses user
        User requestingUser = new User();
        requestingUser.setGuestInfo(new Guest("Barbarian Level 1"));

        // userDB handler sanitizes input
        UserManager userManager = new UserManager(1);
        User sanitizedUser = userManager.sanitizeDBLookup(user, requestingUser);
        assertTrue(sanitizedUser.getBasicUserInfo().getPassword().equals(""));
        assertTrue(sanitizedUser.getSuperAdminInfo() == null);
        assertTrue(sanitizedUser.getMemberInfo() == null);
        assertTrue(sanitizedUser.getAdminInfo() == null);
        assertTrue(sanitizedUser.getDmInfo() == null);
        assertTrue(sanitizedUser.getBasicUserInfo() != null);
    }
    @Test
    void userIsNull(){
        User user = null;
        
        User requestingUser = new User();
        requestingUser.setGuestInfo(new Guest("Barbarian Level 1"));
        UserManager userManager = new UserManager(1);
        Executable e = () -> {userManager.sanitizeDBLookup(user, requestingUser);};
        assertThrows(NullPointerException.class, e);
    }

    @Test 
    void requestingUserIsNull(){
        User user = new User();
        User requestingUser = null;
        UserManager userManager = new UserManager(1);
        
        Executable e = () -> {userManager.sanitizeDBLookup(user, requestingUser);};
        assertThrows(NullPointerException.class, e);
    }
}
