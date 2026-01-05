package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.application.mappers.FreelancerMapper;
import com.ufc.quixada.api.domain.entities.Freelancer;
import com.ufc.quixada.api.domain.repositories.FreelancerRepository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

public class FreelancerRepositoryImpl implements FreelancerRepository {

    private final JpaFreelancerRepository jpaRepo;
    private final FreelancerMapper mapper;

    public FreelancerRepositoryImpl(JpaFreelancerRepository jpaRepo, FreelancerMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    public List<Freelancer> findAll() {
        return jpaRepo.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    public Freelancer findById(int id) {
        return null;
    }

    public Freelancer createFreelancer(Freelancer freelancer) {
        return mapper.toDomain(
                jpaRepo.save(
                        mapper.toJpaEntity(freelancer)
                )
        );
    }
}