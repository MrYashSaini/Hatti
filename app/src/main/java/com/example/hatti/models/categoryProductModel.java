package com.example.hatti.models;

public class categoryProductModel {
    String image,name,description,price,mrp,productId,category,brand,image2,image3,image4,ingredient,manufacturer,quantity,si,stock;
    int qty;
    public categoryProductModel() {
    }

    public categoryProductModel(int qty) {
        this.qty = qty;
    }

    public categoryProductModel(String category) {
        this.category = category;
    }

    public categoryProductModel(String image, String name, String description, String price, String mrp, String productId, String category, String brand, String image2, String image3, String image4, String ingredient, String manufacturer, String quantity, String si, String stock) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.price = price;
        this.mrp = mrp;
        this.productId = productId;
        this.category = category;
        this.brand = brand;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.ingredient = ingredient;
        this.manufacturer = manufacturer;
        this.quantity = quantity;
        this.si = si;
        this.stock = stock;
    }

    public categoryProductModel(String image, String name, String description, String price, String mrp, String productId, String category) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.price = price;
        this.mrp = mrp;
        this.productId = productId;
        this.category=category;

    }

    public categoryProductModel(String image, String name, String price, String mrp, String productId, String category, int qty) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.mrp = mrp;
        this.productId = productId;
        this.category = category;
        this.qty = qty;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategory() {
        return category;
    }
//
    public void setCategory(String category) {
        this.category = category;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSi() {
        return si;
    }

    public void setSi(String si) {
        this.si = si;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
