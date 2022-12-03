package com.example.hatti.models;

public class ProfileModel {
    String name,gmail,phoneNo,state,dob,city,address,gender,profilePhoto,shopPhoto,hattiId;
    int notificationNo;
    public ProfileModel() {}

    public ProfileModel(String name, String gmail, String phoneNo, String state, String dob, String city, String address, String gender, String profilePhoto, String shopPhoto,String hattiId,int notificationNo) {
        this.name = name;
        this.gmail = gmail;
        this.phoneNo = phoneNo;
        this.state = state;
        this.dob = dob;
        this.city = city;
        this.address = address;
        this.gender = gender;
        this.profilePhoto = profilePhoto;
        this.shopPhoto = shopPhoto;
        this.hattiId = hattiId;
        this.notificationNo = notificationNo;
    }

    public int getNotificationNo() {
        return notificationNo;
    }

    public void setNotificationNo(int notificationNo) {
        this.notificationNo = notificationNo;
    }

    public String getHattiId() {
        return hattiId;
    }

    public void setHattiId(String hattiId) {
        this.hattiId = hattiId;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getShopPhoto() {
        return shopPhoto;
    }

    public void setShopPhoto(String shopPhoto) {
        this.shopPhoto = shopPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phone) {
        this.phoneNo = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
