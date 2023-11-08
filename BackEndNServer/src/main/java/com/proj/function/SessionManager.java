package com.proj.function;
import java.time.LocalDate;

public class SessionManager {
    // Field
    private LocalDate currentDate;

    // Constructor
    public SessionManager(LocalDate currentDate){
        this.currentDate = currentDate;
    }

    // Method
    public void setCurrentDate(){
        this.currentDate = LocalDate.now();
    }
}
