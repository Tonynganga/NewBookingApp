package com.example.bookingapp.Models;

public class UserProfile {
    String profilename, profileemail, profilephone, profilecity, profileimage;

    public UserProfile() {
    }

    public UserProfile(String profilename, String profileemail, String profilephone, String profilecity, String profileimage) {
        this.profilename = profilename;
        this.profileemail = profileemail;
        this.profilephone = profilephone;
        this.profilecity = profilecity;
        this.profileimage = profileimage;
    }

    public String getProfilename() {
        return profilename;
    }

    public void setProfilename(String profilename) {
        this.profilename = profilename;
    }

    public String getProfileemail() {
        return profileemail;
    }

    public void setProfileemail(String profileemail) {
        this.profileemail = profileemail;
    }

    public String getProfilephone() {
        return profilephone;
    }

    public void setProfilephone(String profilephone) {
        this.profilephone = profilephone;
    }

    public String getProfilecity() {
        return profilecity;
    }

    public void setProfilecity(String profilecity) {
        this.profilecity = profilecity;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }
}
