package com.proj.function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;

import com.proj.model.users.*;
import com.proj.exception.*;

//TODO: remove all instances of captcha as this is handeld by front end 
/**
 * Class responsible for handling all user management except assigning roles
 */
public class AccountManager {
    // Field
    private Integer numberOfAccounts;

    @Autowired
    private AccountRepository AccountRepository;

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
     * @param isCaptchaSuccesful  Boolean showing if captcha was correct or not.
     * @param isMembershipRequested Boolean showing if user checked the request for membership button on the frontend.
     * @return A new guest object with requested attributes.
     * @throws InvalidCaptchaException When the captcha wasn't solved correctly.
     */
    public void createAccount(String userName, String password, boolean isCaptchaSuccesful, boolean isMembershipRequested) {
        if(isCaptchaSuccesful){
            try {
                validateCreation(userName);

                if(isMembershipRequested) {
                    requestMembership(userName);
                }

                Guest guest = new Guest(userName, password);
                Account account = new Account(guest);

                AccountRepository.save(account); //TODO: Consider whether IllegalArgumentException
                this.numberOfAccounts++; // Increment number of accounts since we just created one.

            } catch (UsernameAlreadyUsedException invlle) {
                // TODO: Send message to frontend: the username is already taken.
                
            } 
        }
        else {
            throw new InvalidCaptchaException("Captcha invalid"); //TODO: REMOVE - capta
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
            lookupAccount(userName);
        } catch (UserNotFoundException usrnfe) { // This error means creation is valid since UserNotFound means this username is available
            return true;
        }
        throw new UsernameAlreadyUsedException(String.format("Username '%s' is already in use", userName));
    }

    /**
     * Makes a request for the specified username to the database.
     * @param userName Display name of the user.
     * @return User object.
     * @throws UserNotFoundException When a username is not found in the database.
     */
    public User lookupAccount(String userName) throws UserNotFoundException {
        // TODO: Request to database goes here

        int request = 200;// Dummy request 
        if(request == 404){ 
            throw new UserNotFoundException(String.format("User '%s' does not exist in the database.", userName));
        }

        // Single user
        User dummyUser = new Guest("LovesToLoot", "youshallpass123");
        return dummyUser;
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
     * @param isCaptchaSuccesful  Boolean showing if captcha was correct or not.
     * @return True if login request is legitimate (valid credentials and captcha).
     * @throws InvalidLoginException   When the login request either had wrong
     *                                 username or password.
     * @throws InvalidCaptchaException When the captcha wasn't solved correctly.
     */
    public boolean validateLogin(String userName, String password, boolean isCaptchaSuccesful)
            throws InvalidLoginException, InvalidCaptchaException {
        boolean isvalid = false;
        if (isCaptchaSuccesful) {
            try {
                lookupAccount(userName);
            } catch (UserNotFoundException usrnfe) {
                throw new InvalidLoginException(String.format("Login failed with: %s", usrnfe.getMessage()), usrnfe);
            }
        } else {
            throw new InvalidCaptchaException("Captcha invalid");
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
        // Check whether user has the right access level 
        boolean isLegalOperation = false;

        try{
            User user = lookupAccount(userName); //TODO: Consider whether an object is returned from database or whether we need to convert this object
            String currentAcessLevel = user.getClass().toString(); // Runtime class -  see https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#getClass--
            
            if(currentAcessLevel.equals(requiredAccessLevel)){
                isLegalOperation = true;
            }
        } catch(UserNotFoundException usfe){
            isLegalOperation = false;
        } catch(IllegalUserOperationException iuoe){
            isLegalOperation = false;
        }
        
        return isLegalOperation;
    }

    public void deactivateAccount(String userName) {
    }

    public void removeAccount(String userName) {
        
    }
}