package com.proj.model.events;

import java.time.LocalDate;

public class RewardsGiven {
    // Fields
    private LocalDate date;
    private String responsibleDM;

    // Getters

    public LocalDate getDate() {
        return this.date;
    }

    public String getResponsibleDm() {
        return this.responsibleDM;
    }

    // Setters

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setResponsibleDM(String responsibleDM) {
        this.responsibleDM = responsibleDM;
    }

    // Constructor(s)

    public RewardsGiven(LocalDate date, String responsibleDM) {
        this.date = date;
        this.responsibleDM = responsibleDM;
    }
}