package com.proj.controller.security.authentication;

import java.util.HashMap;

// Spring necessities
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Spring Events
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.context.ApplicationEventPublisher;

// Authentication
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;

// Passwords
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@Configuration
public class AuthenticationConfig {
	/**
	 * Authenticates a user based on the userDetailsService provided by the frontend.
	 * <p>
	 * If the username/password in userDetailsService match with the corresponding username/password in the database, 
	 * the user is authorized to proceed to the requested page.
	 * <p>
	 * @param userDetailsService The service managing userDetails from the frontend (e.g. password).
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
	 * (but remember that passwords already hashed in the database cannot be converted to the new format).
	 * <p>
	 * TODO: Changing password: https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html#authentication-change-password-configuration
	 * @return  A new PasswordEncoder instance with the selected password encoder.
	 * @see https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		String idForEncode = "bcrypt";

		// Format of HashMap: 
		//	String = name of PasswordEncoder. 
		//	PasswordEncoder = the implementation of the PasswordEncoder.
		HashMap<String, PasswordEncoder> encoders = new HashMap<String, PasswordEncoder>();
		encoders.put(idForEncode, new BCryptPasswordEncoder(10)); // Default strength
		encoders.put("pbkdf2@SpringSecurity_v5_8", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());

		PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(idForEncode, encoders);

		return passwordEncoder;
	}

	/**
	 * Enables support for listening to Spring events using @EventListener annotation.
	 * @param applicationEventPublisher
	 * @return
	 * @see https://docs.spring.io/spring-security/reference/servlet/authentication/events.html
	 */
	@Bean
	public AuthenticationEventPublisher authenticationEventPublisher (ApplicationEventPublisher applicationEventPublisher) {
		return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
	}
}
