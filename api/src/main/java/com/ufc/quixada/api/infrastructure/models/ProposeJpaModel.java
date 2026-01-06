package com.ufc.quixada.api.infrastructure.models;

import com.ufc.quixada.api.domain.enums.ProposeStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "proposes")
@Getter
@Setter
public class ProposeJpaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String description;

    @Column()
    private int duration;

    @Enumerated(EnumType.STRING)
    @Column()
    private ProposeStatus status;

    @Column()
    private BigDecimal price;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectJpaModel project;

    @ManyToOne
    @JoinColumn(name = "freelancer_id", nullable = false, unique = false)
    private FreelancerJpaModel freelancer;

}
