package com.proj.model.users;

import java.time.LocalDateTime;
import java.time.Duration;

public class Ban {
    //Field
    private LocalDateTime startDate;
    private LocalDateTime endDate;   //If this field is null, the user will be banned forever!
    private String banReason;

    //Constructor
    public Ban(String reason, LocalDateTime endDate){
        this.startDate = LocalDateTime.now();
        this.endDate = endDate;
        this.banReason = reason;
    }

    public Ban(String reason, Duration banTime){
        this.startDate = LocalDateTime.now();
        this.endDate = startDate.plus(banTime);
        this.banReason = reason;
    }

    public Ban(String reason){
        this.startDate = LocalDateTime.now();
        this.banReason = reason;
    }

    //Method
    public LocalDateTime getStartDate(){
        return startDate;
    }

    public LocalDateTime getEndDate(){
        return endDate;
    }

    public String getReason(){
        return banReason;
    }

    public void setEndDate(LocalDateTime newEndDate){
        this.endDate = newEndDate;
    }

    public void setReason(String reason){
        this.banReason = reason;
    }
}
