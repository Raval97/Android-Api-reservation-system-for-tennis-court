package com.example.tenniscourtreservation.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class Services {

    private Long id;
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonSerialize(using= LocalDateSerializer.class)
    private LocalDate date;
    @JsonDeserialize(using= LocalTimeDeserializer.class)
    @JsonSerialize(using= LocalTimeSerializer.class)
    private LocalTime time;
    private float numberOfHours;
    private float unitCost;
    private Boolean ifRocket;
    private Boolean ifBalls;
    private Boolean ifShoes;
    private float price;
    private Court court;

    public Services() {
    }

    public Services(LocalDate date, float numberOfHours, LocalTime time, float unitCost, Boolean ifRocket,
                    Boolean ifBalls, Boolean ifShoes, float price, Court court) {
        this.date = date;
        this.numberOfHours = numberOfHours;
        this.time = time;
        this.unitCost = unitCost;
        this.ifRocket = ifRocket;
        this.ifBalls = ifBalls;
        this.ifShoes = ifShoes;
        this.price = price;
        this.court = court;
    }
}
