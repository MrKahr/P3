package com.proj.model.events;
public class ModuleSet {
    // Field 
    private String changedFrom;
    private String changedTo;

    // Constructor 
    public ModuleSet(String changedFrom, String changedTo){
        this.changedFrom = changedFrom;
        this.changedTo = changedTo;
    }

    // Method - No setters since we model instantenous events as object instanstiations.  
    public String getChangedFrom(){
        return changedFrom; 
    }

    public String getChangedTo(){
        return changedTo;
    }

}
