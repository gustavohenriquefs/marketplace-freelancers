package data.repositories;

import domain.entities.Freelancer;
import domain.repositories.FreelancerRepository;

import java.util.List;

public class FreelancerRepositoryImpl implements FreelancerRepository {
    @Override
    public List<Freelancer> getAll() {
        return List.of();
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
