package com.proj.controller.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.proj.controller.security.LoginController.LoginRequest;
import com.proj.controller.security.authentication.AuthenticationProcess;
import com.proj.function.UserManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class SignupController {
	
    // Field
    @Autowired
	private AuthenticationProcess authenticationProcess;
    
    @Autowired
    private UserManager userManager;

    // Method

    /**
     * Serves the signup page when receiving a GET request to the signup endpoint. 
     * @return
     */
    @GetMapping("/signup")
    public String showSignupPage(){
        return "authentication/signupPage";
    }

	@PostMapping(path = "/signup")
	public ModelAndView login (@RequestBody SignupRequest signupRequest, HttpServletRequest request, HttpServletResponse response) {

        userManager.createAccount(signupRequest.username, signupRequest.password);


        if(signupRequest.signUpAsMember){
            userManager.requestMembership(signupRequest.fullName, signupRequest.phone, signupRequest.postalCode, 
                                          signupRequest.address, signupRequest.email, signupRequest.username);
        }

        LoginRequest loginRequest = new LoginRequest(signupRequest.username, signupRequest.password);
		Authentication authentication = authenticationProcess.authenticate(loginRequest, request, response); // Authenticates the user

		// If signup is successful, return this.
		return new ModelAndView(new RedirectView("/", true));
	}

	/**
	 * The object used to match input from the frontend. 
	 * The parameters contained in the object sent from the frontend must match the parameters of this object exactly (or vice versa)
	 */
	public record SignupRequest(String username, String password, String confirmPassword,
                                String email, String fullName, String address, String phone,
                                String postalCode, boolean signUpAsMember) {}

}
