package com.example.tenniscourtreservation.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class MembershipApplication {

    private Long id;
    private User users;
    private LocalDate dateOfApplication;
    private String decision;

    public MembershipApplication() {
    }

    public MembershipApplication(User user, LocalDate dateOfApplication, String decision) {
        this.users = user;
        this.dateOfApplication = dateOfApplication;
        this.decision = decision;
    }

}
