package com.proj.model.events;

public class DescriptionChanged {
    private String newDescription;

    public DescriptionChanged(String newDescription){
        this.newDescription = newDescription;
    }

    public DescriptionChanged(){}

    public String getNewDescription() {
        return newDescription;
    }

    public void setNewDescription(String newDescription) {
        this.newDescription = newDescription;
    }
}
