package com.example.tenniscourtreservation.model;

import lombok.Data;

@Data
public class UserTournament {
    private Long id;
    private Tournament tournament;
    private User user;

    public UserTournament() {
    }

    public UserTournament(Tournament tournament, User user) {
        this.tournament = tournament;
        this.user = user;
    }

}
