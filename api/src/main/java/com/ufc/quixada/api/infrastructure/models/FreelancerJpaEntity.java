package com.ufc.quixada.api.infrastructure.models;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Entity
@Table(name = "freelancers")
public class FreelancerJpaEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(nullable = false)
    private String name;

    @Getter
    private String email;

    @ManyToMany
    @JoinTable(
            name = "freelancer_projects",
            joinColumns = @JoinColumn(name = "freelancer_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<ProjectJpaEntity> projects;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProposeJpaEntity> proposes;
}