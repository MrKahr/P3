package com.proj.model.events;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.proj.model.users.Role;
import com.proj.model.users.RoleType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * A class that represents a request for a membership. Should be assigned to a user or deleted at some point.
 * Only one is stored in the database per user.
 */

@Entity
public class RoleRequest extends Request{
    //Field
    @JdbcTypeCode(SqlTypes.JSON)
    private Role roleObject;    //the information that the user has entered
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private int userId;

    //Constructor
    public RoleRequest(int userId, Role roleObject){
        this.userId = userId;
        this.roleObject = roleObject;
    }

    //Method
    public Role getRoleInfo(){
        return roleObject;
    };

    /**
     * @return The type of the requested role.
     */
    public RoleType getRoleType(){
        return roleObject.getRoleType();
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.ROLE;
    }

    public int getUserId(){
        return this.userId;
    }

    public int getRequestId(){
        return this.id;
    }
}