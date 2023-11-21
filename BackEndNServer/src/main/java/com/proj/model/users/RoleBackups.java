package com.proj.model.users;

import java.util.ArrayList;

import com.proj.model.events.RoleChanged;

public class RoleBackups {
    //Field
    private ArrayList<RoleChanged> history;
    private Role[] roles; 
    private User user;

    //Constructor
    public RoleBackups(User user){
        this.history = new ArrayList<RoleChanged>();
        this.roles = new Role[RoleType.values().length];  //we use the number of possible role types to set the size of the array
        this.user = user;
    }

    //Method
    public void addToHistory(RoleChanged roleChange){
        this.history.add(0, roleChange);    //placing the newest roleChange at the front of the list
    }

    public ArrayList<RoleChanged> getHistory(){
        return this.history;
    }

    public Role[] getBackupArray(){
        return this.roles;
    }

    public User getUser(){
        return this.user;
    }
    /**
     * Backs up a user's role of the give type. Only one role of each type can be stored.
     * This method does not need to be expanded when new roles are added.
     * @param type The type of the role that should be transferred to the backup array.
     */
    public void setRoleBackup(RoleType type){
        this.roles[type.ordinal()] = this.user.getRoleByType(type);
    }

    /**
     * Retrieves a role from a user's backup array and returns it.
     * @param type The type of role that should be returned.
     * @return The role stored in the backup array.
     */
    public Role getBackupByType(RoleType type){
        return this.roles[type.ordinal()];
    }
}
