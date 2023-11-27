package com.proj.controller.Security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class AuthenticationService implements UserDetailsService {
	@Autowired
	private UserDAO userDAO;
	
	/**
	 * Extracts user security info from the DAO and provides the authority level for this user.
	 * @return The UserDetails of the user.
	 * @throws UserNameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		UserSecurityInfo userSecurityInfo = userDAO.getUserInfo(username);
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userSecurityInfo.getRole());
		UserDetails userDetails = (UserDetails)new User(userSecurityInfo.getUsername(), userSecurityInfo.getPassword(), Arrays.asList(authority));
		return userDetails;
	}
}