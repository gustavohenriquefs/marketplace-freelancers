package com.ufc.quixada.api.infrastructure.models;

import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.entities.Propose;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class ProposeJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String description;

    @Column()
    private int duration;

    @Column()
    private BigDecimal price;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ProjectJpaEntity project;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FreelancerJpaEntity freelancer;
}
