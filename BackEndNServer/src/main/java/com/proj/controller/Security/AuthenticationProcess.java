package com.proj.controller.security;

// Spring necessities
import org.springframework.stereotype.Component;

// Authentication
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

// SecurityContext
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

// Servlet
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Our classes
import com.proj.controller.security.LoginController.LoginRequest;


@Component
public class AuthenticationProcess {
    
    // Field
	private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

	private final AuthenticationManager authenticationManager;

    // Constructor
    public AuthenticationProcess(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    // Method
    /**
     * Authenticates the user by calling the necessary methods to do so.
     * The "actual" authentication is handled by Spring Security.
     * @param loginRequest The LoginRequest received from the frontend
     * @return The Authentication object containing e.g. the response.
     * @see https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html
     * @see https://docs.spring.io/spring-security/reference/servlet/authentication/persistence.html
     */
    public Authentication authenticate(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response){
        // Makes an authentication token with the unauthenticated creditials from the frontend.
		UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
		
        // Passes the authentication token to the authentication manager for authentication.
        Authentication authentication = this.authenticationManager.authenticate(token);

        // Sets the security context which is used to create login sessions.
		SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
		context.setAuthentication(authentication);
		this.securityContextHolderStrategy.setContext(context);

        // Save the securityContext in a repository managed by Spring Security. It is possible to manage such a repo manually.
        securityContextRepository.saveContext(context, request, response);

        return authentication;
    }
}