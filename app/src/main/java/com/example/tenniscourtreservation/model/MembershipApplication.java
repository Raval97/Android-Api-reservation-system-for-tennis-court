package com.example.tenniscourtreservation.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class MembershipApplication {

    private Long id;
    private User user;
    private LocalDate dateOfApplication;
    private String decision;

    public MembershipApplication() {
    }

    public MembershipApplication(User user, LocalDate dateOfApplication, String decision) {
        this.user = user;
        this.dateOfApplication = dateOfApplication;
        this.decision = decision;
    }

}
