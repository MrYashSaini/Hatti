package com.example.hatti.models;

public class SearchModel {
    String image,name,category,productId;

    public SearchModel(String image, String name, String category, String productId) {
        this.image = image;
        this.name = name;
        this.category = category;
        this.productId = productId;
    }

    public SearchModel() {
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
