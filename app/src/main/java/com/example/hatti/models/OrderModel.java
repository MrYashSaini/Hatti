package com.example.hatti.models;

public class OrderModel {
    String date,time,noOfProduct,status,price,methode;

    public OrderModel(String date, String time, String noOfProduct, String status, String price) {
        this.date = date;
        this.time = time;
        this.noOfProduct = noOfProduct;
        this.status = status;
        this.price = price;
    }

    public OrderModel(String date, String time, String noOfProduct, String status, String price, String methode) {
        this.date = date;
        this.time = time;
        this.noOfProduct = noOfProduct;
        this.status = status;
        this.price = price;
        this.methode = methode;
    }

    public OrderModel() {
    }

    public String getMethode() {
        return methode;
    }

    public void setMethode(String methode) {
        this.methode = methode;
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
}
