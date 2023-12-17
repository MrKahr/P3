package com.proj.model.users;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class BasicUserInfo {
    //Field
    private String username;
    private String password;

    // Tells Hibernate's jackson instance to parse the date using the class provided in the jackson package 'jsr310'.
    // Should be enabled globally. See: https://stackoverflow.com/questions/37492249/how-to-configure-jackson-with-spring-globally
    // Docs: https://github.com/FasterXML/jackson-modules-java8/tree/2.14/datetime
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime registerDate;
   
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime deactivationDate;
   
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime deletionDate;
   

    private Ban activeBan;          //If this field is not null, the user should be treated as banned.
    private ArrayList<Ban> expiredBans; //Old bans are put here so we can keep track of bad behavior.
    
    //Constructor
    public BasicUserInfo(){}; // Required by jackson to deserialize object
    
    public BasicUserInfo(String userName, String password){
        this.username = userName;
        this.password = password;
        this.registerDate = LocalDateTime.now();
        this.expiredBans = new ArrayList<Ban>();
    }


    //Method
    public String getUserName() {
        return username;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public LocalDateTime getDeactivationDate() {
        return deactivationDate;
    }

    public LocalDateTime getDeletionDate() {
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
        this.username = userName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setDeactivationDate(LocalDateTime deactivationDate) {
        this.deactivationDate = deactivationDate;
    }

    public void setDeletionDate(LocalDateTime deletionDate) {
        this.deletionDate = deletionDate;
    }

    public void setActiveBan(Ban ban){
        this.activeBan = ban;
    }

    public void addExpiredBan(Ban ban){
        expiredBans.add(ban);
    }
}
