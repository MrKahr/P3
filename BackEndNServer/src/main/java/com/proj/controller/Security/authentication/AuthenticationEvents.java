package com.proj.controller.security.authentication;

// Spring necessities
import org.springframework.stereotype.Component;

// Spring Events
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;


/**
 * Listen for specific events fired from Spring Security.
 * <p>
 * Experimental.
 * @see https://docs.spring.io/spring-security/reference/servlet/authentication/events.html
 */
@Component
public class AuthenticationEvents {
	@EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
		// ...
    }

    @EventListener
    public void badCredentials(AuthenticationFailureBadCredentialsEvent badcredits){
        // ...
    }


    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failures) {
		// ...
    }
}