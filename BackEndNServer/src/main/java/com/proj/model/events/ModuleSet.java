package com.proj.model.events;

public class ModuleSet {
    // Field 
    private String changedFrom;
    private String changedTo;

    // Constructor 
    ModuleSet(String changedFrom, String changedTo){
        this.changedFrom = changedFrom;
        this.changedTo = changedTo;
    }

    // Method
    public String getChangedFrom(){
        return changedFrom; 
    }

    public String getChangedTo(){
        return changedTo;
    }


    public void setChangedFrom(String changedFrom) {
        this.changedFrom = changedFrom;
    }

    public void setChangedTo(String changedTo){
        this.changedTo = changedTo;
    }

}
