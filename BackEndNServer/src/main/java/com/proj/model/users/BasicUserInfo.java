package com.proj.model.users;

import java.time.Duration;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class BasicUserInfo {
    //Field
    private String userName;
    private String password; //TODO: Remember to encrypt this

    // Tells Hibernate's jackson instance to parse the date using the class provided in the jackson package 'jsr310'.
    // Should be enabled globally. See: https://stackoverflow.com/questions/37492249/how-to-configure-jackson-with-spring-globally
    // Docs: https://github.com/FasterXML/jackson-modules-java8/tree/2.14/datetime
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime registerDate;
   
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime deletionDate; // We might want to store these as strings in the database and have a method to turn it into a date object
   
    private Duration banDuration = Duration.ZERO;   //If this field is greater than 0, the user is considered banned!
    private String banReason;                       //Accompanying the banDuration, this field holds the reason for a ban. It could be an ArrayList so we can track previous bans.
    private Boolean hasPaid;        //Indicates whether this user has paid for membership or not.
    
    //Constructor
    public BasicUserInfo(){}; // Required by jackson to deserialize object
    
    public BasicUserInfo(String userName, String password){
        this.userName = userName;
        this.password = password;
        this.registerDate = LocalDateTime.now();
    }


    //Method
    public String getUserName() {
        return userName;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public LocalDateTime getDeletionDate() {
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

    public void setDeletionDate(LocalDateTime deletionDate) {
        this.deletionDate = deletionDate;
    }

}
