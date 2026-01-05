package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.application.mappers.ProposeMapper;
import com.ufc.quixada.api.domain.entities.Freelancer;
import com.ufc.quixada.api.domain.entities.Project;
import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.domain.repositories.ProposeRepository;
import com.ufc.quixada.api.infrastructure.models.ProposeJpaEntity;

import java.util.Optional;

public class ProposeRepositoryImpl implements ProposeRepository {
    JpaProposeRepository jpaProposeRepository;
    ProposeMapper proposeMapper;

    public ProposeRepositoryImpl(JpaProposeRepository jpaProposeRepository, ProposeMapper proposeMapper) {
        this.jpaProposeRepository = jpaProposeRepository;
        this.proposeMapper = proposeMapper;
    }

    @Override
    public Propose create(Propose propose) {
        ProposeJpaEntity entity = proposeMapper.toJpaEntity(propose);
        ProposeJpaEntity savedEntity = jpaProposeRepository.save(entity);
        return proposeMapper.toDomain(savedEntity);
    }

    @Override
    public boolean existsByFreelancerIdAndProjectId(Long freelancer_id, Long project_id) {
        return jpaProposeRepository.existsByFreelancerIdAndProjectId(
                freelancer_id,
                project_id
        );
    }

    @Override
    public Optional<Propose> findById(Long id) {
        return jpaProposeRepository.findById(id)
                .map(proposeMapper::toDomain);
    }

    @Override
    public void save(Propose propose) {
        ProposeJpaEntity entity = proposeMapper.toJpaEntity(propose);
        jpaProposeRepository.save(entity);
    }

}
