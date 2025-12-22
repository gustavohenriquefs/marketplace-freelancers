package com.ufc.quixada.api.domain.entities;

import java.util.UUID;

public class Freelancer {
    private UUID id;
    private String name;
    private String email;
    private double hourlyRate;

    // Construtor, Getters e Setters (omitidos para brevidade)
    public Freelancer(UUID id, String name, String email, double hourlyRate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.hourlyRate = hourlyRate;
    }

    // Getters...
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public double getHourlyRate() { return hourlyRate; }
}