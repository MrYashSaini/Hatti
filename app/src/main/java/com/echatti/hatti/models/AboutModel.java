package com.echatti.hatti.models;

public class AboutModel {
    String phoneNo,instagram,facebook,gmail;

    public AboutModel() {
    }

    public AboutModel(String phoneNo, String instagram, String facebook, String gmail) {
        this.phoneNo = phoneNo;
        this.instagram = instagram;
        this.facebook = facebook;
        this.gmail = gmail;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phone) {
        this.phoneNo = phone;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }
}
