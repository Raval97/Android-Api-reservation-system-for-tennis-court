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
    private String typeOfPaying = "online";
    private Object user;

    public Payment() {
    }

    public Payment(Long id, String title, LocalDate dateOfPaying, LocalDate finalPaymentDate,
                   Float price, String statusPaying) {
        this.id = id;
        this.title = title;
        this.dateOfPaying = dateOfPaying;
        this.finalPaymentDate = finalPaymentDate;
        this.price = price;
        this.statusPaying = statusPaying;
    }

    public Payment(Long id, String title, LocalDate dateOfPaying,
                   Float price, String statusPaying, String typeOfPaying) {
        this.id = id;
        this.title = title;
        this.dateOfPaying = dateOfPaying;
        this.price = price;
        this.statusPaying = statusPaying;
        this.typeOfPaying = typeOfPaying;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", finalPaymentDate=" + finalPaymentDate +
                ", price=" + price +
                ", statusPaying='" + statusPaying + '\'' +
                '}';
    }
}
