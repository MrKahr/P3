package com.proj.model.events;
import java.time.LocalDate;

public class DateSet {
    // Field
    private LocalDate date;

    // Construtctor 
    DateSet(LocalDate date){
        this.date = date;
    }

    // Method
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
