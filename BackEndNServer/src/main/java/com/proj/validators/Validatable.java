package com.proj.validators;

import com.proj.model.users.User;

 public interface Validatable {
   public abstract User ValidateStringField(String string);
}
