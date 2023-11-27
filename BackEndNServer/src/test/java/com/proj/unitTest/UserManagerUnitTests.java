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
import com.proj.function.RoleAssigner;

// THIS IS NOT FOR USE - ONLY REFERENCE

//@EnableJpaRepositories(basePackages={"com.proj.function.UserRepository"})
public class UserManagerUnitTests {
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
    public void sanitizeSelf() {
        // User has all possible info to see if filter works correctly
        User user = new User(new BasicUserInfo("user1", "1234"));
        user.setId(1);

        RoleAssigner.setRole(user, new Guest("Bard Level 1"));
        RoleAssigner.setRole(user,
                new Member("John Adventureman", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        RoleAssigner.setRole(user, new DM(new ArrayList<String>()));
        RoleAssigner.setRole(user, new Admin(new ArrayList<String>(), new ArrayList<String>()));
        RoleAssigner.setRole(user, new SuperAdmin());

        // Requesting user has same user id as user being requested
        User requestingUser = new User();
        try {
            requestingUser = user.clone();

        } catch (CloneNotSupportedException cnse) {
            requestingUser.setBasicUserInfo(user.getBasicUserInfo());
            requestingUser.setId(user.getId());
        }

        // userDB handler sanitizes input
        UserManager userManager = new UserManager(2);

        // Sanitized user
        // TODO: consider equals for all roles
        User sanitizedUser = userManager.sanitizeDBLookup(user, requestingUser);
        assertTrue(sanitizedUser.getBasicUserInfo().getPassword().equals(""));
        assertTrue(sanitizedUser.getBasicUserInfo() != null);
        assertTrue(sanitizedUser.getMemberInfo() != null);
        assertTrue(sanitizedUser.getDmInfo() != null);
        assertTrue(sanitizedUser.getAdminInfo() != null);
        assertTrue(sanitizedUser.getSuperAdminInfo() != null);
    }

    @Test
    public void sanitizeLookupSuperAdmin() {

        // User has all possible info to see if filter works correctly
        User user = new User(new BasicUserInfo("user1", "1234"));
        user.setId(1);

        RoleAssigner.setRole(user, new Guest("Bard Level 1"));
        RoleAssigner.setRole(user,
                new Member("John Adventureman", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        RoleAssigner.setRole(user, new DM(new ArrayList<String>()));
        RoleAssigner.setRole(user, new Admin(new ArrayList<String>(), new ArrayList<String>()));
        RoleAssigner.setRole(user, new SuperAdmin());

        // New super admin accesses user
        User requestingUser = new User(new BasicUserInfo("user2", "4321"));
        RoleAssigner.setRole(requestingUser, new Guest("Barbarian Level 1"));
        RoleAssigner.setRole(requestingUser,
                new Member("Bob DungeonMan", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        RoleAssigner.setRole(requestingUser, new Admin());
        RoleAssigner.setRole(requestingUser, new SuperAdmin());
        requestingUser.setId(2);

        // userDB handler sanitizes input
        UserManager userManager = new UserManager(2);

        // Sanitized user
        User sanitizedUser = userManager.sanitizeDBLookup(user, requestingUser);
        assertTrue(sanitizedUser.getBasicUserInfo().getPassword().equals(""));
        assertTrue(sanitizedUser.getMemberInfo() != null);
        assertTrue(sanitizedUser.getSuperAdminInfo() != null);
    }

    @Test
    void SanitizeLookupAdmin() {

        // User has all possible info to see if filter works correctly
        User user = new User(new BasicUserInfo("user1", "1234"));
        user.setId(1);

        RoleAssigner.setRole(user, new Guest("Bard Level 1"));
        RoleAssigner.setRole(user,
                new Member("John Adventureman", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        RoleAssigner.setRole(user, new DM(new ArrayList<String>()));
        RoleAssigner.setRole(user, new Admin(new ArrayList<String>(), new ArrayList<String>()));
        RoleAssigner.setRole(user, new SuperAdmin());

        // New admin accesses user
        User requestingUser = new User(new BasicUserInfo("user2", "4321"));
        requestingUser.setId(2);
        RoleAssigner.setRole(requestingUser, new Guest("Barbarian Level 1"));
        RoleAssigner.setRole(requestingUser,
                new Member("Bob DungeonMan", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        RoleAssigner.setRole(requestingUser, new Admin());

        // userDB handler sanitizes input
        UserManager userManager = new UserManager(2);

        // Sanitized user
        User sanitizedUser = userManager.sanitizeDBLookup(user, requestingUser);
        assertTrue(sanitizedUser.getBasicUserInfo().getPassword().equals(""));
        assertTrue(sanitizedUser.getMemberInfo() == null);
        assertTrue(sanitizedUser.getAdminInfo() != null);
        assertTrue(sanitizedUser.getSuperAdminInfo() == null);

    }

    @Test
    void SanitizeLookupDM() {
        // User has all possible info to see if filter works correctly
        User user = new User(new BasicUserInfo("user1", "1234"));
        user.setId(1);

        RoleAssigner.setRole(user, new Guest("Bard Level 1"));
        RoleAssigner.setRole(user,
                new Member("John Adventureman", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        RoleAssigner.setRole(user, new DM(new ArrayList<String>()));
        RoleAssigner.setRole(user, new Admin(new ArrayList<String>(), new ArrayList<String>()));
        RoleAssigner.setRole(user, new SuperAdmin());

        // New DM accesses user
        User requestingUser = new User(new BasicUserInfo("user2", "4321"));
        requestingUser.setId(2);
        RoleAssigner.setRole(requestingUser, new Guest("Barbarian Level 1"));
        RoleAssigner.setRole(requestingUser,
                new Member("Bob DungeonMan", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        RoleAssigner.setRole(requestingUser, new DM(new ArrayList<String>()));

        // userDB handler sanitizes input
        UserManager userManager = new UserManager(2);

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
        User user = new User(new BasicUserInfo("user1", "1234"));
        user.setId(1);

        RoleAssigner.setRole(user, new Guest("Bard Level 1"));
        RoleAssigner.setRole(user,
                new Member("John Adventureman", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        RoleAssigner.setRole(user, new DM(new ArrayList<String>()));
        RoleAssigner.setRole(user, new Admin(new ArrayList<String>(), new ArrayList<String>()));
        RoleAssigner.setRole(user, new SuperAdmin());

        // New member accesses user
        User requestingUser = new User(new BasicUserInfo("user2", "4321"));
        requestingUser.setId(2);
        RoleAssigner.setRole(requestingUser, new Guest("Barbarian Level 1"));
        RoleAssigner.setRole(requestingUser,
                new Member("Bob DungeonMan", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));

        // userDB handler sanitizes input
        UserManager userManager = new UserManager(2);

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
    void SanitizeLookupGuest() {
               // User has all possible info to see if filter works correctly
        User user = new User(new BasicUserInfo("user1", "1234"));
        user.setId(1);

        RoleAssigner.setRole(user, new Guest("Bard Level 1"));
        RoleAssigner.setRole(user,
                new Member("John Adventureman", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        RoleAssigner.setRole(user, new DM(new ArrayList<String>()));
        RoleAssigner.setRole(user, new Admin(new ArrayList<String>(), new ArrayList<String>()));
        RoleAssigner.setRole(user, new SuperAdmin());

        // New guest accesses user
        User requestingUser = new User(new BasicUserInfo("user2", "4321"));
        requestingUser.setId(2);
        RoleAssigner.setRole(requestingUser, new Guest("Barbarian Level 1"));

        // userDB handler sanitizes input
        UserManager userManager = new UserManager(2);

        User sanitizedUser = userManager.sanitizeDBLookup(user, requestingUser);
        assertTrue(sanitizedUser.getBasicUserInfo().getPassword().equals(""));
        assertTrue(sanitizedUser.getSuperAdminInfo() == null);
        assertTrue(sanitizedUser.getMemberInfo() == null);
        assertTrue(sanitizedUser.getAdminInfo() == null);
        assertTrue(sanitizedUser.getDmInfo() == null);
        assertTrue(sanitizedUser.getBasicUserInfo() != null);
    }

    @Test
    void userIsNull() {
        User user = null;

        User requestingUser = new User();
        requestingUser.setGuestInfo(new Guest("Barbarian Level 1"));
        UserManager userManager = new UserManager(1);
        Executable e = () -> {
            userManager.sanitizeDBLookup(user, requestingUser);
        };
        assertThrows(NullPointerException.class, e);
    }

    @Test
    void requestingUserIsNull() {
        User user = new User();
        User requestingUser = null;
        UserManager userManager = new UserManager(1);

        Executable e = () -> {
            userManager.sanitizeDBLookup(user, requestingUser);
        };
        assertThrows(NullPointerException.class, e);
    }
}
