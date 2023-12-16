package com.proj.controller.security;

// Spring necessities
import org.springframework.beans.factory.annotation.Autowired;

// Spring Security
import org.springframework.security.core.Authentication;

// Controller
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

// Servlet
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Our classes
import com.proj.controller.security.authentication.AuthenticationProcess;

/**
 * The login controller handles requests to the login endpoint on our site.
 */
@Controller
public class LoginController {

	// Field
	@Autowired
	private AuthenticationProcess authenticationProcess;

	// Method
	/**
	 * Serves the login page when receiving a GET request to the login endpoint. 
	 * @return
	 */
	@GetMapping("/login")
	public String showLoginPage(){
		return "authentication/loginPage";
	} 

	/**
	 * Invokes AuthenticationProcess with the supplied user info received in a POST request to the login page.
	 * To enable form support, insert this in postmapping: ", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}" AND remove "@Requestbody"
	 * @param loginRequest The supplied user info from the frontend
	 * @return TODO: If login succesful, redirect to the page that was initially requested.
	 */
	@PostMapping(path = "/login")
	public ModelAndView login (@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
		
		Authentication authentication = authenticationProcess.authenticate(loginRequest, request, response); // Authenticates the user

		// If login is successful, return this.
		return new ModelAndView(new RedirectView("/", true));
	}

	/**
	 * The object used to match input from the frontend. 
	 * The parameters contained in the object sent from the frontend must match the parameters of this object exactly (or vice versa)
	 */
	public record LoginRequest(String username, String password) {}

	
}
