package com.proj.model.users;

import java.util.HashMap;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * This class represents a user and contains fields for all the possible roles a
 * user can have.
 * It is set up to keep this info as objects that are subclasses of Role.
 * Basic user info is also kept here. It is also an object, for consistency's
 * sake, but could be defined as a few fields of another type.
 */
@Entity
public class User implements Cloneable {
    // Field
    @JdbcTypeCode(SqlTypes.JSON)
    private BasicUserInfo basicUserInfo; // Required
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @JdbcTypeCode(SqlTypes.JSON)
    private Guest guestInfo; // Optional
    @JdbcTypeCode(SqlTypes.JSON)
    private Member memberInfo; // Optional
    @JdbcTypeCode(SqlTypes.JSON)
    private DM dmInfo; // Optional
    @JdbcTypeCode(SqlTypes.JSON)
    private Admin adminInfo; // Optional
    @JdbcTypeCode(SqlTypes.JSON)
    private SuperAdmin superAdminInfo; // Optional
    @JdbcTypeCode(SqlTypes.JSON)
    private RoleBackups roleBackups; // Required

    // Constructor
    public User() {
    } // Required by jackson to deserialize object

    public User(BasicUserInfo basicUserInfo) {
        this.basicUserInfo = basicUserInfo;
        this.roleBackups = new RoleBackups();
    }

    public User(String userName, String password) {
        this.basicUserInfo = new BasicUserInfo(userName, password);
        this.roleBackups = new RoleBackups();
    }

    // Method
    // NOTE: ONLY for testing purposes since equality between users is based on Id.
    public void setId(Integer id) {
        this.id = id;
    }

    public void setBasicUserInfo(BasicUserInfo basicUserInfo) { // remove information by passing null to the
                                                                // setter-methods.
        this.basicUserInfo = basicUserInfo;
    }

    public void setGuestInfo(Guest guestInfo) {
        this.guestInfo = guestInfo;
    }

    public void setMemberInfo(Member memberInfo) {
        this.memberInfo = memberInfo;
    }

    public void setDmInfo(DM dmInfo) {
        this.dmInfo = dmInfo;
    }

    public void setAdminInfo(Admin adminInfo) {
        this.adminInfo = adminInfo;
    }

    public void setSuperAdminInfo(SuperAdmin superAdminInfo) {
        this.superAdminInfo = superAdminInfo;
    }

    public BasicUserInfo getBasicUserInfo() {
        return this.basicUserInfo;
    }

    public Integer getId() {
        return this.id;
    }

    public Guest getGuestInfo() {
        return this.guestInfo;
    }

    public Member getMemberInfo() {
        return this.memberInfo;
    }

    public DM getDmInfo() {
        return this.dmInfo;
    }

    public Admin getAdminInfo() {
        return this.adminInfo;
    }

    public SuperAdmin getSuperAdminInfo() {
        return this.superAdminInfo;
    }

    public RoleBackups getRoleBackups() {
        return this.roleBackups;
    }

    /**
     * Gets a role object (or null) from a specified user. Remember to expand this
     * method when adding new roles!
     * 
     * @param type The role's type. Should be the same as the return value for the
     *             role's getRoleType()-method.
     * @return A Role-object corresponding to the given type, or null, if no such
     *         object is present on the user.
     * @throws IllegalArgumentException Thrown if the given RoleType has not been
     *                                  accounted for.
     */
    public Role getRoleByType(RoleType type) {
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
    }

    /**
     * Checks whether two users are equal based on their id.
     * 
     * @param obj - object to compare to user
     * @throws NullPointerException if obj is null
     * @return true if they have identical ids and false if they do not
     */
    @Override
    public boolean equals(Object obj) throws NullPointerException {
        if (obj == null) {
            return false;
        } else if(this.getId() == null || ((User) obj).getId() == null){
            return false;
        } else {
            return this.getId() == ((User) obj).getId();
        }

    }

    /*
     * Makes a deep copy of a user object that can be sanitized, and sent back to
     * the front end to avoid security risks
     */
    @Override
    public User clone() throws CloneNotSupportedException {
        User clonedUser = new User(getBasicUserInfo());
        clonedUser.setGuestInfo(getGuestInfo());
        clonedUser.setMemberInfo(getMemberInfo());
        clonedUser.setDmInfo(getDmInfo());
        clonedUser.setAdminInfo(getAdminInfo());
        clonedUser.setSuperAdminInfo(getSuperAdminInfo());
        clonedUser.setId(getId());
        return clonedUser;
    }

    /**
     * Finds all non-null roles and maps a role and a roletype in a key value pair -
     * for hashmap documentation see
     * https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html
     * 
     * @return a hashmap with the key value pair K:RoleType, V:currentRole
     */
    public HashMap<RoleType, Role> getAllRoles() {
        HashMap<RoleType, Role> roleMap = new HashMap<RoleType, Role>();
        // Check for all possible roles, including NoType.
        for (RoleType roletype : RoleType.values()) {
            // Find all roles and set them in array if they are defined for user
            try {
                Role currentRole = getRoleByType(roletype);
                if (currentRole != null) {
                    roleMap.put(currentRole.getRoleType(), currentRole);
                }
            } catch (IllegalArgumentException iae) {
                continue; // We iterate over a notype
            }

        }
        return roleMap;
    }
}
