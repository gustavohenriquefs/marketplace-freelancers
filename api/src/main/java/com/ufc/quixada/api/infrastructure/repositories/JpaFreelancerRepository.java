package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.infrastructure.models.FreelancerJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFreelancerRepository extends JpaRepository<FreelancerJpaModel, Long>{}