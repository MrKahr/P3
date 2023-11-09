package com.proj.model.users;

/* jakarta persistence (JPA) is a application programming interface that provides specification to describe 
the handling of relational data in java. JPA provides a number of annotations for mapping of java objects to database*/
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Container for user because springboot needs a reference to a model 
 * //TODO: Consider using the template design pattern (Java design patterns s. 327)
 */
@Entity
public class Account {
    // Field
    /*
     * @GeneratedValue specifies how the primary key(A primary key is the column or
     * columns that contain values that uniquely identify each row in a table) value
     * is generated.
     * It can be used with options like `AUTO`, `IDENTITY`, `SEQUENCE`, or `TABLE`
     * and in use with @Id marks a field as the primary key of an entity.
     * generationtype.AUTO to find the best way to generate the data
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private User user; 

    public Account(User user){
        this.user = user;
    }

    public User getUser() {
        return user;
    }
    
}
