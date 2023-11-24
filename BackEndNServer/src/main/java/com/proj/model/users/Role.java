package com.proj.model.users;

/**
 * This class exists to provide a common superclass for all role classes.
 * It also contains methods to get the activation state, type, and type dependencies of roles.
 * New role classes should extend this class.
*/

public abstract class Role {
    //Method
    public abstract RoleType getRoleType();

    public abstract RoleType[] getRoleDependencies();
}