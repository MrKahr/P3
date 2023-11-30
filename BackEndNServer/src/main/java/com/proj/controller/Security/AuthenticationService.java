package com.proj.controller.security;

import java.util.Arrays;
import java.util.HashSet;

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
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userSecurityInfo.getRole()); // call getAuthorities here
		UserDetails userDetails = (UserDetails) new User(userSecurityInfo.getUsername(), userSecurityInfo.getPassword(), Arrays.asList(authority)); // change to conform to getAuthorities return type

		// Example:
		//
		// return org.springframework.security.core.userdetails.User
		// .withUsername(appUser.getUsername())
		// .password(appUser.getPassword())
		// .roles(getAuthorities(appUser))
		// .build();
		
		return userDetails;
	}

	// This implementation should provide role dependencies using authority instead of role.
	//
    // private String[] getAuthorities(User appUser) {
    //     var authorities = new HashSet<String>();
    //     for (var role : appUser.getRoles()) {
    //         var grantedAuthority = new SimpleGrantedAuthority(role.getRole());
    //         authorities.add(grantedAuthority.getAuthority());
    //     }
    //     System.out.println("User authorities are " + authorities);
    //     return Arrays.copyOf(authorities.toArray(),authorities.size(), String[].class);
    // }


}