package com.proj.model.users;

public class Guest extends User {
    // Field
    private String characterInfo;

    // Constructor 
    public Guest(){};

    // Method 
    // Maybe characterInfo attribute could be replaced with "String description" to hold a text box displayed on the user's profile
    public String getCharacterInfo() {
        return characterInfo;
    }

    public void setCharacterInfo(String characterInfo) {
        this.characterInfo = characterInfo;
    }

}
