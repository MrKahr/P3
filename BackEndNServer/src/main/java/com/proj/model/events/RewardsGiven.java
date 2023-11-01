package com.proj.model.events;

import java.time.LocalDate;

public class RewardsGiven {
    // Field
    private LocalDate date;
    private String responsibleDM;

    // Constructor
    public RewardsGiven(LocalDate date, String responsibleDM) {
        this.date = date;
        this.responsibleDM = responsibleDM;
    }

    // Method -  No setDate eince we model instantenous events as object instanstiations.  
    public LocalDate getDate() {
        return this.date;
    }

    public String getResponsibleDm() {
        return this.responsibleDM;
    }

    public void setResponsibleDM(String responsibleDM) {
        this.responsibleDM = responsibleDM;
    }
}