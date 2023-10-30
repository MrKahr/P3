package com.proj.users;

public class Guest extends User {
    private String characterInfo;
    // Maybe characterInfo attribute could be replaced with "String description" to hold a text box displayed on the user's profile

    public String getCharacterInfo() {
        return characterInfo;
    }

    public void setCharacterInfo(String characterInfo) {
        this.characterInfo = characterInfo;
    }

}
