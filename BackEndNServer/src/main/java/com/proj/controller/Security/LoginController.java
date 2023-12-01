package com.proj.controller.security;

import java.net.URI;

import org.hibernate.engine.transaction.jta.platform.internal.OC4JJtaPlatform;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
// Authentication
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

// Controller
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletResponse;

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
	public ModelAndView showLoginPage(){
		
		ModelAndView model = new ModelAndView("authentication/loginPage");
		model.addObject("Username", "Thymeleaf");
		return model; 
	} 

	/**
	 * Invokes AuthenticationManager with the supplied user info received in a POST request to the login page.
	 * @param loginRequest The supplied user info from the frontend
	 * @return If login succesful, redirect to the page that was initially requested.
	 */
	@PostMapping("/login")
	public ModelAndView login(@RequestBody LoginRequest loginRequest) {
		Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
		Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
		
		// TODO: Handle all types of login falied.
		// See SecurityFilters.java

		ModelAndView model = new ModelAndView();
		

		if(authenticationResponse.isAuthenticated()){
			return new ModelAndView("redirect:/");
		}
		return new ModelAndView("redirect:/fisk");
	}

	public record LoginRequest(String username, String password) {
	}
}
