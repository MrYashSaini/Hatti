package com.example.hatti.models;

public class NotificationModel {
    String title,image,description,date,time;

    public NotificationModel(String title, String image, String description, String date, String time) {
        this.title = title;
        this.image = image;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public NotificationModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
