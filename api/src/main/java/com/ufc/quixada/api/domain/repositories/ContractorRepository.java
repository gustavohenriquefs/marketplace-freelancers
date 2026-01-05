package com.ufc.quixada.api.domain.repositories;

import com.ufc.quixada.api.domain.entities.Contractor;

import java.util.Optional;

public interface ContractorRepository {
    Contractor createContractor(Contractor contractor);
    Optional<Contractor> findById(Long id);
}
