package com.example.studentmanagement.Model;

import java.io.Serializable;

public class AccountModel implements Serializable {
    private String img;
    private String name;
    private String age;
    private String phoneNumber;
    private String status;

    public AccountModel() {
        // Default constructor is needed for Firestore to deserialize objects
    }

    public AccountModel(String img, String name, String age, String phoneNumber, String status) {
        this.img = img;
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
