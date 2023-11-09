package com.proj.unitTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.proj.function.AccountManager;
import com.proj.exception.*;

public class AccountManagerTests {
    public void LookupAccountUserNotFound(){
        AccountManager accountManager = new AccountManager(0);
        Executable e = () -> {accountManager.lookupAccount("ynohijrucelpvdcqkpjxpng");}; // This shouldn't exist
        assertThrows(UserNotFoundException.class, e);
    }

    public void LookupAccountFoundUser(){
        AccountManager accountManager = new AccountManager(0);
        Executable e = () -> {accountManager.lookupAccount("");};
    }
}
