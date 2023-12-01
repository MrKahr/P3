package com.proj.controller.security;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.proj.model.users.Role;
import com.proj.model.users.RoleType;
import com.proj.model.users.User;
import com.proj.repositoryhandler.UserdbHandler;

/**
 * The DAO (Data Access Object) was by Spring originally intended to access the database directly.
 * <p>
 * However, that is not the case in this implementation, where database access is abstracted to userdbHandler.
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
		User user = userdbHandler.findByUsername(username); //TODO: Check method runtime in userdbHandler

		UserSecurityInfo userInfo = new UserSecurityInfo(
			user.getBasicUserInfo().getUserName(),
			user.getBasicUserInfo().getPassword(),
			findAuthorities(user)); // Change UserSecInfo to include role dependecies.
    	return userInfo;
    }

    private HashSet<GrantedAuthority> findAuthorities(User user) { 
        HashMap<RoleType, Role> roleMap = user.getAllRoles();
		var authorities = new HashSet<GrantedAuthority>(); // "var" is a generic object type
		//var authorities = new HashSet<String>();

        for (var key : roleMap.keySet()) {
            var grantedAuthority = new SimpleGrantedAuthority(key.toString());
            authorities.add(grantedAuthority);
			//authorities.add(grantedAuthority.getAuthority());
        }
        System.out.println("User: "+ user.getBasicUserInfo().getUserName() +" has authorities: " + authorities);
        return authorities;
		//return Arrays.copyOf(authorities.toArray(),authorities.size(), String[].class);
    }


} 
