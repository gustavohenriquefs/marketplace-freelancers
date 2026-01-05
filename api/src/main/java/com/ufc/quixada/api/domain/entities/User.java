package com.ufc.quixada.api.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;

    private Freelancer freelancerProfile;
    private Contractor contractorProfile;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void getContratactorProfile(Contractor contractor) {
    }
}
