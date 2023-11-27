package com.proj.model.users;

import org.springframework.beans.factory.annotation.Autowired;

import com.proj.function.RoleAssigner;
import com.proj.repositoryhandler.UserdbHandler;
/**
 * A class that represents a request for a membership. Should be assigned to a user or deleted at some point.
 * Only one is stored in the database per user.
 */
public class RoleRequest {
    @Autowired
    private UserdbHandler userdbHandler;
    //Field
    private int requestingId;       //the ID of the requesting user
    private Role roleObject;    //the information that user has entered

    //Constructor
    public RoleRequest(int requestingId, Role roleObject){
        this.requestingId = requestingId;
        this.roleObject = roleObject;
    }

    //Method
    public int getId(){
        return requestingId;
    }

    public Role getRoleInfo(){
        return roleObject;
    };

    /**
     * @return The type of the requested role.
     */
    public RoleType getRequestType(){
        return roleObject.getRoleType();
    }

    /**
     * Fulfills the membership request by getting the requesting user from the database and assigning to role
     */
    public void fulfillRequest(){
        User user = userdbHandler.findById(this.requestingId);
        RoleAssigner.setRole(user, roleObject);
        userdbHandler.save(user);
    }
}
