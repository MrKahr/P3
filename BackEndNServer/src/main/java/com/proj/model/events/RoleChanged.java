package com.proj.model.events;

import java.time.LocalDate;

import com.proj.model.users.User;

public class RoleChanged {
    // Field
    private LocalDate date;
    private String newRole;
    private User previousUserObject;

    // Constructor
    public RoleChanged(LocalDate date, String newRole) {
        this.date = date;
        this.newRole = newRole;
        this. previousUserObject = null;
    }

    public RoleChanged(LocalDate date, String newRole, User user) {
        this.date = date;
        this.newRole = newRole;
        this.previousUserObject = user;     //this constructor is used when we ban someone, so we can restore their account when the ban expires.
    }    

    // Method -  No setters eince we model instantenous events as object instanstiations.
    public LocalDate getDate() {
        return this.date;
    }

    public String getNewRole() {
        return this.newRole;
    }


}