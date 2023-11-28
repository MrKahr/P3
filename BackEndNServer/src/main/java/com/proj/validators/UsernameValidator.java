package com.proj.validators;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
// See -  https://stackoverflow.com/questions/3533408/regex-i-want-this-and-that-and-that-in-any-order
// ^(?=.{8,50}$)(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=\S).*$

public class UsernameValidator implements Validatable {
    // Field
    private Validatable nextValidator;
    // https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html
    // Make pattern to match and initialize variable to get matcher.
    private static Pattern p = Pattern.compile("^(\\w{4,30})");
    private Matcher m;

    // Constructor
    public UsernameValidator(){};

    // Method
    @Override
    public boolean HandleString(String username) {
        boolean isValid;
        // Check whether string matches
        m = p.matcher(username);
        isValid = m.matches();

        if (!isValid) {
            return isValid;
        } else if (nextValidator != null) {
            nextValidator.HandleString(username);
        }
        return isValid;
    }

    @Override 
    public void nextValidator(Validatable validator){
        this.nextValidator = validator;
    }
}
