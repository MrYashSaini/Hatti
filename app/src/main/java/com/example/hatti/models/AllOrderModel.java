package com.example.hatti.models;

public class AllOrderModel {
    String date,time,noOfProduct,status,price,methode,id,image,name;
    int orderId,adminOrderId;
    boolean seen;
    public AllOrderModel(String date, String time, String noOfProduct, String status, String price, String methode, String id, String image, String name, int orderId, int adminOrderId,boolean seen) {
        this.date = date;
        this.time = time;
        this.noOfProduct = noOfProduct;
        this.status = status;
        this.price = price;
        this.methode = methode;
        this.id = id;
        this.image = image;
        this.name = name;
        this.orderId = orderId;
        this.adminOrderId = adminOrderId;
        this.seen = seen;
    }

    public AllOrderModel() {
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public int getAdminOrderId() {
        return adminOrderId;
    }

    public void setAdminOrderId(int adminOrderId) {
        this.adminOrderId = adminOrderId;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNoOfProduct() {
        return noOfProduct;
    }

    public void setNoOfProduct(String noOfProduct) {
        this.noOfProduct = noOfProduct;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMethode() {
        return methode;
    }

    public void setMethode(String methode) {
        this.methode = methode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
