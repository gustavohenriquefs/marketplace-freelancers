package com.ufc.quixada.api.domain.repositories;

import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.domain.enums.ProposeStatus;
import com.ufc.quixada.api.infrastructure.models.ProposeJpaEntity;

import java.util.Optional;


public interface ProposeRepository {
    Propose createPropose(Propose propose);
    Optional<Propose>  findById(Long aLong);
    void save(Propose propose);
}
