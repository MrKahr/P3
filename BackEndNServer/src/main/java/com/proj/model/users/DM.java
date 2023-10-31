package com.proj.model.users;

import java.util.ArrayList;

public class DM extends Member {
    // Field
    private ArrayList<String> hostedSessions; // We might prefer another data structure

    // Constructor 
    public DM(){};

    // Method
    public ArrayList<String> getHostedSessions() {
        return hostedSessions;
    }
}
