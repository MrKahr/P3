package com.proj.users;

import java.util.ArrayList;

public class DM extends Member {
    // Fields
    private ArrayList<String> hostedSessions; // We might prefer another data structure

    //Getters

    public ArrayList<String> getHostedSessions() {
        return hostedSessions;
    }
}
