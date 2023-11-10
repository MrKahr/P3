package com.proj.model.users;

/** 
 * A class that represents a user with the access level "Guest". 
 */
public class Guest extends Role {
    // Field
    private String characterInfo;

    // Constructor 

    // Method 
    
    // Maybe characterInfo attribute could be replaced with "String description" to hold a text box displayed on the user's profile
    public String getCharacterInfo() {
        return characterInfo;
    }

    public void setCharacterInfo(String characterInfo) {
        this.characterInfo = characterInfo;
    }

}
