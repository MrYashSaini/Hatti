package com.echatti.hatti.models;

public class NewProfileModel {
    String name,hattiId,password,gmail,profilePhoto,shopPhoto,phoneNo,address,city,state,dob,gender,authId,token;
    int notificationId,orderId,notificationNo;

    public NewProfileModel(String name, String hattiId, String password, String gmail, String profilePhoto, String shopPhoto, String phoneNo, String address, String city, String state, String dob, String gender, String authId, String token, int notificationId, int orderId, int notificationNo) {
        this.name = name;
        this.hattiId = hattiId;
        this.password = password;
        this.gmail = gmail;
        this.profilePhoto = profilePhoto;
        this.shopPhoto = shopPhoto;
        this.phoneNo = phoneNo;
        this.address = address;
        this.city = city;
        this.state = state;
        this.dob = dob;
        this.gender = gender;
        this.authId = authId;
        this.notificationId = notificationId;
        this.orderId = orderId;
        this.token = token;
        this.notificationNo = notificationNo;
    }

    public NewProfileModel(String name, String profilePhoto, String phoneNo, String address, String authId) {
        this.name = name;
        this.profilePhoto = profilePhoto;
        this.phoneNo = phoneNo;
        this.address = address;
        this.authId = authId;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public NewProfileModel() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getNotificationNo() {
        return notificationNo;
    }

    public void setNotificationNo(int notificationNo) {
        this.notificationNo = notificationNo;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPassword() {
        return password;
    }

    public String getHattiId() {
        return hattiId;
    }

    public void setHattiId(String hattiId) {
        this.hattiId = hattiId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
