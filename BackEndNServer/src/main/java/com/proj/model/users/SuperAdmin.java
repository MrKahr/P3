package com.proj.model.users;

/**
 * A class that represents a user with the access level "Admin". 
 */
public class SuperAdmin extends Role {
    // Method
    @Override
    public RoleType getRoleType(){
        return RoleType.SUPERADMIN;
    }

    @Override
    public RoleType[] getRoleDependencies(){
        RoleType[] types = {RoleType.ADMIN, RoleType.MEMBER};
        return types;
    }
}
