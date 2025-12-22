package com.ufc.quixada.api.data.models;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Table(name = "tb_freelancers")
public class FreelancerJpaEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Getter
    @Column(nullable = false)
    private String fullName;

    @Getter
    private String email;

    @Getter
    private double price;
}