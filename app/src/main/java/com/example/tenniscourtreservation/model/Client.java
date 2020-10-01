package com.example.tenniscourtreservation.model;

import android.text.TextUtils;
import android.util.Patterns;

public class Client {
    private String name;
    private String surname;
    private Long phoneNumber;
    private String emailAddress;

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

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String isValid() {
        if (this.name == null || TextUtils.isEmpty(name) || TextUtils.isDigitsOnly(name)) {
            return "Incorrect Name";
        } else if (this.surname == null || TextUtils.isEmpty(surname) || TextUtils.isDigitsOnly(surname)) {
            return "Incorrect Surname";
        } else if (this.emailAddress == null || TextUtils.isEmpty(emailAddress) || !Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
            return "Incorrect Email Address";
        } else if (this.phoneNumber == null || Patterns.PHONE.matcher(phoneNumber.toString()).matches()) {
            return "Incorrect Phone Number";
        } else
            return "Correct";
    }
}
