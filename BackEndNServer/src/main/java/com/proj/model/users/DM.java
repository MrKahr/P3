package com.proj.model.users;

import java.util.ArrayList;

/**
 * A class that represents a user with the access level "Member". 
 */
public class DM extends Member {
    // Field
    private ArrayList<String> hostedSessions; // We might prefer another data structure

    // Constructor 
    public DM(String userName, String password, String realName, String phoneNumber, String postalCode, String address, String email, ArrayList<String> hostedSessions){
        if(hostedSessions == null) {
            this.hostedSessions = new ArrayList<String>();
        }
        super(userName, password, realName, phoneNumber, postalCode, address, email);
        
    };

    // Method
    public ArrayList<String> getHostedSessions() {
        return hostedSessions;
    }

    public void setHosttedSessions(ArrayList <String> hostedSessions){
        this.hostedSessions = hostedSessions;
    }

     public static DM from(Member m) {
        DM dm = (DM) m;
        dm.setHostedSessions;
        return new DM(firstName, lastName);
 }
}
