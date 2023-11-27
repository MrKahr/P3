package com.proj.validators;


 interface Validatable {
   public abstract void HandleElement(Object element);
   public abstract void nextValidator(Validatable validator);
}
