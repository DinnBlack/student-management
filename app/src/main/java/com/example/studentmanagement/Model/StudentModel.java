package com.example.studentmanagement.Model;

import java.io.Serializable;

public class StudentModel implements Serializable {

    private String img;
    private String name;
    private String id;
    private String stClass;
    private String schoolYear;
    private String birthday;
    private String gender;
    private String faculty;
    private String major;

    public StudentModel() {
        // Required empty constructor for Firebase deserialization
    }

    public StudentModel(String img, String name, String id, String stClass, String schoolYear, String birthday, String gender, String faculty, String major, String documentId) {
        this.img = img;
        this.name = name;
        this.id = id;
        this.stClass = stClass;
        this.schoolYear = schoolYear;
        this.birthday = birthday;
        this.gender = gender;
        this.faculty = faculty;
        this.major = major;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getStClass() {
        return stClass;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getMajor() {
        return major;
    }
}
