package com.proj.controller.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


// Security Filter Chain:
	// https://docs.spring.io/spring-security/reference/servlet/architecture.html#servlet-securityfilterchain

// Important filters:
	// CSRF:  https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#servlet-csrf


// TODO: Backend must only store a hashed version of the password. Compare hash with hashed version of user-input password when logging in
	// Changing password: https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html#authentication-change-password-configuration


@Configuration
@EnableWebMvc
// Based off of https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/index.html
public class SecurityConfig {

	// See: 
		// https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/configuration/EnableWebSecurity.html
		// https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/builders/HttpSecurity.html

	@Bean
	// The Security Filter Chain filters all incoming requests based on applied filters, e.g. Cross Site Request Forgery (CSRF)
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(Customizer.withDefaults())
			.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/login", "/", "/user/**").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	// Authenticates a user based on the UserDetailsService
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);

		ProviderManager providerManager = new ProviderManager(authenticationProvider);
		providerManager.setEraseCredentialsAfterAuthentication(false);

		return providerManager;
	}

	@Bean
	// Basic User Info
	public UserDetailsService userDetailsService() {
		UserDetails userDetails = User.withDefaultPasswordEncoder()
			.username("user")
			.password("password")
			.roles("USER")
			.build();

		return new InMemoryUserDetailsManager(userDetails);
	}

	@Bean
	// Determine the password encoder
	// TODO: We should use: https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html#authentication-password-storage-pbkdf2
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}