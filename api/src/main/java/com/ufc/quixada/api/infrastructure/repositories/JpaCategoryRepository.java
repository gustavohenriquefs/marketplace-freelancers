package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.infrastructure.models.CategoryJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCategoryRepository extends JpaRepository<CategoryJpaModel, Long> {

}
