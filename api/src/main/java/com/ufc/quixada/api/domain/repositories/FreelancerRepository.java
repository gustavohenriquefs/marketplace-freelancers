package com.ufc.quixada.api.domain.repositories;

import com.ufc.quixada.api.domain.entities.Freelancer;

import java.util.List;

public  interface FreelancerRepository {
    public List<Freelancer> findAll();
    public Freelancer findById(int id);
    public Freelancer createFreelancer(Freelancer freelancer);
    public void createProposeAnswear(int idPropose);
}
