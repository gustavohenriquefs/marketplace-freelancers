package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.infrastructure.models.ContractorJpaModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaContractorRepository extends JpaRepository<ContractorJpaModel,Long> {

}
