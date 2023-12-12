package com.proj.controller.fisk.authentication;

// Spring necessities
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Spring Security
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Implements the UserDetailsService interface which is necessary for authentication
 * This overrides Spring Security's default implementation of UserDetailsService.
 * This class is managed by Spring which means it must only be accessed within Spring's Application Context.
 */
@Service
public class AuthenticationService implements UserDetailsService {
	@Autowired
	private UserDAO userDAO;
	
	/**
	 * Extracts user security info from the DAO.
	 * @return The UserDetails of the user.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) {
		
		UserSecurityInfo userSecurityInfo = userDAO.getUserInfo(username);
		
		// The User class here is Spring's User class.
		UserDetails userDetails = (UserDetails) new User(userSecurityInfo.getUsername(), userSecurityInfo.getPassword(), userSecurityInfo.getAuthorities());
		
		return userDetails;
	}


}