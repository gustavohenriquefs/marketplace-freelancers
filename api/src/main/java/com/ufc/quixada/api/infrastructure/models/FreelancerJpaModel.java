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
        @ManyToMany(mappedBy = "freelancers")
    private Set<ProjectJpaModel> projects;

    @JsonIgnore
    @OneToMany(mappedBy = "freelancer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ProposeJpaModel> proposes;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserJpaModel user;
}