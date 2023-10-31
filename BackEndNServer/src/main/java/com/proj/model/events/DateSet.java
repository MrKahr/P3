package com.proj.model.events;
import java.time.LocalDate;
public class DateSet {
    // Field
    private LocalDate date;

    // Constructor
    DateSet(LocalDate date){
        this.date = date;
    }

    // Method - No setters since we model instantenous events as object instanstiations.  
    public LocalDate getDate() {
        return date;
    }

}
