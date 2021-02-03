package com.vinade_app.rollerio;

public class User {
    private String name, surname, email, mobile_number, password;

    public User() {
    }

    public User(String name, String surname, String email, String mobile_number, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.mobile_number = mobile_number;
        this.password = password;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
