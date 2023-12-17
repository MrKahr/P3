package com.proj.controller.navigation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalendarController{
    
    //The calendar we should use.
    @GetMapping("/calendar")
    public String showCalendar(){
    return "usercalendar";
    }
}
