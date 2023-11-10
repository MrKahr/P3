
package com.proj.function;

import com.proj.model.users.*;
//import com.proj.model.users.Member; //need to import this as long as java.lang.reflect.* is here, since it also defines a Member class.

import java.time.Duration;

import com.proj.model.events.RoleChanged;

import java.util.ArrayList;
//import java.lang.reflect.*;

/**
 * The class that is responsible for handling the reassignment of user roles. 
 * @param T the type of user to assign a new role to.
 */

public class RoleAssigner<T> {
    //Method

    public Guest ToGuest(T user){
        if (user instanceof Guest) {
            return (Guest) user;
        } else {                    //if the given user type is not Guest or anywhere in the -> Member -> DM -> Admin -> SuperAdmin chain, create a new representation and give that to the user
            Guest replacementAccount = new Guest(null, null);
            
            //call something like "restoreAccount" to get all the information back

            return replacementAccount;
            }
    }

    public Member ToMember(T user){
        if(!(user instanceof Guest)){
            //throw exception to say you need a guest or higher to get a member;
        } else {
            //check if all necessary information is defined and tell the user to get it for us if not
            //getDeclaredFields or something similar might be useful
            Member memberUser = (Member) user;

            if(  //this if-statement is horrible and I hate it but I can't find any better way to do this.
            memberUser.getRealName() == null ||
            memberUser.getPhoneNumber() == null ||
            memberUser.getPostalCode() == null ||
            memberUser.getAddress() == null ||
            memberUser.getEmail() == null
            ){
                //request info to put into the empty fields
            }
            return (Member) user;
        }
        return null;
    }


    public DM ToDM(T user) {
        if(!(user instanceof Guest)){
            //throw exception to say you need a guest or higher to get a member;
        } else {
            DM dm = (DM) user;
            if(dm.getHostedSessions() == null){
                dm.setHostedSessions(new ArrayList<String>());
            }
            return dm;
        }
        return null;
    }

    public Admin ToAdmin(T user){
        if(!(user instanceof Guest)){
            //throw exception to say you need a guest or higher to get a member;
        } else {
            Admin admin = (Admin) user;
            if(admin.getListOfRoleChanges() == null || admin.getListOfBans() == null){
                //throw something?
            }
            return admin;
        }
        return null;
    }

    public SuperAdmin ToSuperAdmin(T user){
        if(!(user instanceof Guest)){
            //throw exception to say you need a guest or higher to get a member;
        } else {
            SuperAdmin superAdmin = (SuperAdmin) user;
            return superAdmin;
        }
        return null;
    }
}