package com.zbqmal.gtcp_10.Profile;

public abstract class UserType {

    // UserType Enum
    enum Types {
        STUDENT,
        POLICE,
        ADMIN
    }

    // User Information
    private String id;
    private String password;
    private String emailAddress;
    private String phoneNumber;
    private Types userType;

    // Setter
    public void setID(String newID) {id = newID;}
    public void setPassword(String newPassword) {password = newPassword;}
    public void setEmailAddress(String newEmailAddress) {emailAddress = newEmailAddress;}
    public void setPhoneNumber(String newPhoneNumber) {phoneNumber = newPhoneNumber;}
    public void setUserType(Types newUserType) {userType = newUserType;}

    // Getter
    public String getID() {return id;}
    public String getPassword() {return password;}
    public String getEmailAddress() {return emailAddress;}
    public String getPhoneNumber() {return phoneNumber;}
    public Types getUserType() {return userType;}
}