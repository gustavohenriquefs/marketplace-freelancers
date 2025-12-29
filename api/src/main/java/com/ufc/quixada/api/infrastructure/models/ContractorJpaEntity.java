package com.ufc.quixada.api.infrastructure.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ContractorJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectJpaEntity> projects;
}