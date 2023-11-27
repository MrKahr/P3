package com.proj.unitTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.function.Executable;

import org.junit.jupiter.api.Test;

import com.proj.function.RoleAssigner;
import com.proj.model.users.*;

public class UserGetAllRolesTest {
    @Test
    void noRoles(){
        BasicUserInfo info = new BasicUserInfo("user1", "1234");
        User user = new User(info);
        HashMap<RoleType,Role> roleMap = user.getAllRoles();
        assertTrue(roleMap.size() == 0);
    
    }

    @Test
    void OneRole(){
        BasicUserInfo buinfo = new BasicUserInfo("user1", "1234");
        User user = new User(buinfo);
        user.setGuestInfo(new Guest("Bard Level 1"));
        HashMap<RoleType,Role> roleMap = user.getAllRoles();
        assertTrue(roleMap.containsKey(RoleType.GUEST));
        assertTrue(roleMap.size() == 1);
    }

    @Test
    void twoRoles(){
        BasicUserInfo buinfo = new BasicUserInfo("user1", "1234");
        User user = new User(buinfo);
        user.setGuestInfo(new Guest("Bard Level 1"));
        user.setMemberInfo(new Member("John Adventureman", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        HashMap<RoleType,Role> roleMap = user.getAllRoles();
        assertTrue(roleMap.containsKey(RoleType.GUEST));
        assertTrue(roleMap.containsKey(RoleType.MEMBER));
        assertTrue(roleMap.size() == 2);
    }

    @Test
    void twoNoConsequitiveRoles(){
        BasicUserInfo buinfo = new BasicUserInfo("user1", "1234");
        User user = new User(buinfo);
        user.setDmInfo(new DM(new ArrayList<String>()));
        user.setSuperAdminInfo(new SuperAdmin());
        HashMap<RoleType,Role> roleMap = user.getAllRoles();
        assertTrue(roleMap.containsKey(RoleType.DM));
        assertTrue(roleMap.containsKey(RoleType.SUPERADMIN));
        assertTrue(roleMap.size() == 2);
    }
}
