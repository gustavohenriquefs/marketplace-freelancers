package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.application.mappers.ContractorMapper;
import com.ufc.quixada.api.domain.entities.Contractor;
import com.ufc.quixada.api.domain.repositories.ContractorRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ContractorRepositoryImpl implements ContractorRepository {
    JpaContractorRepository jpaContractorRepository;
    ContractorMapper contractorMapper;



    @Override
    public Contractor createContractor(Contractor contractor) {
        return contractorMapper.toDomain(
            jpaContractorRepository.save(
                contractorMapper.toJpaEntity(contractor)
            )
        );
    }
}
