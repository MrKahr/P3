package com.proj.model.users;

import java.util.ArrayList;

public class Admin extends DM {
    // Field
    private ArrayList<String> listOfRoleChanges;
    private ArrayList<String> listOfBans;

    // Constructor
    public Admin(String userName, String password, String realName, String phoneNumber, String postalCode, String address, String email, ArrayList<String> hostedSessions){
        super(userName, password, realName, phoneNumber, postalCode, address, email, hostedSessions);
        this.listOfRoleChanges = new ArrayList<String>();
        this.listOfBans = new ArrayList<String>();
    };

   // Method 
   // TODO add method for adding object to array list 
    public ArrayList<String> getListOfRoleChanges() {
        return listOfRoleChanges;
    }

    public ArrayList<String> getListOfBans() {
        return listOfBans;
    }
}
