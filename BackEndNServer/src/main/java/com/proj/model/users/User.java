package com.proj.model.users;
import java.time.Duration;
import java.time.LocalDate;

/**
 * This class represents a user and contains fields for all the possible roles a user can have.
 * It is set up to keep this info as objects that are subclasses of Role.
 * Basic user info is also kept here. It is also an object, for consistency's sake, but could be defined as a few fields of another type.
 */
public class User {
    // Field
    private BasicUserInfo basicUserInfo;    //Required
    private Guest guestInfo;                //Optional
    private Member memberInfo;              //Optional
    private DM dmInfo;                      //Optional
    private Admin adminInfo;                //Optional
    private SuperAdmin superAdminInfo;      //Optional

    //Constructor
    public User(BasicUserInfo obj){
        this.basicUserInfo = obj;
    }

    // Method
    public void setBasicUserInfo(BasicUserInfo obj){    //remove information by passing null to the setter-methods.
        this.basicUserInfo = obj;
    }

    public void setGuestInfo(Guest obj){
        this.guestInfo = obj;
    }
    
    public void setMemberInfo(Member obj){
        this.memberInfo = obj;
    }
    
    public void setDmInfo(DM obj){
        this.dmInfo = obj;
    }
    
    public void setAdminInfo(Admin obj){
        this.adminInfo = obj;
    }
    
    public void setSuperAdminInfo(SuperAdmin obj){
        this.superAdminInfo = obj;
    }

    public BasicUserInfo getBasicUserInfo(){
        return this.basicUserInfo;
    }
    
    public Guest getGuestInfo(){
        return this.guestInfo;
    }

    public Member getMemberInfo(){
        return this.memberInfo;
    }

    public DM getDmInfo(){
        return this.dmInfo;
    }

    public Admin getAdminInfo(){
        return this.adminInfo;
    }
    
    public SuperAdmin getSuperAdminInfo(){
        return this.superAdminInfo;
    }
}
