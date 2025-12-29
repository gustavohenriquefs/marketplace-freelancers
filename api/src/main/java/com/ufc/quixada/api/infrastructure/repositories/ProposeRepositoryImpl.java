package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.domain.repositories.ProposeRepository;

public class ProposeRepositoryImpl implements ProposeRepository {
    JpaProposeRepository jpaProjectRepository;

    public ProposeRepositoryImpl(JpaProposeRepository jpaProjectRepository) {
        this.jpaProjectRepository = jpaProjectRepository;
    }

    @Override
    public Propose createPropose(Propose propose) {
        return null;
    }
}
