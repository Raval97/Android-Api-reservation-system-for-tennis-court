package com.example.tenniscourtreservation.model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class Services {

    private Long id;
    private LocalDate date;
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

    public Services(LocalDate date, float numberOfHours, LocalTime time, Court court) {
        this.date = date;
        this.numberOfHours = numberOfHours;
        this.time = time;
        this.court = court;
    }
}
