package com.proj.controller.Security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// @Service
// public class AuthenticationService implements UserDetailsService {
// 	@Autowired
// 	private UserDAO userDAO;
// 	@Override
// 	public UserDetails loadUserByUsername(String username)
// 			throws UsernameNotFoundException {
// 		UserSecurityInfo userSecurityInfo = userDAO.getUserInfo(username);
// 		GrantedAuthority authority = new GrantedAuthority(userSecurityInfo.getRole());
// 		UserDetails userDetails = (UserDetails)new User(userSecurityInfo.getUsername(), userSecurityInfo.getPassword(), Arrays.asList(authority));
// 		return userDetails;
// 	}
// }