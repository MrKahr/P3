package com.proj.controller.navigation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
    @GetMapping("/eventeditpage")
    public String showEventEditPage(){
        return "myEvents/eventeditpage";
    }
    // event page
    @GetMapping("/eventpage")
    public String showEventPage(){
        return "myEvents/eventpage";
    }
    // public event page
    @GetMapping("/publiceventpage")
    public String showPublicEventPage(){
        return "myEvents/publiceventpage";
    }

}
