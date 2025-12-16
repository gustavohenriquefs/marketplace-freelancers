package data.repositories;

import domain.entities.Freelancer;
import domain.repositories.FreelancerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
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

    @Override
    public void createProposeAnswear(int idPropose) {

    }
}
