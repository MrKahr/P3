package com.proj.model.events;

import java.time.LocalDateTime;

public class RewardsGiven {
    // Field
    private LocalDateTime date;
    private String responsibleDM;

    // Constructor
    public RewardsGiven(LocalDateTime date, String responsibleDM) {
        this.date = date;
        this.responsibleDM = responsibleDM;
    }

    public RewardsGiven(){}

    // Method -  No setDate eince we model instantenous events as object instanstiations.  
    public LocalDateTime getDate() {
        return this.date;
    }

    public String getResponsibleDm() {
        return this.responsibleDM;
    }

    public void setResponsibleDM(String responsibleDM) {
        this.responsibleDM = responsibleDM;
    }
}