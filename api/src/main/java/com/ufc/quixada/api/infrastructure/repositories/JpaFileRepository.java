package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.infrastructure.models.FileJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFileRepository extends JpaRepository<FileJpaModel, Long> {
}

