package com.proj.model.users;
import java.util.ArrayList;

public class SuperAdmin extends Admin {
    // Field 

    // Constructor
    public SuperAdmin(String userName, String password, String realName, String phoneNumber, String postalCode, String address, String email, ArrayList<String> hostedSessions, ArrayList<String> listOfRoleChanges, ArrayList<String> listOfBans){
        super(userName, password, realName, phoneNumber, postalCode, address, email, hostedSessions, listOfRoleChanges, listOfBans)
    };
    
    // Method
}
