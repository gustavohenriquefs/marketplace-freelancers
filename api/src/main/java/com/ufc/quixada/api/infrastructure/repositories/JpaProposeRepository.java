package com.ufc.quixada.api.infrastructure.repositories;


import com.ufc.quixada.api.infrastructure.models.ProjectJpaEntity;
import com.ufc.quixada.api.infrastructure.models.ProposeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProposeRepository extends JpaRepository<ProposeJpaEntity, Long> {
}
