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
    private String uid;
    private String emailAddress;
    private String phoneNumber;
    private Types userType;
    private String destination;
    private String currentLat;
    private String currentLng;
    private boolean isBeingTracked;

    // Setter
    public void setID(String newID) {id = newID;}
    public void setUid(String newUID) {uid = newUID;}
    public void setEmailAddress(String newEmailAddress) {emailAddress = newEmailAddress;}
    public void setPhoneNumber(String newPhoneNumber) {phoneNumber = newPhoneNumber;}
    public void setUserType(Types newUserType) {userType = newUserType;}
    public void setDestination(String newDestination) {destination = newDestination;}
    public void setCurrentLat(String newCurrentLat) {currentLat = newCurrentLat;}
    public void setCurrentLng(String newCurrentLng) {currentLng = newCurrentLng;}
    public void setIsBeingTracked(boolean newIsBeingTracked) {isBeingTracked = newIsBeingTracked;}

    // Getter
    public String getID() {return id;}
    public String getUid() {return uid;}
    public String getEmailAddress() {return emailAddress;}
    public String getPhoneNumber() {return phoneNumber;}
    public Types getUserType() {return userType;}
    public String getDestination() {return destination;}
    public String getCurrentLat() {return currentLat;}
    public String getCurrentLng() {return currentLng;}
    public Boolean getIsBeingTracked() {return isBeingTracked;}
}