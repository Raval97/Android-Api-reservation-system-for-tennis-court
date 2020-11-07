package com.example.tenniscourtreservation.model;
import android.text.Editable;

import lombok.Data;

@Data
public class User {

    private Long id;
    private String username;
    private String password;
    private String role;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
