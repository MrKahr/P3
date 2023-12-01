package com.proj.model.users;

//ordinal values are kept default on purpose! If other values must be assigned to the types, define a method to covert them.
public enum RoleType {
  GUEST,
  MEMBER,
  DM,
  ADMIN,
  SUPERADMIN,
  BADTYPE      //this one exists to make errors when testing. It is deliberately not accounted for by any code that handles roles.
}