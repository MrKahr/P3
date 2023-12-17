// Handles http requests for users from front end

// Frontend -> UserController -> Backend

// The call hierarchy for the database connection is: Controller -> Manager -> Handler

// Help Repository setup -> https://www.geeksforgeeks.org/spring-boot-crudrepository-with-example/
// Help JPARepositories annotation -> https://stackoverflow.com/questions/27856266/how-to-make-instance-of-crudrepository-interface-during-testing-in-spring 

package com.proj.controller.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.proj.model.events.RoleRequest;
import com.proj.model.session.PlaySession;
import com.proj.model.users.*;
import com.proj.validators.BasicInfoValidator;
import com.proj.validators.MemberValidator;
import com.proj.exception.FailedValidationException;
import com.proj.exception.IllegalUserOperationException;
import com.proj.exception.InvalidInputException;
import com.proj.exception.UserNotFoundException;
import com.proj.function.PlaySessionManager;
import com.proj.function.RoleAssigner;
import com.proj.function.UserManager;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping(path = "/api")
public class UserController {
  @Autowired
  private UserManager userManager;
  @Autowired
  private PlaySessionManager playSessionManager;

  // TODO: FOR TESTING PURPOSES!!!!!
  ArrayList<Integer> ids = new ArrayList<Integer>();

  /**
   * Generates users for testing - Delete if found after completing UserController
   * TODO: delete after compleing UserController
   * 
   * @param number - the number of users you want to create
   * @return - the sanitized user object
   */
  @RequestMapping(path = "/create/{number}")
  @ResponseBody
  ArrayList<User> saveUsers(@PathVariable Integer number) {
    ArrayList<User> sanitizedUsers = new ArrayList<User>();
    try {
      for (int i = 0; i < number; i++) {
        User user = new User(new BasicUserInfo("n" + i, "p" + i));
        if (i >= 1) {
          RoleAssigner.setRole(user, new Guest("Level 1 bard" + i));
        }
        if (i >= 2) {
          RoleAssigner.setRole(user, new Member("Fisk", "1122334" + i, "9000", "Stringwauy", "NoEmail"));
        }
        if (i >= 3) {
          RoleAssigner.setRole(user, new DM(new ArrayList<String>()));
        }
        if (i >= 4) {
          RoleAssigner.setRole(user, new Admin(new ArrayList<String>(), new ArrayList<String>()));
        }
        if (i == 5) {
          RoleAssigner.setRole(user, new SuperAdmin());
        }
        userManager.getUserdbHandler().save(user);
        ids.add(user.getId());
      }
      for (User user : userManager.getUserdbHandler().findAllById(ids)) {
        sanitizedUsers.add(userManager.sanitizeDBLookup(user, user));
      }

      return sanitizedUsers;
    } catch (Exception e) {
      e.printStackTrace();
      return sanitizedUsers;
    }
  }

  /**
   * Get a user from database and return information based on user access level
   * 
   * @param username           - the name of the user looked up on the database
   * @param requestingUsername - the username of the person requesting the page
   * @return a sanitized user object
   */
  @GetMapping(path = "/{username}")
  @ResponseBody
  User profile(@PathVariable String username,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    try {
      // Check whether user has correct priviledges
      User requestingUser = userManager.lookupAccount(authentication.getName());

      // Get user to lookup from database if they exist
      User user = userManager.lookupAccount(username);

      // Get user information
      User sanitizedUser = userManager.sanitizeDBLookup(user, requestingUser);

      return sanitizedUser;
    } catch (NullPointerException npe) {
      throw new UserNotFoundException("Cannot lookup your credentials or cannot find the person you are looking for");
    }
  }

  /**
   * 
   * @param min            Start ID for range of users
   * @param max            End ID for range of users
   * @param authentication The authentication object for requesting user
   * @return ArrayList of sanitized user objects
   */
  @GetMapping(path = "/users_in_range/{min}-{max}")
  @ResponseBody
  ArrayList<User> getAll(@PathVariable Integer min, @PathVariable Integer max,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    try {
      User[] unsanitizedUsers = userManager.getAccountList(min, max);
      ArrayList<User> sanitizedUsers = new ArrayList<User>();
      // Check whether user has correct priviledges
      User requestingUser = userManager.lookupAccount(authentication.getName());

      for (User user : unsanitizedUsers) {
        // Get user information
        User sanitizedUser = userManager.sanitizeDBLookup(user, requestingUser);

        sanitizedUsers.add(sanitizedUser);
      }
      return sanitizedUsers;
    } catch (NullPointerException npe) {
      throw new UserNotFoundException("Cannot lookup your credentials");
    }
  }

  /**
   * Deactivates a user with the given username
   * 
   * @param username           - the username of the account to deactivate
   * @param requestingUsername - the username of the person requesting the
   *                           deactivation
   * @return string message indicating that the deactivation is successful
   */

  @GetMapping(path = "/{username}/deactivate")
  @ResponseBody
  String deactivateAccount(@PathVariable String username,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    User user = userManager.lookupAccount(username);
    User requestingUser = userManager.lookupAccount(authentication.getName());

    if (user.equals(requestingUser)) {
      userManager.deactivateAccount(user.getId());
      return "Deactivation of " + user.getBasicUserInfo().getUserName() + " succesful";
    } else {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You may only deactivate your own account!");
      // throw new IllegalUserOperationException("You may only deactivate your own
      // account!");
    }
  }

  /**
   * Reactivate a user with the given username
   * 
   * @param username           - the username of the account to reactivate
   * @param requestingUsername - the username of the person requesting the
   *                           reactivation
   * @return string message indicating that the reactivation is successful
   */

  @PutMapping(path = "/{username}/reactivate")
  @ResponseBody
  String reactivateAccount(@PathVariable String username,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    User user = userManager.lookupAccount(username);
    User requestingUser = userManager.lookupAccount(authentication.getName());

    if (user.equals(requestingUser)) {
      userManager.restoreAccount(user.getBasicUserInfo().getUserName());
      return "Reactivation of " + user.getBasicUserInfo().getUserName() + " successful";
    } else {
      throw new IllegalUserOperationException("You may only reactivate your own account!");
    }
  }

  /**
   * 
   * @param username
   * @param requestingUsername
   * @param currentInfo
   * @param infoType
   * @return
   */
  @PutMapping(path = "/{username}/editMemberInfo")
  @ResponseBody
  String editMemberInfo(@PathVariable String username,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication,
      @RequestParam String[] currentInfo, @RequestParam String infoType) {

    // Create objects that can are used to check access
    User user = userManager.lookupAccount(username);
    User requestingUser = userManager.lookupAccount(authentication.getName());

    if (user.equals(requestingUser)) {
      // Change info on user object according to the information changed on website
      if (infoType.equals("Profile info")) {
        user.getGuestInfo().setCharacterInfo(currentInfo[0]);
        userManager.getUserdbHandler().save(user);
      } else if (infoType.equals("Password")) {
        user.getBasicUserInfo().setPassword(currentInfo[0]);
        userManager.getUserdbHandler().save(user);
      } else if (infoType.equals("Personal info")) {

        // Put current info into correct fields
        user.getMemberInfo().setAddress(currentInfo[0]);
        user.getMemberInfo().setEmail(currentInfo[1]);
        user.getMemberInfo().setPhoneNumber(currentInfo[2]);
        user.getMemberInfo().setPostalCode(currentInfo[3]);
        user.getMemberInfo().setRealName(currentInfo[4]);

        // Create validator that can check information before it is saved to database
        MemberValidator memberValidator = new MemberValidator(user.getMemberInfo());
        // Validate all fields before user is saved to database
        memberValidator.ValidateAddress().ValidateEmail().ValidatePhoneNumber().ValidatePostCode()
            .ValidateStringField(user.getMemberInfo().getRealName());

        userManager.getUserdbHandler().save(user);
      }
      return "Changes saved succesfully!";
    } else {
      throw new IllegalUserOperationException("You may only edit your own account!");
    }
  }

  /**
   * Saves guestinfo to database - note jackson requires me to send an instance of
   * the superclass "role" rather than the child "guest"
   * https://stackoverflow.com/questions/31665620/is-jacksons-jsonsubtypes-still-necessary-for-polymorphic-deserialization
   * 
   * @param username       the username of the person to save user info for
   * @param authentication the authentication object of the person requesting new
   *                       info
   * @param newGuestInfo   the new info tosave
   * @return status messasge of the reponse
   */
  @PutMapping(path = "/{username}/saveGuestInfo")
  @ResponseBody
  String putGuestInfo(@PathVariable String username,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication,
      @RequestBody Role newGuestInfo) {
    User user = userManager.lookupAccount(username);
    User requestingUser = userManager.lookupAccount(authentication.getName());

    if (user.equals(requestingUser)) {
      // Typecast due to jackson
      user.setGuestInfo((Guest) newGuestInfo);
      userManager.getUserdbHandler().save(user);
      return "Changes saved successfully!";
    } else {
      throw new IllegalUserOperationException("You may only edit your own account!");
    }
  }

  /**
   * Finds all future events between 2 minutes from now and 100 years into the
   * future for a user
   * 
   * @param username       - the username of the person to find future events for
   * @param authentication - the authentication object used to determine user
   *                       identity
   * @return - list of future playsessions for the user
   */
  // TODO: ask for help using this API to generate sessions to test this function
  @GetMapping(path = "{username}/futureEvents")
  @ResponseBody
  List<PlaySession> getFutureEvents(@PathVariable String username,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    User user = userManager.lookupAccount(username);
    User requestingUser = userManager.lookupAccount(authentication.getName());
    boolean userInSession = false;

    if (user.equals(requestingUser)) {
      LocalDateTime currentTime = LocalDateTime.now();
      // Get all playSessions starting in a minute and the 100 years forward - these
      // are future sessions
      List<PlaySession> futureSessions = playSessionManager.getSessions(currentTime.plusMinutes(1),
          currentTime.plusYears(100));

      // Loop through each of the current playsessions and check whether our user is
      // assigned. If not, remove from list
      for (int i = futureSessions.size(); i > 0; i--) {
        for (String assigneduser : futureSessions.get(i).getUsers()) {
          if (assigneduser.equals(user.getBasicUserInfo().getUserName())) {
            userInSession = true;
          }
        }
        if (!userInSession) {
          futureSessions.remove(i);
        }
      }
      return futureSessions;
    } else {
      throw new IllegalUserOperationException("You may only edit your own account!");
    }
  }

  @PutMapping(path = "/{username}/savePassword")
  @ResponseBody
  String changePassword(@PathVariable String username,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication,
      @RequestBody String Password) {
    // TODO: Ask for help putting hashing this password
    User user = userManager.lookupAccount(username);
    User requestingUser = userManager.lookupAccount(authentication.getName());

    if (user.equals(requestingUser)) {
      user.getBasicUserInfo().setPassword(Password);
      userManager.getUserdbHandler().save(user);
      return "Changes saved successfully!";
    } else {
      throw new IllegalUserOperationException("You may only edit your own account!");
    }
  }

  @GetMapping(path = "/{username}/validatePassword")
  @ResponseBody
  boolean validatePassword(@PathVariable String username,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication,
      @RequestBody String password) {
    boolean isValidPassword = false;

    User user = userManager.lookupAccount(username);
    User requestingUser = userManager.lookupAccount(authentication.getName());

    if (user.equals(requestingUser)) {
      BasicInfoValidator validator = new BasicInfoValidator(
          new BasicUserInfo(user.getBasicUserInfo().getUserName(), password));
      return validator.ValidatePassword() instanceof BasicInfoValidator;
    } else {
      throw new IllegalUserOperationException("You may only edit your own account!");
    }
  }

  @PutMapping(path = "/{username}/sendUpgradeToMemberRequest")
  @ResponseBody
  String requestMembershipOnFrontend(@PathVariable String username,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication,
      @RequestBody Member memberInfo) {
    User user = userManager.lookupAccount(username);
    User requestingUser = userManager.lookupAccount(authentication.getName());
    if (user.equals(requestingUser)) {
      return userManager.requestMembership(memberInfo.getRealName(), memberInfo.getPhoneNumber(),
          memberInfo.getPostalCode(), memberInfo.getAddress(), memberInfo.getEmail(),
          user.getBasicUserInfo().getUserName());
    } else {
      throw new IllegalUserOperationException("You may only edit your own account!");
    }
  }

  /**
   * Gets the username of the user that is currently logged in on the site
   * 
   * @param authentication - authentication object that is used to check that user
   *                       has correct credentials
   * @return - name of the current user
   */
  @GetMapping(path = "/currentUser")
  @ResponseBody
  String getCurrentUserName(@CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    try {
      return authentication.getName();
    } catch (Exception e) {
      return "NA";
    }
  }

  /**
   * Change a user's role through the admin menu
   * 
   * @param id             of user to change role of
   * @param newRole        Role object
   * @param authentication The authentication object for requesting user
   * @return
   */
  @PutMapping("/set_role/{username}")
  @ResponseBody
  public User adminSetRole(@PathVariable String username, @RequestParam String newRole,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    Role replacementRole;
    User userToChange = userManager.lookupAccount(username);
    try {
      // Check if user has previously had this role, to reuse role info from history
      Role roleFromHistory = userToChange.getRoleBackups().getBackupByType(RoleType.valueOf(newRole));
      if (roleFromHistory != null) {
        replacementRole = roleFromHistory;
      } else {
        throw new NullPointerException();
      }
    } catch (NullPointerException npe) {
      switch (newRole) {
        case "MEMBER":
          replacementRole = new Member();
          break;
        case "DM":
          replacementRole = new DM();
          break;
        case "ADMIN":
          replacementRole = new Admin();
          break;
        default:
          throw new InvalidInputException("Did not receive a valid role");
      }
    }

    User requestingUser = userManager.lookupAccount(authentication.getName());
    if (requestingUser.getAdminInfo() == null) {
      throw new IllegalUserOperationException("Only admins are allowed to change user roles");
    } else if (replacementRole.getRoleType().equals(RoleType.ADMIN) && requestingUser.getSuperAdminInfo() == null) {
      throw new IllegalUserOperationException("Only superadmins are allowed to promote to admin");
    } else {
      RoleAssigner.setRole(userToChange, replacementRole);
      userManager.getUserdbHandler().save(userToChange);
      return userManager.sanitizeDBLookup(userManager.lookupAccount(username), requestingUser);
    }
  }

  @DeleteMapping("/remove_role/{username}")
  @ResponseBody
  public User adminRemoveRole(@PathVariable String username, @RequestParam String role,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    User requestingUser = userManager.lookupAccount(authentication.getName());
    if (requestingUser.getAdminInfo() == null) {
      throw new IllegalUserOperationException("Only admins are allowed to change user roles");
    } else if (role.equals("ADMIN") && requestingUser.getSuperAdminInfo() == null) {
      throw new IllegalUserOperationException("Only superadmins are allowed to demote admins");
    } else {
      User userToChange = userManager.lookupAccount(username);
      switch (role) {
        case "MEMBER":
          RoleAssigner.setRole(userToChange, new Member());
          userToChange.setMemberInfo(null);
          break;
        case "DM":
          RoleAssigner.setRole(userToChange, new DM());
          userToChange.setDmInfo(null);
          break;
        case "ADMIN":
          RoleAssigner.setRole(userToChange, new Admin());
          userToChange.setAdminInfo(null);
          break;
        default:
          throw new InvalidInputException("Did not receive a valid role");
      }
      userManager.getUserdbHandler().save(userToChange);
      return userManager.sanitizeDBLookup(userManager.lookupAccount(username), requestingUser);
    }
  }

  /**
   * Register a membership payment for a user
   * 
   * @param username       username of the paying user
   * @param authentication of the admin confirming the payment
   */
  @PutMapping("/register_payment/{username}")
  @ResponseBody
  public User registerPayment(@PathVariable String username,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication) throws Exception {
    User requestingUser = userManager.lookupAccount(authentication.getName());
    if (requestingUser.getAdminInfo() == null) {
      throw new IllegalUserOperationException("Only admins are allowed to change user roles");
    }
    User payingUser = userManager.lookupAccount(username);
    Member memberInfo = payingUser.getMemberInfo();
    memberInfo.setLasPaymentDate(LocalDateTime.now());
    // Really weird work-around because Spring Boot seems to cache data unless it is
    // more obvious that it has been changed, e.g. saving the attribute as null
    payingUser.setMemberInfo(null);
    userManager.getUserdbHandler().save(payingUser);

    payingUser.setMemberInfo(memberInfo);
    userManager.getUserdbHandler().save(payingUser);
    return userManager.sanitizeDBLookup(userManager.getUserdbHandler().findById(payingUser.getId()), requestingUser);
  }

  @PutMapping("/handle_role_request")
  @ResponseBody
  public String putMethodName(@RequestBody RoleRequest roleRequest, @RequestParam boolean requestAccepted,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    User authenticatingUser = userManager.lookupAccount(authentication.getName());
    if (authenticatingUser.getAdminInfo() == null) {
      throw new IllegalUserOperationException("Only admins can handle role requests");
    }
    return userManager.handleRoleRequest(roleRequest, requestAccepted);
  }
}