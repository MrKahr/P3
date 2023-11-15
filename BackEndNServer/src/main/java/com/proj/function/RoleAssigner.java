
package com.proj.function;
import com.proj.model.events.RoleChanged;
import com.proj.model.users.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The class that is responsible for handling and logging the (re)assignment of user roles. 
 */

public class RoleAssigner {
    // Field

    // Constructor 
    
    //Method
    /**
     * Places the given role object in the given user object.
     * @param userObject The user who should get assigned the role.
     * @param newRoleObject The role to give the user. Extends the Role class and implements functions to provide its type.
     * @return A RoleChanged object that contains the affected user, the role object that was added, and, if present, the role object that was replaced.
     */
    public static RoleChanged setRole(User userObject, Role newRoleObject){

        Role previousRoleObject = null;
        
        switch (newRoleObject.getRoleType()) {
            case GUEST:
                if(userObject.getGuestInfo() != null){  //might not need this if-statement, but maybe we want to do something else when the role's spot is taken at a later date
                    previousRoleObject = userObject.getGuestInfo();
                }
                userObject.setGuestInfo((Guest)newRoleObject);
                break;
        
            case MEMBER:
                if(userObject.getMemberInfo() != null){
                    previousRoleObject = userObject.getMemberInfo();
                }
                userObject.setMemberInfo((Member)newRoleObject);
                break;

            case DM:
                if(userObject.getDmInfo() != null){
                    previousRoleObject = userObject.getDmInfo();
                }
                userObject.setDmInfo((DM)newRoleObject);
                break;

            case ADMIN:
                if(userObject.getAdminInfo() != null){
                    previousRoleObject = userObject.getAdminInfo();
                }
                userObject.setAdminInfo((Admin)newRoleObject);
                break;
            
            case SUPERADMIN:
                if(userObject.getSuperAdminInfo() != null){
                    previousRoleObject = userObject.getSuperAdminInfo();
                }
                userObject.setSuperAdminInfo((SuperAdmin)newRoleObject);
                break;

            default:
                throw new IllegalArgumentException("newRoleObject returns unknown role type");
        }
        return new RoleChanged(userObject, newRoleObject, previousRoleObject);  //Return an event object to put in the database
    }
}