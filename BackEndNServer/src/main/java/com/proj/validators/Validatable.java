package com.proj.validators;

import com.proj.model.users.User;

 public interface Validatable<T> {
   public abstract T ValidateStringField(String string);
}
