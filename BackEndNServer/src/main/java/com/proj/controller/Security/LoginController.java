package com.proj.controller.security;

// Spring necessities
import org.springframework.beans.factory.annotation.Autowired;

// Spring Security
import org.springframework.security.core.Authentication;

// HTTP
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;


// Controller
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

// Servlet
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class LoginController {

	// Field
	@Autowired
	private AuthenticationProcess authenticationProcess;

	// Method
	/**
	 * Serves the login page when receiving a GET request to the login page. 
	 * @return
	 */
	// @GetMapping("/login")
	// public ModelAndView showLoginPage(){
		
	// 	ModelAndView model = new ModelAndView("authentication/loginPage");
	// 	model.addObject("Username", "Thymeleaf");
	// 	return model; 
	// } 

	// login page
    @GetMapping("/loginpage")
    public String showLoginPage(){
        return "authentication/loginPage";
    }
	// signup page
    @GetMapping("/signuppage")
    public String showSignupPage(){
        return "signuppage";
    }

	/**
	 * Invokes AuthenticationProcess with the supplied user info received in a POST request to the login page.
	 * @param loginRequest The supplied user info from the frontend
	 * @return TODO: If login succesful, redirect to the page that was initially requested.
	 */
	@PostMapping("/login")
	public ModelAndView login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
		
		Authentication authentication = authenticationProcess.authenticate(loginRequest, request, response); // Authenticates the user

		// TODO: Handle all types of login falied.
		// See SecurityFilters.java

		if(authentication.isAuthenticated()){
			return new ModelAndView(new RedirectView("/", true));
		}
		return new ModelAndView("redirect:/fisk");
	}

	public record LoginRequest(String username, String password) {
	}
}
