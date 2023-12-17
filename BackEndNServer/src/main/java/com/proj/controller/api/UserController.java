// Handles http requests for users from front end

// Frontend -> UserController -> Backend

// The call hierarchy for the database connection is: Controller -> Manager -> Handler

// Help Repository setup -> https://www.geeksforgeeks.org/spring-boot-crudrepository-with-example/
// Help JPARepositories annotation -> https://stackoverflow.com/questions/27856266/how-to-make-instance-of-crudrepository-interface-during-testing-in-spring 

package com.proj.controller.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proj.model.users.*;
import com.proj.validators.MemberValidator;
import com.proj.exception.IllegalUserOperationException;
import com.proj.exception.InvalidInputException;
import com.proj.exception.UserNotFoundException;
import com.proj.function.RoleAssigner;
import com.proj.function.UserManager;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping(path = "/api")
public class UserController {
  @Autowired
  private UserManager userManager;

  // TODO: FOR TESTING PURPOSES!!!!!
  ArrayList<Integer> ids = new ArrayList<Integer>();

  /**
   * Generates users for testing - Delete if found after completing UserController
   * TODO: delete after compleing UserController
   * 
   * @param number
   * @return
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
          RoleAssigner.setRole(user, new Member("Fisk", "Randomstuff", "Table", "Stringwauy", "NoEmail"));
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

  @PutMapping(path = "/{username}/deactivate")
  @ResponseBody
  String deactivateAccount(@PathVariable String username, @RequestParam String requestingUsername) {
    User user = userManager.lookupAccount(username);
    User requestingUser = userManager.lookupAccount(requestingUsername);

    if (user.equals(requestingUser)) {
      userManager.deactivateAccount(user.getId());
      return "Deactivation of" + user.getBasicUserInfo().getUserName() + " succesful";
    } else {
      throw new IllegalUserOperationException("You may only deactivate your own account!");
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
  String reactivateAccount(@PathVariable String username, @RequestParam String requestingUsername) {
    User user = userManager.lookupAccount(username);
    User requestingUser = userManager.lookupAccount(requestingUsername);

    if (user.equals(requestingUser)) {
      userManager.restoreAccount(user.getBasicUserInfo().getUserName());
      return "Reactivation of" + user.getBasicUserInfo().getUserName() + " succesful";
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
  @PutMapping(path = "/{username}/editInfo")
  @ResponseBody
  String editInfo(@PathVariable String username, @RequestParam String requestingUsername,
      @RequestParam String[] currentInfo, @RequestParam String infoType) {

    // Create objects that can are used to check access
    User user = userManager.lookupAccount(username);
    User requestingUser = userManager.lookupAccount(requestingUsername);

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

  // TODO: delete before pushing to main
  @PutMapping(path = "/create/Account")
  @ResponseBody
  String createAccount(@RequestParam String username, @RequestParam String password,
      @RequestParam String[] memberInfo) {
    String membershipRequestInfo = "membership request not made";
    userManager.createAccount(username, password);

    // If the user has requested membership
    if (memberInfo.length > 0) {
      membershipRequestInfo = "member request made";
      userManager.requestMembership(memberInfo[0], memberInfo[1], memberInfo[2], memberInfo[3], memberInfo[4],
          memberInfo[5]);
    }

    return "Account created and " + membershipRequestInfo;
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
  public void adminSetRole(@PathVariable String username, @RequestParam String newRole,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    Role replacementRole;
    User userToChange = userManager.lookupAccount(username);
    try {
      // Check if user has previously had this role, to reuse role info from history
      Role roleFromHistory = userToChange.getRoleBackups().getBackupByType(RoleType.valueOf(newRole));
      if(roleFromHistory != null) {
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
    }
  }

  @DeleteMapping("/remove_role/{username}")
  public void adminRemoveRole(@PathVariable String username, @RequestParam String role,
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
          userToChange.setMemberInfo(null);
          break;
        case "DM":
          userToChange.setDmInfo(null);
          break;
        case "ADMIN":
          userToChange.setAdminInfo(null);
          break;
        default:
          throw new InvalidInputException("Did not receive a valid role");
      }
      userManager.getUserdbHandler().save(userToChange);
    }
  }

  /**
   * Register a membership payment for a user
   * 
   * @param username       username of the paying user
   * @param authentication of the admin confirming the payment
   */
  @PutMapping("/register_payment/{username}")
  public void putMethodName(@PathVariable String username,
      @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
    User requestingUser = userManager.lookupAccount(authentication.getName());
    if (requestingUser.getAdminInfo() == null) {
      throw new IllegalUserOperationException("Only admins are allowed to change user roles");
    }
    User payingUser = userManager.lookupAccount(username);
    payingUser.getMemberInfo().setLasPaymentDate(LocalDateTime.now());
    userManager.getUserdbHandler().save(payingUser);
  }
}