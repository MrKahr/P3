package com.proj.model.users;

import java.util.ArrayList;

/**
 * A class that represents a user with the access level "DM". 
 */
public class DM extends Member {
    // Field
    private ArrayList<String> hostedSessions; // We might prefer another data structure

    // Constructor
    /**
     * Creates a DM object with supplied parameters. Note that hostedSessions is optional (must be null then).
     * @Pre-con: Expects all arguments to be sanitized.
     * @param realName The user's full name 
     * @param phoneNumber The user's mobile phone number 
     * @param postalCode The postal code associated with a users home address 
     * @param address The user's physical address
     * @param email The user's email address
     * @param hostedSessions The ArrayList containing all sessions the user has hosted. This param is optional, and if not needed must be null
     */
    public DM(String userName, String password, String realName, String phoneNumber, String postalCode, String address, String email, ArrayList<String> hostedSessions){
        super(userName, password, realName, phoneNumber, postalCode, address, email);
        hostedSessions == null ? this.hostedSessions = new ArrayList<String>(); : this.hostedSessions = hostedSessions; // One-liner if-else to make code more compact
    }

    // Method
    public ArrayList<String> getHostedSessions() {
        return hostedSessions;
    }

    public void setHostedSessions(ArrayList <String> hostedSessions){
        this.hostedSessions = hostedSessions;
    }

    public void addHostedSession(String session) {
        this.hostedSessions.add(session);
    }

    public void removeHostedSession(int index) {
        this.hostedSessions.remove(index);
    }
}
