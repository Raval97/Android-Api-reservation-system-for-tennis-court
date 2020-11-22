package com.example.tenniscourtreservation.model;
import lombok.Data;

@Data
public class Client {

    private Long id;
    private String name;
    private String surname;
    private String emailAddress;
    private int phoneNumber;
    private Boolean isClubMen;
    private User users;

    public Client() {
    }

    public Client(String name, String surname, String emailAddress, int phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.isClubMen = false;
    }
}
