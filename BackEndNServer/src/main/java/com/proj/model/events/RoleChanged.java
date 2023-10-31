package com.proj.model.events;

import java.time.LocalDate;

public class RoleChanged {
    // Fields
    private LocalDate date;
    private String newRole;

    // Getters

    public LocalDate getDate() {
        return this.date;
    }

    public String getNewRole() {
        return this.newRole;
    }

    // Setters

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setNewRole(String newRole) {
        this.newRole = newRole;
    }

    // Constructor(s)
    public RoleChanged(LocalDate date, String newRole) {
        this.date = date;
        this.newRole = newRole;
    }
}