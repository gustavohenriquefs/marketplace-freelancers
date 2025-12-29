package com.ufc.quixada.api.infrastructure.models;

import jakarta.persistence.*;

@Entity
public class SubcategoryJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryJpaEntity category;
}
