package com.proj.function;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import com.proj.model.session.PlaySession;

/**
 * CalendarManager is responsible for setting the 
 */


public class CalendarManager {

    @Autowired
    private CalendarRepository calendarRepository;

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

    public PlaySession validateSession(){
        return null;

    }

    public PlaySession sendSessionUpdate(){
        return null;
    }

    public PlaySession sendCalendar(){
        return null;
    }

    public PlaySession getSessions(){
        return null;
    }

    public PlaySession getSessionInfo(){
        return null;
    }

    public PlaySession setSessionState(){
        return null;
    }

    public PlaySession addToCalendar(){
        return null;
    }
}
