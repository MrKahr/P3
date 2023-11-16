package com.proj.function;

import com.proj.model.users.*;

import java.time.Duration;

import java.time.LocalDateTime;

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
     * @param user The user whose ban should be updated.
     */
    public static void updateBanStatus(User user){
        if(isBanned(user) && user.getBasicUserInfo().getActiveBan().getEndDate() != null){  //proceed only if the user is banned and the ban has a defined expiry date
            BasicUserInfo userInfo = user.getBasicUserInfo();
            Ban activeBan = userInfo.getActiveBan();
            LocalDateTime endDate = activeBan.getEndDate();
            if(LocalDateTime.now().isAfter(endDate)){  //check if the current date is after the ban's expiry date
                userInfo.addExpiredBan(activeBan);
                userInfo.setActiveBan(null);
            }
        }
    }

    /**
     * Combines the functionality of updateBanStatus and isBanned, 
     * moving the user's active ban to their list of expired bans if it has expired
     * and returning a boolean to convey the user's current ban status.
     * @param user The user whose ban should be checked and updated.
     * @return False if the user's ban has expired, true if not.
     */
    public static boolean updateAndCheckBanStatus(User user){
        updateBanStatus(user);
        return isBanned(user);
    }

    /**
     * Bans a user until a specified date or updates an existing ban to last until the given date.
     * @param user The user to ban or to update the ban of.
     * @param reason The reason for the ban.
     * @param endDate The date where the ban should end. Must be sometime in the future.
     * @throws IllegalArgumentException Thrown if the given endDate is in the past.
     */
    public static void banUser(User user, String reason, LocalDateTime endDate){
        if(endDate.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Expiry date cannot be in the past!");
        }
        if(isBanned(user)){
            Ban ban = user.getBasicUserInfo().getActiveBan();   //if the user is already banned, we want to update the reason and expiry date
            ban.setReason(reason);
            ban.setEndDate(endDate);    //doing it like this makes it possible to shorten someone's ban as well
        } else {
            Ban ban = new Ban(reason, endDate);                 //if the user is not banned, we want to add a new active ban
            user.getBasicUserInfo().setActiveBan(ban);
        }
    }

    /**
     * Bans a user for a specified duration or extends an existing ban by the given duration.
     * @param user The user to ban or to update the ban of.
     * @param reason The reason for the ban.
     * @param duration The time to ban the user for or to extend their active ban by.
     */
    public static void banUser(User user, String reason, Duration duration){
        if(isBanned(user)){
            Ban ban = user.getBasicUserInfo().getActiveBan();   //if the user is already banned, we want to update the reason and extend the expiry date
            ban.setReason(reason);
            if(ban.getEndDate() != null){
                LocalDateTime endDate = ban.getEndDate().plus(duration);    //if an endDate is defined, add the duration
                ban.setEndDate(endDate);
            } else {
                LocalDateTime endDate = LocalDateTime.now().plus(duration); //if an endDate is not defined, set it to now + the new duration
                ban.setEndDate(endDate);
            }
        } else {
            Ban ban = new Ban(reason, duration);                //if the user is not banned, we want to add a new active ban
            user.getBasicUserInfo().setActiveBan(ban);
        }
    }

    /**
     * Bans a user or extends an existing ban indefinitely.
     * The ban can still be updated to add an expiry date and can be ended manually.
     * @param user The user to ban or to update the ban of.
     * @param reason The reason for the ban.
     */
    public static void banUser(User user, String reason){
        if(isBanned(user)){
            Ban ban = user.getBasicUserInfo().getActiveBan();   //if the user is already banned, we want to update the reason and expiry date
            ban.setReason(reason);
            ban.setEndDate(null);
        } else {
            Ban ban = new Ban(reason);                          //if the user is not banned, we want to add a new active ban
            user.getBasicUserInfo().setActiveBan(ban);
        }
    }

    /**
     * Bans a user for a specified duration or extends an existing ban by the given duration.
     * @param user The user to ban or to update the ban of.
     * @param endDate The date where the ban should end. Must be sometime in the future.
     * @throws IllegalArgumentException Thrown if the given endDate is in the past.
     */
    public static void banUser(User user, LocalDateTime endDate){
        if(endDate.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Expiry date cannot be in the past!");
        }
        if(isBanned(user)){
            Ban ban = user.getBasicUserInfo().getActiveBan();
            ban.setEndDate(endDate);
        } else {
            Ban ban = new Ban("", endDate);             //no reason is given, so we construct a ban with an empty string
            user.getBasicUserInfo().setActiveBan(ban);
        }
    }

    /**
     * Bans a user for a specified duration or extends an existing ban by the given duration.
     * @param user The user to ban or to update the ban of.
     * @param duration The time to ban the user for or to extend their active ban by.
     */
    public static void banUser(User user, Duration duration){
        if(isBanned(user)){
            Ban ban = user.getBasicUserInfo().getActiveBan();
            if(ban.getReason() != null){
                LocalDateTime endDate = ban.getEndDate().plus(duration);
                ban.setEndDate(endDate);
            } else {
                LocalDateTime endDate = LocalDateTime.now().plus(duration);
                ban.setEndDate(endDate);
            }
        } else {
            Ban ban = new Ban("", duration);
            user.getBasicUserInfo().setActiveBan(ban);
        }
    }

    /**
     * Bans a user or extends an existing ban indefinitely.
     * The ban can still be updated to add an expiry date and can be ended manually.
     * @param user The user to ban or to update the ban of.
     * @param reason The reason for the ban.
     */
    public static void banUser(User user){
        if(isBanned(user)){
            Ban ban = user.getBasicUserInfo().getActiveBan();   //if the user is already banned, we want to update the reason and expiry date
            ban.setEndDate(null);
        } else {
            Ban ban = new Ban("");                       //if the user is not banned, we want to add a new active ban
            user.getBasicUserInfo().setActiveBan(ban);
        }
    }

    /**
     * Instantly ends the active ban of the given user and sets the ban's ending date to the moment this method was called.
     * @param user The user to unban.
     * @throws IllegalArgumentException Thrown if the given user has no active ban.
     */
    public static void unbanUser(User user){
        if(isBanned(user)){ //we can only unban someone if they're banned
            BasicUserInfo userInfo = user.getBasicUserInfo();
            Ban ban = userInfo.getActiveBan();
            ban.setEndDate(LocalDateTime.now());
            userInfo.addExpiredBan(ban);
            userInfo.setActiveBan(null);
        } else {
            throw new IllegalArgumentException("Given user is not banned!");
        }
    }

    /**
     * Changes the reason for the given user's active ban. Expired bans cannot be modified.
     * @param user The user to update the ban of.
     * @param reason The reason for the user's ban. Will replace the old reason if there is one.
     */
    public static void changeBanReason(User user, String reason){
        if(isBanned(user)){
            Ban ban = user.getBasicUserInfo().getActiveBan();
            ban.setReason(reason);
        } else {
            throw new IllegalArgumentException("Given user is not banned!");
        }
    }
}