package com.example.tenniscourtreservation.model;

import lombok.Data;

@Data
public class Court {

    private Long id;
    private String name;
    private String type;
    private Boolean status;

    public Court(Long id, String name, String type, Boolean status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
    }

    public Court() {
    }

}
