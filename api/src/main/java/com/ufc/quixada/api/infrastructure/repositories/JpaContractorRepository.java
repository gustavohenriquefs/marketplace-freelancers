package com.ufc.quixada.api.infrastructure.repositories;


import com.ufc.quixada.api.infrastructure.models.ContractorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaContractorRepository extends JpaRepository<ContractorJpaEntity,Long> {
}
