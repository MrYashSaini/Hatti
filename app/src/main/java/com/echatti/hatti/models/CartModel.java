package com.echatti.hatti.models;

public class CartModel {
    String category,productId;
    int qty;

    public CartModel(String category, String productId, int qty) {
        this.category = category;
        this.productId = productId;
        this.qty = qty;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public CartModel(String category, String productId) {
        this.category = category;
        this.productId = productId;
    }

    public CartModel() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

}
