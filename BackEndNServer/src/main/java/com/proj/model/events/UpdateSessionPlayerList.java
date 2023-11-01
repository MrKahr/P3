package com.proj.model.events;

public class UpdateSessionPlayerList {
    // Fields
    private String type;
    private String userName;

    // Constructor
    public UpdateSessionPlayerList(String type, String userName) {
        this.type = type;
        this.userName = userName;
    }

    // Method -  No setters eince we model instantenous events as object instanstiations.
    public String getType() {
        return this.type;
    }

    public String getUserName() {
        return this.userName;
    }
}