package com.example.myapplication2.ClassObject;


import java.io.Serializable;

public class ObjectForm implements Serializable {

    private String ObjectTitle;
    private String description;
    private String img;
    private String GeneratedKey;

    private String date;
    private String place;
    private String status;
    private String UserID;
    private String category;
    private String happend;

    public ObjectForm() {}

    public ObjectForm(ObjectForm f) {
        this.ObjectTitle = f.ObjectTitle;
        this.description = f.description;
        this.img = f.img;
        this.GeneratedKey =f.GeneratedKey;
        this.date=f.date;
        this.place=f.place;
        this.status=f.status;
        this.UserID=f.UserID;
        this.category=f.category;
        this.happend=f.happend;
    }

    public String getObjectTitle() {
        return ObjectTitle;
    }

    public void setObjectTitle(String ObjectTitle) {
        this.ObjectTitle = ObjectTitle;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String profilePic) {
        this.description = profilePic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getHappend() {
        return happend;
    }

    public void setHappend(String happend) {
        this.happend = happend;
    }

    public String getGeneratedKey() {
        return GeneratedKey;
    }

    public void setGeneratedKey(String GeneratedKey) {
        this.GeneratedKey = GeneratedKey;
    }
}