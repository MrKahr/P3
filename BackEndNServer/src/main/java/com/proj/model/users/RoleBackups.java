package com.proj.model.users;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.proj.model.events.RoleChanged;
import com.proj.function.RoleAssigner;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleBackups {
    //Field
    private ArrayList<RoleChanged> history;
    private HashMap<RoleType,Role> roles;
    //there used to be a field for a user here, but that made it impossible to serialize, so we opt for a user parameter where it's needed instead

    //Constructor
    public RoleBackups(){
        this.history = new ArrayList<RoleChanged>();

        this.roles = new HashMap<RoleType, Role>();
    }

    //Method
    public void addToHistory(RoleChanged roleChange){
        this.history.add(0, roleChange);    //placing the newest roleChange at the front of the list
        if(history.size() > 20){
            history.remove(history.size()-1);   //remove the last element if the history has more than 20 entries now
        }
    }

    public ArrayList<RoleChanged> getHistory(){
        return this.history;
    }

    public HashMap<RoleType,Role> getBackups(){
        return this.roles;
    }

    /**
     * Backs up a user's role of the given type. Only one role of each type can be stored.
     * This method does not need to be expanded when new roles are added.
     * @param user The user whose role should be backed up.
     * @param type The type of the role that should be transferred to the backup hashmap.
     */
    public void setRoleBackup(User user, RoleType type){
        this.roles.put(type, user.getRoleByType(type));
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
     * @param user The user whose backup should be restored.
     * @param type The type of role to restore.
     */
    public void restoreBackup(User user, RoleType type){
        if(!hasBackup(type)){
            throw new IllegalArgumentException("No backup of type " + type + " found!");
        }
        Role newRole = getBackupByType(type); 
        if(user.getRoleByType(type) != null){   //if the field is currently occupied, move it that role the backup array
            setRoleBackup(user, type);
        }
        RoleAssigner.setRole(user, newRole);
    }
}

