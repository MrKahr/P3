package com.proj.integrationTest;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.proj.model.users.BasicUserInfo;
import com.proj.model.users.RoleType;
import com.proj.model.users.User;

import com.proj.function.UserManager;

import com.proj.exception.UserNotFoundException;

@TestMethodOrder(OrderAnnotation.class)  // IMPORTANT NOTE: Ordering tests ensures that saving users in databases does not cause conflicts between tests.
@SpringBootTest                          // For some reason, someone decided that it would be a great idea to let the database overwrite itself while tests were still running.
public class UserManagerIntegrationTest {
    @Autowired
    UserManager userManager;
    BasicUserInfo basicUserInfo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Order(100)
    public void createAccountAndRetriveValidUser() {
        User DnDuser;
        String username = "userxx";
        String password = "fefoeefwe23-A";

        userManager.createAccount(username, password);
        DnDuser = userManager.lookupAccount("userxx");

        assertTrue(DnDuser.getBasicUserInfo().getUserName().equals("userxx"));
        assertTrue(passwordEncoder.matches(password, DnDuser.getBasicUserInfo().getPassword()));

        userManager.getUserdbHandler().delete(DnDuser); //Cleanup after the test
    }

    @Test
    @Order(101)
    /* Bug: Users are deleted after being used in other tests, but these ids cannot be used to users generated in this test
    */ 
    public void createValidAccountsAndRetrieveSubset() {
        int lastId = 0;
        for (int i = 1; i < 10; i++) {
            userManager.createAccount("user" + i, "fefoeefwe23-A" + i);
            lastId = userManager.getLastId();
        }
        int startRange = lastId - 5;    //we'll the last 6 ids with this range
        int endRange = lastId;

        User[] userArrayFromDB = userManager.getAccountList(startRange, endRange);

        // Check whether right length of array and right elements
        assertTrue(userArrayFromDB.length == 6);

        for (int j = 0; j < userArrayFromDB.length; j++) {
            assertTrue(userArrayFromDB[j].getBasicUserInfo().getUserName().equals("user" + (4 + j)));   //checking if the first user is user4, the second user is user5, and so on
            
            userManager.getUserdbHandler().delete(userArrayFromDB[j]);  //Cleanup
        }
    }

    @Test
    @Order(102)
    public void createSomeInvalidAccountAndRetrieveAll() {
        // Make 5 valid and 5 invalid accounts
        String currentmsg;
        for (int i = 1; i < 11; i++) {
            // The first 5 accounts are invalid
            if (i < 6) {
                currentmsg = userManager.createAccount("user" + i, "");
                assertTrue(currentmsg.equals("Cannot create user because: Password is not valid"));
            } else {
                currentmsg = userManager.createAccount("user" + i, "fefoeefwe23-A" + i);
                assertTrue(currentmsg.equals("User creation successful"));
            }
        }

        // Check whether we get the right number of users
        User[] users = userManager.getAccountList(userManager.getLastId() - 9, userManager.getLastId());
        assertTrue(users.length == 5);

        // Check whether the current users have the correct user name
        for (int i = 0; i < users.length; i++) {
            // Get all odd numbered (and therefore valid) accounts
            assertTrue(users[i].getBasicUserInfo().getUserName().equals("user" + (i + 6)));

            userManager.getUserdbHandler().delete(users[i]);    //Cleanup
        }

    }

    @Test
    @Order(103)
    public void RequestAndFulfillValidUpgrade() {
        // Create account
        String statusmsgCreation = userManager.createAccount("ConfusedOwlBear", "helo0+L223");
        assertTrue(statusmsgCreation.equals("User creation successful"));

        // Get user
        User DndUser = userManager.lookupAccount("ConfusedOwlBear");

        // Request membership and fulfillit
        String statusmsgRequest = userManager.requestMembership("Kim", "2233445566", "9000", "Villavej 431",
                "Kim@kimmail.com", "ConfusedOwlBear");
        assertTrue(statusmsgRequest.equals("Membership request made and awaiting approval."));
        userManager.fulfillRoleRequest(DndUser.getId(), RoleType.MEMBER);

        // Get the user that is updated from database
        DndUser = userManager.lookupAccount("ConfusedOwlBear");
        assertTrue(DndUser.getMemberInfo().getRealName().equals("Kim"));

        userManager.getUserdbHandler().delete(DndUser); //Cleanup
    }

    @Test
    @Order(104)
    public void RequestAndRejectValidUpgrade() {
        // Create account
        String statusmsgCreation = userManager.createAccount("ConfusedOwlBear", "helo0+L223");
        assertTrue(statusmsgCreation.equals("User creation successful"));

        // Get user
        User DndUser = userManager.lookupAccount("ConfusedOwlBear");

        // Request membership and reject it
        String statusmsgRequest = userManager.requestMembership("Kim", "2233445566", "9000", "Villavej 431",
                "Kim@kimmail.com", "ConfusedOwlBear");
        assertTrue(statusmsgRequest.equals("Membership request made and awaiting approval."));
        userManager.rejectRoleRequest(DndUser.getId(), RoleType.MEMBER);

        userManager.getUserdbHandler().delete(DndUser); //Cleanup
    }

    @Test
    @Order(105)
    public void removeRemovedateValid() throws InterruptedException {
        // Create account
        userManager.createAccount("ConfusedOwlBear", "helo0+L223");
        User dndUser = userManager.lookupAccount("ConfusedOwlBear");

        userManager.deactivateAccount(dndUser.getId());
        
        // Remove date is hardcoded 6 months into the future, therefore we have to set in manually 
        dndUser = userManager.lookupAccount("ConfusedOwlBear");

        // Set valid deactivation date and save in db
        userManager.deactivateAccount(dndUser.getId());
        dndUser.getBasicUserInfo().setDeletionDate(LocalDateTime.now().minusMinutes(2));
        userManager.getUserdbHandler().save(dndUser);
        
        String statusmsgRemove = userManager.removeAccount(dndUser.getBasicUserInfo().getUserName());
        Thread.sleep(2000);
        assertTrue(statusmsgRemove.equals("User \"" + dndUser.getBasicUserInfo().getUserName() + "\" scheduled for deletion."));
    }

    @Test
    @Order(107)
    public void activateInactiveAccount() {
        // Create account
        userManager.createAccount("ConfusedOwlBear3", "helo0+L223");
        User dndUser = userManager.lookupAccount("ConfusedOwlBear3");
        userManager.deactivateAccount(dndUser.getId());
        assertTrue(userManager.restoreAccount(dndUser.getBasicUserInfo().getUserName()).equals("User: ConfusedOwlBear3 with ID: " + dndUser.getId() + " was successfully restored"));

        userManager.getUserdbHandler().delete(dndUser); //Cleanup
    }

    @Test
    @Order(108)
    public void activateRemovedAccount() throws InterruptedException {
        // Create account
        userManager.createAccount("ConfusedOwlBear4", "helo0+L223");
        User dndUser = userManager.lookupAccount("ConfusedOwlBear4");

        // Deactivate account, but set deletion date since it is set 6 months from now
        userManager.deactivateAccount(dndUser.getId());
        dndUser.getBasicUserInfo().setDeletionDate(LocalDateTime.now().minusMinutes(2));

        // Save user and remove account
        userManager.getUserdbHandler().save(dndUser);
        userManager.removeAccount(dndUser.getBasicUserInfo().getUserName());
        Thread.sleep(2000); //Wait for the account to be removed
        // Attempt to restore account
        Executable e = () -> {userManager.restoreAccount(dndUser.getBasicUserInfo().getUserName());};
        Throwable thrown = assertThrows(UserNotFoundException.class, e);
        assertEquals(thrown.getMessage(), "UserdbHandler: User '" + dndUser.getBasicUserInfo().getUserName() + "' not found");
    }
    /*
    @Test       //this test is used to clean up the database by deleting all users in it.
    @Order(??)  //Ensure it runs last and |ABSOLUTELY DO NOT| use it if the database has real users in it.
    public void wipeDataBase(){
        Iterable<User> users = userManager.getUserdbHandler().findAll();
        userManager.getUserdbHandler().deleteAll(users);
    }
    */
}