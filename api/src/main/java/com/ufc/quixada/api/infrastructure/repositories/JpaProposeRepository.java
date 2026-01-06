package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.infrastructure.models.ProposeJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProposeRepository extends JpaRepository<ProposeJpaModel, Long> {
    boolean existsByFreelancerIdAndProjectId(Long freelancerId, Long projectId);
}
