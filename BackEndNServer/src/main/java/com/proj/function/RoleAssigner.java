package com.proj.function;

import com.proj.model.users.*;
import java.util.ArrayList;

import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;

/**
 * The class that is responsible for handling the reassignment of user roles. 
 * @param T the type of user to assign a new role to.
 */

public class RoleAssigner<T> {
    //Method

    public Guest ToGuest(T user){
        if (user instanceof Guest) {
            return (Guest) user;
        } else {                    //if the given user type is not Guest or anywhere in the -> Member -> DM -> Admin -> SuperAdmin chain, create a new representation a give that to the user
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
        }

        return (Member) user;
    }


    public DM ToDM(Member member) {
        DM dm = (DM) member;
        dm.setHostedSessions(new ArrayList<String>());
        return dm;
    }

    public DM ToDM(Admin admin) {
        return (DM) admin;
    }

    public DM ToDM(SuperAdmin superAdmin) {
        return (DM) superAdmin;
    }
}

