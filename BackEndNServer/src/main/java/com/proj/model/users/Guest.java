package com.proj.model.users;

/** 
 * A class that represents a user with the access level "Guest". 
 */
public class Guest extends User {
    // Field
    private String characterInfo;

    // Constructor 
    /** Creates a guest with a username and a password.
    * @param userName it's the user's name! What did you think it was, dumbass? Did you really need a docString to tell you that "userName" is the NAME of the USER? Holy shit, are you actually so dense that you couldn't infer that from the parameter's name alone? Maybe I should tell oy
    * @param password this is a string, but it should ideally be hashed some way
    * @author Frederik, Mads, Emil
    */
    public Guest(String userName, String password){
        this.setUserName(userName);
        this.setPassword(password);// TODO: Do something with passwords and encrypt them
    };

    // Method 
    // Maybe characterInfo attribute could be replaced with "String description" to hold a text box displayed on the user's profile
    public String getCharacterInfo() {
        return characterInfo;
    }

    public void setCharacterInfo(String characterInfo) {
        this.characterInfo = characterInfo;
    }

}
