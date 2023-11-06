package com.proj.model.session;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Module {

    // Field
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    private String description;
    private String levelRange; // Consider range start/range end

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

}