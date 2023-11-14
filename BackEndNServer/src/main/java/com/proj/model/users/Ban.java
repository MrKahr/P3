package com.proj.model.users;

import java.time.LocalDate;
import java.time.Duration;

public class Ban {
    //Field
    private LocalDate startDate;
    private LocalDate endDate;   //If this field is null, the user will be banned forever!
    private String banReason;

    //Constructor
    public Ban(String reason, LocalDate endDate){
        this.startDate = LocalDate.now();
        this.endDate = endDate;
        this.banReason = reason;
    }

    public Ban(String reason, Duration banTime){
        this.startDate = LocalDate.now();
        this.endDate = startDate.plus(banTime);
        this.banReason = reason;
    }

    //Method
    public LocalDate getStartDate(){
        return startDate;
    }

    public LocalDate getEndDate(){
        return endDate;
    }

    public String getReason(){
        return banReason;
    }

    public void setEndDate(LocalDate newEndDate){
        this.endDate = newEndDate;
    }

    public void setReason(String reason){
        this.banReason = reason;
    }
}
