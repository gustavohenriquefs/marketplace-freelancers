package com.ufc.quixada.api.infrastructure.models;

import jakarta.persistence.*;

@Entity
@Table(
        name = "subcategories"
)
public class SubcategoryJpaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryJpaModel category;
}
