package com.proj.model.events;

import java.time.LocalDateTime;

import com.proj.model.users.*;

public class RoleChanged {
    // Field
    //Could need a field for the user's ID here.
    private LocalDateTime date;
    private Role newRoleObject;
    private Role previousRoleObject;

    // Constructor
    /**
     * Creates an object of class RoleChanged and sets its date-attribute using localdate.now().
     * @param user The user affected by the role change.
     * @param newRoleObject The role that was added to the user.
     * @param previousRoleObject The role that was replaced by newRoleObject. Should be null if no role object was present before.
     */
    public RoleChanged(User user, Role newRoleObject, Role previousRoleObject) {
        this.date = LocalDateTime.now();
        this.newRoleObject = newRoleObject;
        this.previousRoleObject = previousRoleObject;
    }

    // Method -  No setters since we model instantenous events as object instanstiations.
    public LocalDateTime getDate() {
        return this.date;
    }

    public Role getNewRole() {
        return this.newRoleObject;
    }

    public Role getPreviousRole() {
        return this.previousRoleObject;
    }
}