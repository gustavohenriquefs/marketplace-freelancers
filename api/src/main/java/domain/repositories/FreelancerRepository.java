package domain.repositories;

import domain.entities.Freelancer;

import java.util.List;

public  interface FreelancerRepository {
    public List<Freelancer> getAll();
    public Freelancer findById(int id);
    public Freelancer createFreelancer(Freelancer freelancer);
}
