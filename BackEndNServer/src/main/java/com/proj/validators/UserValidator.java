package com.proj.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.proj.exception.FailedValidationException;
import com.proj.model.users.User;

public class UserValidator implements Validatable {
    // Field
    private User user;
    // https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html
    // Make pattern to match and initialize variable to get matcher.
    private static Pattern genericStringPattern = Pattern.compile("^(\\\\w)");
    private static Pattern userNamePattern = Pattern.compile("^(\\w{4,30})");
    private static Pattern passwordPattern = Pattern
            .compile("^(?=.{4,30}$)(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*['+-^&%¤$]).*$");
    private static Pattern emailPattern = Pattern.compile("^([\\w-]{1,})(\\w[@]\\w{1,})([.]\\w{2,4}$)");
    private Matcher m;

    // Constructor
    public UserValidator(User user) {
        this.user = user;
    };

    // Method
    public User getUser() {
        return user;
    }

    @Override
    public User ValidateStringField(String string) throws NullPointerException {
        if (string.equals(null)) {
            throw new NullPointerException("Cannot validate null string");
        } else {

            boolean isValidString;

            // Check whether generic string matches the regex pattern
            m = genericStringPattern.matcher(string);
            isValidString = m.matches();

            if (isValidString) {
                return this.user;
            } else {
                throw new FailedValidationException("Generic String is not valid");
            }
        }

    }

    public UserValidator ValidateUserName() throws NullPointerException, FailedValidationException {
        if (getUser() == null) {
            throw new NullPointerException("Cannot validate null user");
        } else if (getUser().getBasicUserInfo() == null) {
            throw new NullPointerException("Cannot validate null basicUserInfo");
        } else if (getUser().getBasicUserInfo().getUserName() == null) {
            throw new NullPointerException("Cannot validate null Username");
        } else {
            boolean isValidUserName;
            // Check whether username matches the regex pattern
            m = userNamePattern.matcher(getUser().getBasicUserInfo().getUserName());
            isValidUserName = m.matches();

            if (isValidUserName) {
                return this;
            } else {
                throw new FailedValidationException("Username is not valid");
            }
        }
    }

    public UserValidator ValidatePassword() throws NullPointerException, FailedValidationException {
        if (getUser() == null) {
            throw new NullPointerException("Cannot validate null user");
        } else if (getUser().getBasicUserInfo() == null) {
            throw new NullPointerException("Cannot validate null basicUserInfo");
        } else if (getUser().getBasicUserInfo().getPassword() == null) {
            throw new NullPointerException("Cannot validate null password");
        } else {
            boolean isValidPassword;
            // Check whether password matches the regex pattern
            m = passwordPattern.matcher(getUser().getBasicUserInfo().getPassword());
            isValidPassword = m.matches();

            if (isValidPassword) {
                return this;
            } else {
                throw new FailedValidationException("Password is not valid");
            }
        }
    }
    public UserValidator ValidatePhoneNumber(){
        //TODO: Insert correct validator code 
        return this;
    }

    public UserValidator ValidatePostCode(){
        //TODO: Insert correct validator code 
        return this;
    }

    public UserValidator AddressValidator(){
        //TODO: Insert correct validator code 
        return this;
    }
 
    public UserValidator ValidateEmail() {
        if (getUser() == null) {
            throw new NullPointerException("Cannot validate null user");
        } else if (getUser().getMemberInfo() == null) {
            throw new NullPointerException("Cannot validate null memberInfo");
        } else if (getUser().getMemberInfo().getEmail() == null) {
            throw new NullPointerException("Cannot validate null email");
        } else {
            boolean isValidEmail;
            // Check whether password matches the regex pattern
            m = emailPattern.matcher(getUser().getMemberInfo().getEmail());
            isValidEmail = m.matches();

            if (isValidEmail) {
                return this;
            } else {
                throw new FailedValidationException("Email is not valid");
            }
        }
    }
}
