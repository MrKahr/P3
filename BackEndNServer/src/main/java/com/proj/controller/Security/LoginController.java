package com.proj.controller.Security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

	private final AuthenticationManager authenticationManager;

	public LoginController(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	/**
	 * Serves the login page when receiving a GET request to the login page. 
	 * @return
	 */
	@GetMapping("/login")
	public @ResponseBody String showLoginPage(){
		// This String maps to loginPage.html in "/FrontEnd/**" as defined in ressources/application.properties.
		// Note: This will not work with @RestController
		return "loginPage"; 
	} 

	/**
	 * Authenticates a user with the supplied user info in a POST request to the login page.
	 * @param loginRequest The supplied user info from the frontend
	 * @return If login succesful, redirect to the page that was initially requested.
	 */
	@PostMapping("/login")
	public @ResponseBody String login(@RequestBody LoginRequest loginRequest) {
		Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
		Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
		
		// TODO: Handle all types of login falied.
		
		//return ResponseEntity.ok(authenticationResponse);
		return "redirect:/login"; // 
	}

	public record LoginRequest(String username, String password) {
	}

}
