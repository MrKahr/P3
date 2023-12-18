package com.proj.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.proj.exception.FailedValidationException;
import com.proj.model.users.Member;

/**
 * The purpose of this class is to validate objects from the member-class.
 * It is constructed with the member-object it should validate.
 */
public class MemberValidator implements Validatable<MemberValidator> {
    // Field
    private Member member;
    // https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html
    // Make pattern to match and initialize variable to get matcher.
    private static Pattern genericStringPattern = Pattern.compile("^(\\\\w)$");
    private static Pattern emailPattern = Pattern.compile("^\\w{2,}+@+\\w{1,}+.\\w{2,4}$");
    private static Pattern phoneNumberPattern = Pattern.compile("^(\\d{8,10})$");
    private static Pattern postcodePattern = Pattern.compile("^(\\d{4})$"); // currently, only danish postcodes are
                                                                           // considered valid
    private static Pattern addressPattern = Pattern.compile("^\\w{2,}+[ ]+\\d{1,}$");
    private Matcher m;

    // Constructor
    public MemberValidator(Member member) {
        this.member = member;
    };

    // Method
    public Member getMemberInfo() {
        return member;
    }

    public void setMemberInfo(Member newMember) {
        this.member = newMember;
    }

    @Override
    /**
     * Validates a generis string field consisting of at least one element
     * @return the current membervalidator. This is necessary for method chaining.
     * @throws NullPointerException
     * @throws FailedValidationException
     */
    public MemberValidator ValidateStringField(String string) throws NullPointerException, FailedValidationException {
        if (string == null) {
            throw new NullPointerException("Cannot validate null string");
        } else {

            boolean isValidString;

            // Check whether generic string matches the regex pattern
            m = genericStringPattern.matcher(string);
            isValidString = m.matches();

            if (isValidString) {
                return this;
            } else {
                throw new FailedValidationException("Generic String is not valid");
            }
        }
    }

    /**
     * Validates the email attribute of a member info object.
     * @return the current membervalidator. This is necessary for method chaining.
     * @throws NullPointerException
     * @throws FailedValidationException
     */
    public MemberValidator ValidateEmail() throws NullPointerException, FailedValidationException {
        if (getMemberInfo() == null) {
            throw new NullPointerException("Cannot validate null member");
        } else if (getMemberInfo().getEmail() == null) {
            throw new NullPointerException("Cannot validate null email");
        } else {
            boolean isValidEmail;
            // Check whether password matches the regex pattern
            m = emailPattern.matcher(getMemberInfo().getEmail());
            isValidEmail = m.matches();

            if (isValidEmail) {
                return this;
            } else {
                throw new FailedValidationException("Email is not valid");
            }
        }
    }

    /**
     * Validates the phone number attribute of a member info object.
     * @return the current membervalidator. This is necessary for method chaining.
     * @throws NullPointerException
     * @throws FailedValidationException
     */
    public MemberValidator ValidatePhoneNumber() throws NullPointerException, FailedValidationException {
        if (getMemberInfo() == null) {
            throw new NullPointerException("Cannot validate null member");
        } else if (getMemberInfo().getPhoneNumber() == null) {
            throw new NullPointerException("Cannot validate null phone number");
        } else {
            boolean isValidPhoneNumber;
            // Check whether username matches the regex pattern
            m = phoneNumberPattern.matcher(getMemberInfo().getPhoneNumber());
            isValidPhoneNumber = m.matches();

            if (isValidPhoneNumber) {
                return this;
            } else {
                throw new FailedValidationException("Phone number is not valid");
            }
        }
    }

    /**
     * Validates the validate post code attribute of a member info object.
     * @return the current membervalidator. This is necessary for method chaining.
     * @throws NullPointerException
     * @throws FailedValidationException
     */
    public MemberValidator ValidatePostCode() throws NullPointerException, FailedValidationException {
        if (getMemberInfo() == null) {
            throw new NullPointerException("Cannot validate null member");
        } else if (getMemberInfo().getPostalCode() == null) {
            throw new NullPointerException("Cannot validate null postal code");
        } else {
            boolean isValidPostalCode;
            // Check whether username matches the regex pattern
            m = postcodePattern.matcher(getMemberInfo().getPostalCode());
            isValidPostalCode = m.matches();

            if (isValidPostalCode) {
                return this;
            } else {
                throw new FailedValidationException("Postalcode is not valid");
            }
        }
    }

    /**
     * Validates the address attribute of a member info object.
     * @return the current membervalidator. This is necessary for method chaining.
     * @throws NullPointerException
     * @throws FailedValidationException
     */
    public MemberValidator ValidateAddress() throws NullPointerException, FailedValidationException {
        if (getMemberInfo() == null) {
            throw new NullPointerException("Cannot validate null member");
        } else if (getMemberInfo().getAddress() == null) {
            throw new NullPointerException("Cannot validate null address");
        } else {
            boolean isValidAddress;
            // Check whether username matches the regex pattern
            m = addressPattern.matcher(getMemberInfo().getAddress());
            isValidAddress = m.matches();

            if (isValidAddress) {
                return this;
            } else {
                throw new FailedValidationException("Address is not valid");
            }
        }
    }

}