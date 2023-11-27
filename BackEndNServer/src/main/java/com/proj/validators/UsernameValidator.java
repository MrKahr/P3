package com.proj.validators;

public class UsernameValidator implements Validatable {
    private Validatable nextValidator; 

    @Override
    public void HandleElement(Object element) {
        if(!element instanceof String){
        } else if (){

        } else if(){
            
        }
        
    }

    @Override 
    public void nextValidator(Validatable validator){
        this.nextValidator = validator;
    }
    

}
