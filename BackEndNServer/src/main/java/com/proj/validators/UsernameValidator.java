package com.proj.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
