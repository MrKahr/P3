package com.proj.model.users;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A class that represents a user with the access level "DM". 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DM extends Role {
    // Field
    private ArrayList<String> hostedSessions; // We might prefer another data structure

    // Constructor
    /**
     * Creates a DM object with supplied parameters. Note that hostedSessions is optional (must be null then).
     * @Pre-con: Expects all arguments to be sanitized.
     * @param hostedSessions The ArrayList containing all sessions the user has hosted. This param is optional, and if not needed must be null
     */
    public DM(ArrayList<String> hostedSessions){
        this.hostedSessions = hostedSessions;
    }

    public DM(){
        this.hostedSessions = new ArrayList<String>();
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

    @Override
    public RoleType getRoleType(){
        return RoleType.DM;
    }

    @Override
    public RoleType[] getRoleDependencies(){
        RoleType[] types = {RoleType.MEMBER};
        return types;
    }
}
