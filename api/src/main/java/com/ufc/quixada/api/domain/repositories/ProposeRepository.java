package com.ufc.quixada.api.domain.repositories;

import com.ufc.quixada.api.domain.entities.Freelancer;
import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.domain.enums.ProposeStatus;
import com.ufc.quixada.api.infrastructure.models.ProposeJpaEntity;

import java.util.Optional;


public interface ProposeRepository {
    Propose create(Propose propose);
    Optional<Propose>  findById(Long aLong);
    void save(Propose propose);
    boolean existsByFreelancerIdAndProjectId(Long freelancerId, Long projectId);
}
