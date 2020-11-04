package com.example.tenniscourtreservation.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;

import lombok.Data;

@Data

public class Payment {

    private Long id;
    private String title;
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonSerialize(using= LocalDateSerializer.class)
    private LocalDate dateOfPaying;
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonSerialize(using= LocalDateSerializer.class)
    private LocalDate finalPaymentDate;
    private Float price;
    private String statusPaying;
    private User user;

    public Payment() {
    }

    public Payment(String title, LocalDate dateOfPaying, LocalDate finalPaymentDate,
                   Float price, String statusPaying, User user) {
        this.title = title;
        this.dateOfPaying = dateOfPaying;
        this.finalPaymentDate = finalPaymentDate;
        this.price = price;
        this.statusPaying = statusPaying;
        this.user = user;
    }

    public Payment(String title, LocalDate finalPaymentDate, Float price, String statusPaying, User user) {
        this.title = title;
        this.finalPaymentDate = finalPaymentDate;
        this.price = price;
        this.statusPaying = statusPaying;
        this.user = user;
    }

    public Payment(String title, Float price, LocalDate dateOfPaying, String statusPaying, User user) {
        this.title = title;
        this.dateOfPaying = dateOfPaying;
        this.price = price;
        this.statusPaying = statusPaying;
        this.user = user;
    }
}
