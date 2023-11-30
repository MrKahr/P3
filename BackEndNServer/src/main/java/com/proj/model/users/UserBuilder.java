package com.proj.model.users;

import java.util.ArrayList;

import com.proj.function.RoleAssigner;

/**
 * Builds a user by adding each element wrapper object to a user object.
 * @pre-con elements passed to builder are validated
 */
public class UserBuilder {
    // Field
    private User user;

    // Constructor
    UserBuilder(User user) {
        this.user = user;
    }

    // Method
    public UserBuilder BasicUserInfo(String username, String password) {
        BasicUserInfo basicUserInfo = new BasicUserInfo(password, username);
        this.user.setBasicUserInfo(basicUserInfo);
        return this;
    };

    public UserBuilder BuildGuestInfo(String characterInfo) {
        RoleAssigner.setRole(this.user, new Guest(characterInfo));
        return this;

    };

    public UserBuilder BuildMemberInfo(String realName, String phoneNumber,String postalCode, String address, String email){
        RoleAssigner.setRole(user, new Member(realName, phoneNumber,postalCode, address, email));
        return this;
    };

    public UserBuilder BuildDmInfo(ArrayList<String> hostedSessions) {
        RoleAssigner.setRole(user, new DM(hostedSessions));
        return this; 
    };

    public UserBuilder BuildAdminInfo(ArrayList<String> listOfRoleChanges, ArrayList<String> listOfBans){
        RoleAssigner.setRole(user, new Admin(listOfRoleChanges, listOfBans));
        return this;
    };

    public UserBuilder BuildSuperAdminInfo() {
        RoleAssigner.setRole(user, new SuperAdmin());
        return this;
    }

    public User getUser(){
        return this.user;
    }
}
