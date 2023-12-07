package com.proj.function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.proj.repositoryhandler.UserdbHandler;

import com.proj.model.users.User;

import java.util.ArrayList;

import java.time.LocalDateTime;
@Service
public class DatabaseCleaner{
    //Field
    @Autowired
    private UserdbHandler userdbHandler;
    
    //Method
    /*
    @Scheduled(cron = "* * * * * ?")  //should fire every second for testing. "0 0 0 ? * *" should be used for the real application to make this happen once per day
    public void cleanupDeletedUsers() {
        long now = System.currentTimeMillis() / 1000;
        System.out.println("Beginning cleanup of expired users at system time " + now);
        ArrayList<String> deletedUsers = new ArrayList<String>();
        Iterable<User> users = userdbHandler.findAll();
        for (User user : users) {
            if(user.getBasicUserInfo().getDeletionDate() != null && user.getBasicUserInfo().getDeletionDate().isBefore(LocalDateTime.now())){
                userdbHandler.delete(user); //if we're past a user's deletion date, remove them permanently
                deletedUsers.add(user.getBasicUserInfo().getUserName());
            }
        }
        //Printing the names of the users we've removed so the information is available
        now = System.currentTimeMillis() / 1000;
        System.out.println("Finished cleanup at system time " + now + "\nDeleted " + deletedUsers.size() + " deactivated users:");
        for (String name : deletedUsers) {
            System.out.println(name);
        }
    }
    */
}