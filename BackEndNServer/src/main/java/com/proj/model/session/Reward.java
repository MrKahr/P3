package com.proj.model.session;

public class Reward {
    // Field 
    private String name;
    private Integer amount;
    private String description;

    // Constructor
    public Reward(String name, Integer amount, String description){
        this.name = name;
        this.amount = amount;
        this.description = description;
    }

    // Method
    public String getName() {
        return name;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
