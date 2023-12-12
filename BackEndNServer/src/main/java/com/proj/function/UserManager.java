package com.proj.function;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proj.model.events.Request;
import com.proj.model.events.RequestType;
import com.proj.model.events.RoleRequest;
import com.proj.model.users.*;
import com.proj.exception.*;
import com.proj.repositoryhandler.RoleRequestdbHandler;
import com.proj.repositoryhandler.UserdbHandler;
import com.proj.validators.BasicInfoValidator;
import com.proj.validators.MemberValidator;

// TODO: rewrite all functions that end string message to write exception

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
    private int lastId; // the id of the most recent user created by createAccount

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

    /**
     * Gets the current dbhandler.
     * This getter is mainly used for testing purposes
     * 
     * @return - the user database handler associated with the current manager
     */
    public UserdbHandler getUserdbHandler() {
        return userdbHandler;
    }

    /**
     * Gets the current role request db handler.
     * This getter is mainly used for testing purposes
     * 
     * @return - the role request handler associated with the current manager
     */
    public RoleRequestdbHandler getRoleRequestdbHandler() {
        return roleRequestdbHandler;
    }

    public int getLastId() {
        return lastId;
    }

    /**
     * Creates an account
     * 
     * @param userName Display name of user.
     * @param password Password of user.
     * @return A new guest object with requested attributes.
     */
    public String createAccount(String userName, String password)
            throws NullPointerException {
        if (userName == null || password == null) {
            throw new NullPointerException("Cannot create user with username or password null");
        } else {
            try {
                // Create user object
                BasicUserInfo basicUserInfo = new BasicUserInfo(userName, password);
                User user = new User(basicUserInfo);

                // Validate user fields
                BasicInfoValidator userValidator = new BasicInfoValidator(basicUserInfo);
                userValidator.ValidateUserName().ValidatePassword();

                // Check whether username already exists in database
                validateUsernameStatusInDB(userName);

                // Set empty guest info
                user.setGuestInfo(new Guest(""));

                // Save user to db
                userdbHandler.save(user);
                // Increment number of users currently saved in db
                this.numberOfUsers++;
                // Write down what ID we just used
                this.lastId = user.getId();
                return "User creation successful";
            } catch (FailedValidationException ife) {
                return "Cannot create user because: " + ife.getMessage();
            }
        }

    }

    /**
     * Ensures the username of the user account that is to be created does not
     * already exist.
     * 
     * @param username Display name of user.
     * @throws FailedValidationException Thrown when the username is already take by
     *                                   another user (i.e. exists in the database).
     */
    public String validateUsernameStatusInDB(String username) {
        try {
            boolean IsUserInDB = userExistsInDatabase(username);
            if (IsUserInDB) {
                throw new FailedValidationException("Username:" + username + " is already in database");
            }
            return "Validation Successful";
        } catch (NullPointerException npe) {
            return npe.getMessage();
        }
    }

    /**
     * Queries the database for an account with a given username.
     * This is possible because usernames are unique.
     * 
     * @param username Display name of the user.
     * 
     * @return The user object with the given username or null if this user is not
     *         found
     * 
     * @throws UserNotFoundException Thrown if the user is not found in the
     *                               database.
     */

    public User lookupAccount(String username) throws UserNotFoundException, IllegalArgumentException {
        return userdbHandler.findByUserName(username);

    }

    /**
     * Makes a request to the database to check whether user exists in database
     * 
     * @param quiriedName The name of the user to search for.
     * @return True if username is found, false otherwise.
     * @throws IllegalArgumentException UserID is null.
     */
    public boolean userExistsInDatabase(String queriedUsername) throws NullPointerException {
        if (queriedUsername == null) {
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
    public boolean userExistsInDatabase(int userID) {
        boolean isUserInDB = false;
        try {
            // TODO: Try seeing what happens when optional is returned
            User user = userdbHandler.findById(userID);
            if (user instanceof User) {
                isUserInDB = true;
            }
        } catch (UserNotFoundException unfe) {
            System.out.println("Cannot check if user exists due to " + unfe.getMessage());
        }
        return isUserInDB;
    }

    /**
     * Retrieves a list of users that a controller can sanitize and send to the
     * front end.
     * Function gets list of users with id
     * 
     * @param startRangeID - the start of range of user to get from database
     * @param endRangeID   - the end of range of user to get from database
     * @return - array of users from database
     */

    public User[] getAccountList(int startRangeID, int endRangeID) {
        if (startRangeID < 0 || endRangeID < 0) {
            throw new IllegalArgumentException("Range cannot be negative!");
        }
        if (startRangeID > endRangeID) { // quick swap if the start is greater than the end
            int temp = startRangeID; // to prevent errors
            startRangeID = endRangeID;
            endRangeID = temp;
        }

        ArrayList<Integer> IDs = new ArrayList<Integer>(); // we make a list of all the IDs from the start of the range
                                                           // to the end
        for (int index = startRangeID; index <= endRangeID; index++) {
            IDs.add(index);
        }
        Iterable<User> userList = userdbHandler.findAllById(IDs); // get all the users in the given ID range
        ArrayList<User> users = new ArrayList<User>();
        for (User user : userList) {
            users.add(user);
        }

        // we convert the list to an array to make it easier to handle in the controller
        // due to there being no easy way of converting javas arrayList to a javascript
        // datastructure
        User[] userArray = new User[users.size()];
        userArray = users.toArray(userArray);

        return userArray;
    }

    /**
     * Validates and creates a membership request from the user with the given name,
     * if the information they've entered is valid.
     * 
     * @param realName    - the given name of a member
     * @param phoneNumber - the phone number of a member
     * @param postalCode  - the postal code of the member's current place of
     *                    residence
     * @param address     - the address of the member's current place of residence
     * @param email       - the member's current email address
     * @param username    - the username of the member making the request
     * @return A string with the request's status, which is either awaiting approval
     *         or some variation of an error message.
     */
    public String requestMembership(String realName, String phoneNumber, String postalCode, String address,
            String email, String username) {
        try {
            User requestingUser = lookupAccount(username);
            Role member = new Member(realName, phoneNumber, postalCode, address, email);
            MemberValidator validator = new MemberValidator((Member) member);
            validator.ValidateAddress() // if none of these throw, we create the request
                    .ValidateEmail()
                    .ValidatePhoneNumber()
                    .ValidatePostCode();
            this.createRoleRequest(requestingUser.getId(), member); // we have to use "this", since createRoleRequest
                                                                    // relies on non-static fields and so cannot be
                                                                    // static.
            return "Membership request made and awaiting approval.";
        } catch (UserNotFoundException unfe) {
            return unfe.getMessage();
        } catch (IllegalArgumentException iae) {
            return iae.getMessage();
        } catch (FailedValidationException fve) {
            return fve.getMessage();
        } catch (NullPointerException npe) {
            return npe.getMessage();
        } catch (Exception e){
            return e.getMessage();
        }
    }

    /**
     * Deactivates the account with the given ID and schedules it for deletion
     * 
     * @param userId the id of the user to deactivate
     */
    public void deactivateAccount(int userId) {
        User userToDeactivate = userdbHandler.findById(userId);
        userToDeactivate.getBasicUserInfo().setDeactivationDate(LocalDateTime.now());
        /*
         * There are different limits for how long a business is allowed to keep
         * personal info.
         * see e.g.
         * https://www.datatilsynet.dk/hvad-siger-reglerne/vejledning/databeskyttelse-i-
         * forbindelse-med-ansaettelsesforhold/opbevaring-af-personoplysninger-om-
         * ansoegere-som-ikke-bliver-ansat
         * We opt for caution and select 6 months
         */
        userToDeactivate.getBasicUserInfo().setDeletionDate(LocalDateTime.now().plusMonths(6));
        // Save updated user to userDatabase
        userdbHandler.save(userToDeactivate);

    }

    /**
     * Removes the deactivation-date and deletion-date from the account with the
     * given ID.
     * This method cannot be used to recreate an account that has already been
     * deleted.
     * Ensure that the user has consented to restoring their information. Otherwise
     * use of this method may be illegal.
     * 
     * @param userId - the id of the user to restore
     */
    public String restoreAccount(int userId) {
        User userToActivate = userdbHandler.findById(userId);
        userToActivate.getBasicUserInfo().setDeactivationDate(null);
        userToActivate.getBasicUserInfo().setDeletionDate(null);
        userdbHandler.save(userToActivate);
        return "User: " + userToActivate.getBasicUserInfo().getUserName() + " with ID: " + userToActivate.getId()
                + " was successfully restored";
    }

    /**
     * Check if the account with the given ID is past its deletion date and remove
     * it from the database if it
     * 
     * @param userId - identifier of the user whose account the system removes
     */
    public String removeAccount(int userId) {
        try {
            User userToDelete = userdbHandler.findById(userId);
            String statusmsg = "Deletion of " + userToDelete.getBasicUserInfo().getUserName();
            LocalDateTime deletionDate = userToDelete.getBasicUserInfo().getDeletionDate();
            if (deletionDate != null && LocalDateTime.now().isAfter(deletionDate)) { // check if we're past the deletion
                                                                                     // date
                userdbHandler.delete(userToDelete);
                return statusmsg + " successful";
            } else {
                return statusmsg + " unsuccessful";
            }
        } catch (UserNotFoundException unfe) {
            return unfe.getMessage();
        }

    }

    /**
     * Removes sensitive information from db-lookup based on accessingUser privilege
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
     * @return A copy of the user object that was saved to the database.
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
        roleRequestdbHandler.delete(roleRequest);
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
     */
    public RoleRequest createRoleRequest(int requestingId, Role role) {
        // check if dependencies are fulfilled
        if (!RoleAssigner.dependenciesFulfilled(this.getUserdbHandler().findById(requestingId), role)) {
            throw new IllegalArgumentException(
                    "User with id " + requestingId + " does not fulfill the requirements for the given role.");
        }
        // get an old request and replace it if it's there
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

    public String handleRoleRequest(RoleRequest roleRequest, boolean requestAccepted) {
        String statusMessage = "Role changed to " + roleRequest.getRoleType();
        if (requestAccepted) {
            fulfillRoleRequest(numberOfUsers, roleRequest.getRoleType());
            return statusMessage + "accepted";
        } else {
            rejectRoleRequest(numberOfUsers, roleRequest.getRoleType());
            return statusMessage + "rejected";
        }
    }
}
