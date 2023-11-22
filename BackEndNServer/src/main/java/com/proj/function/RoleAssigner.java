
package com.proj.function;
import com.proj.model.events.RoleChanged;
import com.proj.model.users.*;

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
     * @throws IllegalArgumentException Thrown if the user is missing the new role's dependencies or if the new role has an unreconized type.
     */
    public static RoleChanged setRole(User userObject, Role newRoleObject){
        if(!dependenciesFulfilled(userObject, newRoleObject)){
            throw new IllegalArgumentException("newRoleObject is missing dependencies!"); //throw an error if the role is missing its dependencies
        }

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
        RoleChanged event = new RoleChanged(userObject, newRoleObject, previousRoleObject);
        userObject.getRoleBackups().addToHistory(event);  //Add an event object to the user's rolechange history
        return event;
    }

    /**
     * Checks if a role can be added to a given user by checking if the role's dependencies are present on the user.
     * @param user The user to investigate.
     * @param role The role whose dependencies should be looked for.
     * @return True if all dependencies are present, false if a dependency is missing.
     */
    public static boolean dependenciesFulfilled(User user, Role role){
        RoleType[] dependencies = role.getRoleDependencies();
        int dependencyNeeded = dependencies.length;
        int dependenciesFound = 0;
        for (RoleType type : dependencies) {
            Role dependency = user.getRoleByType(type);
            if(dependency != null && dependency.isActive()){    //check if the role is present on the user and currently active
                dependenciesFound++;
            }
        }
        return dependencyNeeded == dependenciesFound;    //comparing the number of dependencies we need with the number of dependencies we found present
    }
}