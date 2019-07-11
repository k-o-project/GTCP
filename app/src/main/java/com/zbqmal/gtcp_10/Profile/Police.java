package com.zbqmal.gtcp_10.Profile;

public class Police extends UserType {

    // Constructors
    public Police(String id, String uid, String password, String emailAddress, String phoneNumber) {

        this.setID(id);
        this.setUid(uid);
        this.setPassword(password);
        this.setEmailAddress(emailAddress);
        this.setPhoneNumber(phoneNumber);
    }
}