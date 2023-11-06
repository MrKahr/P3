package com.proj.model.session;

public class Module {
    // Field
    String name;
    String description;
    String levelRange; // Consider range start/range end

    // Constructor
    public Module(String name, String description, String levelRange) {
        this.name = name;
        this.description = description;
        this.levelRange = levelRange;
    };

    // Method
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLevelRange() {
        return levelRange;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLevelRange(String levelRange) {
        this.levelRange = levelRange;
    }

    @Override
    public String toString(){
        return this.name + "\n" + this.description + "\nlevels:" + this.levelRange;
    }

}