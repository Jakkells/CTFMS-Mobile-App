package com.example.ctfmsproject;

public class User {
    private String name;
    private String surname;
    private String mobileNo;
    private String idNo;
    private String email;
    private boolean isManager;

    // Required default constructor
    public User() {
        // Default constructor required for Firebase
    }

    public User(String name, String surname, String mobileNo, String idNo, String email, boolean isManager) {
        this.name = name;
        this.surname = surname;
        this.mobileNo = mobileNo;
        this.idNo = idNo;
        this.email = email;
        this.isManager = isManager;
    }

    // Getters and setters (if needed)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }
}
