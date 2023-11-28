package com.proj.validators;

 public interface Validatable {
   public abstract boolean HandleString(String element);
   public abstract void nextValidator(Validatable validator);
}
