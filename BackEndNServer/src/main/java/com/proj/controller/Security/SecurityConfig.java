package com.proj.controller.security;

import java.util.HashMap;

// Spring Application Context
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

// Web Security (Intercept incoming request)
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
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

// Jakarta
import jakarta.servlet.DispatcherType;

// Our classes
import com.proj.model.users.RoleType;


/**
 * Configuration/implementation of filters in Spring Security Filter Chain.
 * <p>
 * The Security Filter Chain is a chain of filters that intercepts all incoming requests which are then processed through the filters.
 * <p>
 * The applied filters determine what action should be taken with the incoming request, e.g. denied or require authentication (login).
 * @see https://docs.spring.io/spring-security/reference/servlet/architecture.html#servlet-securityfilterchain
 */
@Configuration
public class SecurityConfig {

	@Bean
	@Order(1)
	 public SecurityFilterChain testFilter(HttpSecurity http) throws Exception {
		http
			.csrf(Customizer.withDefaults())
			.authorizeHttpRequests((authorize) -> authorize
				.anyRequest().permitAll()
			);
		return http.build();
	}




	/**
	 * The basic security filter is a default/fallback authentication filter which requires authentication for all requests
	 * which are not specified in the requestMatcher methods. This is a basic security measure to prevent unathorized access in case someone
	 * forgot to define a special SecurityFilter for a URL (e.g. /admin). 
	 * <p>
	 * All URLs defined in requestMatchers.permitAll are always allowed.
	 * The actual authentication of users is handled by AuthenticationManager (roughly speaking).
	 * @param http
	 * @return
	 * @throws Exception
	 */
	@Bean
	//@Order(1)
	 public SecurityFilterChain basicFilter(HttpSecurity http) throws Exception {
		http
			.csrf(Customizer.withDefaults())
			.authorizeHttpRequests((authorize) -> authorize
				.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR, DispatcherType.INCLUDE).permitAll()
				.requestMatchers("/login", "/", "/user/**", "FrontEnd/**", "/public/**").permitAll()
				.requestMatchers("/fisk").hasRole(RoleType.GUEST.toString())
				.anyRequest().authenticated()
			)
			.formLogin(Customizer.withDefaults());
			// .formLogin(form -> form
			// 	.loginPage("/login")
			// 	.failureUrl("/login?error=true")
			// 	.permitAll())
			// .logout(logout -> logout
			// 	.logoutUrl("/logout")
			// 	.logoutSuccessUrl("/home")
			// 	.permitAll());

		return http.build();
	}

	/**
	 * Requires all requests to URLs specified in securityMatcher to require a role of admin+.
	 * TODO: Implement role dependencies.
	 * <p>
	 * The actual authentication of users is handled by the AuthenticationManager (roughly speaking).
	 * @param http
	 * @return
	 * @throws Exception
	 */
	@Bean
	@Order(2) // The order in which to invoke this filter. No order defaults to last.
	 public SecurityFilterChain adminFilter(HttpSecurity http) throws Exception {
		RequestCache nullRequestCache = new NullRequestCache(); // Do not save the original request, if authentication is required.
		
		http
			.securityMatcher("/admin/**")
			.authorizeHttpRequests((authorize) -> authorize
				//.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()	
				.requestMatchers("/admin/**").hasAnyRole(RoleType.ADMIN.toString(), RoleType.SUPERADMIN.toString())
				.anyRequest().denyAll()
			)
			.formLogin(Customizer.withDefaults())
			.requestCache((cache) -> cache
				.requestCache(nullRequestCache)
			);

		return http.build();
	}

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