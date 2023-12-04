package com.proj.unitTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.proj.model.users.*;

import com.proj.validators.BasicInfoValidator;
import com.proj.validators.MemberValidator;
import com.proj.function.RoleAssigner;

import com.proj.exception.FailedValidationException;

public class ValidatorUnitTest {
    User user;
    BasicInfoValidator basicUserInfoValidator;
    MemberValidator memberValidator;

    // Provide a user to test before each test with all possible roles fulfilled
    @BeforeEach
    void init() {
        // Set all users so that they have all possible info to see if filter works
        // correctly
        user = new User(new BasicUserInfo("user1", "233Gel+"));
        user.setId(1);

        RoleAssigner.setRole(user, new Guest("Bard Level 1"));
        RoleAssigner.setRole(user,
                new Member("John Adventureman", "9988776655", "9000", "Villavej 123", "John@Adventureman.dk"));
        RoleAssigner.setRole(user, new DM(new ArrayList<String>()));
        RoleAssigner.setRole(user, new Admin(new ArrayList<String>(), new ArrayList<String>()));
        RoleAssigner.setRole(user, new SuperAdmin());
        // create instance of new user validators to use in all validation tests
        basicUserInfoValidator = new BasicInfoValidator(user.getBasicUserInfo());
        memberValidator = new MemberValidator(user.getMemberInfo());
    }

    // BASIC USER INFO TESTS
    @Test
    public void nullBasicuserInfo() {
        basicUserInfoValidator.setBasicUserInfo(null);

        try {
            basicUserInfoValidator.ValidateUserName().ValidatePassword();
        } catch (NullPointerException npr) {
            String errormsg = npr.getMessage();
            assertTrue(errormsg.equals("Cannot validate null basicUserInfo"));
        }
    }

    @Test
    public void nullUsername() {
        user.getBasicUserInfo().setUserName(null);
        basicUserInfoValidator.setBasicUserInfo(user.getBasicUserInfo());

        try {
            basicUserInfoValidator.ValidateUserName().ValidatePassword();
        } catch (NullPointerException npr) {
            String errormsg = npr.getMessage();
            assertTrue(errormsg.equals("Cannot validate null Username"));
        }
    }

    @Test
    public void nullPassword() {
        user.getBasicUserInfo().setPassword(null);
        basicUserInfoValidator.setBasicUserInfo(user.getBasicUserInfo());

        try {
            basicUserInfoValidator.ValidateUserName().ValidatePassword();
        } catch (NullPointerException npr) {
            String errormsg = npr.getMessage();
            assertTrue(errormsg.equals("Cannot validate null password"));
        }
    }

    @Test
    public void userNameInvalid() {
        user.getBasicUserInfo().setUserName("");
        basicUserInfoValidator.setBasicUserInfo(user.getBasicUserInfo());

        try {
            basicUserInfoValidator.ValidateUserName().ValidatePassword();
        } catch (FailedValidationException fve) {
            String errormsg = fve.getMessage();
            assertTrue(errormsg.equals("Username is not valid"));
        }

    }

    @Test
    public void passwordnvalid() {
        user.getBasicUserInfo().setPassword("");
        basicUserInfoValidator.setBasicUserInfo(user.getBasicUserInfo());

        try {
            basicUserInfoValidator.ValidateUserName().ValidatePassword();
        } catch (FailedValidationException fve) {
            String errormsg = fve.getMessage();
            assertTrue(errormsg.equals("Password is not valid"));
        }
    }

    // MEMBER INFO TESTS
    @Test
    public void nullMemberInfo() {
        memberValidator.setMemberInfo(null);

        try {
            memberValidator.ValidateEmail().ValidatePhoneNumber().ValidatePostCode().ValidateAddress();
        } catch (NullPointerException npr) {
            String errormsg = npr.getMessage();
            assertTrue(errormsg.equals("Cannot validate null member"));
        }
    }

    @Test
    public void nullEmail() {
        user.getMemberInfo().setEmail(null);
        memberValidator.setMemberInfo(user.getMemberInfo());
        try {
            memberValidator.ValidateEmail().ValidatePhoneNumber().ValidatePostCode().ValidateAddress();
        } catch (NullPointerException npr) {
            String errormsg = npr.getMessage();
            assertTrue(errormsg.equals("Cannot validate null email"));
        }
    }

    @Test
    public void emailInvalid() {
        user.getMemberInfo().setEmail("");
        memberValidator.setMemberInfo(user.getMemberInfo());

        try {
            memberValidator.ValidateEmail().ValidatePhoneNumber().ValidatePostCode().ValidateAddress();
        } catch (FailedValidationException fve) {
            String errormsg = fve.getMessage();
            assertTrue(errormsg.equals("Email is not valid"));
        }
    }

    @Test
    public void nullPhoneNumber() {
        user.getMemberInfo().setPhoneNumber(null);
        memberValidator.setMemberInfo(user.getMemberInfo());

        try {
            memberValidator.ValidateEmail().ValidatePhoneNumber().ValidatePostCode().ValidateAddress();
        } catch (NullPointerException npe) {
            String errormsg = npe.getMessage();
            assertTrue(errormsg.equals("Cannot validate null phone number"));
        }
    }

    @Test
    public void invalidPhoneNumber() {
        user.getMemberInfo().setPhoneNumber("");
        memberValidator.setMemberInfo(user.getMemberInfo());

        try {
            memberValidator.ValidateEmail().ValidatePhoneNumber().ValidatePostCode().ValidateAddress();
        } catch (FailedValidationException fve) {
            String errormsg = fve.getMessage();
            assertTrue(errormsg.equals("Phone number is not valid"));
        }
    }

    @Test
    public void nullAddress() {
        user.getMemberInfo().setAddress(null);
        memberValidator.setMemberInfo(user.getMemberInfo());

        try {
            memberValidator.ValidateEmail().ValidatePhoneNumber().ValidatePostCode().ValidateAddress();
        } catch (NullPointerException npe) {
            String errormsg = npe.getMessage();
            assertTrue(errormsg.equals("Cannot validate null address"));
        }
    }

    @Test
    public void invalidAddress() {
        user.getMemberInfo().setAddress("");
        memberValidator.setMemberInfo(user.getMemberInfo());

        try {
            memberValidator.ValidateEmail().ValidatePhoneNumber().ValidatePostCode().ValidateAddress();
        } catch (FailedValidationException fve) {
            String errormsg = fve.getMessage();
            assertTrue(errormsg.equals("Address is not valid"));
        }
    }

    @Test
    public void nullPostcode() {
        user.getMemberInfo().setPostalCode(null);
        memberValidator.setMemberInfo(user.getMemberInfo());

        try {
            memberValidator.ValidateEmail().ValidatePhoneNumber().ValidatePostCode().ValidateAddress();
        } catch (NullPointerException npe) {
            String errormsg = npe.getMessage();
            assertTrue(errormsg.equals("Cannot validate null postal code"));
        }
    }

    @Test
    public void invalidPostcode() {
        user.getMemberInfo().setPostalCode("");
        memberValidator.setMemberInfo(user.getMemberInfo());

        try {
            memberValidator.ValidateEmail().ValidatePhoneNumber().ValidatePostCode().ValidateAddress();
        } catch (FailedValidationException fve) {
            String errormsg = fve.getMessage();
            assertTrue(errormsg.equals("Postalcode is not valid"));
        }
    }
}
