package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.application.mappers.SkillMapper;
import com.ufc.quixada.api.domain.entities.Skill;
import com.ufc.quixada.api.domain.repositories.SkillRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SkillRepositoryImpl implements SkillRepository {

    private final JpaSkillRepository jpaRepo;
    private final SkillMapper mapper;

    public SkillRepositoryImpl(JpaSkillRepository jpaRepo, SkillMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Skill> findAll() {
        return jpaRepo.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Skill> findById(Long id) {
        return jpaRepo.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public Skill save(Skill skill) {
        return mapper.toDomain(
                jpaRepo.save(
                        mapper.toJpaEntity(skill)
                )
        );
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Skill> findAllById(List<Long> ids) {
        return jpaRepo.findAllById(ids).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}

