package com.zbqmal.gtcp_10.Profile;

public class Student extends UserType {

    // Student Information
    private String from;
    private String destination;
    private String currentLocation;

    // Constructors
    public Student(String id, String uid, String emailAddress, String phoneNumber) {

        from = "N/A";
        destination = "N/A";
        currentLocation = "N/A";

        this.setID(id);
        this.setUid(uid);
        this.setEmailAddress(emailAddress);
        this.setPhoneNumber(phoneNumber);
    }
}