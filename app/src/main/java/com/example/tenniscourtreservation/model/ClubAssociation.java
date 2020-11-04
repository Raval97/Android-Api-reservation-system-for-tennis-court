package com.example.tenniscourtreservation.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ClubAssociation {

    private Long id;
    private User user;
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonSerialize(using= LocalDateSerializer.class)
    private LocalDate dateOfJoining;
    private Boolean ifActive;
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonSerialize(using= LocalDateSerializer.class)
    private LocalDate dateOfEndActive;

    public ClubAssociation() {
    }

    public ClubAssociation(LocalDate dateOfJoining, Boolean ifActive, User user) {
        this.dateOfJoining = dateOfJoining;
        this.ifActive = ifActive;
        this.user= user;
    }

}
