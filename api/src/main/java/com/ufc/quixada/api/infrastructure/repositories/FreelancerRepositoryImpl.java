package com.ufc.quixada.api.infrastructure.repositories;

import com.ufc.quixada.api.application.mappers.FreelancerMapper;
import com.ufc.quixada.api.domain.entities.Freelancer;
import com.ufc.quixada.api.domain.repositories.FreelancerRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service()
public class FreelancerRepositoryImpl implements FreelancerRepository {

    private final JpaFreelancerInterface jpaRepo;
    private final FreelancerMapper mapper;

    public FreelancerRepositoryImpl(JpaFreelancerInterface jpaRepo, FreelancerMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }

    @Override
    public List<Freelancer> findAll() {
        return jpaRepo.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Freelancer findById(int id) {
        return null;
    }

    @Override
    public Freelancer createFreelancer(Freelancer freelancer) {
        return null;
    }
}