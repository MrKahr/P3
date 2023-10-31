package com.proj.model.session;
import java.time.LocalDateTime; 

public class playSession {
    // Field 
    private String title;
    private Integer maxNumberOfPlayers;
    private Integer currentNumberOfPlayers;
    private LocalDateTime date;
    private String state; 
    
    // Constructor 
    playSession(String title, Integer currentNumberOfPlayers, LocalDateTime date, String state, Integer maxNumberOfPlayers){
        this.title = title;
        this.currentNumberOfPlayers = currentNumberOfPlayers;
        this.date = date;
        this.state = state;
        this.maxNumberOfPlayers = maxNumberOfPlayers;
    }
    // Method

    public String getTitle() {
        return title;
    }

    public Integer getCurrentNumberOfPlayers() {
        return currentNumberOfPlayers;
    }

    public Integer getMaxNumberOfPlayers() {
        return maxNumberOfPlayers;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getState() {
        return state;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMaxNumberOfPlayers(Integer maxNumberOfPlayers) {
        this.maxNumberOfPlayers = maxNumberOfPlayers;
    }

    public void setCurrentNumberOfPlayers(Integer currentNumberOfPlayers) {
        this.currentNumberOfPlayers = currentNumberOfPlayers;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
