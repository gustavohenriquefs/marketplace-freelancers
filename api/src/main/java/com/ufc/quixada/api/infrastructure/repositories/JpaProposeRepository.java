package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.infrastructure.models.ProposeJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaProposeRepository extends JpaRepository<ProposeJpaModel, Long> {
    boolean existsByFreelancerIdAndProjectId(Long freelancerId, Long projectId);
    Optional<ProposeJpaModel> findByFreelancerIdAndProjectId(Long freelancerId, Long projectId);
    void deleteById(Long id);
}
