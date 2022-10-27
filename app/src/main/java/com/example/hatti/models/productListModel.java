package com.example.hatti.models;

public class productListModel {
    String image,price,mrp,qty,totalPrice,name;

    public productListModel(String image, String price, String mrp, String qty, String totalPrice, String name) {
        this.image = image;
        this.price = price;
        this.mrp = mrp;
        this.qty = qty;
        this.totalPrice = totalPrice;
        this.name = name;
    }

    public productListModel() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
