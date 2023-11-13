package com.proj.model.users;

/**
 * This class exists to provide a common superclass for all role classes.
 * New role classes should extend this class.
*/

public abstract class Role {
    //Method
    public abstract RoleType getRoleType();

    public abstract RoleType[] getRoleDependencies();
}
