package com.proj.model.users;

import java.time.LocalDate;
import java.util.ArrayList;

public class BasicUserInfo {
    //Field
    private String userName;
    private String password; //TODO: Remember to encrypt this
    private LocalDate registerDate;
    private LocalDate deletionDate;
    private Boolean hasPaid;        //Indicates whether this user has paid for membership or not.
    private Ban activeBan;          //If this field is not null, the user should be treated as banned.
    private ArrayList<Ban> expiredBans; //Old bans are put here so we can keep track of bad behavior.
    
    //Constructor
    public BasicUserInfo(String userName, String password){
        this.userName = userName;
        this.password = password;
        this.registerDate = LocalDate.now();
        this.expiredBans = new ArrayList<Ban>();
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

    public Ban getActiveBan(){
        return activeBan;
    }

    public ArrayList<Ban> getExpiredBans(){
        return expiredBans;
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

    public void setActiveBan(Ban ban){
        this.activeBan = ban;
    }

    public void addExpiredBan(Ban ban){
        expiredBans.add(ban);
    }
}
