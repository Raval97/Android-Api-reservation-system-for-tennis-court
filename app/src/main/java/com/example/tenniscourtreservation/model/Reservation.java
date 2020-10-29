package com.example.tenniscourtreservation.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Reservation {

    private Long id;
    private boolean byAdmin;
    private LocalDate dateOfReservation;
    private float price;
    private float finalPrice;
    private boolean discount;
    private String statusOfReservation;
    private String typeOfPaying;
    private LocalDate finalPaymentDate;
    private String statusPaying;

    public Reservation() {
    }

    public Reservation(LocalDate dateOfReservation, float price, boolean discount, float finalPrice,
                       String statusOfReservation, String typeOfPaying, LocalDate finalPaymentDate,
                       String statusPaying) {
        this.dateOfReservation = dateOfReservation;
        this.finalPrice = finalPrice;
        this.price = price;
        this.discount = discount;
        this.statusOfReservation = statusOfReservation;
        this.typeOfPaying = typeOfPaying;
        this.finalPaymentDate = finalPaymentDate;
        this.statusPaying = statusPaying;
        this.byAdmin = false;
    }

    public Reservation(LocalDate dateOfReservation, String statusOfReservation, UserReservation userReservation) {
        this.byAdmin = true;
        this.dateOfReservation = dateOfReservation;
        this.statusOfReservation = statusOfReservation;
    }
}
