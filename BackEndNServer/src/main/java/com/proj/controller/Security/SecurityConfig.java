package com.proj.controller.Security;

import java.util.HashMap;

// Spring Application Context
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Web Security (Intercept incoming request)
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


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

// TODO: Apply important filters to the Security Filter Chain.
	// Important filters (more should be added):
		// CSRF:  https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#servlet-csrf


@Configuration
@EnableWebMvc
// Based off of https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/index.html
public class SecurityConfig {

	// For difference between EnableWebSecurity and HttpSecurity, see: 
		// https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/configuration/EnableWebSecurity.html
		// https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/builders/HttpSecurity.html

		
	/**
	 * The Security Filter Chain is a chain of filters that intercepts all incoming requests which are then processed through the filters.
	 * <p>
	 * The applied filters determine what action should be taken with the incoming request, e.g. denied or require authentication (login).	
	 * @param http An http request
	 * @return The action which must be performed with this request. (TODO: Verify this)
	 * @throws Exception TODO: Explain this
	 * @see https://docs.spring.io/spring-security/reference/servlet/architecture.html#servlet-securityfilterchain
	 */
	@Bean
	 public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(Customizer.withDefaults())
			.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/login", "/", "/user/**").permitAll()
				.requestMatchers("/fisk").hasAuthority("GUEST")
				.anyRequest().authenticated()
			)
			.formLogin(Customizer.withDefaults());

		return http.build();
	}

	/**
	 * Authenticates a user based on the userDetailsService provided by the frontend.
	 * <p>
	 * If the username/password in userDetailsService match with the corresponding username/password in the database, 
	 * the user is authorized to proceed to the requested page.
	 * <p>
	 * TODO: Implement access restriction based on roles.
	 * @param userDetailsService
	 * @param passwordEncoder
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