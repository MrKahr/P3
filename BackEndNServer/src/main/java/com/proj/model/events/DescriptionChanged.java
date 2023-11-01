package com.proj.model.events;

public class DescriptionChanged {
    private String newDescription;

    DescriptionChanged(String newDescription){
        this.newDescription = newDescription;
    }

    public String getNewDescription() {
        return newDescription;
    }

    public void setNewDescription(String newDescription) {
        this.newDescription = newDescription;
    }
}
