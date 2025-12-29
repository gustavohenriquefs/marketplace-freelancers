package com.ufc.quixada.api.domain.repositories;

import com.ufc.quixada.api.domain.entities.Freelancer;

import java.util.List;

public  interface FreelancerRepository {
    List<Freelancer> findAll();
    Freelancer findById(int id);
    Freelancer createFreelancer(Freelancer freelancer);
}
