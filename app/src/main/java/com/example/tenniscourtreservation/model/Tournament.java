package com.example.tenniscourtreservation.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import lombok.Data;

@Data

public class Tournament {

    private Long id;
    private String title;
    private int maxCountOFParticipant;
    private int countOFRegisteredParticipant;
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonSerialize(using= LocalDateSerializer.class)
    private LocalDate dateOfStarted;
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonSerialize(using= LocalDateSerializer.class)
    private LocalDate dateOfEnded;
    private float entryFee;

    public Tournament() {
    }

    public Tournament(String title, int maxCountOFParticipant, int countOFRegisteredParticipant,
                      LocalDate dateOfStarted, LocalDate dateOfEnded, float entryFee) {
        this.title = title;
        this.maxCountOFParticipant = maxCountOFParticipant;
        this.countOFRegisteredParticipant = countOFRegisteredParticipant;
        this.dateOfStarted = dateOfStarted;
        this.dateOfEnded = dateOfEnded;
        this.entryFee = entryFee;
    }
}
