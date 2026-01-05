package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.infrastructure.models.SubcategoryJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaSubcategoryRepository extends JpaRepository<SubcategoryJpaModel, Long> {
    List<SubcategoryJpaModel> findByCategoryId(Long categoryId);
}

