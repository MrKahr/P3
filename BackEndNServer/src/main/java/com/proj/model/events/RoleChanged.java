package com.proj.model.events;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import com.proj.model.users.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleChanged {
    // Field
    //Could need a field for the user's ID here.
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime date;
    private User affectedUser;
    private Role newRoleObject;
    private Role previousRoleObject;

    // Constructor
    /**
     * Creates an object of class RoleChanged and sets its date-attribute using localdate.now().
     * @param user The user affected by the role change.
     * @param newRoleObject The role that was added to the user.
     * @param previousRoleObject The role that was replaced by newRoleObject. Should be null if no role object was present before.
     */
    public RoleChanged(User user, Role newRoleObject, Role previousRoleObject) {
        this.date = LocalDateTime.now();
        this.affectedUser = user;
        this.newRoleObject = newRoleObject;
        this.previousRoleObject = previousRoleObject;
    }

    public RoleChanged(){}

    // Method -  No setters since we model instantenous events as object instanstiations.
    public LocalDateTime getDate() {
        return this.date;
    }

    public Role getNewRole() {
        return this.newRoleObject;
    }

    public Role getPreviousRole() {
        return this.previousRoleObject;
    }
}