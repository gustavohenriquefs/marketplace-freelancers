package com.ufc.quixada.api.infrastructure.models;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Entity
@Table(name = "freelancers")
public class FreelancerJpaModel {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "freelancer_projects",
            joinColumns = @JoinColumn(name = "freelancer_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<ProjectJpaModel> projects;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProposeJpaEntity> proposes;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserJpaModel user;
}