package com.proj.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.proj.model.users.User;

public class EmailValidator implements Validatable{
        // Field 
    private Validatable nextValidator;
    // https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html
    // Make pattern to match and initialize variable to get matcher.
    private static Pattern p = Pattern.compile("^([\\w-]{1,})(\\w[@]\\w{1,})([.]\\w{2,4}$)");
    private Matcher m;

    // Constructor
    public EmailValidator(){};
    
    // Method
    @Override
    public boolean ValidateStringField(User user) {
        boolean isValid;
        // Check whether string matches
        m = p.matcher(user.getMemberInfo().getEmail());
        isValid = m.matches();

        if (!isValid) {
            return isValid;
        } else if (nextValidator != null) {
            nextValidator.ValidateStringField(user);
        }
        return isValid;
    }
    @Override 
    public void nextValidator(Validatable validator){
        this.nextValidator = validator;
    }
}
