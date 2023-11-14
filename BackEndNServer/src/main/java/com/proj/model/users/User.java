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
    public User(BasicUserInfo basicUserInfo){
        this.basicUserInfo = basicUserInfo;
    }

    // Method
    public void setBasicUserInfo(BasicUserInfo basicUserInfo){    //remove information by passing null to the setter-methods.
        this.basicUserInfo = basicUserInfo;
    }

    public void setGuestInfo(Guest guestInfo){
        this.guestInfo = guestInfo;
    }
    
    public void setMemberInfo(Member memberInfo){
        this.memberInfo = memberInfo;
    }
    
    public void setDmInfo(DM dmInfo){
        this.dmInfo = dmInfo;
    }
    
    public void setAdminInfo(Admin adminInfo){
        this.adminInfo = adminInfo;
    }
    
    public void setSuperAdminInfo(SuperAdmin superAdminInfo){
        this.superAdminInfo = superAdminInfo;
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
