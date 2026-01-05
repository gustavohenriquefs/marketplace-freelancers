package com.ufc.quixada.api.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "freelancers")
public class FreelancerJpaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "freelancer_projects",
            joinColumns = @JoinColumn(name = "freelancer_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<ProjectJpaModel> projects;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProposeJpaEntity> proposes;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserJpaModel user;
}