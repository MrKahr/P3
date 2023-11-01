package com.proj.model.events;

import java.time.LocalDate;

public class RoleChanged {
    // Field
    private LocalDate date;
    private String newRole;

    // Constructor
    public RoleChanged(LocalDate date, String newRole) {
        this.date = date;
        this.newRole = newRole;
    }

    // Method -  No setters eince we model instantenous events as object instanstiations.
    public LocalDate getDate() {
        return this.date;
    }

    public String getNewRole() {
        return this.newRole;
    }


}