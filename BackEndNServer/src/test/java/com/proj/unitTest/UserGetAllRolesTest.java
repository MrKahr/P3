package com.proj.unitTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.function.Executable;

import org.junit.jupiter.api.Test;

import com.proj.function.RoleAssigner;
import com.proj.model.users.*;

public class UserGetAllRolesTest {
    @Test
    void noRoles(){
        BasicUserInfo info = new BasicUserInfo("user1", "1234");
        User user = new User(info);
        RoleType[] roles = user.getAllRoles();
        for(int i = 0; i < roles.length; i++){
            assertTrue(roles[i] == null);
        }
    }

    @Test
    void OneRole(){
        BasicUserInfo buinfo = new BasicUserInfo("user1", "1234");
        User user = new User(buinfo);
        user.setGuestInfo(new Guest("Bard Level 1"));
        RoleType[] roles = user.getAllRoles();
        assertTrue(roles[0] == RoleType.GUEST);
        assertTrue(roles[1] == null);
    }

    @Test
    void twoRoles(){
        BasicUserInfo buinfo = new BasicUserInfo("user1", "1234");
        User user = new User(buinfo);
        user.setGuestInfo(new Guest("Bard Level 1"));
        user.setMemberInfo(new Member("John Adventureman", "123-339933", "9000", "Villavej 123", "John@Adventureman.dk"));
        RoleType[] roles = user.getAllRoles();
        assertTrue(roles[0] == RoleType.GUEST);
        assertTrue(roles[1] == RoleType.MEMBER);
        assertTrue(roles[2] == null);
    }

    @Test
    void twoNoConsequitiveRoles(){
        BasicUserInfo buinfo = new BasicUserInfo("user1", "1234");
        User user = new User(buinfo);
        user.setDmInfo(new DM(new ArrayList<String>()));
        user.setSuperAdminInfo(new SuperAdmin());
        RoleType[] roles = user.getAllRoles();
        assertTrue(roles[2] == RoleType.DM);
        assertTrue(roles[4] == RoleType.SUPERADMIN);
    }
}
