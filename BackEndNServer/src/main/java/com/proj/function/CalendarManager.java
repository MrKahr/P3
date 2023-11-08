package com.proj.function;
import java.time.LocalDate;

public class CalendarManager {
    // Field
    private LocalDate currentDate; 

    // Constructor 
    public CalendarManager(LocalDate currentDate){
        this.currentDate = currentDate;    
    }

    // Method
    public void setCurrentDate(){
        this.currentDate = LocalDate.now();
    }
}
