package com.proj.model.users;

import java.util.ArrayList;

/**
 * A class that represents a user with the access level "Admin".
 */
public class Admin extends Role {
    // Field
    private ArrayList<String> listOfRoleChanges; // The ArrayList containing all role change events the user has
                                                 // performed (bans excluded)
    private ArrayList<String> listOfBans; // The ArrayList containing all Ban role changes issued by the user.

    // Constructor
    /**
     * Creates an Admin object with supplied parameters.
     * 
     * @Pre-con: Expects all arguments to be sanitized.
     * @param listOfRoleChanges The ArrayList containing all role change events the
     *                          user has performed (bans excluded). This param is
     *                          optional. Must be null if not used
     * @param listOfBans        The ArrayList containing all Ban role changes issued
     *                          by the user. This param is optional. Must be null if
     *                          not used
     */
    public Admin(ArrayList<String> listOfRoleChanges, ArrayList<String> listOfBans) {
        if (listOfRoleChanges != null) {
            this.listOfRoleChanges = listOfRoleChanges;
        } else {
            this.listOfRoleChanges = new ArrayList<String>();
        }
        if (listOfRoleChanges != null) {
            this.listOfBans = listOfBans;
        } else {
            this.listOfBans = new ArrayList<String>();
        }
    }

    // Method
    // TODO add method for adding object to array list
    public ArrayList<String> getListOfRoleChanges() {
        return listOfRoleChanges;
    }

    public ArrayList<String> getListOfBans() {
        return listOfBans;
    }

    @Override
    public RoleType getRoleType(){
        return RoleType.ADMIN;
    }

    @Override
    public RoleType[] getRoleDependencies(){
        RoleType[] types = {RoleType.MEMBER};
        return types;
    }
}
