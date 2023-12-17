package com.proj.controller.api;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proj.controller.security.authentication.UserDAO;

/**
 * Part of our API to send data to frontend.
 * The /api endpoint is public by default.
 * Better not send sensitive info to the requester.
 */
@Controller
@RequestMapping("/api")
public class GetUserSessionAPI {
    @Autowired
    private UserDAO userDAO;

    /**
     * Looks up the requesting user's login session if requested.
     * @param getLoginSession Request to get login session info. Must be true to activate method.
     * @param authentication The user's authenticaion info from their securityContext.
     * @return True if user is logged in. False if not. Null if getLoginSession is false (should be disregarded).
     */
    @GetMapping(path = "/user")
    public ResponseEntity<?> getUserSession(@RequestParam boolean getLoginSession, @CurrentSecurityContext(expression = "authentication") Authentication authentication){
        
        ArrayList<String> list = new ArrayList<String>();
        if(getLoginSession){
            if(userDAO.checkAuthority(authentication, "ROLE_ANONYMOUS")){
                list.add("false");
                return new ResponseEntity<>(list, HttpStatus.OK);
            }
            else{
                list.add("true");
                list.add(authentication.getName().toString());
                return new ResponseEntity<>(list, HttpStatus.OK);
            }
        }
        list.add("null"); // If a request is made to this endpoint but the requestparam is not true, then return null.
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
