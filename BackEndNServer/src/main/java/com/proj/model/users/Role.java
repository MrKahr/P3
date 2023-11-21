package com.proj.model.users;

/**
 * This class exists to provide a common superclass for all role classes.
 * It also contains methods to get the activation state, type, and type dependencies of roles.
 * New role classes should extend this class.
*/

public abstract class Role {
    //Field
    private boolean isActive = true; //roles are active by default.
                                     //We want to deactivate them instead of deleting them so we don't lose information.
    //Method
    public abstract RoleType getRoleType();

    public abstract RoleType[] getRoleDependencies();

    public void setActivation(boolean state){
        this.isActive = state;
    };

    public boolean isActive(){
        return isActive;
    };
}
