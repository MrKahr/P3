package com.proj.function;

import com.proj.model.users.*;
import java.util.ArrayList;

/**
 * The class that is responsible for handling the reassignment of user roles. 
 */

public class RoleAssigner {
    
    /** Changes the given user's role to DM, while preserving as much information as possible.
    * @param 
    */

    public static DM DMfrom(Member member) {
        DM dm = (DM) member;
        dm.setHostedSessions(new ArrayList<String>());
        return dm;
    }

        public static DM DMfrom(Admin admin) {
        return (DM) admin;
    }

        public static DM DMfrom(SuperAdmin superAdmin) {
        return (DM) superAdmin;
    }
}

