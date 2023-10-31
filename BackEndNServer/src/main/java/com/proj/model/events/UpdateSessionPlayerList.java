package com.proj.model.events;

public class UpdateSessionPlayerList {
    // Fields
    private String type;
    private String userName;

    // Getters

    public String getType() {
        return this.type;
    }

    public String getUserName() {
        return this.userName;
    }

    // Setters

    public void setType(String type) {
        this.type = type;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Constructor(s)
    
    public UpdateSessionPlayerList(String type, String userName) {
        this.type = type;
        this.userName = userName;
    }
}