package com.ufc.quixada.api.infrastructure.repositories;


import com.ufc.quixada.api.infrastructure.models.ProposeJpaEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProposeRepository extends JpaRepository<ProposeJpaEntity, Long> {
    boolean existsByFreelancerIdAndProjectId(Long freelancerId, Long projectId);
    Optional<ProposeJpaEntity> findByFreelancerIdAndProjectId(Long freelancerId, Long projectId);
    void deleteById(Long id);
}
