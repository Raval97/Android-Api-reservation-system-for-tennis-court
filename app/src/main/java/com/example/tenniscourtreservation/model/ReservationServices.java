package com.example.tenniscourtreservation.model;

import lombok.Data;

@Data
public class ReservationServices {

    private Long id;
    private Reservation reservation;
    private Services services;

    public ReservationServices() {
    }

    public ReservationServices(Reservation reservation) {
        this.reservation = reservation;
    }
}
