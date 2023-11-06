package com.proj.model.users;

import java.util.ArrayList;

/**
 * A class that represents a user with the access level "Admin". 
 */
public class Admin extends DM {
    // Field
    private ArrayList<String> listOfRoleChanges; // The ArrayList containing all role change events the user has performed (bans excluded)
    private ArrayList<String> listOfBans; // The ArrayList containing all Ban role changes issued by the user.

    // Constructor
    /**
     * Creates an Admin object with supplied parameters.
     * @Pre-con: Expects all arguments to be sanitized.
     * @param realName The user's full name 
     * @param phoneNumber The user's mobile phone number 
     * @param postalCode The postal code associated with a users home address 
     * @param address The user's physical address
     * @param email The user's email address
     * @param hostedSessions The ArrayList containing all sessions the user has hosted
     * @param listOfRoleChanges The ArrayList containing all role change events the user has performed (bans excluded). This param is optional. Must be null if not used
     * @param listOfBans The ArrayList containing all Ban role changes issued by the user. This param is optional. Must be null if not used
     */
    public Admin(String userName, String password, String realName, String phoneNumber, String postalCode, String address, String email, ArrayList<String> hostedSessions, ArrayList<String> listOfRoleChanges, ArrayList<String> listOfBans){
        super(userName, password, realName, phoneNumber, postalCode, address, email, hostedSessions);
        listOfRoleChanges == null ? this.listOfRoleChanges = new ArrayList<String>(); : this.listOfRoleChanges = listOfRoleChanges; // One-liner if-else to make code more compact
        listOfBans == null ? this.listOfBans = new ArrayList<String>(); : this.listOfBans = listOfBans;

    }

   // Method 
   // TODO add method for adding object to array list 
    public ArrayList<String> getListOfRoleChanges() {
        return listOfRoleChanges;
    }

    public ArrayList<String> getListOfBans() {
        return listOfBans;
    }
}
