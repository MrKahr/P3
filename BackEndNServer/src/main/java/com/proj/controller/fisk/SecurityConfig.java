package com.proj.controller.fisk;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.session.HttpSessionEventPublisher;


/**
 * Configuration for general security components.
 */
@Configuration
public class SecurityConfig {
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
