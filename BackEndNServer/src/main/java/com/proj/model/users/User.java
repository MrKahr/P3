package com.proj.model.users;

import java.time.LocalDate;
// TODO consider whether we want constructor chaining or we want to use a factory design pattern
public abstract class User {
    // Field
    private String userName;
    private String password; //TODO: Remember to encrypt this
    private LocalDate registerDate;
    private LocalDate deletionDate; // We might want to store these as strings in the database and have a method to turn it into a date object
    // Password and email likely also go here

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

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setRegisterDate(LocalDate registerDate) {
        this.registerDate = registerDate;
    }

    public void setDeletionDate(LocalDate deletionDate) {
        this.deletionDate = deletionDate;
    }

}
