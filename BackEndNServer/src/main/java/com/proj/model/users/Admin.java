package com.proj.model.users;

import java.util.ArrayList;

public class Admin extends DM {
    // Field
    private ArrayList<String> listOfRoleChanges;
    private ArrayList<String> listOfBans;

    // Constructor
    public Admin(){};

   // Method 
   // TODO add method for adding object to array list 
    public ArrayList<String> getListOfRoleChanges() {
        return listOfRoleChanges;
    }

    public ArrayList<String> getListOfBans() {
        return listOfBans;
    }
}
