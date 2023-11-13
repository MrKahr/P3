package com.proj.model.users;
import java.util.ArrayList;

/**
 * A class that represents a user with the access level "Admin". 
 */
public class SuperAdmin extends Role {
    // Method
    public RoleType getRoleType(){
        return RoleType.SUPERADMIN;
    }

    public RoleType[] getRoleDependencies(){
        RoleType[] types = {RoleType.ADMIN, RoleType.MEMBER};
        return types;
    }
}
