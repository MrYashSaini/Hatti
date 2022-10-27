package com.example.hatti.models;

public class HorizontalProductScrollModel {
    private String image;
    private String name,price,category,productId;

    public HorizontalProductScrollModel(String image, String name, String price, String category, String productId) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.category = category;
        this.productId = productId;
    }

    public HorizontalProductScrollModel() {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
