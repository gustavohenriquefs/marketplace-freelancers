package com.ufc.quixada.api.application.usecases;

import com.ufc.quixada.api.domain.entities.Freelancer;
import com.ufc.quixada.api.domain.repositories.FreelancerRepository;

import java.util.List;

public class GetFreelancers {
    private final FreelancerRepository repository;

    public GetFreelancers(FreelancerRepository repository) {
        this.repository = repository;
    }

    public List<Freelancer> execute() {
        return repository.findAll();
    }
}