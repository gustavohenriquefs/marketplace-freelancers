package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.application.mappers.ContractorMapper;
import com.ufc.quixada.api.application.mappers.ContractorMapperImpl;
import com.ufc.quixada.api.domain.repositories.ContractorRepository;

public class ContractorRepositoryImpl implements ContractorRepository {
    ContractorMapper contractorMapper;
    JpaContractorRepository jpaContractorRepository;
}
