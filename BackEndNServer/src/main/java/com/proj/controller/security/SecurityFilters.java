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
	// 		We have disabled it as trying to implement it took longer than we had allocated time for. 
	//		As per our report, we did not prioritise security features highly, hence why we decided against spending more time to implement it. 
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
	// Howto: Redirect to requested page upon successful login or prevent it: https://docs.spring.io/spring-security/reference/servlet/architecture.html#requestcache-prevent-saved-request


	// ############################
	// #### Exception Handling ####
	// ############################
	// These exceptions need to be handled:
	// (Use Advisors for this)
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
		// http
		// .csrf((csrf) -> csrf.disable())
		// .authorizeHttpRequests((authorize) -> authorize /* All requests most be authorized */
		// 	.anyRequest().permitAll() /* Allow anything */
		// );
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
		.securityMatcher("/css/**", "/login/**", "/", "/js/**", "/images/**", "/favicon.ico", "/error") /* All Http requests matching these paths will trigger this filter */
		.csrf((csrf) -> csrf.disable())
		.authorizeHttpRequests((authorize) -> authorize /* All requests most be authorized */
			.anyRequest().permitAll() /* Allow anything */
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
		.securityMatcher("/private/**") /* All Http requests matching these paths will trigger this filter */
		.csrf((csrf) -> csrf.disable())
		.authorizeHttpRequests((authorize) -> authorize /* All requests most be authorized */
			.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll() /* A request is actually a dispatch. Always allow these types of dispatches */
			.anyRequest().denyAll() /* Deny anything */
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
		// .csrf((csrf) -> csrf
		// 	.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		// );

		// Disable CSRF
		http.csrf((csrf) -> csrf.disable());


		// Authorization
		http
		.authorizeHttpRequests((authorize) -> authorize /* All requests most be authorized */
			.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll() /* A request is actually a dispatch. Always allow these types of dispatches */
			.requestMatchers("/user/**").permitAll() // TODO: For testing purposes
			.anyRequest().authenticated() /* All requests require login */
		);
		
		// Login
		http
		//.formLogin(Customizer.withDefaults()); // TODO: Check form login methods in Spring Docs
		.formLogin(form -> form /* Login is of type FormLogin */
			.loginPage("/login") /* Specify endpoint for login page. Spring Security will redirect to this. */
			//.loginProcessingUrl("/login")
			//.defaultSuccessUrl("/", true)
			.permitAll() /* Always allow requests to login */
		);

		// Logout
		http
		.logout(logout -> logout /* Customize logout functionality */
			.addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(Directive.COOKIES))) /* Tell the client to clear stored cookies from our site */
			.logoutUrl("/login?logout") /* Specify logout endpoint. A request to this will automatically logout the user */
			.logoutSuccessUrl("/") /* If logout is successful, redirect to this endpoint */
			.permitAll() /* Always allow requests to logout */
		);

		// Session management
		http
		.sessionManagement(session -> session /* Customize session management (login session) */
			.maximumSessions(1) /* Specify number of concurrent login sessions (how many times can a user be logged in?) */
			.maxSessionsPreventsLogin(true) /* Should exceeding maximumSessions prevent a new login attempt? */
		);
		return http.build();
	}

	/**
	 * Requires all requests to URLs specified in securityMatcher to require a role of admin+.
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
		.securityMatcher("/admin/**") /* All Http requests matching these paths will trigger this filter */
		.csrf((csrf) -> csrf.disable())
		.authorizeHttpRequests((authorize) -> authorize /* All requests most be authorized */
			.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll() /* A request is actually a dispatch. Always allow these types of dispatches */
			.requestMatchers("/admin/**").hasAnyAuthority(RoleType.ADMIN.toString(), RoleType.SUPERADMIN.toString()) /* All http requests matching these paths must have the required role(s) to proceed */
			.anyRequest().denyAll() /* Deny anything */
		);

		// Login
		http
		//.formLogin(Customizer.withDefaults()); // TODO: Check form login methods in Spring Docs
		.formLogin(form -> form /* Login is of type FormLogin */
			.loginPage("/login") /* Specify endpoint for login page. Spring Security will redirect to this. */
			//.loginProcessingUrl("/login")
			//.defaultSuccessUrl("/", true)
			.permitAll() /* Always allow requests to login */
		);

		// Logout
		http
		.logout(logout -> logout /* Customize logout functionality */
			.addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(Directive.COOKIES))) /* Tell the client to clear stored cookies from our site */
			.logoutUrl("/login?logout") /* Specify logout endpoint. A request to this will automatically logout the user */
			.logoutSuccessUrl("/") /* If logout is successful, redirect to this endpoint */
			.permitAll() /* Always allow requests to logout */
		);

		// Session management
		http
		.sessionManagement(session -> session /* Customize session management (login session) */
			.maximumSessions(1) /* Specify number of concurrent login sessions (how many times can a user be logged in?) */
			.maxSessionsPreventsLogin(true) /* Should exceeding maximumSessions prevent a new login attempt? */
		);

		return http.build();
	}

	@Order(5)
	@Bean 
	public SecurityFilterChain dmFilter(HttpSecurity http) throws Exception{
		// Match all sites with addresses starting with /dm/
		http
		.securityMatcher("/dm/**")
		.csrf((csrf) -> csrf.disable()) // Disable csrf 
		.authorizeHttpRequests((authorize) -> authorize // Authorize user 
			.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll() // Allow all dispatches of the correct type
			.requestMatchers("/dm/**").hasAnyAuthority(RoleType.DM.toString())
			.anyRequest().denyAll() // Deny requests from users that do not have the necessary authorization
		);

		return http.build();
	}
}
