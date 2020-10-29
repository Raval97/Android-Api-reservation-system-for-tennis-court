package com.example.tenniscourtreservation.model;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ClubAssociation {

    private Long id;
    private User user;
    private LocalDate dateOfJoining;
    private Boolean ifActive;
    private LocalDate dateOfEndActive;

    public ClubAssociation() {
    }

    public ClubAssociation(LocalDate dateOfJoining, Boolean ifActive, User user) {
        this.dateOfJoining = dateOfJoining;
        this.ifActive = ifActive;
        this.user= user;
    }

}
