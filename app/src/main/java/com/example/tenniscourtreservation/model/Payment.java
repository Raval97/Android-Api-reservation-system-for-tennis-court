package com.example.tenniscourtreservation.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;

import lombok.Data;

@Data

public class Payment implements Comparable<Payment>{

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
    private Object users;

    public Payment() {
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
    public int compareTo(Payment p) {
        if (getDateOfPaying() == null || p.getDateOfPaying() == null) {
            return 0;
        }
        return getDateOfPaying().compareTo(p.getDateOfPaying());
    }
}
