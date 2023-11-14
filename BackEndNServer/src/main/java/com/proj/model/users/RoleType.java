package com.proj.model.users;

public enum RoleType {
  GUEST,
  MEMBER,
  DM,
  ADMIN,
  SUPERADMIN,
  NOTYPE      //this one exists to make errors when testing. It is deliberately not accounted for by any code that handles roles.
}
