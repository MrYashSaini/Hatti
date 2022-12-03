package com.example.hatti.models;

public class NotificationModel {
    String title,image,description,date,time,type,category;
    int notificationId,id;
    boolean seen;
    public NotificationModel(String title, String image, String description, String date, String time, String type, String category, int notificationId, int id,boolean seen) {
        this.title = title;
        this.image = image;
        this.description = description;
        this.date = date;
        this.time = time;
        this.type = type;
        this.category = category;
        this.notificationId = notificationId;
        this.id = id;
        this.seen =seen;
    }

    public NotificationModel(String title, String image, String description, String date, String time, int notificationId,boolean seen) {
        this.title = title;
        this.image = image;
        this.description = description;
        this.date = date;
        this.time = time;
        this.notificationId = notificationId;
        this.seen =seen;
    }

    public NotificationModel(String title, String image, String description, String date, String time) {
        this.title = title;
        this.image = image;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public NotificationModel() {
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
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
