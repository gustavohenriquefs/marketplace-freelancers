package com.ufc.quixada.api.data.repositories;

import com.ufc.quixada.api.data.models.FreelancerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface JpaFreelancerInterface extends JpaRepository<FreelancerJpaEntity, UUID> {}