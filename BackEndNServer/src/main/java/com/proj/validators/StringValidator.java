package com.proj.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringValidator implements Validatable {
    protected Validatable nextValidator;
    private static Pattern p = Pattern.compile("(\\w{1,})");
    private Matcher m;

    @Override 
    public boolean HandleString(String string){
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
    };
    @Override 
    public void nextValidator(Validatable validator){
        this.nextValidator = validator;
    }
}
