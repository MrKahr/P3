package com.proj.model.users;

import java.time.LocalDate;

public abstract class User {
    // Field
    private String userName;
    private LocalDate registerDate;
    private LocalDate deletionDate; // We might want to store these as strings in the database and have a method to turn it into a date object
    // Password and email likely also go here

    // Constructor 
    public User(){};

    // Method
    public String getUserName() {
        return userName;
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }

    public LocalDate getDeletionDate() {
        return deletionDate;
    }  

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRegisterDate(LocalDate registerDate) {
        this.registerDate = registerDate;
    }

    public void setDeletionDate(LocalDate deletionDate) {
        this.deletionDate = deletionDate;
    }

}
