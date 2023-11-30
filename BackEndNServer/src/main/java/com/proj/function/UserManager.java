package com.proj.function;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.proj.model.users.*;
import com.proj.exception.*;
import com.proj.repositoryhandler.UserdbHandler;
import com.proj.validators.UserValidator;

// This class is broken/dummy code. NEEDS FULL REWRITE OF METHODS!!!!!

/*TODO:  
 * TEST USER MANAGER METHODS 
 * TEST USER BUILDER
 * Create new equals for user 
 */

/**
 * Class responsible for handling all user management except assigning roles
 */
public class UserManager {
    // Field
    @Autowired
    UserdbHandler userdbHandler;

    private Integer numberOfUsers;

    // Constructor
    public UserManager(Integer numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    public UserManager() {
        this.numberOfUsers = 0;
    }

    // Method
    public Integer getNumberOfUsers() {
        return this.numberOfUsers;
    }

    public void setNumberOfUsers(Integer numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    /**
     * 
     * @param userName              Display name of user.
     * @param password              Password of user.
     * @param isMembershipRequested Boolean showing if user checked the request for
     *                              membership button on the frontend.
     * @return A new guest object with requested attributes.
     */
    public String createAccount(String userName, String password)
    // TODO: consider whether we can get an array instead of single params 
            throws NullPointerException {
        if (userName == null || password == null) {
            throw new NullPointerException("Cannot create user with username or password null");
        } else {
            try {
                if (userExistsInDatabase(userName)) {
                    throw new UsernameAlreadyUsedException("Cannot create user because user is already in database");
                }
                // Create user object
                BasicUserInfo basicUserInfo = new BasicUserInfo(userName, password);
                User user = new User(basicUserInfo);
                // Validate user fields
                UserValidator userValidator = new UserValidator(user);
                userValidator.ValidateUserName().ValidatePassword();
                // Check whether user is already in db - only by username

                // Something with hashing of passwords before being saved to database.

                // Save user to db
                userdbHandler.save(user);

                // Increment number of users currently saved in db
                this.numberOfUsers++;
                return "User creation successful";

            } catch (UsernameAlreadyUsedException uaue) {
                return "Cannot create user because: " + uaue.getMessage();
            } catch (FailedValidationException ife) {
                return "Cannot create user because: " + ife.getMessage();
            }
        }

    }

    public String upgradeToMember() {
    }

    /**
     * Ensures the username of the user account that is to be created does not
     * already exist.
     * 
     * @param userName Display name of user.
     * @return True if the username does not exist.
     * @throws UsernameAlreadyUsedException When the username is already taken by
     *                                      another user (i.e. exists in the
     *                                      database).
     */
    public boolean validateCreation(String userName) {
        // TODO: We could perform user input validation of username here
        try {
            // accountExists(userName);
        } catch (UserNotFoundException usrnfe) { // This error means creation is valid since UserNotFound means this
                                                 // username is available
            return true;
        }
        throw new UsernameAlreadyUsedException(String.format("Username '%s' is already in use", userName));
    }

    /**
     * Queries the database for account and gets account if it exists
     * 
     * @param userID Display name of the user.
     * @return User object
     * @throws UserNotFoundException    Username is not found in the database.
     * @throws IllegalArgumentException userID is null.
     */
    public User lookupAccount(Integer userID) throws UserNotFoundException, IllegalArgumentException {
        User user = null;
        try{
                user = userdbHandler.findById(userID);
        } 

        if (userExistsInDatabase(userID)) {

        } else {
            throw new UserNotFoundException("User with userID '" + userID + "' does not exist in the database.");
        }
        return user;
    }

    /**
     * Makes a request to the database for a given username.
     * 
     * @param quiriedName The name of the user to search for.
     * @return True if username is found, false otherwise.
     * @throws IllegalArgumentException UserID is null.
     */
    public boolean userExistsInDatabase(String queriedUsername) throws NullPointerException {
        if (queriedUsername.equals(null)) {
            throw new NullPointerException("Cannot determine whether " + queriedUsername + " exists in database");
        }

        boolean isUserInDB = false;
        Iterable<User> users = userdbHandler.findAll();
        for (User user : users) {
            if (user.getBasicUserInfo().getUserName().equals(queriedUsername)) {
                isUserInDB = true;
            }
        }
        return isUserInDB;
    }

    /**
     * Makes a request to the database for a given userID.
     * 
     * @param userID The ID of the user to search for.
     * @return True if userID is found, false otherwise.
     * @throws IllegalArgumentException UserID is null.
     */
    public boolean userExistsInDatabase(int UserID) {
        boolean isUserInDB = false;
        // TODO: Find type of exception thrown here
        User user = userdbHandler.findById(UserID);
        if (user instanceof User) {
            isUserInDB = true;
        }
        return isUserInDB;

    }

    /**
     * Retrieves all user accounts from the database. Username only.
     * 
     * @return
     */
    public void getAccountList() {
        // TODO: Request to database goes here
        // See
        // https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html

        // Check return type of database. if singular, wrap all users in a list or
        // something
        // return all_users;
    }

    public void requestMembership(String userName) {
        // Send message to frontend about it
    }

    /**
     * When the user performs a function on the front end, the frontend sends a
     * hashed user name to the server.
     * This function ensures that the user has the correct access level when
     * 
     * @param userName            - user name associated with user
     * @param requiredAccessLevel - required level of accessed to perform an
     *                            operation.
     * @pre-con access level is specified for the function a user is trying to call
     * @return
     */
    public boolean manageAccessLevel(String userName, String requiredAccessLevel) {
        return false;
    }

    public void deactivateAccount(int UserID) {
    }

    public void removeAccount(int userID) {

    }

    /**
     * Removes sensitive information from dp-lookup based on accessingUser privilege
     * 
     * @param user           - user object that is looked up in the data
     * @param requestingUser - user requesting access to user object
     * @param role           - the access level of the requesting user e.g. admin
     * @return - user object with correct elements removed
     * @throws NullPointerException
     * @pre-con: user element must be defined
     */
    public User sanitizeDBLookup(User user, User requestingUser) throws NullPointerException {
        User sanitizedUser = new User();

        if (user.equals(null)) {
            throw new NullPointerException("Cannot sanitize null element");
        }

        // Check whether user accesses their own page, otherwise build payload based
        if (requestingUser.equals(user)) {
            try {
                sanitizedUser = user.clone();
                // Remove password before sending back
                sanitizedUser.getBasicUserInfo().setPassword("");
            } catch (CloneNotSupportedException cnse) {
                throw new FailedSanitizationException("Sanitization of users failed due to" + cnse.getMessage());
            }

        } else {
            HashMap<RoleType, Role> requestingUserRoles = requestingUser.getAllRoles();

            // Build payload based on user permissions - cannot be a switch since roles are
            // not ordered
            if (requestingUserRoles.containsKey(RoleType.SUPERADMIN)) {
                sanitizedUser.setSuperAdminInfo(user.getSuperAdminInfo());
                sanitizedUser.setMemberInfo(user.getMemberInfo());
            }

            if (requestingUserRoles.containsKey(RoleType.ADMIN)) {
                sanitizedUser.setAdminInfo(user.getAdminInfo());
            }

            if (requestingUserRoles.containsKey(RoleType.DM)) {
                sanitizedUser.setDmInfo(user.getDmInfo());
            }
            if (requestingUserRoles.containsKey(RoleType.MEMBER)) {
                sanitizedUser.setGuestInfo(user.getGuestInfo());
            }

            sanitizedUser.setBasicUserInfo(user.getBasicUserInfo());
            // Remove password before sending back
            sanitizedUser.getBasicUserInfo().setPassword("");

        }
        return sanitizedUser;
    }
}