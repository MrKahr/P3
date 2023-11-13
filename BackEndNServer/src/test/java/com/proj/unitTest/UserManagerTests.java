package com.proj.unitTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.proj.function.UserManager;
import com.proj.exception.*;
import com.proj.model.users.*;
import com.proj.function.UserRepository;

public class UserManagerTests {
        //UserManager accountManager = new UserManager(0);
        //Account accountOne = new Account(new Guest("Fisk", "FiskPassword"));
        //Account accountTWo = new Account(new Guest("Aborre", "AborrePassword"));
        //AccountManager.accountRepository.save(accountOne);
        //AccountRepository accountRepo = accountManager.getAccountRepository();
        //accountRepo.save(accountOne);
    
    @Test
    public void AccountExistsTest(){
        //Assertions.assertTrue(accountManager.accountExists(1));
        
    }
    
    public void LookupAccountUserNotFound(){
        //Executable e = () -> {accountManager.accountExists("ynohijrucelpvdcqkpjxpng");}; // This shouldn't exist
        //assertThrows(UserNotFoundException.class, e);
    }

    public void LookupAccountFoundUser(){
        //Executable e = () -> {accountManager.accountExists("");};
    }
}

