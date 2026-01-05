package com.ufc.quixada.api.infrastructure.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "contractors")
public class ContractorJpaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectJpaModel> projects;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserJpaModel user;
}