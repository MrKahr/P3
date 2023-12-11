package com.proj.controller.security;

// Spring Application Context
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

// Web Security (Intercept incoming request)
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

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
public class SecurityFilters {

	// #####################
	// #### IMPORTANT!! ####
	// #####################
	// CSRF protection is disabled! This is NOT ideal for a webpage with users.
	// To use CSRF protection, the client must send a CSRF token with POST/PUT requests which the server recognises.
	// See: https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html

	// Spring Security should be enabled at the method level for best protection - known as defence in depth.
	// See: https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html#method-security-architecture


	// ###############
	// #### Notes ####
	// ###############
	// @Order(1) -> The order in which to invoke this filter. No order defaults to last.
	// TODO: Find a way to apply parts of the filters globally to reduce duplicate code. E.g.: http.formlogin(...) or http.logout(...)
	//
	// Redirect to requested page upon successful login or prevent it: https://docs.spring.io/spring-security/reference/servlet/architecture.html#requestcache-prevent-saved-request


	// ############################
	// #### Exception Handling ####
	// ############################
	// These exceptions need to be handled:
	// Use Advisors for this!! (Update: use events for this - WIP)
	//
	// org.springframework.security.authentication.AccessDeniedException

// ########################################

	/**
	 * Testing filter. Do NOT enable this in production!
	 * @param http
	 * @return
	 * @throws Exception
	 */
	// @Bean
	// @Order(-1)
	//  public SecurityFilterChain testFilter(HttpSecurity http) throws Exception {
	// 	http
	// 		.csrf((csrf) -> csrf.disable())
	// 		.authorizeHttpRequests((authorize) -> authorize
	// 			.anyRequest().permitAll()
	// 		);
	// 	return http.build();
	// }



	/**
	 * Allows all requests matching this filter. This must only be used on resources and urls which are always public, e.g. "/".
	 * @param http
	 * @return
	 * @throws Exception
	 */
	@Bean
	@Order(1)
	 public SecurityFilterChain allowFilter(HttpSecurity http) throws Exception {
		http
			.securityMatcher("/css/**", "/login/**", "/", "/js/**", "/images/**", "/favicon.ico", "/error")
			.csrf((csrf) -> csrf.disable())
			.authorizeHttpRequests((authorize) -> authorize
				.anyRequest().permitAll()
			);

		return http.build();
	}

	/**
	 * Denies all requests matching this filter. This is useful for protecting private resources, e.g. resources used in development.
	 * @param http
	 * @return
	 * @throws Exception
	 */
	@Bean
	@Order(2)
	 public SecurityFilterChain denyFilter(HttpSecurity http) throws Exception {
		http
			.securityMatcher("/private/**")
			.csrf((csrf) -> csrf.disable())
			.authorizeHttpRequests((authorize) -> authorize
				.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
				.anyRequest().denyAll()
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
	@Order(200)
	 public SecurityFilterChain basicDefaultFilter(HttpSecurity http) throws Exception {
		
		// Enable CSRF
		// http
		// 	.csrf((csrf) -> csrf
		// 		.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		// 	);

		// Disable CSRF
		http
			.csrf((csrf) -> csrf.disable());


		// Authorization
		http
			.authorizeHttpRequests((authorize) -> authorize
				.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
				.requestMatchers("/user/**").permitAll() // TODO: For testing purposes
				//.requestMatchers("/fisk").hasAnyAuthority(RoleType.GUEST.toString())
				.anyRequest().authenticated()
			);
		
		// Login
		http
			//.formLogin(Customizer.withDefaults()); // TODO: Check form login methods in Spring Docs
			.formLogin(form -> form
				.loginPage("/login")
				//.loginProcessingUrl("/login")
				//.defaultSuccessUrl("/", true)
				.permitAll()
			);

		// Logout
		http
			.logout(logout -> logout
				.addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(Directive.COOKIES)))
				.logoutUrl("/logout")
				.logoutSuccessUrl("/")
				.permitAll()
			);

		// Session management
		http
			.sessionManagement(session -> session
				.maximumSessions(1)
				.maxSessionsPreventsLogin(true)
			);
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
	@Order(3)
	 public SecurityFilterChain adminFilter(HttpSecurity http) throws Exception {
		//RequestCache nullRequestCache = new NullRequestCache(); // Do not save the original request, if authentication is required.
		http
			.securityMatcher("/admin/**")
			.csrf((csrf) -> csrf.disable())
			.authorizeHttpRequests((authorize) -> authorize
				.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
				.requestMatchers("/admin/**").hasAnyAuthority(RoleType.ADMIN.toString(), RoleType.SUPERADMIN.toString())
				.anyRequest().denyAll()
			);

		// Login
		http
			.formLogin(Customizer.withDefaults());

		// Logout
		http
			.logout(logout -> logout
				.addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(Directive.COOKIES)))
				.logoutUrl("/logout")
				.logoutSuccessUrl("/")
				.permitAll()
			);

		// Session management
		http
			.sessionManagement(session -> session
				.maximumSessions(1)
				.maxSessionsPreventsLogin(true)
			);

		return http.build();
	}
}