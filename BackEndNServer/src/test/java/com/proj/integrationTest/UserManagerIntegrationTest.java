package com.proj.integrationTest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.proj.model.users.BasicUserInfo;
import com.proj.model.users.User;
import com.proj.function.UserManager;

@SpringBootTest
public class UserManagerIntegrationTest {
    @Autowired
    UserManager userManager;
    BasicUserInfo basicUserInfo;

    /*@BeforeEach
     public void init() {
        BasicUserInfo buinfo = new BasicUserInfo("user1", "fefoeefwe23-A");
    } */

    @Test
    public void createAccountAndRetriveValidUser() {
        User DnDuser; 
        String username = "userxx";
        String password = "fefoeefwe23-A";

        userManager.createAccount(username, password);
        DnDuser = userManager.lookupAccount("userxx");
        System.out.println(DnDuser.getBasicUserInfo().getPassword());

        assertTrue(DnDuser.getBasicUserInfo().getUserName().equals("userxx"));
        assertTrue(DnDuser.getBasicUserInfo().getPassword().equals("fefoeefwe23-A"));
    }

    @Test
    public void createValidAccountsAndRetrieveSubset() {
        for(int i = 0; i < 10; i++){
            userManager.createAccount("user" + i, "fefoeefwe23-A" + i);
        }
        int startRange = 1;
        int endRange = 4; 
        int currentRange = startRange;

        User[] users = userManager.getAccountList(startRange,endRange);
        
        // Check whether right length of array and right elements 
        assertTrue(users.length == 4);

        for(int j = 0; j < users.length; j++){
            System.out.println(users[j].getBasicUserInfo().getUserName());
            assertTrue(users[j].getBasicUserInfo().getUserName().equals("user" + currentRange));
        }
    }

    @Test
    public void createSomeInvalidAccountAndRetrieveAll() {
    }
    
    @Test 
    public void createAccountAndRequestValidUpgrade(){}

    @Test 
    public void createAccountAndRequestInvalidUpgrade(){}


}
