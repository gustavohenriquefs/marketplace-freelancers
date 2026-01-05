package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.infrastructure.models.ProjectJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProjectRepository extends JpaRepository<ProjectJpaModel, Long> {
}