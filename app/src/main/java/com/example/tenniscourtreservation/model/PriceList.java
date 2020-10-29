package com.example.tenniscourtreservation.model;
import lombok.Data;

@Data
public class PriceList {

    private Long id;
    private String type;
    private String time;
    private Float price;

    public PriceList() {
    }

    public PriceList(Long id, String type, String time, Float price) {
        this.id = id;
        this.type = type;
        this.time = time;
        this.price = price;
    }

}
