package com.echatti.hatti.models;

public class HomeMenuModel {
    String name,image;

    public HomeMenuModel(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public HomeMenuModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
