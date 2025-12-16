package application.usecases;

import domain.entities.Freelancer;
import domain.repositories.FreelancerRepository;
import domain.usecases.GetFreelancers;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetFreelancersUseCaseImpl implements GetFreelancers {
    private final FreelancerRepository freelancerRepository;

    public GetFreelancersUseCaseImpl(FreelancerRepository freelancerRepository) {
        this.freelancerRepository = freelancerRepository;
    }
    @Override
    public List<Freelancer> getFreelancers() {
        return this.freelancerRepository.getAll();
    }
}
