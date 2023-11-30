package com.proj.function;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;

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

// This class is broken/dummy code. NEEDS FULL REWRITE OF METHODS!!!!!
// Check import statements

/**
 * Class responsible for handling all user management except assigning roles
 */
public class UserManager {
    // Field
    @Autowired
    UserdbHandler userdbHandler;
    @Autowired
    RoleRequestdbHandler requestdbHandler;

    private Integer numberOfUsers;

    // Constructor
    public UserManager(Integer numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
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
    public void createAccount(String userName, String password, boolean isMembershipRequested) {
        try {
            // UserRepositoryManager userRepoMan = new UserRepositoryManager();

            validateCreation(userName);

            if (isMembershipRequested) {
                requestMembership(userName);
            }
            this.numberOfUsers++; // Increment number of accounts since we just created one.

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
        String Dummy = "0"; // Dummy quick fix

        User user = null;
        if (userExists(userID)) {
            // user = userRepository.findById(userID).get();
        } else {
            throw new UserNotFoundException("User with userID '" + userID + "' does not exist in the database.");
        }
        return user;
    }

    /**
     * Makes a request to the database for a given userID.
     * 
     * @param userID The ID of the user to search for.
     * @return True if userID is found, false otherwise.
     * @throws IllegalArgumentException UserID is null.
     */
    public boolean userExists(Integer userID) throws IllegalArgumentException {
        String dummy = "0"; // Dummy quick fix

        // Optional dataBaseObject = userRepository.findById(userID);
        boolean isFoundInDB = false;
        // This check exists because we don't consider empty accounts account
        // if(userRepository.existsById(userID) && !(dataBaseObject.isEmpty())){
        // isFoundInDB = true;
        // }
        return isFoundInDB;
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

    /**
     * Checks login requests to ensure that the captcha and credentials of the user
     * requesting login exist and are correct.
     * 
     * @param userName Display name of user.
     * @param password Password of user.
     * @return True if login request is legitimate (valid credentials and captcha).
     * @throws InvalidLoginException When the login request either had wrong
     *                               username or password.
     */
    public boolean validateLogin(String userName, String password)
            throws InvalidLoginException {
        boolean isvalid = false;

        Integer userID = 0; // Dummy quick fix

        try {
            userExists(userID);
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

    public void deactivateAccount(String userName) {
    }

    public void removeAccount(String userName) {

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
            try{
                sanitizedUser = user.clone();
                // Remove password before sending back
                sanitizedUser.getBasicUserInfo().setPassword("");
            } catch(CloneNotSupportedException cnse){
                throw new FailedSanitizationException("Sanitization of users failed due to" + cnse.getMessage());
            }
       
        } else {
            HashMap<RoleType, Role> requestingUserRoles = requestingUser.getAllRoles();

            // Build payload based on user permissions - cannot be a switch since roles are not ordered
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
     * Fulfills a role request by getting the requesting user from the database and assigning to role
     */
    public void fulfillRoleRequest(int requestingId, RoleType type){
        ArrayList<Request> requests = new ArrayList<Request>();
        for (Request r : requestdbHandler.findAllByUserId(requestingId)) {
            if(r.getRequestType() == RequestType.ROLE){
                requests.add(r);
            }
        }
        RoleRequest roleRequest = null;
        for (Request r : requests) {
            roleRequest = (RoleRequest) r;
            if(roleRequest.getRoleType() == type){
                break;
            }
        }
        if(roleRequest == null){
            throw new IllegalArgumentException("No request of the given type exists for user with ID " + requestingId);
        }
        User user = userdbHandler.findById(requestingId);
        RoleAssigner.setRole(user, roleRequest.getRoleInfo());
        userdbHandler.save(user);
    }

    public void createRoleRequest(int requestingId, Role role){
        RoleRequest newRequest = new RoleRequest(requestingId, role);
        ArrayList<Request> requests = new ArrayList<Request>();
        for (Request r : requestdbHandler.findAllByUserId(requestingId)) {
            if(r.getRequestType() == RequestType.ROLE){
                requests.add(r);
            }
        }
        RoleRequest oldRequest = null;
        for (Request r : requests) {
            oldRequest = (RoleRequest) r;
            if(oldRequest.getRoleType() == role.getRoleType()){
                break;
            }
        }
        if(oldRequest == null){
            requestdbHandler.save(newRequest);
        } else {
            requestdbHandler.delete(oldRequest);    //make sure there's only one request of a given type and id
            requestdbHandler.save(newRequest);
        }
    }

    public void rejectRoleRequest(int requestingId, RoleType type){
        ArrayList<Request> requests = new ArrayList<Request>();
        for (Request r : requestdbHandler.findAllByUserId(requestingId)) {
            if(r.getRequestType() == RequestType.ROLE){
                requests.add(r);
            }
        }
        RoleRequest roleRequest = null;
        for (Request r : requests) {
            roleRequest = (RoleRequest) r;
            if(roleRequest.getRoleType() == type){
                break;
            }
        }
        if(roleRequest == null){
            throw new IllegalArgumentException("No request of the given type exists for user with ID " + requestingId);
        } else {
            requestdbHandler.delete(roleRequest);
        }
    }
}