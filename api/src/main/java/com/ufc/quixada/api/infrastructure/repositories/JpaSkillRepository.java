package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.infrastructure.models.SkillJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSkillRepository extends JpaRepository<SkillJpaModel, Long> {
}

