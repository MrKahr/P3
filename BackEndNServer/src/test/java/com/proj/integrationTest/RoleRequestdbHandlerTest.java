package com.proj.integrationTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.proj.function.UserManager;
import com.proj.model.events.RoleRequest;
import com.proj.model.users.Guest;
import com.proj.model.users.Role;
import com.proj.model.users.RoleType;
import com.proj.model.users.User;
@SpringBootTest
public class RoleRequestdbHandlerTest {
    @Autowired
    private UserManager userManager;

    @Test
    @Order(94)
    public void createAndRetrieveRoleRequest() {
        Role role = new Guest("Here's a string");
        RoleRequest request = new RoleRequest(1, role);

        userManager.getRoleRequestdbHandler().save(request);
        RoleRequest foundRequest = userManager.getRoleRequestdbHandler().findById(request.getRequestId());

        assertTrue(foundRequest.getUserId() == request.getUserId());    //did we get a request with the right user?
        RoleRequest roleRequest = (RoleRequest)request;
        assertTrue(roleRequest.getRoleInfo() == role);                  //is the info there?
        assertTrue(((Guest)roleRequest.getRoleInfo()).getCharacterInfo().equals("Here's a string"));    //is the info intact?
        
        userManager.getRoleRequestdbHandler().delete(request);  //Cleanup
    }

    @Test
    @Order(95)
    public void createAndFulfillRoleRequest() {
        User user = new User("guestWanter1000", "giveRolePlease");

        userManager.getUserdbHandler().save(user);

        Executable e = () -> {userManager.fulfillRoleRequest(user.getId(), RoleType.BADTYPE);};   //trying to fulfill a nonexistant request should throw an error
        Throwable thrown = assertThrows(IllegalArgumentException.class, e, "Expected fulfillRoleRequest() to throw, but it didn't");
        assertTrue(thrown.getMessage().contains("No request of the given type exists for user with ID " + user.getId()));

        Guest role = new Guest("Here's a string"); //create a guest-role
        userManager.createRoleRequest(user.getId(), role);      //make a request from the user we just saved
        
        userManager.fulfillRoleRequest(user.getId(), RoleType.GUEST); //try to fulfill the user's request
        assertTrue(userManager.getUserdbHandler().findById(user.getId()).getGuestInfo() != null);   //the object should be on the user now
        assertTrue(userManager.getUserdbHandler().findById(user.getId()).getGuestInfo().getCharacterInfo().contains("Here's a string"));
    
        userManager.getUserdbHandler().delete(user);    //Cleanup
    }

    @Test
    @Order(96)
    public void createAndRejectRoleRequest() {
        User user = new User("guestWanter1000", "giveRolePlease");

        userManager.getUserdbHandler().save(user);

        Executable e = () -> {userManager.rejectRoleRequest(user.getId(), RoleType.BADTYPE);};   //trying to fulfill a nonexistant request should throw an error
        Throwable thrown = assertThrows(IllegalArgumentException.class, e, "Expected fulfillRoleRequest() to throw, but it didn't");
        assertTrue(thrown.getMessage().contains("No request of the given type exists for user with ID " + user.getId()));

        Guest role = new Guest("Here's a string");
        RoleRequest request = userManager.createRoleRequest(user.getId(), role);    //create a role request and keep the copy

        userManager.rejectRoleRequest(user.getId(), RoleType.GUEST);
        assertTrue(userManager.getUserdbHandler().findById(user.getId()).getGuestInfo() == null);   //there should be no guest-role on the user, since we never added it
        assertFalse(userManager.getRoleRequestdbHandler().existsById(request.getRequestId()));      //the request should no longer exist

        userManager.getUserdbHandler().delete(user); //Cleanup
    }
}
