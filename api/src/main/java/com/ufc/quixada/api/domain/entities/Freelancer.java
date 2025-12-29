package com.ufc.quixada.api.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class Freelancer {
    private Long id;
    private String name;
    private String email;
    private List<Project> projects;
    private List<Propose> proposes;

    // Construtor, Getters e Setters (omitidos para brevidade)
    public Freelancer(Long id, String name, String email, double hourlyRate) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}