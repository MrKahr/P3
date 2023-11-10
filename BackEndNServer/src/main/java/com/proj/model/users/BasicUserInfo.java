package com.proj.model.users;

import java.time.Duration;
import java.time.LocalDate;

public class BasicUserInfo {
    //Field
    private String userName;
    private String password; //TODO: Remember to encrypt this
    private LocalDate registerDate;
    private LocalDate deletionDate; // We might want to store these as strings in the database and have a method to turn it into a date object
    private Duration banDuration = Duration.ZERO;   //If this field is greater than 0, the user is considered banned!
    private String banReason;                       //Accompanying the banDuration, this field holds the reason for a ban. It could be an ArrayList so we can track previous bans.
    private Boolean hasPaid;        //Indicates whether this user has paid for membership or not.
    
    //Constructor
    public BasicUserInfo(String userName, String password){
        this.userName = userName;
        this.password = password;
        this.registerDate = LocalDate.now();
    }

    //Method
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

    public void setDeletionDate(LocalDate deletionDate) {
        this.deletionDate = deletionDate;
    }

}
