package com.proj.unitTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.proj.function.UserManager;
import com.proj.model.events.RoleRequest;
import com.proj.model.users.*;
import com.proj.exception.FailedValidationException;
import com.proj.exception.UserNotFoundException;
import com.proj.exception.RequestNotFoundException;
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
        RoleAssigner.setRole(user, new Guest("Bard Level 1"));
        RoleAssigner.setRole(user,
                new Member("John Adventureman", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        RoleAssigner.setRole(user, new DM(new ArrayList<String>()));
        RoleAssigner.setRole(user, new Admin(new ArrayList<String>(), new ArrayList<String>()));
        RoleAssigner.setRole(user, new SuperAdmin());

        // Requesting user is always different from user unless specified by test
        requestingUser = new User(new BasicUserInfo("user2", "1234Hell+o"));
    }
    
    @Test
    @Order(34)
    public void sanitizeSelf() {
        // Requesting user has same user id as user being requested
        try {
            requestingUser = user.clone();

        } catch (CloneNotSupportedException cnse) {
            requestingUser.setBasicUserInfo(user.getBasicUserInfo());
            requestingUser.setId(user.getId());
        }

        // Sanitized user
        User sanitizedUser = userManager.sanitizeDBLookup(user, requestingUser);
        assertTrue(sanitizedUser.getBasicUserInfo().getPassword().equals(""));
        assertTrue(sanitizedUser.getBasicUserInfo() != null);
        assertTrue(sanitizedUser.getMemberInfo() != null);
        assertTrue(sanitizedUser.getDmInfo() != null);
        assertTrue(sanitizedUser.getAdminInfo() != null);
        assertTrue(sanitizedUser.getSuperAdminInfo() != null);
    }

    @Test
    @Order(35)
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
    @Order(36)
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
    @Order(37)
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
    @Order(38)
    void SanitizeLookupMember() {
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
    @Order(39)
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
    @Order(40)
    void userIsNull() {
        User user = null;
        Executable e = () -> {
            userManager.sanitizeDBLookup(user, requestingUser);
        };
        assertThrows(NullPointerException.class, e);
    }

    @Test
    @Order(41)
    void requestingUserIsNull() {
        User requestingUser = null;

        Executable e = () -> {
            userManager.sanitizeDBLookup(user, requestingUser);
        };
        assertThrows(NullPointerException.class, e);
    }

    @Test
    @Order(42)
    public void createValidGuestAccount() {
        String creationResult = userManager.createAccount("APerson",
                "helLo+3214");
        assertTrue(creationResult.equals("User creation successful"));
    }

    @Test
    @Order(43)
    public void createInvalidUsernameGuestAccount() {
        String creationResult = userManager.createAccount("", "helLo+3214");
        assertTrue(creationResult.equals("Cannot create user because: Username is not valid"));
    }

    @Test
    @Order(44)
    public void createInvalidPasswordGuestAccount() {
        // Bug: User3 was already made, using arbitrary name instead
        String creationResult = userManager.createAccount("user1234", "");
        assertEquals(creationResult, "Cannot create user because: Password is not valid");
    }

    @Test
    @Order(45)
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
    @Order(46)
    public void createAccountNullInfo() {
        try {
            userManager.createAccount(null, null);
        } catch (NullPointerException npe) {
            assertTrue(npe.getMessage().equals("Cannot create user with username or password null"));
        }
    }

    @Test
    @Order(47)
    public void createAccountAlreadyInDB() {
        String username = user.getBasicUserInfo().getUserName();
        String password = user.getBasicUserInfo().getPassword();
        userManager.createAccount(username, password);
        String creationStatus = userManager.createAccount(username, password);
        // I am lazy - I don't have to check exact match, only that these elements are
        // part of other string -> I don't have to check for exact whitespaces
        assertTrue(creationStatus
                .contains("Cannot create user because: " + "Username:" + username + " is already in database"));
        
        userManager.getUserdbHandler().delete(userManager.lookupAccount(username)); //cleanup
    }

    @Test
    @Order(48)
    public void createdAccountInvalidInfo() {
        String creationResultmsg = userManager.createAccount("", "");
        assertTrue(creationResultmsg.contains("Cannot create user because: " + "Username is not valid"));
    }

    @Test
    @Order(49)
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
    @Order(50)
    public void saveAndValidateStatusInDB() {
        userManager.getUserdbHandler().save(user);
        Executable e = () -> {
            String username = user.getBasicUserInfo().getUserName();
            userManager.validateUsernameStatusInDB(username);
        };
        assertThrows(FailedValidationException.class, e);
    }

    @Test
    @Order(51)
    public void validateNullUser() {
        String msg = userManager.validateUsernameStatusInDB(null);
        assertTrue(msg.equals("Cannot determine whether null exists in database"));
    }

    @Test
    @Order(52)
    public void lookupNonExistingAccount() {
        String username = "IdontExist";
        try {
            userManager.lookupAccount(username);
        } catch (UserNotFoundException unfe) {
            // Note that this error is thrown by findByUserName in the userdbhandler
            assertTrue(
                    unfe.getMessage().equals("UserdbHandler: User " + username + " not found"));
        }

    }

    @Test
    @Order(53)
    public void lookupExistingAccount() {
        String username = user.getBasicUserInfo().getUserName();
        Executable e = () -> {
            userManager.lookupAccount(username);
        };
        assertDoesNotThrow(e);
    }

    @Test
    @Order(54)
    public void getAccountListNegativeRange() {
        Executable e = () -> {
            userManager.getAccountList(-1, 0);
        };
        assertThrows(IllegalArgumentException.class, e);

    }

    @Test
    @Order(55)
    public void getAccountListSwappedRange() {
        userManager.createAccount("thisisauser", "123HelLo+");
        User user = userManager.lookupAccount("thisisauser");
        User[] userList = userManager.getAccountList(user.getId(), user.getId() - 1);
        assertTrue(userList.length == 2);

        userManager.getUserdbHandler().delete(user);    //cleanup
    }

    @Test
    @Order(56)
    public void requestUpgradeValidUser() {
        // Create user account in db
        userManager.createAccount("TheBobinator", "heloO+123");
        User userToUpgrade = userManager.lookupAccount("TheBobinator");

        // Create and fulfill role requests for user
        userManager.createRoleRequest(userToUpgrade.getId(), new Guest("Druid level 1"));
        userManager.fulfillRoleRequest(userToUpgrade.getId(), RoleType.GUEST);

        // Make membership request after all necessary roledependencies are fulfilled
        String statusMsg = userManager.requestMembership("Bob", "43114311", "9000", "Villavej 123", "Bob@bobmail.com",
                "TheBobinator");

        // Check whether there is only one role request of type Member in for this user
        Iterable<RoleRequest> roleRequests = userManager.getRoleRequestdbHandler()
                .findAllByUserId(userToUpgrade.getId());
        RoleRequest request = roleRequests.iterator().next();
        assertTrue(request.getRoleInfo() instanceof Member);
        // Check whether correct message is returned by usermanager
        assertTrue(statusMsg.equals("Membership request made and awaiting approval."));

        userManager.getUserdbHandler().delete(userToUpgrade);   //cleanup
        userManager.getRoleRequestdbHandler().delete(request);  //cleanup
    }

    @Test
    @Order(57)
    public void requestUpgradeUserNotInDB() {
        String statusmsg = userManager.requestMembership("Bob", "43114311", "9000", "Villavej 123", "Bob@bobmail.com",
                "IdontExist123");
        assertTrue(statusmsg.equals("UserdbHandler: User IdontExist123 not found"));
    }

    @Test
    @Order(58)
    public void requestUserUpgradeInvalidDependencies() {
        // Create new account
        userManager.createAccount("TheBobinator", "heloO+123");
        User userToUpgrade = userManager.lookupAccount("TheBobinator");
        // Remove guest role 
        userToUpgrade.setGuestInfo(null);
        userManager.getUserdbHandler().save(userToUpgrade);

        // Attempt to upgrade without necessary dependencies
        String statusmsg = userManager.requestMembership("Bob", "43114311", "9000", "Villavej 123", "Bob@bobmail.com",
                "TheBobinator");
        assertTrue(statusmsg.equals("User with id " + userToUpgrade.getId()+  " does not fulfill the requirements for the given role."));
   
        userManager.getUserdbHandler().delete(userToUpgrade);   //Cleanup
    }

    @Test
    @Order(59)
    // This test should ideally cover all cases when some invalid information has
    // been entered in upgrade - we know because we have already tested the
    // validator
    public void requestUpgradeUserInvalidEmail() {
        // Create new account
        userManager.createAccount("TheBobinator", "heloO+123");
        User userToUpgrade = userManager.lookupAccount("TheBobinator");

        // Create and fulfill guest request for user
        userManager.createRoleRequest(userToUpgrade.getId(), new Guest("Druid level 1"));
        userManager.fulfillRoleRequest(userToUpgrade.getId(), RoleType.GUEST);

        // Attempt to upgrade with invalid email
        String statusmsg = userManager.requestMembership("Bob", "43114311", "9000", "Villavej 123", "bobmail.com",
                "TheBobinator");
        assertTrue(statusmsg.equals("Email is not valid"));

        userManager.getUserdbHandler().delete(userToUpgrade);   //Cleanup
    }

    @Test
    @Order(60)
    public void getValidRange() {
        userManager.createAccount("thisisauser1", "123HelLo+");
        userManager.createAccount("thisisauser2", "123HelLo+");
        User user1 = userManager.lookupAccount("thisisauser1");
        User user2 = userManager.lookupAccount("thisisauser2");
        User[] userList = userManager.getAccountList(user1.getId(), user2.getId());
        assertTrue(userList.length == 2);

        userManager.getUserdbHandler().delete(user1);   //Cleanup
        userManager.getUserdbHandler().delete(user2);   //Cleanup
    }

    @Test
    @Order(61)
    public void deactivateAccountInDB() {
        userManager.getUserdbHandler().save(user);
        userManager.deactivateAccount(user.getId());
        user = userManager.getUserdbHandler().findById(user.getId());
        //user = userManager.lookupAccount(user.getBasicUserInfo().getUserName()); Bug here where user in not in db
        assertTrue(user.getBasicUserInfo().getDeactivationDate() != null);

        userManager.getUserdbHandler().delete(user);   //Cleanup
    }

    @Test
    @Order(62)
    public void deactivateAccountNotInDB() {
        BasicUserInfo userInfo = new BasicUserInfo("userx10", "helLo23+");
        User dndUser = new User(userInfo);

        Executable e = () -> {
            userManager.deactivateAccount(dndUser.getId());
        };

        assertThrows(NullPointerException.class, e);

        userManager.getUserdbHandler().delete(dndUser);   //Cleanup
    }

    @Test
    @Order(63)
    public void removeValidAccount() throws InterruptedException {
        userManager.removeAccount(user.getBasicUserInfo().getUserName());
        Thread.sleep(2000);
        Executable e = () -> {userManager.lookupAccount(user.getBasicUserInfo().getUserName());};
        assertThrows(UserNotFoundException.class, e);
    }

    @Test
    @Order(64)
    public void removeInvalidAccount() {
        String nonExistantUserName = user.getBasicUserInfo().getUserName();
        userManager.getUserdbHandler().delete(user);    //Make the user not exist anymore
        String statusmsg = userManager.removeAccount(nonExistantUserName);
        System.out.println(statusmsg);
        assertTrue(statusmsg.equals("UserdbHandler: User " + nonExistantUserName + " not found"));
    }

    @Test
    @Order(65)
    public void restoreValidAccount() {
        user.getBasicUserInfo().setDeletionDate(LocalDateTime.now().minusMinutes(2));
        user.getBasicUserInfo().setDeactivationDate(LocalDateTime.now().minusMinutes(2));
        // Save user with new deletion and deactivation date
        userManager.getUserdbHandler().save(user);
        userManager.restoreAccount(user.getBasicUserInfo().getUserName());
        // Check whether restore account has set relevant fields to null in database
        user = userManager.lookupAccount(user.getBasicUserInfo().getUserName());
        assertTrue(user.getBasicUserInfo().getDeactivationDate() == null);
        assertTrue(user.getBasicUserInfo().getDeletionDate() == null);

        userManager.getUserdbHandler().delete(user);    //Cleanup
    }

    @Test
    @Order(66)
    public void restoreInvalidAccount() {
        // Restore user with arbitrary Id that does not exist in db
         String nonExistantUserName = user.getBasicUserInfo().getUserName();
        userManager.getUserdbHandler().delete(user);    //Make the user not exist anymore
        Executable e = () -> {
            userManager.restoreAccount(nonExistantUserName);
        };
        assertThrows(UserNotFoundException.class, e);
    }

    @Test
    @Order(67)  //TODO: This test has no assertions!
    public void makeRoleRequest() {
        // Create user in database
        userManager.createAccount(requestingUser.getBasicUserInfo().getUserName(),
                requestingUser.getBasicUserInfo().getPassword());
        // Get user from database
        requestingUser = userManager.lookupAccount(requestingUser.getBasicUserInfo().getUserName());

        // Make request
        userManager.createRoleRequest(requestingUser.getId(), new Guest("Level 1 barbarian"));
        
        // Check whether role request is in database - cannot find 
        Iterable<RoleRequest> request = userManager.getRoleRequestdbHandler().findAllByUserId(requestingUser.getId());
        
        // Get elements associated with user and check whether these are of guest type
        for (RoleRequest roleRequest : request) {
            assertTrue(request.iterator().next().getRoleInfo() instanceof Guest);
        }
        
        
        userManager.rejectRoleRequest(requestingUser.getId(), RoleType.GUEST);  //Cleanup
        userManager.getUserdbHandler().delete(requestingUser);  //Cleanup
    }

    @Test
    @Order(68)
    public void makeInvalidRoleRequest() {
        // Create user in database
        userManager.createAccount(requestingUser.getBasicUserInfo().getUserName(),
                requestingUser.getBasicUserInfo().getPassword());
        // Get user from database
        requestingUser = userManager.lookupAccount(requestingUser.getBasicUserInfo().getUserName());
        // Make invalid request
        Executable e = () -> {
            userManager.createRoleRequest(requestingUser.getId(), new SuperAdmin());
        };
        Throwable thrown = assertThrows(IllegalArgumentException.class, e);
        assertTrue(thrown.getMessage().equals(
                "User with id " + requestingUser.getId() + " does not fulfill the requirements for the given role."));

        userManager.getUserdbHandler().delete(requestingUser);  //Cleanup
    }

    @Test
    @Order(69)
    public void acceptValidRoleRequest() {
        // Create new requesting user account
        userManager.createAccount(requestingUser.getBasicUserInfo().getUserName(),
                requestingUser.getBasicUserInfo().getPassword());
        requestingUser = userManager.lookupAccount(requestingUser.getBasicUserInfo().getUserName());

        // Make role request to upgrade account from basic user to guest
        RoleRequest roleRequest = userManager.createRoleRequest(requestingUser.getId(), new Guest("Barbarian Level 2"));
        userManager.fulfillRoleRequest(requestingUser.getId(), RoleType.GUEST);

        // Get user from database once upgrade has happend
        requestingUser = userManager.lookupAccount(requestingUser.getBasicUserInfo().getUserName());

        // Check whether upgrade was successful
        assertTrue(requestingUser.getGuestInfo() != null);

        // Check whether role request was deleted from database
        Executable e = () -> {
            userManager.getRoleRequestdbHandler().findById(roleRequest.getRequestId());
        };
        Throwable thrown = assertThrows(RequestNotFoundException.class, e);
        assertTrue(
                thrown.getMessage().contains("Request with id '" + roleRequest.getRequestId() + "' does not exist."));

        userManager.getUserdbHandler().delete(requestingUser); //Cleanup
    }

    @Test
    @Order(70)
    public void acceptNullRoleRequest() {
        userManager.getUserdbHandler().save(requestingUser);
        Executable e = () -> {
            userManager.fulfillRoleRequest(requestingUser.getId(), null);
        };
        Throwable thrown = assertThrows(IllegalArgumentException.class, e);
        assertTrue(thrown.getMessage()
                .contains("No request of the given type exists for user with ID " + requestingUser.getId()));
        
        userManager.getUserdbHandler().delete(requestingUser); //Cleanup
    }

    @Test
    @Order(71)
    public void rejectValidRoleRequest() {
        userManager.getUserdbHandler().save(requestingUser);
        RoleRequest roleRequest = userManager.createRoleRequest(requestingUser.getId(), new Guest());
        userManager.rejectRoleRequest(requestingUser.getId(), RoleType.GUEST);
        Executable e = () -> {
            userManager.getRoleRequestdbHandler().findById(roleRequest.getRequestId());
        };
        Throwable thrown = assertThrows(RequestNotFoundException.class, e);
        assertTrue(
                thrown.getMessage().contains("Request with id '" + roleRequest.getRequestId() + "' does not exist."));

        userManager.getUserdbHandler().delete(requestingUser); //Cleanup
    }

    @Test
    @Order(72)
    public void rejectNullRoleRequest() {
        userManager.getUserdbHandler().save(requestingUser);
        // rejecting without having a request to reject
        Executable e = () -> {
            userManager.rejectRoleRequest(requestingUser.getId(), RoleType.GUEST);
        };
        Throwable thrown = assertThrows(IllegalArgumentException.class, e);
        assertTrue(thrown.getMessage()
                .contains("No request of the given type exists for user with ID " + requestingUser.getId()));

        userManager.getUserdbHandler().delete(requestingUser); //Cleanup        
    }
}
