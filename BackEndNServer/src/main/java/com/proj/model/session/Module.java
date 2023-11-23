package com.proj.model.session;

import java.time.LocalDateTime;

/* jakarta persistence (JPA) is a application programming interface that provides specification to describe
 * the handling of relational data in java. JPA provides a number of annotations for mapping of java objects to database*/
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Description of an official DnD module to be used in a playsession
 * Be aware that java has a "Module" by default, which is different
 */
@Entity
public class Module {
    // Field
    /*
     * @GeneratedValue specifies how the primary key(A primary key is the column or
     * columns that contain values that uniquely identify each row in a table) value
     * is generated.
     * It can be used with options like `AUTO`, `IDENTITY`, `SEQUENCE`, or `TABLE`
     * and in use with @Id marks a field as the primary key of an entity.
     * In Entity model the primary keys is id, name, description and levelRange and
     * generationtype.AUTO to find the best way to generate the data
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private String levelRange; // Consider range start/range end
    private LocalDateTime addedDate;
    private LocalDateTime removedDate;

    // Constructor
    /**
     * 
     * @param name
     * @param description
     * @param levelRange
     */
    public Module(String name, String description, String levelRange) {
        this.name = name;
        this.description = description;
        this.levelRange = levelRange;
    }

    public Module() {}

    // Method
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLevelRange() {
        return levelRange;
    }

    public LocalDateTime getAddedDate() {
        return addedDate;
    }

    public LocalDateTime getRemovedDate() {
        return removedDate;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public void setAddedDate(LocalDateTime addedDate) {
        this.addedDate = addedDate;
    }

    public void setRemovedDate(LocalDateTime removedDate) {
        this.removedDate = removedDate;
    }

    @Override
    public String toString() {
        return "Name:\n" + getName()
                + "\n Description:\n" + getDescription()
                + "\n Level Range: \n" + getLevelRange();
    }

}