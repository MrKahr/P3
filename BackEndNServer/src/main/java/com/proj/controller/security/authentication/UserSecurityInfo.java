package com.proj.controller.security.authentication;

import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;

/**
 * A special class to store user info necessary for authentication.
 */
public class UserSecurityInfo {
    // Field
    String username;
    String password;
    HashSet<GrantedAuthority> authorities;

    // Constructor
    public UserSecurityInfo(String username, String password, HashSet<GrantedAuthority> authorities){
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    // Provided in case this needs JSON serializing/deserializing
    public UserSecurityInfo(){}

    // Method
    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public HashSet<GrantedAuthority> getAuthorities(){
        return this.authorities;
    }
}
