package com.proj.controller.security;

import java.util.HashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Authentication
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;

// Passwords
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class Authentication {
	/**
	 * Authenticates a user based on the userDetailsService provided by the frontend.
	 * <p>
	 * If the username/password in userDetailsService match with the corresponding username/password in the database, 
	 * the user is authorized to proceed to the requested page.
	 * <p>
	 * TODO: Implement access restriction based on roles.
	 * @param userDetailsService // The Service managing userDetails from the frontend (e.g. password).
	 * @param passwordEncoder The password encoder to hash the password from the frontend. Should be identical to the encoder used on the database.
	 * @return
	 * @see https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/index.html
	 */
	@Bean
	 public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);

		ProviderManager providerManager = new ProviderManager(authenticationProvider);
		providerManager.setEraseCredentialsAfterAuthentication(false);

		return providerManager;
	}

	/**
	 * Defines a hashmap of password encoders to use. Includes a default password encoder (bcrypt).
	 * The hashmap is implemented to allow different password encoders to be used rather easily 
	 * (but remember to convert passwords already encoded in the database to the new format).
	 * <p>
	 * A new PasswordEncoder instance is then created with the default password encoder.
	 * <p>
	 * TODO: Changing password: https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html#authentication-change-password-configuration
	 * @return The selected PasswordEncoder.
	 * @see https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		String idForEncode = "bcrypt";
		HashMap<String, PasswordEncoder> encoders = new HashMap<String, PasswordEncoder>();
		encoders.put(idForEncode, new BCryptPasswordEncoder(10)); // Default strength
		encoders.put("pbkdf2@SpringSecurity_v5_8", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());

		PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(idForEncode, encoders);

		return passwordEncoder;
	} 
}
