package com.proj.function;

import com.proj.model.users.*;

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

    public Guest createAccount(String userName, String password, boolean captcha, boolean requestMembership) {
        try {
            validateCreation(userName, captcha);
        } catch (invalidCaptchaException invlce) {
            // TODO: Send message to frontend: your captcha is wrong.
        } catch (invalidLoginException invlle) {
            // TODO: Send message to frontend: your username or password is incorrect.
        }
        if (requestMembership) {
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
     * @param captcha  Boolean showing if captcha was correct or not.
     * @return True if the username does not exist and the captcha is correct.
     * @throws invalidLoginException   When the login request either had wrong
     *                                 username or password.
     * @throws invalidCaptchaException When the captcha wasn't solved correctly.
     */
    public boolean validateCreation(String userName, boolean captcha)
            throws invalidLoginException, invalidCaptchaException {
        boolean isvalid = false;
        // TODO: We could perform user input validation of username here
        if (captcha) {
            try {
                lookupAccount(userName, false);
            } catch (userNotFoundException usrnfe) {
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
     * @throws userNotFoundException When a username is not found in the database.
     */
    public User lookupAccount(String userName) throws userNotFoundException {
        // TODO: Request to database goes here
        
        if(request = 404){ 
            // TODO: throw new userNotFoundException()
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
     * @param captcha  Boolean showing if captcha was correct or not.
     * @return True if login request is legitimate (valid credentials and captcha).
     * @throws invalidLoginException   When the login request either had wrong
     *                                 username or password.
     * @throws invalidCaptchaException When the captcha wasn't solved correctly.
     */
    public boolean validateLogin(String userName, String password, boolean captcha)
            throws invalidLoginException, invalidCaptchaException {
        boolean isvalid = false;
        if (captcha) {
            try {
                lookupAccount(userName, false);
            } catch (userNotFoundException usrnfe) {
                // TODO: throw new invalidLoginException()
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