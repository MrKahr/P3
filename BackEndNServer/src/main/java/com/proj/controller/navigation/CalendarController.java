package com.proj.controller.navigation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalendarController{
    
    // The calendar we should use.
    // @GetMapping("/calendar")
    // public String showCalendar(){
    // return "calendar";
    // }

    // public calendar
    @GetMapping("/publiccalendar")
    public String showPublicCalendar(){
    return "publiccalendar";
    }
    // user calendar
    @GetMapping("/usercalendar")
    public String showUserCalendar(){
    return "usercalendar";
    }
}
