package com.example.tenniscourtreservation.model;

import android.text.TextUtils;

public class User {
    private String username , password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String surname) {
        this.username = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String isValid() {
        if (this.username == null || TextUtils.isEmpty(username)){
            return "Incorrect Username";
        }
        else if (this.password == null || TextUtils.isEmpty(password))
            return "Incorrect Password";
        else
            return "Correct";
    }
}
