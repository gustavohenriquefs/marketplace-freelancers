package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.application.mappers.ProposeMapper;
import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.domain.repositories.ProposeRepository;
import com.ufc.quixada.api.infrastructure.models.FreelancerJpaModel;
import com.ufc.quixada.api.infrastructure.models.ProjectJpaModel;
import com.ufc.quixada.api.infrastructure.models.ProposeJpaEntity;
import jakarta.persistence.EntityManager;

import java.util.Optional;

public class ProposeRepositoryImpl implements ProposeRepository {
    JpaProposeRepository jpaProposeRepository;
    ProposeMapper proposeMapper;
    private final EntityManager entityManager;

    public ProposeRepositoryImpl(JpaProposeRepository jpaProposeRepository, ProposeMapper proposeMapper, EntityManager entityManager) {
        this.jpaProposeRepository = jpaProposeRepository;
        this.proposeMapper = proposeMapper;
        this.entityManager = entityManager;
    }

    private void attachManagedRefs(ProposeJpaEntity entity) {
        if (entity == null) {
            return;
        }

        if (entity.getProject() != null && entity.getProject().getId() != null) {
            entity.setProject(entityManager.getReference(ProjectJpaModel.class, entity.getProject().getId()));
        }

        if (entity.getFreelancer() != null && entity.getFreelancer().getId() != null) {
            entity.setFreelancer(entityManager.getReference(FreelancerJpaModel.class, entity.getFreelancer().getId()));
        }
    }

    @Override
    public Propose create(Propose propose) {
        ProposeJpaEntity entity = proposeMapper.toJpaEntity(propose);
        attachManagedRefs(entity);
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
        attachManagedRefs(entity);
        jpaProposeRepository.save(entity);
    }

}
