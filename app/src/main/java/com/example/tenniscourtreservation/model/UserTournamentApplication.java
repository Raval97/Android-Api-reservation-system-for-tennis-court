package com.example.tenniscourtreservation.model;

import lombok.Data;

@Data
public class UserTournamentApplication {

    private Long id;
    private String status;
    private boolean ifPaidEntryFee;
    private Tournament tournament;
    private User user;

    public UserTournamentApplication() {
    }

    public UserTournamentApplication(String status, boolean ifPaidEntryFee, Tournament tournament, User user) {
        this.status = status;
        this.ifPaidEntryFee = ifPaidEntryFee;
        this.tournament = tournament;
        this.user = user;
    }

}
