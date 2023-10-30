package com.proj.users;

import java.util.ArrayList;

public class Admin extends DM {
    // Fields
    private ArrayList<String> listOfRoleChanges;
    private ArrayList<String> listOfBans;

    // Getters

    public ArrayList<String> getListOfRoleChanges() {
        return listOfRoleChanges;
    }

    public ArrayList<String> getListOfBans() {
        return listOfBans;
    }
}
