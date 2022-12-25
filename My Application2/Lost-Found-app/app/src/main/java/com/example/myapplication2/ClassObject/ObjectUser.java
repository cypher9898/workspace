package com.example.myapplication2.ClassObject;

public class ObjectUser{

     String email;
     String fullName;
     String phone;
     String UserType;

    public ObjectUser(){}

    public ObjectUser(String email,String fName,String phone,String UserType){
        this.email=email;
        this.fullName=fName;
        this.phone=phone;
        this.UserType=UserType;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserType() {
        return UserType;
    }
}
