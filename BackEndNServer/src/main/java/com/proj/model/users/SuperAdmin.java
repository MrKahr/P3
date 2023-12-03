package com.proj.model.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A class that represents a user with the access level "Admin". 
 */
@JsonIgnoreProperties(ignoreUnknown = true)

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
