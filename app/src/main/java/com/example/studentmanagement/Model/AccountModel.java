package com.example.studentmanagement.Model;

import java.io.Serializable;

public class AccountModel implements Serializable {
    private String img;
    private String name;
    private String role;
    private String birthday;
    private String phoneNumber;
    private String status;
    private String email;
    private String password;

    public AccountModel() {
        // Default constructor is needed for Firestore to deserialize objects
    }

    public AccountModel(String img, String name, String role, String birthday, String phoneNumber, String status, String email, String password) {
        this.img = img;
        this.name = name;
        this.role = role;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.email = email;
        this.password = password;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
