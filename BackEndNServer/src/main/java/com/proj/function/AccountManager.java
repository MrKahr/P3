package com.proj.function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;

import com.proj.model.users.*;
import com.proj.exception.*;
import java.util.Optional; // Class that is returned if object is not found in database 

//TODO: remove all instances of captcha as this is handeld by front end 
/**
 * Class responsible for handling all user management except assigning roles
 */
public class AccountManager {
    // Field
    private Integer numberOfAccounts;

    @Autowired
    private AccountRepository accountRepository;

    // Constructor
    public AccountManager(Integer numberOfAccounts) {
        this.numberOfAccounts = numberOfAccounts;
    }

    // Method
    public Integer getNumberOfAccounts() {
        return this.numberOfAccounts;
    }

    public void setNumberOfAccounts(Integer numberOfAccounts) {
        this.numberOfAccounts = numberOfAccounts;
    }

    /**
     * 
     * @param userName Display name of user.
     * @param password Password of user.
     * @param isMembershipRequested Boolean showing if user checked the request for membership button on the frontend.
     * @return A new guest object with requested attributes.
     */
    public void createAccount(String userName, String password, boolean isMembershipRequested) {
        try {
            validateCreation(userName);

            if(isMembershipRequested) {
                requestMembership(userName);
            }

            Guest guest = new Guest(userName, password);
            Account account = new Account(guest);

            accountRepository.save(account); //TODO: Consider whether IllegalArgumentException
            this.numberOfAccounts++; // Increment number of accounts since we just created one.

        } catch (UsernameAlreadyUsedException invlle) {
            // TODO: Send message to frontend: the username is already taken.
            
        } 
    }

    /**
     * Ensures the username of the user account that is to be created does not
     * already exist.
     * 
     * @param userName Display name of user.
     * @return True if the username does not exist.
     * @throws UsernameAlreadyUsedException When the username is already taken by another user (i.e. exists in the database).
     */
    public boolean validateCreation(String userName) {
        // TODO: We could perform user input validation of username here
        try {
            //accountExists(userName);
        } catch (UserNotFoundException usrnfe) { // This error means creation is valid since UserNotFound means this username is available
            return true;
        }
        throw new UsernameAlreadyUsedException(String.format("Username '%s' is already in use", userName));
    }

    /**
     * Queries the database for account and gets account if it exists  
     * @param userID Display name of the user.
     * @return account object 
     * @throws UserNotFoundException Username is not found in the database.
     * @throws IllegalArgumentException userID is null.
     */
    public Account lookupAccount(Integer userID) throws UserNotFoundException, IllegalArgumentException {
        Account account;
        Optional dataBaseObject;
        if(accountExists(userID)){
            account = accountRepository.findById(userID).get();  
        } else {
            throw new UserNotFoundException("User with userID '"+userID+"' does not exist in the database.");
        }
        return account; 
    }

    /**
     * Makes a request to the database for a given userID.
     * @param userID The ID of the user to search for.
     * @return True if userID is found, false otherwise.
     * @throws IllegalArgumentException UserID is null.
    */
    public boolean accountExists(Integer userID) throws IllegalArgumentException {
        Optional dataBaseObject = accountRepository.findById(userID);
        boolean isFoundInDB = false;
        // This check exists because we don't consider empty accounts account
        if(accountRepository.existsById(userID) && !(dataBaseObject.isEmpty())){
            isFoundInDB = true;
        }
        return isFoundInDB;
    }
    /**
     * Retrieves all user accounts from the database. Username only.
     * 
     * @return
     */
    public void getAccountList() {
        // TODO: Request to database goes here
        // See https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html


        // Check return type of database. if singular, wrap all users in a list or
        // something
        // return all_users;
    }

    /**
     * Checks login requests to ensure that the captcha and credentials of the user
     * requesting login exist and are correct.
     * 
     * @param userName Display name of user.
     * @param password Password of user.
     * @return True if login request is legitimate (valid credentials and captcha).
     * @throws InvalidLoginException   When the login request either had wrong
     *                                 username or password.
     */
    public boolean validateLogin(String userName, String password)
            throws InvalidLoginException {
        boolean isvalid = false;
        try {
            //accountExists(userName);
        } catch (UserNotFoundException usrnfe) {
            throw new InvalidLoginException(String.format("Login failed with: %s", usrnfe.getMessage()), usrnfe);
        }
        isvalid = true;
        return isvalid;
    }

    public void requestMembership(String userName) {
        // Send message to frontend about it
    }
    /**
     * When the user performs a function on the front end, the frontend sends a hashed user name to the server. 
    * This function ensures that the user has the correct access level when 
    * @param userName - user name associated with user 
    * @param requiredAccessLevel - required level of accessed to perform an operation.
    * @pre-con access level is specified for the function a user is trying to call 
     * @return
    */
    public boolean manageAccessLevel(String userName, String requiredAccessLevel) {
        return false;
    }

    public void deactivateAccount(String userName) {
    }

    public void removeAccount(String userName) {
        
    }
}