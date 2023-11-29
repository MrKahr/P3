package com.proj.validators;

import com.proj.model.users.User;

 public interface Validatable {
   public abstract boolean ValidateStringField(User user);
   public abstract void nextValidator(Validatable validator);
}
