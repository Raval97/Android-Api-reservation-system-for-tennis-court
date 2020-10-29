package com.example.tenniscourtreservation.model;

import java.time.LocalDate;

import lombok.Data;

@Data

public class Tournament {

    private Long id;
    private String title;
    private int maxCountOFParticipant;
    private int countOFRegisteredParticipant;
    private LocalDate dateOfStarted;
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
