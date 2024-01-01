package com.proj.controller.navigation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EventController{
    // event list page
    @GetMapping("/eventlistpage")
    public String showEventListPage(){
        return "myEvents/eventlistpage";
    }
    // event create page
    @GetMapping("/eventcreatepage")
    public String showEventCreatePage(){
        return "myEvents/eventcreatepage";
    }
    // event edit page
    @GetMapping("/eventeditpage/{eventID}")
    public String showEventEditPage(@PathVariable int eventID){
        return "myEvents/eventeditpage";
    }
    // // event page
    @GetMapping(path = "/eventpage/{eventID}")
    public String showEventPage(@PathVariable int eventID){
        System.out.println(eventID);
        return "myEvents/eventpage";
    }

}
