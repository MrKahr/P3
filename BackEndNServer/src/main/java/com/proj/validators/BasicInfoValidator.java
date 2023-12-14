package com.proj.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.proj.exception.FailedValidationException;
import com.proj.model.users.BasicUserInfo;

public class BasicInfoValidator implements Validatable<BasicInfoValidator> {
    // Field
    private BasicUserInfo basicUserInfo;
    // https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html
    // Make pattern to match and initialize variable to get matcher.
    private static Pattern genericStringPattern = Pattern.compile("^(\\\\w)$");
    private static Pattern userNamePattern = Pattern.compile("^(\\w{4,30})$");
    private static Pattern passwordPattern = Pattern
            .compile("^(?=.{4,30}$)(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*['+-^&%¤$]).*$");
    private Matcher m;

    // Constructor
    public BasicInfoValidator(BasicUserInfo basicUserInfo) {
        this.basicUserInfo = basicUserInfo;
    };

    // Method
    public BasicUserInfo getBasicUserInfo() {
        return basicUserInfo;
    }

    public void setBasicUserInfo(BasicUserInfo basicUserInfo) {
        this.basicUserInfo = basicUserInfo;
    }

    @Override
    /**
     * Validates a generic string field consisting of at least one element
     * 
     * @return the current basicuservalidator. This is necessary for method chaining.
     * @throws NullPointerException
     * @throws FailedValidationException
     */
    public BasicInfoValidator ValidateStringField(String string)
            throws NullPointerException, FailedValidationException {
        if (string == null) {
            throw new NullPointerException("Cannot validate null string");
        } else {

            boolean isValidString;

            // Check whether generic string matches the regex pattern
            m = genericStringPattern.matcher(string);
            isValidString = m.matches();

            if (isValidString) {
                return this;
            } else {
                throw new FailedValidationException("Generic String is not valid");
            }
        }

    }

    /**
     * Validates the username attribute of a basic info object.
     * 
     * @return the current basicuservalidator.. This is necessary for method chaining.
     * @throws NullPointerException
     * @throws FailedValidationException
     */
    public BasicInfoValidator ValidateUserName() throws NullPointerException, FailedValidationException {
        if (getBasicUserInfo() == null) {
            throw new NullPointerException("Cannot validate null basicUserInfo");
        } else if (getBasicUserInfo().getUserName() == null) {
            throw new NullPointerException("Cannot validate null Username");
        } else {
            boolean isValidUserName;
            // Check whether username matches the regex pattern
            m = userNamePattern.matcher(getBasicUserInfo().getUserName());
            isValidUserName = m.matches();

            if (isValidUserName) {
                return this;
            } else {
                throw new FailedValidationException("Username is not valid");
            }
        }
    }

    /**
     * Validates the username attribute of a basic info object.
     * 
     * @return the current basicuservalidator.. This is necessary for method chaining.
     * @throws NullPointerException
     * @throws FailedValidationException
     */
    public BasicInfoValidator ValidatePassword() throws NullPointerException, FailedValidationException {
        if (getBasicUserInfo() == null) {
            throw new NullPointerException("Cannot validate null basicUserInfo");
        } else if (getBasicUserInfo().getPassword() == null) {
            throw new NullPointerException("Cannot validate null password");
        } else {
            boolean isValidPassword;
            // Check whether password matches the regex pattern
            m = passwordPattern.matcher(getBasicUserInfo().getPassword());
            isValidPassword = m.matches();

            if (isValidPassword) {
                return this;
            } else {
                throw new FailedValidationException("Password is not valid");
            }
        }
    }

}
