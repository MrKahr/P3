package com.proj.controller.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.session.HttpSessionEventPublisher;


/**
 * Configuration for general security components.
 */
@Configuration
public class SecurityConfig {

    // Doesn't seem to work as intended. Intention was to set all cookies to "SameSite=strict"
    // @Bean
    // public CookieSameSiteSupplier applicationCookieSameSiteSupplier() {
    //     return CookieSameSiteSupplier.ofStrict();
    // }
    
 	/**
	 * Enables Spring Security to listen to http requests. Used to limit max concurrent login sessions in SecurityFilterChain.
	 * @return
	 * @see https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html#ns-concurrent-sessions
	 */
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}   

}
