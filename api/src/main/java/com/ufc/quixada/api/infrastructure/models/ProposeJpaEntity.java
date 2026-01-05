package com.ufc.quixada.api.infrastructure.models;

import com.ufc.quixada.api.domain.enums.ProposeStatus;
import jakarta.persistence.*;
import lombok.Setter;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

@Entity
@Table(name = "propose")
public class ProposeJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String description;

    @Column()
    private int duration;

    @Setter
    @Column()
    private ProposeStatus status;

    @Column()
    private BigDecimal price;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ProjectJpaModel project;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FreelancerJpaModel freelancer;

}
