package com.proj.model.users;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * This class exists to provide a common superclass for all role classes.
 * It also contains methods to get the activation state, type, and type dependencies of roles.
 * New role classes should extend this class.
*/
@JsonTypeInfo(  //this part makes it possible to deserialize Role's subclasses properly
    use = JsonTypeInfo.Id.MINIMAL_CLASS,
    include = JsonTypeInfo.As.PROPERTY,
    property = "@class")
public abstract class Role {
    //Method
    public abstract RoleType getRoleType();

    public abstract RoleType[] getRoleDependencies();
}