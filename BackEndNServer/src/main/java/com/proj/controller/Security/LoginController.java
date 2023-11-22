package com.proj.controller.Security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


// Spring Boot Reference Docs: https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#web


// What is Thymeleaf? -> A View technology. A way to serve e.g. html to the frontend.
// What is Thymeleaf's relation to Spring? -> https://stackoverflow.com/a/52660274
// Docs: https://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html
// Getting started in Spring: https://spring.io/guides/gs/serving-web-content/


@Controller
public class LoginController {

	private final AuthenticationManager authenticationManager;

	public LoginController(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@GetMapping("/login")
	public @ResponseBody String showLoginPage(){
		// This String maps to loginPage.html in "/FrontEnd/**" as defined in ressources/application.properties.
		// Note: This will not work with @RestController
		return "loginPage"; 
	} 


	@PostMapping("/login")
	public @ResponseBody ResponseEntity<Authentication> login(@RequestBody LoginRequest loginRequest) {
		Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
		Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
		
		return ResponseEntity.ok(authenticationResponse);
	}

	public record LoginRequest(String username, String password) {
	}

}
