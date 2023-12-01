package com.proj.function;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.proj.model.events.Request;
import com.proj.model.events.RequestType;
import com.proj.model.events.RoleRequest;
import com.proj.model.users.*;
import com.proj.exception.*;
import com.proj.repositoryhandler.RoleRequestdbHandler;
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
@Service
public class UserManager {
    // Field
    @Autowired
    private UserdbHandler userdbHandler;
    @Autowired
    private RoleRequestdbHandler roleRequestdbHandler;

    private int numberOfUsers;

    // Constructor
    public UserManager(int numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    public UserManager() {
        this.numberOfUsers = 0;
    }

    // Method
    public int getNumberOfUsers() {
        return this.numberOfUsers;
    }

    public void setNumberOfUsers(int numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    public UserdbHandler getUserdbHandler() {
        return userdbHandler;
    }

    public RoleRequestdbHandler getRoleRequestdbHandler() {
        return roleRequestdbHandler;
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
            throws NullPointerException {
        if (userName == null || password == null) {
            throw new NullPointerException("Cannot create user with username or password null");
        } else {
            try {
                // Check whether username already exists in database
                validateUsernameStatusInDB(userName);

                // Create user object
                BasicUserInfo basicUserInfo = new BasicUserInfo(userName, password);
                User user = new User(basicUserInfo);

                // Validate user fields
                UserValidator userValidator = new UserValidator(user);
                userValidator.ValidateUserName().ValidatePassword();

                // Save user to db
                userdbHandler.save(user);

                // Increment number of users currently saved in db
                this.numberOfUsers++;
                return "User creation successful";
            } catch (FailedValidationException ife) {
                return "Cannot create user because: " + ife.getMessage();
            }
        }

    }

    public String upgradeToMember() {
        return "";
    }

    /**
     * Ensures the username of the user account that is to be created does not
     * already exist.
     * 
     * @param userName Display name of user.
     * @throws FailedValidationException When the username is already taken by
     *                                   another user (i.e. exists in the
     *                                   database).
     */
    public void validateUsernameStatusInDB(String username) {
        try {
            boolean IsUserInDB = userExistsInDatabase(username);
            if (IsUserInDB) {
                throw new FailedValidationException("Username:" + username + " is already in database");
            }
        } catch (NullPointerException npe) {
            System.out.println(npe.getMessage());
        }
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
        try {
            user = userdbHandler.findById(userID);
        } catch (NullPointerException npe) {
            System.out.println("Put exception here");
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
        try {
            // TODO: Try seeing what happens when optional is returned
            User user = userdbHandler.findById(UserID);
            if (user instanceof User) {
                isUserInDB = true;
            }
        } catch (UserNotFoundException unfe) {
            System.out.println("Cannot check if user exists due to " + unfe.getMessage());
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

    /**
     * Assigns a role given by a role request to the user that made the request, and
     * removes the request from the database.
     * This method assumes that a the requesting user exists in the user database
     * already.
     * 
     * @param requestingId The ID of the user that requested the role.
     * @param type         The type of role the user has requested and is allowed to
     *                     have.
     */
    public void fulfillRoleRequest(int requestingId, RoleType type) {
        ArrayList<Request> requests = new ArrayList<Request>();
        for (Request r : roleRequestdbHandler.findAllByUserId(requestingId)) {
            if (r.getRequestType() == RequestType.ROLE) {
                requests.add(r);
            }
        }
        RoleRequest roleRequest = null;
        for (Request r : requests) {
            roleRequest = (RoleRequest) r;
            if (roleRequest.getRoleType() == type) {
                break;
            }
        }
        if (roleRequest == null) {
            throw new IllegalArgumentException("No request of the given type exists for user with ID " + requestingId);
        }
        User user = userdbHandler.findById(requestingId);
        RoleAssigner.setRole(user, roleRequest.getRoleInfo());
        userdbHandler.save(user);
    }

    /**
     * Create a request from the user with the given ID for a specific Role-object
     * and store it in the database.
     * This method also ensures that there is only one request for any role per user
     * at any given time.
     * 
     * @param requestingId The ID of the user that is requesting the role.
     * @param role         The Role-object that the user wishes to have assigned to
     *                     them.
     * @return A copy of the RoleRequest-object that was saved to the database.
     */
    public RoleRequest createRoleRequest(int requestingId, Role role) {
        RoleRequest newRequest = new RoleRequest(requestingId, role);
        ArrayList<Request> requests = new ArrayList<Request>();
        for (Request r : roleRequestdbHandler.findAllByUserId(requestingId)) {
            if (r.getRequestType() == RequestType.ROLE) {
                requests.add(r);
            }
        }
        RoleRequest oldRequest = null;
        for (Request r : requests) {
            oldRequest = (RoleRequest) r;
            if (oldRequest.getRoleType() == role.getRoleType()) {
                break;
            }
        }
        if (oldRequest == null) {
            roleRequestdbHandler.save(newRequest);
        } else {
            roleRequestdbHandler.delete(oldRequest); // make sure there's only one request of a given type and id
            roleRequestdbHandler.save(newRequest);
        }
        return newRequest;
    }

    /**
     * Removes a request for a specified type of role from the user with the given
     * ID.
     * 
     * @param requestingId The user whose request should be removed.
     * @param type         The type of role the user wanted, but is not allowed to
     *                     have.
     */
    public void rejectRoleRequest(int requestingId, RoleType type) {
        ArrayList<Request> requests = new ArrayList<Request>();
        for (Request r : roleRequestdbHandler.findAllByUserId(requestingId)) {
            if (r.getRequestType() == RequestType.ROLE) {
                requests.add(r);
            }
        }
        RoleRequest roleRequest = null;
        for (Request r : requests) {
            roleRequest = (RoleRequest) r;
            if (roleRequest.getRoleType() == type) {
                break;
            }
        }
        if (roleRequest == null) {
            throw new IllegalArgumentException("No request of the given type exists for user with ID " + requestingId);
        } else {
            roleRequestdbHandler.delete(roleRequest);
        }
    }

}