package com.zbqmal.gtcp_10.Profile;

public class Student extends UserType {

    // Constructors
    public Student(String id, String uid, String password, String emailAddress, String phoneNumber) {

        this.setID(id);
        this.setUid(uid);
        this.setPassword(password);
        this.setEmailAddress(emailAddress);
        this.setPhoneNumber(phoneNumber);
    }
}