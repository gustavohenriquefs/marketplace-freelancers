package com.ufc.quixada.api.data.repositories;

import com.ufc.quixada.api.domain.entities.Freelancer;
import com.ufc.quixada.api.domain.repositories.FreelancerRepository;
import com.ufc.quixada.api.data.mappers.FreelancerPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FreelancerRepositoryImpl implements FreelancerRepository {

    private final JpaFreelancerInterface jpaRepo;
    private final FreelancerPersistenceMapper mapper;

    public FreelancerRepositoryImpl(JpaFreelancerInterface jpaRepo, FreelancerPersistenceMapper mapper) {
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

    @Override
    public void createProposeAnswear(int idPropose) {

    }
}