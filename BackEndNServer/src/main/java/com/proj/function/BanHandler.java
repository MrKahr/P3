package com.proj.function;

import com.proj.model.users.*;
import java.time.LocalDate;

public class BanHandler {
    //Field

    //Constructor

    //Method

    /**
     * Checks if a user is banned by looking at their basicUserInfo object.
     * @param user The user to check.
     * @return True if the user is banned, false if not.
     */
    private static boolean isBanned(User user){
        return (user.getBasicUserInfo().getActiveBan() != null);
    }

    /**
     * Checks if a user's ban has expired and moves it to their list of expired bans if it has.
     * Uses information stored on the user's basicUserInfo object to do so.
     * @param user The user to check.
     * @return False if the ban has expired, otherwise true.
     */
    public static boolean updateBanStatus(User user){
        if(isBanned(user)){
            Ban activeBan = user.getBasicUserInfo().getActiveBan();
            LocalDate endDate = activeBan.getEndDate();
            if(LocalDate.now().isAfter(endDate)){  //check if the current date is after the ban's expiry date
                user.getBasicUserInfo().addExpiredBan(activeBan);
                user.getBasicUserInfo().setActiveBan(null);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
