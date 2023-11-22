package com.proj.model.users;

import java.util.ArrayList;
import java.util.HashMap;

import com.proj.model.events.RoleChanged;
import com.proj.function.RoleAssigner;

public class RoleBackups {
    //Field
    private ArrayList<RoleChanged> history;
    private HashMap<RoleType,Role> roles; 
    private User user;

    //Constructor
    public RoleBackups(User user){
        this.history = new ArrayList<RoleChanged>();
        this.roles = new HashMap<RoleType, Role>();
        this.user = user;
    }

    //Method
    public void addToHistory(RoleChanged roleChange){
        this.history.add(0, roleChange);    //placing the newest roleChange at the front of the list
    }

    public ArrayList<RoleChanged> getHistory(){
        return this.history;
    }

    public HashMap<RoleType,Role> getBackups(){
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
        this.roles.put(type, this.user.getRoleByType(type));
    }

    /**
     * Retrieves a role from a user's backup hashmap and returns it.
     * @param type The type of role that should be returned.
     * @return The role stored in the backup hashmap.
     */
    public Role getBackupByType(RoleType type){
        return (Role) this.roles.get(type); //casting to role to avoid compilation error
    }

    /**
     * Checks if there exists a backup of a specific role on the object it is called on.
     * @param type The type of role to check for.
     * @return True if there is a backup, false if not.
     */
    private boolean hasBackup(RoleType type){
        return this.roles.get(type) != null;
    }

    /**
     * Moves the role of the given type to the corresponding field in the associated user object.
     * If a role already exists in that field, it swaps places with the new role.
     * @param type The type of role to restore.
     */
    public void restoreBackup(RoleType type){
        if(!hasBackup(type)){
            throw new IllegalArgumentException("No backup of type " + type + " found!");
        }
        Role newRole = getBackupByType(type); 
        if(user.getRoleByType(type) != null){   //if the field is currently occupied, move it that role the backup array
            setRoleBackup(type);
        }
        RoleAssigner.setRole(user, newRole);
    }
}

