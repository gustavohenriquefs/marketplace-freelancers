package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.application.mappers.ProposeMapper;
import com.ufc.quixada.api.domain.entities.Propose;
import com.ufc.quixada.api.domain.repositories.ProposeRepository;
import com.ufc.quixada.api.infrastructure.models.FreelancerJpaModel;
import com.ufc.quixada.api.infrastructure.models.ProjectJpaModel;
import com.ufc.quixada.api.infrastructure.models.ProposeJpaModel;
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

    /**
     * Converts detached associated entities (project and freelancer) into managed JPA
     * references before persisting a {@link ProposeJpaModel}.
     * <p>
     * When a {@code ProposeJpaModel} is mapped from the domain model, its
     * {@link ProjectJpaModel} and {@link FreelancerJpaModel} references may be
     * detached instances that already exist in the database. Passing such detached
     * entities directly to the persistence context can lead to
     * {@code "detached entity passed to persist"} errors.
     * <p>
     * This method replaces those detached references with managed proxies obtained
     * via {@link EntityManager#getReference(Class, Object)}. It should be invoked
     * before calling repository {@code save} operations on proposals that reference
     * existing project or freelancer records.
     */
    private void attachManagedRefs(ProposeJpaModel entity) {
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
        ProposeJpaModel entity = proposeMapper.toJpaEntity(propose);
        attachManagedRefs(entity);
        ProposeJpaModel savedEntity = jpaProposeRepository.save(entity);
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
    public Optional<Propose> findByFreelancerIdAndProjectId(Long freelancerId, Long projectId) {
        return jpaProposeRepository.findByFreelancerIdAndProjectId(freelancerId, projectId)
                .map(proposeMapper::toDomain);
    }

    @Override
    public void save(Propose propose) {
        ProposeJpaModel entity = proposeMapper.toJpaEntity(propose);
        attachManagedRefs(entity);
        jpaProposeRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        jpaProposeRepository.deleteById(id);
    }

}
