package com.proj.function;

import com.proj.model.users.*;
import com.proj.exception.*;


/**
 * Class responsible for handling all user management except assigning roles
 */
public class AccountManager {
    // Field
    private Integer numberOfAccounts;

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

    public Guest createAccount(String userName, String password, boolean isCaptchaSuccesful, boolean isMembershipRequested) {
        try {
            validateCreation(userName, isCaptchaSuccesful);
        } catch (InvalidCaptchaException invlce) {
            // TODO: Send message to frontend: your captcha is wrong.
        } catch (InvalidLoginException invlle) {
            // TODO: Send message to frontend: your username or password is incorrect.
        }
        if(isMembershipRequested) {
            requestMembership(userName);
        }

        Guest guest = new Guest(userName, password);
        this.numberOfAccounts++; // Increment number of accounts since we just created one.
        return guest;
    }

    /**
     * Ensures the username of the user account that is to be created does not
     * already exist.
     * 
     * @param userName Display name of user.
     * @param password Password of user.
     * @param isCaptchaSuccesful  Boolean showing if captcha was correct or not.
     * @return True if the username does not exist and the captcha is correct.
     * @throws InvalidLoginException   When the login request either had wrong
     *                                 username or password.
     * @throws InvalidCaptchaException When the captcha wasn't solved correctly.
     */
    public boolean validateCreation(String userName, boolean isCaptchaSuccesful)
            throws InvalidLoginException, InvalidCaptchaException {
        boolean isvalid = false;
        // TODO: We could perform user input validation of username here
        if (isCaptchaSuccesful) {
            try {
                lookupAccount(userName);
            } catch (UserNotFoundException usrnfe) {
                // TODO: throw new invalidLoginException()
            }
        } else {
            /* TODO: throw new invalidCaptchaException */}
        isvalid = true;
        return isvalid;
    }

    /**
     * Makes a request for the specified username to the database.
     * @param userName Display name of the user.
     * @return User object.
     * @throws UserNotFoundException When a username is not found in the database.
     */
    public User lookupAccount(String userName) throws UserNotFoundException {
        // TODO: Request to database goes here
        
        if(request = 404){ 
            throw new UserNotFoundException("User ");
        }

        // Single user
        return User user;
    }

    /**
     * Retrieves all user accounts from the database. Username only.
     * 
     * @return
     */
    public Object getAccountList() {
        // TODO: Request to database goes here

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
                throw new InvalidLoginException("");
            }
        } else {
            /* TODO: throw new invalidCaptchaException */}
        isvalid = true;
        return isvalid;
    }

    public void requestMembership(String userName) {
    }

    public boolean manageAccessLevel(String userName) {
    }

    public void deactivateAccount(String userName) {
    }

    public void removeAccount(String userName) {
    }

}