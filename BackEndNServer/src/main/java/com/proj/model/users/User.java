package com.proj.model.users;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * This class represents a user and contains fields for all the possible roles a user can have.
 * It is set up to keep this info as objects that are subclasses of Role.
 * Basic user info is also kept here. It is also an object, for consistency's sake, but could be defined as a few fields of another type.
 */
@Entity
public class User {
    // Field
    @JdbcTypeCode(SqlTypes.JSON)
    private BasicUserInfo basicUserInfo;    //Required
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @JdbcTypeCode(SqlTypes.JSON)
    private Guest guestInfo;                //Optional
    @JdbcTypeCode(SqlTypes.JSON)
    private Member memberInfo;              //Optional
    @JdbcTypeCode(SqlTypes.JSON)
    private DM dmInfo;                      //Optional
    @JdbcTypeCode(SqlTypes.JSON)
    private Admin adminInfo;                //Optional
    @JdbcTypeCode(SqlTypes.JSON)
    private SuperAdmin superAdminInfo;      //Optional
    @JdbcTypeCode(SqlTypes.JSON)
    private RoleBackups roleBackups;        //Required

    //Constructor
    public User(){} // Required by jackson to deserialize object 

    public User(BasicUserInfo basicUserInfo){
        this.basicUserInfo = basicUserInfo;
        this.roleBackups = new RoleBackups(this);
    }

    public User(String userName, String password){
        this.basicUserInfo = new BasicUserInfo(userName, password);
        this.roleBackups = new RoleBackups(this);
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
    
    public Integer getId(){
        return this.id;
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


    public RoleBackups getRoleBackups(){
        return this.roleBackups;
    }

    /**
     * Gets a role object (or null) from a specified user. Remember to expand this method when adding new roles!
     * @param type The role's type. Should be the same as the return value for the role's getRoleType()-method.
     * @return A Role-object corresponding to the given type, or null, if no such object is present on the user.
     * @throws IllegalArgumentException Thrown if the given RoleType has not been accounted for.
     */
    public Role getRoleByType(RoleType type){
        switch (type) {
            case GUEST:
                return this.getGuestInfo();
            case MEMBER:
                return this.getMemberInfo();
            case DM:
                return this.getDmInfo();
            case ADMIN:
                return this.getAdminInfo();
            case SUPERADMIN:
                return this.getSuperAdminInfo();
            default:
                throw new IllegalArgumentException("roleType not recognized!");
        }

    /*Makes a deep copy of a user object that can be sanitized, and sent back to the front end to avoid security risks */
    @Override
    public User clone() throws CloneNotSupportedException {
        User clonedUser = new User(getBasicUserInfo());
        clonedUser.setGuestInfo(getGuestInfo());
        clonedUser.setMemberInfo(getMemberInfo());
        clonedUser.setAdminInfo(getAdminInfo());
        clonedUser.setSuperAdminInfo(getSuperAdminInfo());
        clonedUser.setDmInfo(getDmInfo());
        return clonedUser;
    }
}
