package com.example.hatti.models;

public class AboutModel {
    String phone,instagram,facebook,gmail;

    public AboutModel() {
    }

    public AboutModel(String phone, String instagram, String facebook, String gmail) {
        this.phone = phone;
        this.instagram = instagram;
        this.facebook = facebook;
        this.gmail = gmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
