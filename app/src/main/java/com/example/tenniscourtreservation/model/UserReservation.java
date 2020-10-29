package com.example.tenniscourtreservation.model;

import lombok.Data;

@Data
public class UserReservation {

    private Long id;
    private User user;
    private Reservation reservation;

    public UserReservation() {
    }

    public UserReservation(User user) {
        this.user = user;
    }
}
