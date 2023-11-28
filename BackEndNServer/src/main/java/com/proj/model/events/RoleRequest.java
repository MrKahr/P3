package com.proj.model.events;

import org.springframework.beans.factory.annotation.Autowired;

import com.proj.function.RoleAssigner;
import com.proj.model.users.Role;
import com.proj.model.users.RoleType;
import com.proj.model.users.User;
import com.proj.repositoryhandler.RequestdbHandler;
/**
 * A class that represents a request for a membership. Should be assigned to a user or deleted at some point.
 * Only one is stored in the database per user.
 */
public class RoleRequest extends Request{
    @Autowired
    private RequestdbHandler requestdbHandler;
    //Field
    private Role roleObject;    //the information that user has entered

    //Constructor
    public RoleRequest(int requestingId, Role roleObject){
        this.requestingId = requestingId;
        this.roleObject = roleObject;
        this.requestType = RequestType.ROLE;
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
}