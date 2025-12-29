package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.domain.repositories.ProposeRepository;
import org.springframework.stereotype.Service;

@Service()
public class ProposeRepositoryImpl implements ProposeRepository {

    @Override
    public Propose createPropose(Propose propose) {
        return null;
    }
}
