package com.proj.unitTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.proj.model.users.*;
import com.proj.model.events.RoleChanged;

public class roleBackupTests{
    @Test
    public void constructingBackupsClass(){
        User user1 = new User("Person", "OneMillion");
        assertNotNull(user1.getRoleBackups());
        assertTrue(user1.getRoleBackups().getUser() == user1);
        assertTrue(user1.getRoleBackups().getBackupArray().length == RoleType.values().length);

        User user2 = new User(new BasicUserInfo("Human", "Real"));
        assertNotNull(user1.getRoleBackups());
        assertTrue(user2.getRoleBackups().getUser() == user2);
        assertTrue(user2.getRoleBackups().getBackupArray().length == RoleType.values().length);
    }

    @Test
    public void addingToHistory(){
        User user = new User("Person", "OneMillion");
        assertNotNull(user.getRoleBackups().getHistory());
        RoleChanged roleChanged = new RoleChanged(user, null, null);    //pretending a role change happened
        user.getRoleBackups().addToHistory(roleChanged);
        assertTrue(user.getRoleBackups().getHistory().contains(roleChanged));
    }

    @Test
    public void usingBackupArray(){
        User user = new User("Person", "OneMillion");
        Guest role = new Guest("");
        user.setGuestInfo(role);
        RoleType type = role.getRoleType();
        user.getRoleBackups().setRoleBackup(type);    //store the role we just created in the backup array
        assertTrue(user.getRoleBackups().getBackupByType(type) == role);
    }
}