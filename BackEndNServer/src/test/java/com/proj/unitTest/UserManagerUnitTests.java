package com.proj.unitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.proj.function.UserManager;
import com.proj.model.users.*;
import com.proj.exception.FailedValidationException;
import com.proj.exception.UserNotFoundException;
import com.proj.function.RoleAssigner;

@SpringBootTest
public class UserManagerUnitTests {
    private User user;
    private User requestingUser;
    @Autowired
    private UserManager userManager;

    @BeforeEach
    void init() {
        // Set all users so that they have all possible info to see if filter works
        // correctly
        user = new User(new BasicUserInfo("user1", "1234Hell+o"));
        user.setId(1);
        RoleAssigner.setRole(user, new Guest("Bard Level 1"));
        RoleAssigner.setRole(user,
                new Member("John Adventureman", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        RoleAssigner.setRole(user, new DM(new ArrayList<String>()));
        RoleAssigner.setRole(user, new Admin(new ArrayList<String>(), new ArrayList<String>()));
        RoleAssigner.setRole(user, new SuperAdmin());

        // Requesting user is always different from user unless specified by test
        requestingUser = new User(new BasicUserInfo("user2", "1234Hell+o"));
        requestingUser.setId(2);

        // userDB handler sanitizes input
        userManager = new UserManager(2);

        // Create simple account for the current user
        String username = user.getBasicUserInfo().getUserName();
        String password = user.getBasicUserInfo().getPassword();
        userManager.createAccount(username, password);

    }

    @Test
    public void sanitizeSelf() {
        // Requesting user has same user id as user being requested
        try {
            requestingUser = user.clone();

        } catch (CloneNotSupportedException cnse) {
            requestingUser.setBasicUserInfo(user.getBasicUserInfo());
            requestingUser.setId(user.getId());
        }

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
        // New super admin accesses user
        RoleAssigner.setRole(requestingUser, new Guest("Barbarian Level 1"));
        RoleAssigner.setRole(requestingUser,
                new Member("Bob DungeonMan", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        RoleAssigner.setRole(requestingUser, new Admin());
        RoleAssigner.setRole(requestingUser, new SuperAdmin());

        // Sanitized user
        User sanitizedUser = userManager.sanitizeDBLookup(user, requestingUser);
        assertTrue(sanitizedUser.getBasicUserInfo().getPassword().equals(""));
        assertTrue(sanitizedUser.getMemberInfo() != null);
        assertTrue(sanitizedUser.getSuperAdminInfo() != null);
    }

    @Test
    void SanitizeLookupAdmin() {

        // New admin accesses user
        RoleAssigner.setRole(requestingUser, new Guest("Barbarian Level 1"));
        RoleAssigner.setRole(requestingUser,
                new Member("Bob DungeonMan", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        RoleAssigner.setRole(requestingUser, new Admin());

        // Sanitized user
        User sanitizedUser = userManager.sanitizeDBLookup(user, requestingUser);
        assertTrue(sanitizedUser.getBasicUserInfo().getPassword().equals(""));
        assertTrue(sanitizedUser.getMemberInfo() == null);
        assertTrue(sanitizedUser.getAdminInfo() != null);
        assertTrue(sanitizedUser.getSuperAdminInfo() == null);

    }

    @Test
    void SanitizeLookupDM() {
        // New DM accesses user
        RoleAssigner.setRole(requestingUser, new Guest("Barbarian Level 1"));
        RoleAssigner.setRole(requestingUser,
                new Member("Bob DungeonMan", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        RoleAssigner.setRole(requestingUser, new DM(new ArrayList<String>()));

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
        RoleAssigner.setRole(requestingUser, new Guest("Barbarian Level 1"));
        RoleAssigner.setRole(requestingUser,
                new Member("Bob DungeonMan", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));

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
        // New guest accesses user
        RoleAssigner.setRole(requestingUser, new Guest("Barbarian Level 1"));

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
        Executable e = () -> {
            userManager.sanitizeDBLookup(user, requestingUser);
        };
        assertThrows(NullPointerException.class, e);
    }

    @Test
    void requestingUserIsNull() {
        User requestingUser = null;

        Executable e = () -> {
            userManager.sanitizeDBLookup(user, requestingUser);
        };
        assertThrows(NullPointerException.class, e);
    }

    @Test
    public void createValidGuestAccount() {
        BasicUserInfo basicUserInfo = new BasicUserInfo("APerson", "helLo+3214");
        User aUser = new User(basicUserInfo);
        String creationResult = userManager.createAccount(aUser.getBasicUserInfo().getUserName(),
                aUser.getBasicUserInfo().getPassword());
        assertTrue(creationResult.equals("User creation successful"));
    }

    @Test
    public void createInvalidUsernameGuestAccount() {
        BasicUserInfo basicUserInfo = new BasicUserInfo("", "helLo+3214");
        User aUser = new User(basicUserInfo);
        String creationResult = userManager.createAccount(aUser.getBasicUserInfo().getUserName(),
                aUser.getBasicUserInfo().getPassword());
        assertTrue(creationResult.equals("Cannot create user because: Username is not valid"));
    }

    @Test
    public void createInvalidPasswordGuestAccount() {
        BasicUserInfo basicUserInfo = new BasicUserInfo("user3", "");
        User aUser = new User(basicUserInfo);
        String creationResult = userManager.createAccount(user.getBasicUserInfo().getUserName(),
                user.getBasicUserInfo().getPassword());
        System.out.println(creationResult);
        assertTrue(creationResult.equals("Cannot create user because: Password is not valid"));
    }

    @Test
    // Test whether accessingUser is the same as user when accessing elements in db
    public void checkUserNotInDB() {
        boolean userInDb = userManager.userExistsInDatabase("userxxx39");
        Executable e = () -> {
            // Check username that is not in db
            userManager.userExistsInDatabase("userxxx39");
        };
        assertDoesNotThrow(e);
        assertTrue(!userInDb);
    }

    @Test
    public void createAccountNullInfo() {
        try {
            userManager.createAccount(null, null);
        } catch (NullPointerException npe) {
            assertTrue(npe.getMessage().equals("Cannot create user with username or password null"));
        }
    }

    @Test
    public void createAccountAlreadyInDB() {
        String username = user.getBasicUserInfo().getUserName();
        String password = user.getBasicUserInfo().getPassword();
        String creationStatus = userManager.createAccount(username, password);
        // I am lazy - I don't have to check exact match, only that these elements are
        // part of other string -> I don't have to check for exact whitespaces
        assertTrue(creationStatus
                .contains("Cannot create user because: " + "Username:" + username + " is already in database"));

    }

    @Test
    public void createdAccountInvalidInfo() {
        String creationResultmsg = userManager.createAccount("", "");
        assertTrue(creationResultmsg.contains("Cannot create user because: " + "Username is not valid"));
    }

    @Test
    public void validateUserNotInDB() {
        // Create new user with valid username and password
        BasicUserInfo basicUserInfo = new BasicUserInfo("John", "MyPassword12+");
        User addtionalUser = new User(basicUserInfo);
        String username = addtionalUser.getBasicUserInfo().getUserName();
        String statusmsg = userManager.validateUsernameStatusInDB(username);

        assertTrue(statusmsg.equals("Validation Successful"));

    }

    /**
     * Save user and try to validate its status in database - it should already
     * exist
     */
    @Test
    public void saveAndValidateStatusInDB() {
        userManager.getUserdbHandler().save(user);
        Executable e = () -> {
            String username = user.getBasicUserInfo().getUserName();
            userManager.validateUsernameStatusInDB(username);
        };
        assertThrows(FailedValidationException.class, e);
    }

    @Test
    public void validateNullUser() {
        Executable e = () -> {
            userManager.validateUsernameStatusInDB(null);
        };
        assertThrows(NullPointerException.class, e);
    }

    @Test
    public void lookupNonExistingAccount() {
        String username = "IdontExist";
        try {
            userManager.lookupAccount(username);
        } catch (UserNotFoundException unfe) {
            assertTrue(
                    unfe.getMessage().equals("User with username '" + username + "' does not exist in the database."));
        }

    }

    @Test
    public void lookupExistingAccount() {
        String username = user.getBasicUserInfo().getUserName();
        Executable e = () -> {
            userManager.lookupAccount(username);
        };
        assertDoesNotThrow(e);
    }

    @Test
    public void getAccountListNegativeRange() {
        Executable e = () -> {
            userManager.getAccountList(-1, 0);
        };
        assertThrows(NullPointerException.class, e);

    }

    @Test
    public void getAccountListSwappedRange() {
        userManager.createAccount("thisisauser", "123HelLo+");
        User[] userList = userManager.getAccountList(2, 1);
        assertTrue(userList.length == 2);
    }

    // TODO: put request membership tests here

    @Test
    public void getValidRange() {
        userManager.createAccount("thisisauser", "123HelLo+");
        User[] userList = userManager.getAccountList(2, 1);
        assertTrue(userList.length == 2);
    }

    @Test
    public void deactivateAccountInDB() {
        userManager.deactivateAccount(user.getId());
        assertTrue(user.getBasicUserInfo().getDeactivationDate() != null);
    }

    @Test
    public void deactivateAccountNotInDB() {
        userManager.deactivateAccount(user.getId());
        assertTrue(user.getBasicUserInfo().getDeactivationDate() != null);
    }

    // removeAccount
    @Test
    public void removeValidAccount() {
        String statusmsg = userManager.removeAccount(user.getId());
        assertTrue(statusmsg.equals("Deletion of " + user.getBasicUserInfo().getUserName() + "successful"));
    }

    @Test
    public void removeInvalidAccount() {
        String statusmsg = userManager.removeAccount(user.getId());
        assertTrue(statusmsg.equals("Deletion of " + user.getBasicUserInfo().getUserName() + "unsuccessful"));
    }

    @Test
    public void restoreValidAccount() {
    }

    @Test
    public void restoreInvalidAccount() {
    }

    @Test
    public void acceptValidRoleRequest() {
    }

    @Test
    public void rejectValidRoleRequest() {
    }

}
