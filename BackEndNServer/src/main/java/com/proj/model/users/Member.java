package com.proj.model.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//The constructor for this method used to use constructor chaining. This link has some info on that
//https://docs.oracle.com/javase/tutorial/java/IandI/super.html

/**
 * A class that represents a user with the access level "Member". 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member extends Role {
    // Field
    private String realName;
    private String phoneNumber;
    private String postalCode;
    private String address;
    private String email;


    // Constructor 
    /** Creates a Member object with Name, phoneNumber, postalCode, address, and email.
     * @Pre-con: Expects all arguments to be sanitized.
     * @param realName The user's full name 
     * @param phoneNumber The user's mobile phone number 
     * @param postalCode The postal code associated with a users home address 
     * @param address The user's physical address
     * @param email The user's email address
     */
    public Member(String realName, String phoneNumber, String postalCode, String address, String email){
        this.realName = realName;
        this.phoneNumber = phoneNumber;
        this.postalCode = postalCode;
        this.address = address;
        this.email = email;
    }

    public Member(){}

    // Method
    public String getRealName() {
        return realName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public RoleType getRoleType(){
        return RoleType.MEMBER;
    }

    @Override
    public RoleType[] getRoleDependencies(){
        RoleType[] types = {RoleType.GUEST};
        return types;
    }
}
