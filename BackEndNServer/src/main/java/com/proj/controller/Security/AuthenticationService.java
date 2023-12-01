package com.proj.controller.security;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proj.model.users.Role;
import com.proj.model.users.RoleType;

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
		//var authority = new SimpleGrantedAuthority(userSecurityInfo.getRole()); // call getAuthorities here
		//Arrays.asList
		UserDetails userDetails = (UserDetails) new User(userSecurityInfo.getUsername(), userSecurityInfo.getPassword(), userSecurityInfo.getAuthorities()); // change to conform to getAuthorities return type

		// Example:
		//
		// return org.springframework.security.core.userdetails.User
		// .withUsername(appUser.getUsername())
		// .password(appUser.getPassword())
		// .roles(getAuthorities(appUser))
		// .build();
		
		return userDetails;
	}


}