package com.zbqmal.gtcp_10.Profile;

public class Student extends UserType {

    // Student Information
    private String destination;
    private String currentLat;
    private String currentLng;

    // Constructors
    public Student(String id, String uid, String emailAddress, String phoneNumber) {

        destination = "N/A";
        currentLat = "N/A";
        currentLng = "N/A";

        this.setID(id);
        this.setUid(uid);
        this.setEmailAddress(emailAddress);
        this.setPhoneNumber(phoneNumber);
    }
}