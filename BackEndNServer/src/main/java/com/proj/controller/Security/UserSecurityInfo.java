package com.proj.controller.security;

/**
 * A special class to store user info necessary for authentication.
 */
public class UserSecurityInfo {
    // Field
    String username;
    String password;
    String role;

    // Constructor
    public UserSecurityInfo(String username, String password, String role){
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Provided in case this needs JSON serializing/deserializing
    public UserSecurityInfo(){}

    // Method
    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setRole(String role){
        this.role = role;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public String getRole(){
        return this.role;
    }

}
