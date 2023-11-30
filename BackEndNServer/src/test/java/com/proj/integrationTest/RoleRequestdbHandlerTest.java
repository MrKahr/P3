package com.proj.integrationTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.proj.repositoryhandler.RoleRequestdbHandler;
import com.proj.model.events.RoleRequest;
import com.proj.model.users.Guest;
import com.proj.model.users.Role;
@SpringBootTest
public class RoleRequestdbHandlerTest {
    @Autowired
    private RoleRequestdbHandler requestdbHandler;

    @Test
    public void createAndFulfillRoleRequest() {
        Role role = new Guest("Here's a string");
        RoleRequest request = new RoleRequest(1, role);

        requestdbHandler.save(request);
        RoleRequest foundRequest = requestdbHandler.findById(request.getRequestId());

        assertTrue(foundRequest.getUserId() == request.getUserId());    //did we get a request with the right user?
        RoleRequest roleRequest = (RoleRequest)request;
        assertTrue(roleRequest.getRoleInfo() == role);                  //is the info there?
        assertTrue(((Guest)roleRequest.getRoleInfo()).getCharacterInfo().equals("Here's a string"));    //is the info intact?
        
        requestdbHandler.delete(request);
    }
/* 
    @Test
    public void createAndRejectRoleRequest() {
        ArrayList<User> users = new ArrayList<User>();
        for (int i = 0; i < 5; i++) {
            BasicUserInfo info = new BasicUserInfo("UserWithANumber" + i + 1, "password" + i + 1);
            users.add(new User(info));
        }
        requestdbHandler.saveAll(users);

        for(User user : users) {
            assertTrue(requestdbHandler.existsById(user.getId()));
        }
        requestdbHandler.deleteAll(users);
    }
*/
}
