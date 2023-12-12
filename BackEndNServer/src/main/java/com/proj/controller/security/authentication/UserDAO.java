package com.proj.controller.security.authentication;

import java.util.HashMap;
import java.util.HashSet;

// Spring necessities
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Spring Security
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// Our classes
import com.proj.exception.UserNotFoundException;
import com.proj.model.users.Role;
import com.proj.model.users.RoleType;
import com.proj.model.users.User;
import com.proj.repositoryhandler.UserdbHandler;

/**
 * The DAO (Data Access Object) was by Spring originally intended to access the database directly.
 * However, that is not the case in this implementation, where database access is abstracted to userdbHandler.
 * <p>
 * The purpose of UserDAO is to search the database for the user requesting login, fetch info used for authentication, 
 * and insert it into a UserSecurityInfo object which is returned.
 * @see https://docs.spring.io/spring-framework/reference/data-access/dao.html
 */
@Service
public class UserDAO {
	@Autowired
  	private UserdbHandler userdbHandler;

	/**
	 * Searches the database for the given username to obtain the user security info necessary for authentication.
	 * <p>
	 * This info is stored in the special object UserSecurityInfo.
	 * <p>
	 * TODO: Implement user roles.
	 * @param username The username from which to obtain user info 
	 * @return The user security info necessary for authentication.
	 */
	public UserSecurityInfo getUserInfo(String username){
		User user;
		try {
			user = userdbHandler.findByUsername(username); //TODO: Check method runtime in userdbHandler
		} catch (UserNotFoundException unfe) {
			// Converts UserNotFoundException to Spring Security's UsernameNotFoundException in order to fire the event AuthenticationFailureBadCredentialsEvent
			// and throw BadCredentialsException.
			throw new UsernameNotFoundException("User with username"+ username + "could not be found.", unfe);
		}

		UserSecurityInfo userInfo = new UserSecurityInfo(
			user.getBasicUserInfo().getUserName(),
			user.getBasicUserInfo().getPassword(),
			findAuthorities(user));
    	return userInfo;
    }

	/**
	 * Finds the granted authorities for the supplied user and stores them in a HashMap.
	 * Used for authentication in Spring Security.
	 * @param user A user object.
	 * @return A HashSet of granted authoritíes.
	 */
    private HashSet<GrantedAuthority> findAuthorities(User user) { 
        HashMap<RoleType, Role> roleMap = user.getAllRoles();
		var authorities = new HashSet<GrantedAuthority>(); // "var" is a generic object type

        for (var key : roleMap.keySet()) {
            var grantedAuthority = new SimpleGrantedAuthority(key.toString());
            authorities.add(grantedAuthority);
        }
        return authorities;
    }
} 
