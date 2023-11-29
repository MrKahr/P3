package com.proj.controller.security;

// Authentication
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

// Controller
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
	public String showLoginPage(){
		return "authentication/loginPage"; 
	} 

	/**
	 * Invokes AuthenticationManager with the supplied user info received in a POST request to the login page.
	 * @param loginRequest The supplied user info from the frontend
	 * @return If login succesful, redirect to the page that was initially requested.
	 */
	@PostMapping("/login")
	public String login(@RequestBody LoginRequest loginRequest) {
		Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
		Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
		
		// TODO: Handle all types of login falied.
		
		//return ResponseEntity.ok(authenticationResponse);
		return "redirect:/login";
	}

	public record LoginRequest(String username, String password) {
	}
}
