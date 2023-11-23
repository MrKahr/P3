package com.proj.unitTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.proj.function.UserManager;
import com.proj.exception.*;
import com.proj.model.users.*;
import com.proj.repositories.UserRepository;

// THIS IS NOT FOR USE - ONLY REFERENCE


//@EnableJpaRepositories(basePackages={"com.proj.function.UserRepository"})
public class UserManagerTests {
    //@Autowired 
    //private UserRepository testRepository;

    //UserManager userManager = new UserManager(0);
    //User user1 = new Guest("Fisk", "FiskPassword");
    //User user2 = new Guest("Aborre", "AborrePassword");

    //@Test
    public void dbSaveUser(){
        //Executable e = () -> {testRepository.save(user1);};
        //assertDoesNotThrow(e); // Specifically IllegalArgumentException
    }
    
    public void LookupAccountUserNotFound(){
        //Executable e = () -> {accountManager.accountExists("ynohijrucelpvdcqkpjxpng");}; // This shouldn't exist
        //assertThrows(UserNotFoundException.class, e);
    }

    public void LookupAccountFoundUser(){
        //Executable e = () -> {accountManager.accountExists("");};
    }
    @Test
    public void sanitizeLookupSuperAdmin(){
        User user;
        User accessingUser = new User();
    }
    @Test 
    void SanitizeLookupAdmin(){}

    @Test
    void SanitizeLookupDM(){}

    @Test 
    void SanitizeLookMember(){}

    @Test 
    void SanitizeLookupGuest(){}
}

