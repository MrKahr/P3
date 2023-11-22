package com.proj.unitTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.proj.model.users.*;
import com.proj.model.events.RoleChanged;

public class roleBackupTests{
    @Test
    public void constructingBackupsClass(){
        User user1 = new User("Person", "OneMillion");
        assertNotNull(user1.getRoleBackups());
        assertTrue(user1.getRoleBackups().getUser() == user1);

        User user2 = new User(new BasicUserInfo("Human", "Real"));
        assertNotNull(user1.getRoleBackups());
        assertTrue(user2.getRoleBackups().getUser() == user2);
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
    public void usingBackupMap(){
        User user = new User("Person", "OneMillion");
        Guest role = new Guest("");
        user.setGuestInfo(role);
        RoleType type = role.getRoleType();
        user.getRoleBackups().setRoleBackup(type);    //store the role we just created in the backup array
        assertTrue(user.getRoleBackups().getBackupByType(type) == role);
    }

    @Test
    public void restoringBackup(){
        User user = new User("Person", "OneMillion");
        Guest guest = new Guest("A string");
        RoleType type = guest.getRoleType();
        user.setGuestInfo(guest);   //placing the guest role on the user
        user.getRoleBackups().setRoleBackup(type);    //making the backup
        user.setGuestInfo(null);    //removing the role again
        user.getRoleBackups().restoreBackup(type);  //restore the backed up role
        assertTrue(user.getGuestInfo() == guest); //it should be there again
        assertTrue(user.getRoleBackups().getBackupByType(type) == guest);   //we replaced nothing, so the role should still be backed up

        Guest newGuest = new Guest("Not the same string as before");
        user.setGuestInfo(newGuest);    //overwriting with a new role
        user.getRoleBackups().restoreBackup(type);  //restoring the backup again
        assertTrue(user.getGuestInfo() == guest); //old guest should be there again
        assertTrue(user.getRoleBackups().getBackupByType(type) == newGuest);    //we replaced newGuest, so it should be backed up

        Executable e = () -> {user.getRoleBackups().restoreBackup(RoleType.NOTYPE);};   //trying to restore something we don't have a backup for
        Throwable thrown = assertThrows(IllegalArgumentException.class, e, "Expected setRole() to throw, but it didn't");
        assertTrue(thrown.getMessage().contains("No backup of type " + RoleType.NOTYPE + " found!"));
    }
}